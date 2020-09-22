package com.growatt.grohome.module.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.User;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.http.API;
import com.growatt.grohome.module.login.RegisterLoginActivity;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MD5andKL;
import com.growatt.grohome.utils.SharedPreferencesUnit;
import com.growatt.grohome.utils.SystemUtil;
import com.growatt.grohome.utils.UrlUtil;

import org.json.JSONObject;

public class SplashPresenter extends BasePresenter<ISplashView> {

    private String username;
    private String password;

    private String userUrl;//用户所属服务器
    public boolean isRemmenberPassword = false;

    public SplashPresenter(ISplashView baseView) {
        super(baseView);
    }

    public SplashPresenter(Context context, ISplashView baseView) {
        super(context, baseView);
        getUserInfo();
    }


    public void getUserInfo() {
        isRemmenberPassword = SharedPreferencesUnit.getInstance(context).getBoolean(GlobalConstant.SP_REMMENER_PASSWORD);
        username = SharedPreferencesUnit.getInstance(context).get(GlobalConstant.SP_USER_NAME);
        password = SharedPreferencesUnit.getInstance(context).get(GlobalConstant.SP_USER_PASSWORD);
    }


    public void autologin() {
        if (!isRemmenberPassword && TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            toLogin();
        } else {
            getUserType();
        }

    }


    public void toLogin() {
        Intent intent = new Intent(context, RegisterLoginActivity.class);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }

    /**
     * 获取用户类型
     */

    public void getUserType() {
        addDisposable(apiServer.getUserType(username, MD5andKL.encryptPassword(password), String.valueOf(CommentUtils.getLanguage())), new BaseObserver<String>(baseView, false) {

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int result = jsonObject.getInt("result");
                    if (result == 1) {
                        JSONObject obj = jsonObject.getJSONObject("obj");
                        //用户类型
                        int userType = obj.getInt("userType");
                        String userServerUrl = obj.getString("userServerUrl");
                        if (userType == 0) {//监控用户
                            userLogin(userServerUrl, username, password);
                        }
                    } else {
                        toLogin();
                    }

                } catch (Exception e) {
                    toLogin();
                }

            }

            @Override
            public void onError(String msg) {
                toLogin();
            }
        });
    }


    /**
     * 登录
     */
    public void userLogin(String url, String username, String password) {
        userUrl = UrlUtil.replaceUrl(url);
        String userServerUrl = GlobalConstant.HTTP_PREFIX + userUrl + API.NEWTWOLOGINAPI;

        String systemModel = SystemUtil.getSystemModel();
        String serialNumber = SystemUtil.getSerialNumber(context);
        String language = String.valueOf(CommentUtils.getLanguage());
        String apptype="GroHome";

        //正式登录
        addDisposable(apiServer.login(userServerUrl, username, MD5andKL.encryptPassword(password),apptype,serialNumber,systemModel,language), new BaseObserver<String>(baseView, false) {
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
                        toLogin();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                toLogin();
            }
        });
    }


    /**
     * 登录成功，保存用户信息
     */
    public void savaUserInfo(String username, String password, User user) {
        SharedPreferencesUnit.getInstance(context).putBoolean(GlobalConstant.SP_REMMENER_PASSWORD, isRemmenberPassword);
        SharedPreferencesUnit.getInstance(context).putBoolean(GlobalConstant.SP_AUTO_LOGIN, true);
        SharedPreferencesUnit.getInstance(context).put(GlobalConstant.SP_USER_NAME, username);
        if (isRemmenberPassword) {
            SharedPreferencesUnit.getInstance(context).put(GlobalConstant.SP_USER_PASSWORD, password);
        } else {
            SharedPreferencesUnit.getInstance(context).remove(GlobalConstant.SP_USER_PASSWORD);
        }
        App.setUserBean(user);
    }

}
