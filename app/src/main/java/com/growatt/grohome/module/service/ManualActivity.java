package com.growatt.grohome.module.service;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.service.presenter.ManualPresenter;
import com.growatt.grohome.module.service.view.IManualView;

public class ManualActivity extends BaseActivity<ManualPresenter> implements IManualView {




    @Override
    protected ManualPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manual;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }


}
