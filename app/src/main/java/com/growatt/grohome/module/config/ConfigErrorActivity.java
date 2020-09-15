package com.growatt.grohome.module.config;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.constants.DeviceConfigConstant;
import com.growatt.grohome.module.config.Presenter.ConfigErrorPresenter;
import com.growatt.grohome.module.config.view.IConfigErrorView;

import butterknife.BindView;
import butterknife.OnClick;

public class ConfigErrorActivity extends BaseActivity<ConfigErrorPresenter> implements IConfigErrorView {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_sub_title)
    TextView tvSubTitle;
    @BindView(R.id.tv_error_tip1)
    TextView tvErrorTip1;
    @BindView(R.id.tv_error_tip2)
    TextView tvErrorTip2;
    @BindView(R.id.btn_apconfig)
    Button btnApconfig;

    @Override
    protected ConfigErrorPresenter createPresenter() {
        return new ConfigErrorPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_config_error;
    }

    @Override
    protected void initViews() {
        //初始化头部
        tvTitle.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.icon_return);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        super.initListener();
    }


    @OnClick({ R.id.btn_retry, R.id.btn_apconfig})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_retry:
                presenter.reTryConfig();
                break;
            case R.id.btn_apconfig:
                presenter.toApConfig();
                break;
        }
    }


    @Override
    public void showTipsByMode(int mode) {
        if (mode == DeviceConfigConstant.EC_MODE||mode== DeviceConfigConstant.BLUETOOTH_MODE) {
            tvSubTitle.setText(R.string.m128_device_not_responding);
            tvErrorTip1.setText(R.string.m130_confirm_device_reset_fast);
            tvErrorTip2.setText(R.string.m135_check_is_2_4GHz);
            btnApconfig.setVisibility(View.VISIBLE);
        } else {
            tvSubTitle.setText(R.string.m136_check_try_again);
            tvErrorTip1.setText(R.string.m129_confirm_device_reset_slowly);
            tvErrorTip2.setText(R.string.m131_whether_connect_hotspot);
            btnApconfig.setVisibility(View.GONE);
        }
    }
}
