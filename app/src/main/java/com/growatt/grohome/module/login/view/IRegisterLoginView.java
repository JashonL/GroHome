package com.growatt.grohome.module.login.view;

import com.growatt.grohome.base.BaseView;

public interface IRegisterLoginView extends BaseView {

    String getUserName();

    String getPassword();

    void onError(String error);

    void loginSuccess(String user);

    String getEmail();

    void getCodeStart();

    void timing(int second);

    void getCodeEnd();

    void setZone(String zone);

}
