package com.growatt.grohome.module.personal.view;

import com.growatt.grohome.base.BaseView;

public interface INewEmailView extends BaseView {

    String getEmail();

    void getVerificationCode();

    void getVerificationCodeEnd();

    void setCountDown(String countDown);

    void onError(String onError);
}
