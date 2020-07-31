package com.growatt.grohome.module.device;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.AllDeviceAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.device.presenter.AllDevicePrensenter;
import com.growatt.grohome.module.device.view.IAllDeviceView;

import java.util.ArrayList;

public class AllDeviceActivity extends BaseActivity<AllDevicePrensenter>implements IAllDeviceView {

    private AllDeviceAdapter mAllDeviceAdapter;

    @Override
    protected AllDevicePrensenter createPresenter() {
        return new AllDevicePrensenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_device;
    }

    @Override
    protected void initViews() {
        mAllDeviceAdapter=new AllDeviceAdapter(R.layout.item_all_device,new ArrayList<>());
    }

    @Override
    protected void initData() {

    }
}
