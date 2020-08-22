package com.growatt.grohome.module.personal;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.personal.presenter.CacheClearPresenter;
import com.growatt.grohome.module.personal.view.ICacheClearView;

public class CacheClearActivity extends BaseActivity<CacheClearPresenter> implements ICacheClearView {

    @Override
    protected CacheClearPresenter createPresenter() {
        return new CacheClearPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cache_clear;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {
        presenter.showClearDialog();
    }
}
