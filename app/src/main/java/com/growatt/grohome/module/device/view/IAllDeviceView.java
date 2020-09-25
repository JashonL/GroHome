package com.growatt.grohome.module.device.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.GroDeviceBean;

import java.util.List;

public interface IAllDeviceView extends BaseView {
    void setAllDeviceSuccess(List<GroDeviceBean> deviceList);

    void setNoAddDevices(List<GroDeviceBean> deviceList);

    void onError(String onError);

    void alreadyTask();

    void alreadyConditon();

    void lunchTabTorun();

    void smart();
}
