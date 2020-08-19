package com.growatt.grohome.module.personal;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.personal.presenter.AboutPresenter;
import com.growatt.grohome.module.personal.view.IAboutView;

public class AboutActivity extends BaseActivity implements IAboutView {


    @Override
    protected BasePresenter createPresenter() {
        return new AboutPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
