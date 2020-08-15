package com.growatt.grohome.module.device;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.device.presenter.DeviceUpdataPresenter;
import com.growatt.grohome.module.device.view.IDeviceUpdataView;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceUpdataActivity extends BaseActivity<DeviceUpdataPresenter>implements IDeviceUpdataView {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_current)
    TextView tvCurrent;
    @BindView(R.id.tv_newversion)
    TextView tvNewVersion;
    @BindView(R.id.btn_updata)
    Button btnUpdata;
    @BindView(R.id.tv_info)
    TextView tvInfo;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected DeviceUpdataPresenter createPresenter() {
        return new DeviceUpdataPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_updata;
    }

    @Override
    protected void initViews() {
        //头部
        tvTitle.setText("");
        toolbar.setNavigationIcon(R.drawable.icon_return);
        //当前是最新版本
        tvNewVersion.setText(getString(R.string.m270_latest_version));
        tvNewVersion.setTextColor(ContextCompat.getColor(this, R.color.color_theme_green));
        btnUpdata.setVisibility(View.GONE);
        tvInfo.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        presenter.getIntentData();
    }

    @OnClick({R.id.btn_updata})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_updata:
                presenter.toUpGradeNow();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setName(String name) {
        if (!TextUtils.isEmpty(name)){
            tvName.setText(name);
        }
    }

    @Override
    public void setlastVersion() {
        tvNewVersion.setText(getString(R.string.m270_latest_version));
        tvNewVersion.setTextColor(ContextCompat.getColor(this, R.color.color_theme_green));
        btnUpdata.setVisibility(View.GONE);
        tvInfo.setVisibility(View.GONE);
    }

    @Override
    public void currentVersion(String curentVersion) {
         tvCurrent.setText(curentVersion);
    }

    @Override
    public void newVersion(String newVersion) {
        tvNewVersion.setText(newVersion);
        tvNewVersion.setTextColor(ContextCompat.getColor(this, R.color.color_text_33));
        btnUpdata.setVisibility(View.VISIBLE);
    }
}
