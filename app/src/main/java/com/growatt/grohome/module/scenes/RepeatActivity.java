package com.growatt.grohome.module.scenes;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.scenes.presenter.RepeatPresenter;
import com.growatt.grohome.module.scenes.view.IRepeatActivityView;

public class RepeatActivity extends BaseActivity<RepeatPresenter> implements IRepeatActivityView {
    @Override
    protected RepeatPresenter createPresenter() {
        return new RepeatPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_repeat;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
