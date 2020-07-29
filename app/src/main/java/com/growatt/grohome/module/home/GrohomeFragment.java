package com.growatt.grohome.module.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.GroHomeDevGridAdapter;
import com.growatt.grohome.adapter.GroHomeDevLineAdapter;
import com.growatt.grohome.adapter.RoomAdapter;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.bean.HomeDeviceBean;
import com.growatt.grohome.customview.GridDivider;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.device.DeviceTypeActivity;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.home.presenter.GrohomePresenter;
import com.growatt.grohome.module.home.view.IGrohomeView;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.CommentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GrohomeFragment extends BaseFragment<GrohomePresenter> implements IGrohomeView, Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rlv_room)
    RecyclerView rlvRoom;
    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;
    @BindView(R.id.iv_switch_devlist)
    ImageView ivSwitchDevList;


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
    protected void initView() {
        //头部toolBar
        tvTitle.setVisibility(View.GONE);
        toolbar.setTitle(R.string.m34_welcome_groHome);
        toolbar.inflateMenu(R.menu.menu_grohome);
        toolbar.setOnMenuItemClickListener(this);
        //房间列表初始化
        mRoomAdapter = new RoomAdapter(R.layout.item_room_view, new ArrayList<>());
        rlvRoom.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rlvRoom.setAdapter(mRoomAdapter);
        rlvRoom.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.HORIZONTAL, 30, ContextCompat.getColor(getActivity(), R.color.nocolor)));

        //设备列表初始化
        setGridAdapter();
    }

    @Override
    protected void initData() {
        //获取列表设备列表
        try {
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
        mGroHomeDevLineAdapter.setEmptyView(view);
        mGroHomeDevLineAdapter.setOnItemChildClickListener(this);
        mGroHomeDevLineAdapter.setOnItemClickListener(this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getContext(), DeviceTypeActivity.class));
           /*     View bodyView=LayoutInflater.from(getActivity()).inflate(R.layout.bulb_dialog_white_mode,null,false);
                CircleDialogUtils.showBulbWhiteMode(bodyView, GrohomeFragment.this.getFragmentManager(), view -> {

                });*/
                break;
        }
        return true;
    }

    @Override
    public void getAllDeviceSuccess(HomeDeviceBean bean) {
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
        if (homeAllDevice!=-1){
            data.get(homeAllDevice).setOnoff(Integer.parseInt(value));
        }
        if (mLayoutType == TYPE_GRID) {
            mGrohomeGridAdapter.notifyDataSetChanged();
        } else {
            mGroHomeDevLineAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        HomeDeviceBean.DataBean bean = (HomeDeviceBean.DataBean) adapter.getData().get(position);
        String deviceType=bean.getDevType();
          switch (view.getId()){
              case R.id.card_item:
                  presenter.jumpTodevice(bean);
                  break;
              case R.id.iv_onoff:
                  if (DeviceTypeConstant.TYPE_PANELSWITCH.equals(deviceType)){
                      presenter.deviceSwitch(bean.getDevId(),bean.getRoad(),bean.getOnoff());
                  }else {
                      presenter.deviceSwitch(bean.getDevId(),bean.getDevType());
                  }
                  break;
          }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        HomeDeviceBean.DataBean bean = (HomeDeviceBean.DataBean) adapter.getData().get(position);
        presenter.jumpTodevice(bean);
    }

    @OnClick(R.id.rl_switch_click)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_switch_click:
                changeLayout();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
