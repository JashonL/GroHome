package com.growatt.grohome.module.device.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.DeviceTimingBean;

import java.util.List;

public interface IDeviceTimingView extends BaseView {
    void updata(List<DeviceTimingBean> beanList);

    void upList();
}
