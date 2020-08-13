package com.growatt.grohome.module.device;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.device.presenter.DeviceTimingPresenter;
import com.growatt.grohome.module.device.view.IDeviceTimingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceTimingActivity extends BaseActivity<DeviceTimingPresenter> implements IDeviceTimingView {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_timing)
    RecyclerView rvTiming;
    @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;


    private String mDevName;
    private String mDevId;
//    private DeviceTimingAdapter mAdapter;
    private String devType;
    private String tempUnit;//当前温标
    private String mode;//当前模式
    private boolean config = false;
    private String roomId;
    private String roomName;

    @Override
    protected DeviceTimingPresenter createPresenter() {
        return new DeviceTimingPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_timing;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }


}
