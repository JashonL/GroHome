package com.growatt.grohome.module.room;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.RoomDevListAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.room.presenter.RoomListPresenter;
import com.growatt.grohome.module.room.view.IRoomListView;
import com.growatt.grohome.utils.GlideUtils;

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
        mRoomDevListAdapter.setEmptyView(view);
        mRoomDevListAdapter.setOnItemChildClickListener(this);
        mRoomDevListAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        presenter.parserData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
        tabTitle.addOnTabSelectedListener(this);
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
            GlideUtils.showImageAct(this, roomBean.getCdn(), ivRoomPic);
            //设置设备列表
            List<GroDeviceBean> devList = roomBean.getDevList();
            if (devList != null) {
                for (int i = 0; i < devList.size(); i++) {
                    GroDeviceBean deviceBean = devList.get(i);
                    presenter.initTuyaDevices(deviceBean.getDevId());
                    int onOff = presenter.initDevOnOff(deviceBean.getDevType(), deviceBean.getDevId());
                    deviceBean.setOnoff(onOff);
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
    }



    @OnClick({R.id.iv_pic_edit})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.iv_pic_edit:
                presenter.jumpEditRoom();
                break;
        }
    }
}
