package com.growatt.grohome.module.personal;



import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.personal.presenter.SettingPresenter;
import com.growatt.grohome.module.personal.view.ISettingView;

public class SetttingActivity extends BaseActivity<SettingPresenter> implements ISettingView {

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settting;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
