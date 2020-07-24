package com.growatt.grohome.module.device;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.device.presenter.BulbScenePresenter;
import com.growatt.grohome.module.device.view.IBulbSceneView;

public class BulbSceneEditActivity extends BaseActivity<BulbScenePresenter>implements IBulbSceneView {
    @Override
    protected BulbScenePresenter createPresenter() {
        return new BulbScenePresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bulb_edit;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
