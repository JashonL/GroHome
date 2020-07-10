package com.growatt.grohome.module.config;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.config.Presenter.DeviceAPLightPresenter;
import com.growatt.grohome.module.config.view.IDeviceAPLightView;

public class DeviceAPLightActivity extends BaseActivity<DeviceAPLightPresenter>implements IDeviceAPLightView {
    @Override
    protected DeviceAPLightPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_ap_light;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
