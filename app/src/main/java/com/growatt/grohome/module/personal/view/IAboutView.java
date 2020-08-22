package com.growatt.grohome.module.personal.view;


import com.growatt.grohome.base.BaseView;

public interface IAboutView extends BaseView {

    void setVersionName(String name);

    void setAppicon();

    void setPhone(String phone);

    void setEmail(String email);
}
