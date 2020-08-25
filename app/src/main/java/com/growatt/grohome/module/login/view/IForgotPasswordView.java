package com.growatt.grohome.module.login.view;

import com.growatt.grohome.base.BaseView;

public interface IForgotPasswordView extends BaseView {

    String userEmail();

    void onError(String onError);
}
