package com.growatt.grohome.module.config;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.config.Presenter.ConnectHotsPotPresenter;
import com.growatt.grohome.module.config.view.IConnectHotsPotView;

public class ConnectHotsPotActivity extends BaseActivity<ConnectHotsPotPresenter>implements IConnectHotsPotView {
    @Override
    protected ConnectHotsPotPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_hotspot;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
