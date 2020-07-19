package com.growatt.grohome.module.login.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.User;
import com.growatt.grohome.http.API;
import com.growatt.grohome.module.login.view.IRegisterLoginView;
import com.growatt.grohome.utils.MD5andKL;
import com.hjq.toast.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterLoginPresenter extends BasePresenter<IRegisterLoginView> {

    public RegisterLoginPresenter(IRegisterLoginView baseView) {
        super(baseView);
    }


    /**
     * 获取用户类型
     */

    public void getUserType(){
        String username=baseView.getUserName();
        String password = MD5andKL.encryptPassword(baseView.getPassword());
        addDisposable(apiServer.getUserType(username, password, "0"), new BaseObserver<String>(baseView,true) {

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject jsonObject =new JSONObject(json);
                    JSONObject obj = jsonObject.getJSONObject("obj");
                    //用户类型
                    int userType = obj.getInt("userType");
                    if (userType == 0) {//监控用户
                        userLogin(username,password);
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
    public void userLogin(String username,String password) {
        //正式登录
        addDisposable(apiServer.login(username,password), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String bean) {
                baseView.hideLoading();
                try {
                    JSONObject jsonObject =new JSONObject(bean);
                    JSONObject user = jsonObject.getJSONObject("back").getJSONObject("user");
                    //用户解析
                    User userInfo = new Gson().fromJson(user.toString(), User.class);
                    savaUserInfo(userInfo);
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
    public void savaUserInfo(User user){
        App.setUserBean(user);
    }


}
