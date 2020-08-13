package com.growatt.grohome.module.device.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.device.view.IDeviceTimingView;

public class DeviceTimingPresenter extends BasePresenter<IDeviceTimingView> {
    public DeviceTimingPresenter(IDeviceTimingView baseView) {
        super(baseView);
    }

    public DeviceTimingPresenter(Context context, IDeviceTimingView baseView) {
        super(context, baseView);
    }
}
