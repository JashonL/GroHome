package com.growatt.grohome.module.home;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.module.home.presenter.GrohomePresenter;
import com.growatt.grohome.module.home.view.IGrohomeView;

public class GrohomeFragment extends BaseFragment<GrohomePresenter> implements IGrohomeView {

    @Override
    protected GrohomePresenter createPresenter() {
        return new GrohomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_grohome;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
