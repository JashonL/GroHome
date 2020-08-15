package com.growatt.grohome.module.device.view;

import com.growatt.grohome.base.BaseView;

public interface IDeviceUpdataView extends BaseView {

    void setName(String name);

    void setlastVersion();

    void currentVersion(String curentVersion);

    void newVersion(String newVersion);


}
