package com.growatt.grohome.module.config.view;

import com.growatt.grohome.base.BaseView;

public interface IDeviceConfigView extends BaseView {

    void showConnectPage();

    void showSuccessPage(String devId,String pId,String devName);

    void showFailurePage(int mode);

    void setConnectProgress(float progress, int animationDuration);

    void showNetWorkFailurePage(int mode);

    void showBindDeviceSuccessTip();

    void showDeviceFindTip(String gwId);

    void showConfigSuccessTip();

    void showBindDeviceSuccessFinalTip();

    void setAddDeviceName(String name);

    void getTokenFail(String s,String s1);

    void showConfigFail(String errorCode, String error, int mode);
}
