package com.growatt.grohome.module.device.view;

import com.growatt.grohome.base.BaseView;

public interface IBulbView  extends BaseView {
    void setDeviceTitle(String devName);
    void setOnoff(String onoff);
    void setBright(String bright);
    void setColour(String colour);
    void set(String controdata);
    void setCuntDown(String countdown);
    void setScene(String scene);
    void setMode(String mode);
    void setTemp(String temp);
    void setControData(String controdata);

    void sendCommandSucces();
    void sendCommandError(String code, String error);
}
