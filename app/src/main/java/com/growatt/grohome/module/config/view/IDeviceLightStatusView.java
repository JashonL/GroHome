package com.growatt.grohome.module.config.view;

import com.growatt.grohome.base.BaseView;

public interface IDeviceLightStatusView extends BaseView {

    void showDialog();

    void dissmissDialog();

    void setMode(int mode);

    void getTuyaTokenSuccess();

    void getTuyaTokenFails(String errorCode,String errorMsg);
}
