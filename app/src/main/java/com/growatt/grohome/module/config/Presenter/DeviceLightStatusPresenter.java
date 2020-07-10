package com.growatt.grohome.module.config.Presenter;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.config.view.IDeviceLightStatusView;
import com.growatt.grohome.module.config.view.IWiFiOptionsView;

public class DeviceLightStatusPresenter extends BasePresenter<IDeviceLightStatusView> {

    public DeviceLightStatusPresenter(IDeviceLightStatusView baseView) {
        super(baseView);
    }
}
