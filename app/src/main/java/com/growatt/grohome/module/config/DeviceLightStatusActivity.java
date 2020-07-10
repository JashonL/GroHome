package com.growatt.grohome.module.config;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.config.Presenter.DeviceLightStatusPresenter;
import com.growatt.grohome.module.config.view.IDeviceLightStatusView;

public class DeviceLightStatusActivity extends BaseActivity<DeviceLightStatusPresenter> implements IDeviceLightStatusView {
    @Override
    protected DeviceLightStatusPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_light_status;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
