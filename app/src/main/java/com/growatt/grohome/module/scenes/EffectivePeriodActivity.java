package com.growatt.grohome.module.scenes;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.scenes.presenter.EffectivePeriodPresenter;
import com.growatt.grohome.module.scenes.view.IEffectivePeriodView;

public class EffectivePeriodActivity extends BaseActivity<EffectivePeriodPresenter>implements IEffectivePeriodView {
    @Override
    protected EffectivePeriodPresenter createPresenter() {
        return new EffectivePeriodPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_effective_period;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
