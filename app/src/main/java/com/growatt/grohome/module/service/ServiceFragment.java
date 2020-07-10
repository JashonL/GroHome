package com.growatt.grohome.module.service;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.ServiceCommondityAdapter;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.module.service.presenter.ServicePresenter;
import com.growatt.grohome.module.service.view.IServiceFragmentView;

import java.util.ArrayList;

public class ServiceFragment extends BaseFragment<ServicePresenter> implements IServiceFragmentView {


    private ServiceCommondityAdapter mServiceCommondityAdapter;

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
        mServiceCommondityAdapter=new ServiceCommondityAdapter(R.layout.item_commodity,new ArrayList<>());
    }

    @Override
    protected void initData() {

    }
}
