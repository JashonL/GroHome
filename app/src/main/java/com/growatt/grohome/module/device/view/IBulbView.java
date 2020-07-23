package com.growatt.grohome.module.device.view;

import com.growatt.grohome.base.BaseView;

public interface IBulbView  extends BaseView {
    void setDeviceTitle(String devName);
    void setOnoff(String onoff);
    void setBright(String bright);
    void setColour(int color);
    void set(String controdata);
    void setCuntDown(String countdown);
    void setScene(String scene);
    void setMode(String mode);
    void setTemp(String temp);
    void setControData(String controdata);
    void setSatProgress(int progress);
    void setVatProgress(int progress);
    void sendCommandSucces();
    void sendCommandError(String code, String error);
    void setCenterColor(int color);
    //设置白光的背景
    void setWhiteBgColor(int color);
    //设置白光遮罩
    void setWhiteMaskView(int color);
}
