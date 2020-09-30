package com.growatt.grohome.module.room;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.RoomDevListAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.eventbus.DeviceAddOrDelMsg;
import com.growatt.grohome.eventbus.HomeRoomEvent;
import com.growatt.grohome.eventbus.TransferDevMsg;
import com.growatt.grohome.module.device.DeviceTypeActivity;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.room.presenter.RoomListPresenter;
import com.growatt.grohome.module.room.view.IRoomListView;
import com.growatt.grohome.utils.GlideUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomListActivity extends BaseActivity<RoomListPresenter> implements IRoomListView, Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, TabLayout.OnTabSelectedListener {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_title)
    TabLayout tabTitle;
    @BindView(R.id.iv_room_pic)
    ImageView ivRoomPic;
    @BindView(R.id.iv_pic_edit)
    ImageView ivPicEdit;
    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;
    @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;

    private List<HomeRoomBean> mRoomList = new ArrayList<>();
    private RoomDevListAdapter mRoomDevListAdapter;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView) .statusBarColor(R.color.white).init();
    }

    @Override
    protected RoomListPresenter createPresenter() {
        return new RoomListPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_list;
    }

    @Override
    protected void initViews() {
        //头部toolBar
        tvTitle.setText(R.string.m186_room_list);
        toolbar.inflateMenu(R.menu.menu_grohome);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        //头部索引
        tabTitle.setVisibility(View.GONE);

        //图片
        ivRoomPic.setVisibility(View.GONE);
        ivPicEdit.setVisibility(View.GONE);
        //设备列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvDevice.setLayoutManager(layoutManager);
        mRoomDevListAdapter = new RoomDevListAdapter(new ArrayList<>());
        rlvDevice.setAdapter(mRoomDevListAdapter);
        rlvDevice.addItemDecoration(new LinearDivider(this, LinearLayoutManager.HORIZONTAL, ContextCompat.getColor(this, R.color.nocolor), 32));
        //设置空布局
        View view = LayoutInflater.from(this).inflate(R.layout.list_empty_view, rlvDevice, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RoomListActivity.this, DeviceTypeActivity.class));
            }
        });
        mRoomDevListAdapter.setEmptyView(view);
        mRoomDevListAdapter.setOnItemChildClickListener(this);
        mRoomDevListAdapter.setOnItemClickListener(this);

        //下拉刷新
        srlPull.setColorSchemeColors(ContextCompat.getColor(this, R.color.color_theme_green));


    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        presenter.parserData();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
        tabTitle.addOnTabSelectedListener(this);
        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.getRoomList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
           /*     View bodyView=LayoutInflater.from(getActivity()).inflate(R.layout.bulb_dialog_white_mode,null,false);
                CircleDialogUtils.showBulbWhiteMode(bodyView, GrohomeFragment.this.getFragmentManager(), view -> {

                });*/
                presenter.addRoom();
                break;
        }
        return true;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        GroDeviceBean bean = (GroDeviceBean) adapter.getData().get(position);
        String deviceType = bean.getDevType();
        String id = bean.getDevId();
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
            case R.id.tv_delete:
                presenter.showWarnDialog(id, deviceType);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void updata(List<HomeRoomBean> roomList, int position) {
        mRoomList = roomList;
        if (roomList.size() > 0) {
            //刷新房间数量
            tabTitle.setVisibility(View.VISIBLE);
            tabTitle.removeAllTabs();
            for (int i = 0; i < roomList.size(); i++) {
                TabLayout.Tab tab = tabTitle.newTab();
                tab.setText(roomList.get(i).getName());
                if (i == position) {
                    tabTitle.addTab(tab, true);
                } else {
                    tabTitle.addTab(tab);
                }
            }

            //设置图片
            ivRoomPic.setVisibility(View.VISIBLE);
            ivPicEdit.setVisibility(View.VISIBLE);
            setSelected(position);
        }
    }

    @Override
    public void setSelected(int position) {
        if (mRoomList != null && mRoomList.size() > position) {
            HomeRoomBean roomBean = mRoomList.get(position);//当前选中的房间
            presenter.setmCurrenRoom(roomBean);
            presenter.setCurrentPosition(position);
            GlideUtils.showImageAct(this, roomBean.getCdn(), ivRoomPic);
            //设置设备列表
            List<GroDeviceBean> devList = roomBean.getDevList();
            if (devList != null) {
                for (int i = 0; i < devList.size(); i++) {
                    GroDeviceBean deviceBean = devList.get(i);
                    deviceBean.setRoomName(roomBean.getName());
                    deviceBean.setRoomId(roomBean.getCid());
                    presenter.initTuyaDevices(deviceBean.getDevId());
                    presenter.initDevOnOff(deviceBean);
                }
                mRoomDevListAdapter.replaceData(devList);
            } else {
                mRoomDevListAdapter.replaceData(new ArrayList<>());
            }
        }
    }

    @Override
    public List<GroDeviceBean> getData() {
        return mRoomDevListAdapter.getData();
    }

    @Override
    public void upDataStatus(String devId, String value) {
        List<GroDeviceBean> data = mRoomDevListAdapter.getData();
        GroDeviceBean bean = new GroDeviceBean();
        bean.setDevId(devId);
        int homeAllDevice = data.indexOf(bean);
        if (homeAllDevice != -1) {
            data.get(homeAllDevice).setOnoff(Integer.parseInt(value));
        }
        mRoomDevListAdapter.notifyDataSetChanged();
    }

    @Override
    public void upDataOnline(String devId, boolean online) {
        List<GroDeviceBean> data = mRoomDevListAdapter.getData();
        GroDeviceBean bean = new GroDeviceBean();
        bean.setDevId(devId);
        int homeAllDevice = data.indexOf(bean);
        if (homeAllDevice != -1) {
            if (online){
                data.get(homeAllDevice).setDeviceConfig(true);
                data.get(homeAllDevice).setOnline(1);
            }else {
                data.get(homeAllDevice).setOnline(0);
            }
        }
        mRoomDevListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onError(String onError) {
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
        requestError(onError);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        setSelected(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @OnClick({R.id.iv_pic_edit})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.iv_pic_edit) {
            presenter.jumpEditRoom();
        }
    }
}
