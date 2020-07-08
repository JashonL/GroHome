package com.growatt.grohome.module.service;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.module.service.presenter.ServicePresenter;
import com.growatt.grohome.module.service.view.IServiceFragmentView;

public class ServiceFragment extends BaseFragment<ServicePresenter> implements IServiceFragmentView {

    @Override
    protected ServicePresenter createPresenter() {
        return new ServicePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_service;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
