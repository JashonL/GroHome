package com.growatt.grohome.module.login.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.User;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.http.API;
import com.growatt.grohome.module.commom.WebViewActivity;
import com.growatt.grohome.module.login.CountryListActivity;
import com.growatt.grohome.module.login.ForgotpasswordActivity;
import com.growatt.grohome.module.login.view.IRegisterLoginView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MD5andKL;
import com.growatt.grohome.utils.MyToastUtils;
import com.growatt.grohome.utils.PickViewUtils;
import com.growatt.grohome.utils.SharedPreferencesUnit;
import com.growatt.grohome.utils.UrlUtil;
import com.hjq.toast.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class RegisterLoginPresenter extends BasePresenter<IRegisterLoginView> {

    //跳转选择国家
    public static final int START_FOR_RESULT_COUNTRY = 100;

    private static final int MESSAGE_SHOW_TIMING = 102;
    private final int TOTAL_TIME = 180;
    private int count = TOTAL_TIME;
    private String registerUrl;
    private String verificationCode;
    private String userUrl;//用户所属服务器

    public boolean isRemmenberPassword = false;




    public RegisterLoginPresenter(IRegisterLoginView baseView) {
        super(baseView);
    }

    public RegisterLoginPresenter(Context context, IRegisterLoginView baseView) {
        super(context, baseView);
        initHandler(context);
        isRemmenberPassword=SharedPreferencesUnit.getInstance(context).getBoolean(GlobalConstant.SP_REMMENER_PASSWORD);
    }

    /**
     * 获取用户类型
     */

    public void getUserType() {
        String username = baseView.getUserName();
        String password = baseView.getPassword();
        addDisposable(apiServer.getUserType(username, MD5andKL.encryptPassword(password), String.valueOf(CommentUtils.getLanguage())), new BaseObserver<String>(baseView, false) {

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int result = jsonObject.getInt("result");
                    String errorMsg = "";
                    switch (result) {
                        case 0://失败
                            errorMsg = context.getString(R.string.m3_bad_network_msg);
                            baseView.onError(errorMsg);
                            break;
                        case 1:
                            JSONObject obj = jsonObject.getJSONObject("obj");
                            //用户类型
                            int userType = obj.getInt("userType");
                            String userServerUrl = obj.getString("userServerUrl");
                            if (userType == 0) {//监控用户
                                userLogin(userServerUrl, username, password);
                            }
                            break;
                        default:
                            errorMsg = context.getString(R.string.m221_username_password_error);
                            break;
                    }
                    if (result != 1) {
                        baseView.onError(errorMsg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String msg) {
                baseView.onError(msg);
            }
        });
    }


    /**
     * 登录
     */
    public void userLogin(String url, String username, String password) {
        userUrl = UrlUtil.replaceUrl(url);
        String userServerUrl = GlobalConstant.HTTP_PREFIX + userUrl + API.NEWTWOLOGINAPI;
        //正式登录
        addDisposable(apiServer.login(userServerUrl, username, MD5andKL.encryptPassword(password)), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    JSONObject result = jsonObject.getJSONObject("back");
                    if (result.optString("success").equals("true")) {
                        JSONObject user = result.getJSONObject("user");
                        //用户解析
                        User userInfo = new Gson().fromJson(user.toString(), User.class);
                        userInfo.setUrl(userUrl);
                        if (userUrl.contains("-cn"))
                            userInfo.setUserTuyaCode(GlobalConstant.CHINA_AREA_CODE);
                        else userInfo.setUserTuyaCode(GlobalConstant.EUROPE_AREA_CODE);
                        savaUserInfo(username, password, userInfo);
                        baseView.loginSuccess(bean);
                    } else {
                        String msg = context.getString(R.string.m221_username_password_error);
                        baseView.onError(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.onError(msg);
            }
        });
    }

    /**
     * 选择国家
     */

    public void getCountry() {
        Intent intent = new Intent(context, CountryListActivity.class);
        ActivityUtils.startActivityForResult((Activity) context, intent, START_FOR_RESULT_COUNTRY, ActivityUtils.ANIMATE_FORWARD, false);
    }

    /**
     * 选择时区
     */
    public void setZone() {
        List<String> zones = CommentUtils.getZones();
        PickViewUtils.showPickView((Activity) context, zones, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String zone = zones.get(options1);
                zone = "GMT" + zone;
                baseView.setZone(zone);
            }
        }, App.getInstance().getString(R.string.m185_zone));

    }


    /**
     * 重置密码
     */
    public void resetPassword() {
        Intent intent = new Intent(context, ForgotpasswordActivity.class);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);

    }

    /**
     * 根据国家获取服务器
     */
    public void getServerByCountry(String country) {
        if (GlobalConstant.STRING_CHINA_CHINESE.equals(country) || country.toLowerCase().equals(GlobalConstant.STRING_CHINA_ENLISH)) {
            country = "China";
        }
        if (!TextUtils.isEmpty(country)) {
            try {
                addDisposable(apiServer.getServerCountry(URLEncoder.encode(country, "UTF-8")), new BaseObserver<String>(baseView, true) {
                    @Override
                    public void onSuccess(String bean) {
                        try {
                            JSONObject jsonObject = new JSONObject(bean);
                            String success = jsonObject.get("success").toString();
                            if (success.equals("true")) {
                                registerUrl = UrlUtil.replaceUrl(jsonObject.getString("server"));
                            } else {
                                registerUrl = API.URL_HOST;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            registerUrl = API.URL_HOST;
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        baseView.onError(msg);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void getVerificationCode(View vKeyWord) {
        CommentUtils.hideKeyboard(vKeyWord);
        String email = baseView.getEmail();
        if (TextUtils.isEmpty(baseView.getEmail())) {
            ToastUtils.show(R.string.m176_enter_email);
            return;
        }

        if (TextUtils.isEmpty(registerUrl)) {
            ToastUtils.show(R.string.m177_select_country);
            return;
        }
        String url = GlobalConstant.HTTP_PREFIX + registerUrl + API.VERIFICATION_CODE;
        //发送消息
        handler.sendEmptyMessage(MESSAGE_SHOW_TIMING);
        addDisposable(apiServer.getVerificationCode(url, email, email, String.valueOf(CommentUtils.getLanguage())), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.optString("success");
                    if ("true".equals(success)) {
                        verificationCode = jsonObject.optString("msg");
                        MyToastUtils.toast(R.string.m178_verification_send_email);
                        baseView.getCodeStart();
                    } else {
                        String s = jsonObject.optString("msg");
                        if (!TextUtils.isEmpty(s)) {
                            MyToastUtils.toast(s);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.onError(msg);
            }
        });
    }


    public void register(String regUserName, String regPassword, String repeatePassword, String regTimeZone, String regEmail, String regCountry, String verificationCode) {
        if (TextUtils.isEmpty(regCountry)) {
            MyToastUtils.toast(R.string.m177_select_country);
            return;
        }
        if (TextUtils.isEmpty(regTimeZone)) {
            MyToastUtils.toast(R.string.m179_select_time_zone);
            return;
        }

        if (regPassword.length() < 6) {
            MyToastUtils.toast(R.string.m282_password_must_six_characters);
            return;
        }

        if (!CommentUtils.regexPassword(regPassword)) {
            MyToastUtils.toast(R.string.m311_password_requires_mix);
            return;
        }


        if (TextUtils.isEmpty(regPassword)) {
            MyToastUtils.toast(R.string.m180_enter_password);
            return;
        }

        if (TextUtils.isEmpty(repeatePassword)) {
            MyToastUtils.toast(R.string.m180_enter_password);
            return;
        }


        if (TextUtils.isEmpty(repeatePassword)) {
            MyToastUtils.toast(R.string.m176_enter_email);
            return;
        }


        if (TextUtils.isEmpty(regEmail)) {
            MyToastUtils.toast(R.string.m176_enter_email);
            return;
        }

        if (TextUtils.isEmpty(this.verificationCode) || TextUtils.isEmpty(verificationCode) || !verificationCode.equals(this.verificationCode)) {
            MyToastUtils.toast(R.string.m181_verificationcode_error);
            return;
        }

        if (!baseView.isAgreement()) {
            MyToastUtils.toast(R.string.m312_tick_user_agreement);
            return;
        }

        String url = GlobalConstant.HTTP_PREFIX + registerUrl + API.CREATACCOUNT;
        addDisposable(apiServer.groHomeRegister(url, regUserName, regPassword, regTimeZone, regEmail, regCountry), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result).getJSONObject("back");
                    String msg = jsonObject.optString("msg");
                    if (jsonObject.opt("success").toString().equals("true")) {
                        if (msg.equals("200")) {
                            MyToastUtils.toast(R.string.m182_register_success);
                            userLogin(registerUrl, regUserName, regPassword);
                        }
                    } else {
                        if (msg.equals("503")) {
                            MyToastUtils.toast(R.string.m183_registered_name);

                        } else {
                            MyToastUtils.toast(R.string.m184_register_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.onError(msg);
            }
        });
    }


    /**
     * 登录成功，保存用户信息
     */
    public void savaUserInfo(String username, String password, User user) {
        SharedPreferencesUnit.getInstance(context).putBoolean(GlobalConstant.SP_REMMENER_PASSWORD,isRemmenberPassword);
        SharedPreferencesUnit.getInstance(context).putBoolean(GlobalConstant.SP_AUTO_LOGIN,true);
        SharedPreferencesUnit.getInstance(context).put(GlobalConstant.SP_USER_NAME, username);
        if (isRemmenberPassword){
            SharedPreferencesUnit.getInstance(context).put(GlobalConstant.SP_USER_PASSWORD, password);
        }else {
            SharedPreferencesUnit.getInstance(context).remove(GlobalConstant.SP_USER_PASSWORD);
        }
        App.setUserBean(user);
    }


    public Map<String, String> getUserInfo() {
        Map<String, String> userInfo = new HashMap<>();
        String username = SharedPreferencesUnit.getInstance(context).get(GlobalConstant.SP_USER_NAME);
        String password = SharedPreferencesUnit.getInstance(context).get(GlobalConstant.SP_USER_PASSWORD);
        userInfo.put(GlobalConstant.SP_USER_NAME, username);
        userInfo.put(GlobalConstant.SP_USER_PASSWORD, password);
        return userInfo;
    }


    public void getCurrentZone() {
        String currentTimeZone = CommentUtils.getCurrentTimeZone();
        int index = currentTimeZone.indexOf(":");
        if (index != -1) {
            currentTimeZone = currentTimeZone.substring(0, index);
        }
        baseView.setZone(currentTimeZone);
    }





    public void getUserAgreement() {
        String lan= String.valueOf(CommentUtils.getLanguage());
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("lan",lan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.userAgreement(body), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = jsonObject.optJSONArray("data");
                        if (data!=null){
                            String url = null;
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.optJSONObject(i);
                                url= object.optString("content","");
                            }
                            startAgreement(url);
                        }else {
                            startAgreement("");
                        }

                    }

                } catch (Exception e) {
                    startAgreement("");
                }
            }

            @Override
            public void onError(String msg) {
                startAgreement("");
            }
        });
    }





    private void startAgreement(String url) {
        if (TextUtils.isEmpty(url)){
            if (CommentUtils.getLanguage()==0){
                url=GlobalConstant.USER_AGREEMENT_CN;
            }else {
                url=GlobalConstant.USER_AGREEMENT_EN;
            }
        }
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(GlobalConstant.WEB_URL,url);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }





    public void getPrivacyPolicy() {
        String lan= String.valueOf(CommentUtils.getLanguage());
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("lan",lan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.userAgreement(body), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = jsonObject.optJSONArray("data");
                        if (data!=null){
                            String url = null;
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.optJSONObject(i);
                                url= object.optString("content","");
                            }
                            startPolicy(url);
                        }else {
                            startPolicy("");
                        }

                    }

                } catch (Exception e) {
                    startPolicy("");
                }
            }

            @Override
            public void onError(String msg) {
                startPolicy("");
            }
        });
    }




    private void startPolicy(String url) {
        if (TextUtils.isEmpty(url)){
            if (CommentUtils.getLanguage()==0){
                url=GlobalConstant.PRIVACY_POLICY_CN;
            }else {
                url=GlobalConstant.PRIVACY_POLICY_EN;
            }
        }

        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(GlobalConstant.WEB_URL,url);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what == MESSAGE_SHOW_TIMING) {//倒计时
            count--;
            if (count > 0) {
                baseView.timing(count);
                handler.sendEmptyMessageDelayed(MESSAGE_SHOW_TIMING, 1000);
            } else {
                baseView.getCodeEnd();
            }
        }
        return super.handleMessage(msg);
    }

    @Override
    public void onDestroy() {
        handler.removeMessages(MESSAGE_SHOW_TIMING);
        super.onDestroy();
    }
}
