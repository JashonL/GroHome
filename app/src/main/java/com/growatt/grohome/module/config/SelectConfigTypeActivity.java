package com.growatt.grohome.module.config;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.config.Presenter.SelectConfigPresenter;
import com.growatt.grohome.module.config.view.ISelectConfigView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectConfigTypeActivity extends BaseActivity<SelectConfigPresenter> implements ISelectConfigView {


    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_sub_title)
    TextView tvSubTitle;
    @BindView(R.id.tv_fast_title)
    TextView tvFastTitle;
    @BindView(R.id.tv_fast_content)
    TextView tvFastContent;
    @BindView(R.id.cl_fast_flash)
    ConstraintLayout clFastFlash;
    @BindView(R.id.tv_slow_title)
    TextView tvSlowTitle;
    @BindView(R.id.tv_slow_content)
    TextView tvSlowContent;
    @BindView(R.id.cl_slow_flash)
    ConstraintLayout clSlowFlash;

    public static final int AP_MODE = 0;
    public static final int EC_MODE = 1;
    public static final String CONFIG_MODE = "mode";


    @Override
    protected SelectConfigPresenter createPresenter() {
        return new SelectConfigPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_config;
    }

    @Override
    protected void initViews() {
        //导航栏
        tvTitle.setText(R.string.m109_select_config_type);
        toolbar.setNavigationIcon(R.drawable.icon_return);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    @OnClick({R.id.cl_fast_flash, R.id.cl_slow_flash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_fast_flash:
                presenter.selectMode(EC_MODE);
                break;
            case R.id.cl_slow_flash:
                presenter.selectMode(AP_MODE);
                break;
        }
    }

    @Override
    public void resultSelectMode() {
        finish();
    }
}
