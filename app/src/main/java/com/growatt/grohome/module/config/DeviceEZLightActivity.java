package com.growatt.grohome.module.config;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.config.Presenter.DeviceEZLightPresenter;
import com.growatt.grohome.module.config.view.IDeviceEZLightView;

public class DeviceEZLightActivity extends BaseActivity<DeviceEZLightPresenter> implements IDeviceEZLightView {
    @Override
    protected DeviceEZLightPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_ez_light;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
