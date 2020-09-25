package com.growatt.grohome.module.device;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.AllDeviceAdapter;
import com.growatt.grohome.adapter.CannotAddAdapter;
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

public class AllDeviceActivity extends BaseActivity<AllDevicePrensenter> implements IAllDeviceView , BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvDevice)
    RecyclerView rvDevice;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.rv_cannot_add)
    RecyclerView rvConnotAdd;
    @BindView(R.id.tv_cannot_add)
    TextView tvCannotAdd;



    private AllDeviceAdapter mAllDeviceAdapter;
    private CannotAddAdapter mCannotAddapter;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

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

        //不能添加
        rvConnotAdd.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mCannotAddapter = new CannotAddAdapter(R.layout.item_all_canot_add, new ArrayList<>());
        View emptyView2 = LayoutInflater.from(this).inflate(R.layout.comment_empty_view, rvConnotAdd, false);
        mCannotAddapter.setEmptyView(emptyView2);
        rvConnotAdd.setAdapter(mCannotAddapter);

        //下拉刷新
        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.color_theme_green));
    }

    @Override
    protected void initData() {
        try {
            presenter.getData();
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
        swipeRefresh.setOnRefreshListener(() -> {
            try {
                presenter.getAlldevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setAllDeviceSuccess(List<GroDeviceBean> deviceList) {
        mAllDeviceAdapter.replaceData(deviceList);
    }

    @Override
    public void setNoAddDevices(List<GroDeviceBean> deviceList) {
        mCannotAddapter.replaceData(deviceList);
    }

    @Override
    public void onError(String onError) {
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
        requestError(onError);
    }

    @Override
    public void alreadyTask() {
        tvCannotAdd.setText(R.string.m331_device_already_condition);
    }

    @Override
    public void alreadyConditon() {
        tvCannotAdd.setText(R.string.m251_device_already_task);
    }

    @Override
    public void lunchTabTorun() {
        tvCannotAdd.setVisibility(View.GONE);
        rvConnotAdd.setVisibility(View.GONE);
    }

    @Override
    public void smart() {
        tvCannotAdd.setVisibility(View.VISIBLE);
        rvConnotAdd.setVisibility(View.VISIBLE);
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
    public void hideLoading() {
        super.hideLoading();
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
