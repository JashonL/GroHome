package com.growatt.grohome.module.login.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.User;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.http.API;
import com.growatt.grohome.module.login.view.IRegisterLoginView;
import com.growatt.grohome.utils.MD5andKL;
import com.growatt.grohome.utils.SharedPreferencesUnit;
import com.hjq.toast.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterLoginPresenter extends BasePresenter<IRegisterLoginView> {


    public RegisterLoginPresenter(IRegisterLoginView baseView) {
        super(baseView);
    }

    public RegisterLoginPresenter(Context context, IRegisterLoginView baseView) {
        super(context, baseView);
    }

    /**
     * 获取用户类型
     */

    public void getUserType(){
        String username=baseView.getUserName();
        String password =baseView.getPassword() ;
        addDisposable(apiServer.getUserType(username, MD5andKL.encryptPassword(password), "0"), new BaseObserver<String>(baseView,true) {

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject jsonObject =new JSONObject(json);
                    JSONObject obj = jsonObject.getJSONObject("obj");
                    //用户类型
                    int userType = obj.getInt("userType");
                    String userServerUrl = "http://"+obj.getString("userServerUrl")+"/newTwoLoginAPI.do";
                    if (userType == 0) {//监控用户
                        userLogin(userServerUrl,username,password);
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
    public void userLogin(String url,String username,String password) {
        //正式登录
        addDisposable(apiServer.login(url,username,MD5andKL.encryptPassword(password)), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String bean) {
                baseView.hideLoading();
                try {
                    JSONObject jsonObject =new JSONObject(bean);
                    JSONObject user = jsonObject.getJSONObject("back").getJSONObject("user");
                    //用户解析
                    User userInfo = new Gson().fromJson(user.toString(), User.class);
                    savaUserInfo(username,password,userInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                baseView.loginSuccess(bean);
            }

            @Override
            public void onError(String msg) {
                baseView.hideLoading();
                baseView.loginError(msg);
            }
        });
    }


    /**
     * 登录成功，保存用户信息
     */
    public void savaUserInfo(String username,String password,User user){
        SharedPreferencesUnit.getInstance(context).put(GlobalConstant.SP_USER_NAME,username);
        SharedPreferencesUnit.getInstance(context).put(GlobalConstant.SP_USER_PASSWORD,password);
        App.setUserBean(user);
    }


    public Map<String,String> getUserInfo(){
        Map<String,String>userInfo=new HashMap<>();
        String username = SharedPreferencesUnit.getInstance(context).get(GlobalConstant.SP_USER_NAME);
        String password = SharedPreferencesUnit.getInstance(context).get(GlobalConstant.SP_USER_PASSWORD);
        userInfo.put(GlobalConstant.SP_USER_NAME,username);
        userInfo.put(GlobalConstant.SP_USER_PASSWORD,password);
        return userInfo;
    }


}
