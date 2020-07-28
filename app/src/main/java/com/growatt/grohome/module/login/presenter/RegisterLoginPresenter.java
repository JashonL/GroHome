package com.growatt.grohome.module.login.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.User;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.http.API;
import com.growatt.grohome.module.device.BulbSceneEditActivity;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.login.CountryListActivity;
import com.growatt.grohome.module.login.view.IRegisterLoginView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MD5andKL;
import com.growatt.grohome.utils.MyToastUtils;
import com.growatt.grohome.utils.PickViewUtils;
import com.growatt.grohome.utils.SharedPreferencesUnit;
import com.hjq.toast.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Field;

import static com.growatt.grohome.module.config.DeviceLightStatusActivity.START_FOR_RESULT_CONFIG;

public class RegisterLoginPresenter extends BasePresenter<IRegisterLoginView> {

    //跳转选择国家
    public static final int START_FOR_RESULT_COUNTRY = 100;

    private static final int MESSAGE_SHOW_TIMING = 102;
    private final int TOTAL_TIME = 180;
    private int count = TOTAL_TIME;
    private String registerUrl;
    private String verificationCode;


    public RegisterLoginPresenter(IRegisterLoginView baseView) {
        super(baseView);
    }

    public RegisterLoginPresenter(Context context, IRegisterLoginView baseView) {
        super(context, baseView);
        initHandler(context);
    }

    /**
     * 获取用户类型
     */

    public void getUserType() {
        String username = baseView.getUserName();
        String password = baseView.getPassword();
        addDisposable(apiServer.getUserType(username, MD5andKL.encryptPassword(password), String.valueOf(CommentUtils.getLanguage())), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject obj = jsonObject.getJSONObject("obj");
                    //用户类型
                    int userType = obj.getInt("userType");
                    String userServerUrl = "http://" + obj.getString("userServerUrl") + "/newTwoLoginAPI.do";
                    if (userType == 0) {//监控用户
                        userLogin(userServerUrl, username, password);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String msg) {

            }
        });
    }


    /**
     * 登录
     */
    public void userLogin(String url, String username, String password) {
        //正式登录
        addDisposable(apiServer.login(url, username, MD5andKL.encryptPassword(password)), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    JSONObject user = jsonObject.getJSONObject("back").getJSONObject("user");
                    //用户解析
                    User userInfo = new Gson().fromJson(user.toString(), User.class);
                    savaUserInfo(username, password, userInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                baseView.loginSuccess(bean);
            }

            @Override
            public void onError(String msg) {
                baseView.loginError(msg);
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
    public void setZone(){
        List<String> zones = CommentUtils.getZones();
        PickViewUtils.showPickView((Activity) context,zones,new OnOptionsSelectListener(){

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String zone = zones.get(options1);
                baseView.setZone(zone);
            }
        },App.getInstance().getString(R.string.m185_zone));

    }


    /**
     * 根据国家获取服务器
     */
    public void getServerByCountry(String country) {
        if (GlobalConstant.STRING_CHINA_CHINESE.equals(country)||   country.toLowerCase().equals(GlobalConstant.STRING_CHINA_ENLISH)) {
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
                                registerUrl = jsonObject.getString("server");
                            } else {
                                registerUrl=API.USER_URL;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            registerUrl=API.USER_URL;
                        }
                    }

                    @Override
                    public void onError(String msg) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void getVerificationCode(View vKeyWord){
        CommentUtils.hideKeyboard(vKeyWord);
        String email = baseView.getEmail();
        if (TextUtils.isEmpty(baseView.getEmail())){
            ToastUtils.show(R.string.m176_enter_email);
            return;
        }

        if (TextUtils.isEmpty(registerUrl)){
            ToastUtils.show(R.string.m177_select_country);
            return;
        }
        String url="http://"+registerUrl+API.VERIFICATION_CODE;
        baseView.getCodeStart();
        //发送消息
        handler.sendEmptyMessageDelayed(MESSAGE_SHOW_TIMING, 1000);
        addDisposable(apiServer.getVerificationCode(url, email, email, String.valueOf(CommentUtils.getLanguage())), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.optString("success");
                    if ("true".equals(success)){
                        verificationCode=jsonObject.optString("msg");
                        MyToastUtils.toast(R.string.m178_verification_send_email);
                    }else {
                        String s = jsonObject.optString("msg");
                        if (!TextUtils.isEmpty(s)){
                            MyToastUtils.toast(s);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }



    public void register(String regUserName, String regPassword,String repeatePassword, String regTimeZone,String regEmail, String regCountry,String verificationCode){
        if (TextUtils.isEmpty(regCountry)){
            MyToastUtils.toast(R.string.m177_select_country);
            return;
        }
        if (TextUtils.isEmpty(regTimeZone)){
            MyToastUtils.toast(R.string.m179_select_time_zone);
            return;
        }

        if (TextUtils.isEmpty(regPassword)){
            MyToastUtils.toast(R.string.m180_enter_password);
            return;
        }

        if (TextUtils.isEmpty(repeatePassword)){
            MyToastUtils.toast(R.string.m180_enter_password);
            return;
        }


        if (TextUtils.isEmpty(regEmail)){
            MyToastUtils.toast(R.string.m176_enter_email);
            return;
        }

        if (TextUtils.isEmpty(verificationCode)||!this.verificationCode.equals(verificationCode)){
            MyToastUtils.toast(R.string.m181_verificationcode_error);
            return;
        }

        addDisposable(apiServer.groHomeRegister(registerUrl, regUserName, regPassword, regTimeZone,regEmail,regCountry), new BaseObserver<String>(baseView,true) {
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

                        }else {
                            MyToastUtils.toast(R.string.m184_register_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }


    /**
     * 登录成功，保存用户信息
     */
    public void savaUserInfo(String username, String password, User user) {
        SharedPreferencesUnit.getInstance(context).put(GlobalConstant.SP_USER_NAME, username);
        SharedPreferencesUnit.getInstance(context).put(GlobalConstant.SP_USER_PASSWORD, password);
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

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case MESSAGE_SHOW_TIMING://倒计时
                count--;
                if (count <= 0) {
                    handler.sendEmptyMessageDelayed(MESSAGE_SHOW_TIMING, 1000);
                    baseView.timing(count);
                } else {
                    baseView.getCodeEnd();
                }
                break;
        }
        return super.handleMessage(msg);
    }

    @Override
    public void onDestroy() {
        handler.removeMessages(MESSAGE_SHOW_TIMING);
        super.onDestroy();
        super.onDestroy();
    }
}
