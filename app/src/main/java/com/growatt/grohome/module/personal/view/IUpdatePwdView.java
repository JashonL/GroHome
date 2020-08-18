package com.growatt.grohome.module.personal.view;

import com.growatt.grohome.base.BaseView;

public interface IUpdatePwdView extends BaseView {
    String getOldPassWord();

    String getNewPassWord();

    String getRepeatePassWord();
}
