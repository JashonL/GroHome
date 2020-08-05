package com.growatt.grohome.module.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.AllDeviceAdapter;
import com.growatt.grohome.adapter.SceneConditionAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.SceneConditionBean;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.module.device.presenter.AllDevicePrensenter;
import com.growatt.grohome.module.device.view.IAllDeviceView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllDeviceActivity extends BaseActivity<AllDevicePrensenter> implements IAllDeviceView , BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvDevice)
    RecyclerView rvDevice;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private AllDeviceAdapter mAllDeviceAdapter;

    @Override
    protected AllDevicePrensenter createPresenter() {
        return new AllDevicePrensenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_device;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);
        tvTitle.setText(R.string.m205_all_device);
        //设备列表
        rvDevice.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAllDeviceAdapter = new AllDeviceAdapter(R.layout.item_all_device, new ArrayList<>());
        View emptyView = LayoutInflater.from(this).inflate(R.layout.comment_empty_view, rvDevice, false);
        mAllDeviceAdapter.setEmptyView(emptyView);
        rvDevice.setAdapter(mAllDeviceAdapter);
    }

    @Override
    protected void initData() {
        try {
            presenter.getAlldevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().register(this);

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
        mAllDeviceAdapter.setOnItemClickListener(this);
    }

    @Override
    public void setAllDeviceSuccess(List<GroDeviceBean> deviceList) {
        mAllDeviceAdapter.replaceData(deviceList);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mAllDeviceAdapter) {
            GroDeviceBean deviceBean = mAllDeviceAdapter.getData().get(position);
            presenter.toSetDevice(deviceBean);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventScenes(@NonNull SceneTaskBean bean) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventScenesConditionBean(@NonNull SceneConditionBean bean) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
