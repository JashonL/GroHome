package com.growatt.grohome.module.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.GroHomeDevGridAdapter;
import com.growatt.grohome.adapter.GroHomeDevLineAdapter;
import com.growatt.grohome.adapter.RoomAdapter;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.bean.HomeDeviceBean;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.customview.GridDivider;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.eventbus.DevEditNameBean;
import com.growatt.grohome.eventbus.DeviceAddOrDelMsg;
import com.growatt.grohome.eventbus.HomeRoomEvent;
import com.growatt.grohome.eventbus.HomeRoomStatusBean;
import com.growatt.grohome.eventbus.TransferDevMsg;
import com.growatt.grohome.module.device.DeviceTypeActivity;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.home.presenter.GrohomePresenter;
import com.growatt.grohome.module.home.view.IGrohomeView;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.CommentUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GrohomeFragment extends BaseFragment<GrohomePresenter> implements IGrohomeView, Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rlv_room)
    RecyclerView rlvRoom;
    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;
    @BindView(R.id.iv_switch_devlist)
    ImageView ivSwitchDevList;
    @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;


    /*房间部分*/
    private RoomAdapter mRoomAdapter;
    /*设备部分*/
    private GroHomeDevGridAdapter mGrohomeGridAdapter;
    private GroHomeDevLineAdapter mGroHomeDevLineAdapter;


    //切换布局
    public static final int TYPE_GRID = 1;
    public static final int TYPE_LINE = 2;

    private int mLayoutType = TYPE_GRID;

    private List<HomeDeviceBean.DataBean> deviceList = new ArrayList<>();

    @Override
    protected GrohomePresenter createPresenter() {
        return new GrohomePresenter(getContext(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_grohome;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarView(statusBarView).statusBarColor(R.color.white).statusBarDarkFont(true, 0.2f).init();
    }

    @Override
    protected void initView() {
        //头部toolBar
        tvTitle.setVisibility(View.GONE);
        toolbar.setTitle(R.string.m34_welcome_groHome);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getContext(), R.drawable.icon_home_add));
        toolbar.inflateMenu(R.menu.men_grohome_home);
        toolbar.setOnMenuItemClickListener(this);
        //房间列表初始化
        mRoomAdapter = new RoomAdapter(R.layout.item_room_view, new ArrayList<>());
        rlvRoom.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rlvRoom.setAdapter(mRoomAdapter);
        rlvRoom.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.HORIZONTAL, 30, ContextCompat.getColor(getActivity(), R.color.nocolor)));

        //设备列表初始化
        setGridAdapter();
        //下拉刷新
        srlPull.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.color_theme_green));
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        //获取列表设备列表
        try {
            presenter.getRoomList();
            presenter.getAlldevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void changeLayout() {
        int itemDecorationCount = rlvDevice.getItemDecorationCount();
        for (int i = 0; i < itemDecorationCount; i++) {
            rlvDevice.removeItemDecorationAt(i);
        }
        //布局切换方法
        if (mLayoutType == TYPE_LINE) {
            setGridAdapter();
            mLayoutType = TYPE_GRID;
        } else {
            //列表模式
            setLineAdapter();
            mLayoutType = TYPE_LINE;
        }

    }


    /**
     * 设置表格布局
     */
    private void setGridAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rlvDevice.setLayoutManager(layoutManager);
        mGrohomeGridAdapter = new GroHomeDevGridAdapter(deviceList);
        rlvDevice.setAdapter(mGrohomeGridAdapter);
        ivSwitchDevList.setImageResource(R.drawable.icon_card);
        int div = CommentUtils.dip2px(getActivity(), 10);
        rlvDevice.addItemDecoration(new GridDivider(ContextCompat.getColor(getActivity(), R.color.nocolor), div, div));
        //设置空布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_empty_view, rlvDevice, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DeviceTypeActivity.class));
            }
        });
        mGrohomeGridAdapter.setEmptyView(view);
        mGrohomeGridAdapter.setOnItemChildClickListener(this);
        mGrohomeGridAdapter.setOnItemClickListener(this);
    }


    private void setLineAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvDevice.setLayoutManager(layoutManager);
        mGroHomeDevLineAdapter = new GroHomeDevLineAdapter(deviceList);
        rlvDevice.setAdapter(mGroHomeDevLineAdapter);
        rlvDevice.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.HORIZONTAL, ContextCompat.getColor(getActivity(), R.color.nocolor), 32));
        ivSwitchDevList.setImageResource(R.drawable.icon_list);
        //设置空布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_empty_view, rlvDevice, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DeviceTypeActivity.class));
            }
        });
        mGroHomeDevLineAdapter.setEmptyView(view);
        mGroHomeDevLineAdapter.setOnItemChildClickListener(this);
        mGroHomeDevLineAdapter.setOnItemClickListener(this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_room:
                presenter.addRoom();
                break;
            case R.id.action_add_device:
                startActivity(new Intent(getContext(), DeviceTypeActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void setAllDeviceSuccess(HomeDeviceBean bean) {
        if (bean == null) return;
        deviceList.clear();
        List<HomeDeviceBean.DataBean> newList = bean.getData();
        for (int i = 0; i < newList.size(); i++) {
            HomeDeviceBean.DataBean deviceBean = newList.get(i);
            String devType = deviceBean.getDevType();
            if (!TuyaApiUtils.isShowDevice(devType)) continue;
            String devId = deviceBean.getDevId();
            presenter.initTuyaDevices(devId);
            int onOff = presenter.initDevOnOff(devType, devId);
            deviceBean.setOnoff(onOff);
            deviceList.add(deviceBean);
        }
        mGrohomeGridAdapter.replaceData(deviceList);
    }

    @Override
    public void onError(String onError) {
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
        requestError(onError);
    }

    @Override
    public List<HomeDeviceBean.DataBean> getDeviceList() {
        if (mLayoutType == TYPE_GRID) {
            return mGrohomeGridAdapter.getData();
        } else {
            return mGroHomeDevLineAdapter.getData();
        }

    }

    @Override
    public void upDataStatus(String devId, String value) {
        List<HomeDeviceBean.DataBean> data;
        if (mLayoutType == TYPE_GRID) {
            data = mGrohomeGridAdapter.getData();
        } else {
            data = mGroHomeDevLineAdapter.getData();
        }
        HomeDeviceBean.DataBean bean = new HomeDeviceBean.DataBean();
        bean.setDevId(devId);
        int homeAllDevice = data.indexOf(bean);
        if (homeAllDevice != -1) {
            data.get(homeAllDevice).setOnoff(Integer.parseInt(value));
        }
        if (mLayoutType == TYPE_GRID) {
            mGrohomeGridAdapter.notifyDataSetChanged();
        } else {
            mGroHomeDevLineAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setRoomListSuccess(List<HomeRoomBean> roomList) {
        mRoomAdapter.replaceData(roomList);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        HomeDeviceBean.DataBean bean = (HomeDeviceBean.DataBean) adapter.getData().get(position);
        String deviceType = bean.getDevType();
        switch (view.getId()) {
            case R.id.card_item:
                presenter.jumpTodevice(bean);
                break;
            case R.id.iv_onoff:
                if (DeviceTypeConstant.TYPE_PANELSWITCH.equals(deviceType)) {
                    presenter.deviceSwitch(bean.getDevId(), bean.getRoad(), bean.getOnoff());
                } else {
                    presenter.deviceSwitch(bean.getDevId(), bean.getDevType());
                }
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter==mRoomAdapter){
            String roomlist = new Gson().toJson(mRoomAdapter.getData());
            presenter.jumpToRoom(roomlist, position);
        }else {
            HomeDeviceBean.DataBean bean = (HomeDeviceBean.DataBean) adapter.getData().get(position);
            presenter.jumpTodevice(bean);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.getRoomList();
                presenter.getAlldevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mRoomAdapter.setOnItemClickListener(this);
    }

    @OnClick({R.id.rl_switch_click, R.id.cl_all_room})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_switch_click:
                changeLayout();
                break;
            case R.id.cl_all_room:
                String roomlist = new Gson().toJson(mRoomAdapter.getData());
                presenter.jumpToRoom(roomlist, 0);
                break;
        }
    }


    /*修改设备名称*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showEditSuccess(DevEditNameBean msg) {
        if (msg != null) {
            String devId = msg.getDevId();
            List<HomeDeviceBean.DataBean> data;
            //布局切换方法
            if (mLayoutType == TYPE_LINE) {
                data = mGroHomeDevLineAdapter.getData();
            } else {
                data = mGrohomeGridAdapter.getData();
            }
            for (HomeDeviceBean.DataBean bean : data) {
                String deviceId = bean.getDevId();
                if (deviceId.equals(devId)) {
                    bean.setName(msg.getName());
                    break;
                }
            }

            if (mLayoutType == TYPE_LINE) {
                mGroHomeDevLineAdapter.notifyDataSetChanged();
            } else {
                mGrohomeGridAdapter.notifyDataSetChanged();
            }

            //获取列表设备列表
            try {
                presenter.getRoomList();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDevTransferBean(TransferDevMsg bean) {
        if (bean != null) {
            //获取列表设备列表
            try {
                presenter.getRoomList();
                presenter.getAlldevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 刷新房间
     *
     * @param bean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRoomBean(@NonNull HomeRoomStatusBean bean) {
        try {
            presenter.getRoomList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 接收房间名称修改
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRoomEventBean(HomeRoomEvent bean) {
        if (bean != null) {
            //获取列表设备列表
            try {
                presenter.getRoomList();
                presenter.getAlldevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(DeviceAddOrDelMsg bean) {
        if (bean != null) {
            //获取列表设备列表
            try {
                presenter.getRoomList();
                presenter.getAlldevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
    }
}
