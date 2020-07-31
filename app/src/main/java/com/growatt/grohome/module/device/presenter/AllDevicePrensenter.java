package com.growatt.grohome.module.device.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.device.view.IAllDeviceView;

public class AllDevicePrensenter extends BasePresenter<IAllDeviceView> {
    public AllDevicePrensenter(IAllDeviceView baseView) {
        super(baseView);
    }

    public AllDevicePrensenter(Context context, IAllDeviceView baseView) {
        super(context, baseView);
    }
}
