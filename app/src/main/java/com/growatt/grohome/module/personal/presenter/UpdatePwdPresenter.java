package com.growatt.grohome.module.personal.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.login.RegisterLoginActivity;
import com.growatt.grohome.module.personal.view.IUpdatePwdView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.MyToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePwdPresenter extends BasePresenter<IUpdatePwdView> {
    public UpdatePwdPresenter(IUpdatePwdView baseView) {
        super(baseView);
    }

    public UpdatePwdPresenter(Context context, IUpdatePwdView baseView) {
        super(context, baseView);
    }


    public void changePassword() {
        String userUrl = App.getUserBean().getUrl();
        String accountName = App.getUserBean().getAccountName();
        String passwordOld = baseView.getOldPassWord();
        String passwordNew = baseView.getNewPassWord();
        String passwordRepeat = baseView.getRepeatePassWord();
        if (TextUtils.isEmpty(passwordOld)||TextUtils.isEmpty(passwordNew)||TextUtils.isEmpty(passwordRepeat)){
            MyToastUtils.toast(R.string.m144_password_empty);
            return;
        }

        if (passwordNew.length() < 6) {
            MyToastUtils.toast(R.string.m282_password_must_six_characters);
            return;
        }
        if(!passwordNew.equals(passwordRepeat)){
            MyToastUtils.toast(R.string.m283_enter_the_same_password);
            return;
        }

        addDisposable(apiServer.updateUserPassword(userUrl, accountName, passwordOld, passwordNew), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.get("msg").toString().equals("200")) {
                        MyToastUtils.toast(R.string.m200_success);
                        toLoginActivity();
                    } else if (jsonObject.get("msg").toString().equals("502")) {
                        MyToastUtils.toast(R.string.m284_password_incorrect);
                    } else if ("701".equals(jsonObject.get("msg").toString())) {
                        MyToastUtils.toast(R.string.m285_not_have_permission);
                    } else {
                        MyToastUtils.toast(R.string.m286_fail_connect_server);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.onError(msg);
            }
        });

    }


    private void toLoginActivity(){
        Intent intent = new Intent(context, RegisterLoginActivity.class);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,true);
    }


}
