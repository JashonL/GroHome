package com.growatt.grohome.module.personal.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.http.API;
import com.growatt.grohome.module.commom.WebViewActivity;
import com.growatt.grohome.module.personal.view.IAboutView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AboutPresenter extends BasePresenter<IAboutView> {

    public AboutPresenter(IAboutView baseView) {
        super(baseView);
    }

    public AboutPresenter(Context context, IAboutView baseView) {
        super(context, baseView);
    }

    public void getVersionName() {
        String verSionName = context.getString(R.string.m271_current_version) + "    " + CommentUtils.getVerSionName(context);
        baseView.setVersionName(verSionName);
        baseView.setAppicon();
    }


    /**
     * 获取客服电话
     */
    public void getCustomerPhone() {
        String userUrl = GlobalConstant.HTTP_PREFIX + App.getUserBean().getUrl() + API.GET_SERVICE_PHONENUM + CommentUtils.getLocale() + "&kind=12&type=2";
        addDisposable(apiServer.getServicePhoneNum(userUrl), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String num = jsonObject.optString("servicePhoneNum");
                    String email = jsonObject.optString("serviceEmail");
                    baseView.setEmail(email);
                    baseView.setPhone(num);
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


    public void toWebSite() {
        Intent intent = new Intent(context, WebViewActivity.class);
        if (0==CommentUtils.getLanguage()){
            intent.putExtra(GlobalConstant.WEB_URL,GlobalConstant.COMPANY_WEBSITE_CN);
        }else {
            intent.putExtra(GlobalConstant.WEB_URL,GlobalConstant.COMPANY_WEBSITE_EN);
        }
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
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
        addDisposable(apiServer.privacyPolicy(body), new BaseObserver<String>(baseView,true) {
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
}
