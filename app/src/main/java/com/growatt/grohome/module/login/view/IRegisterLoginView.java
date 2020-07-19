package com.growatt.grohome.module.login.view;

import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.User;

public interface IRegisterLoginView extends BaseView {

    String getUserName();

    String getPassword();

    void loginSuccess(String user);

    void loginError(String errorMessage);
}
