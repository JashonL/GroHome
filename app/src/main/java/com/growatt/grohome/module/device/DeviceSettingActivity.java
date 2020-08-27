package com.growatt.grohome.module.device;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.SceneGridAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.ScenesBean;
import com.growatt.grohome.customview.GridDivider;
import com.growatt.grohome.eventbus.TransferDevMsg;
import com.growatt.grohome.module.device.manager.DeviceAirCon;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DevicePanel;
import com.growatt.grohome.module.device.manager.DevicePlug;
import com.growatt.grohome.module.device.manager.DeviceStripLights;
import com.growatt.grohome.module.device.manager.DeviceThermostat;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.presenter.DeviceSettingPresenter;
import com.growatt.grohome.module.device.view.IDeviceSettingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceSettingActivity extends BaseActivity<DeviceSettingPresenter> implements IDeviceSettingView , BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_device_icon)
    ImageView ivDeviceIcon;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_room_name)
    TextView tvRoomName;
    @BindView(R.id.tv_device_id)
    TextView tvDeviceId;
    @BindView(R.id.rlv_scenes_list)
    RecyclerView rlvScenesList;

    private SceneGridAdapter mSceneGridAdapter;

    @Override
    protected DeviceSettingPresenter createPresenter() {
        return new DeviceSettingPresenter(this, this);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_setting;
    }

    @Override
    protected void initViews() {
        //头部
        tvTitle.setText(R.string.m148_edit);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        //场景列表
        //定时列表
        rlvScenesList.setLayoutManager(new GridLayoutManager(this,2));
        mSceneGridAdapter = new SceneGridAdapter(new ArrayList<>());
        int div = getResources().getDimensionPixelSize(R.dimen.dp_10);
        GridDivider linearDivider =  new GridDivider(ContextCompat.getColor(this, R.color.nocolor), div, div);
        rlvScenesList.setAdapter(mSceneGridAdapter);
        rlvScenesList.addItemDecoration(linearDivider);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.comment_empty_view, rlvScenesList, false);
        TextView emptyTextView = emptyView.findViewById(R.id.tv_empty_tips);
        emptyTextView.setText(R.string.m260_no_data);
        mSceneGridAdapter.setEmptyView(emptyView);

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        presenter.getIntentData();
        try {
            presenter.getSceneById();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.tv_device_name, R.id.iv_scene_edit,R.id.ll_room, R.id.ll_chack_version, R.id.ll_reconfig, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_device_name:
            case R.id.iv_scene_edit:
                presenter.editName();
                break;
            case R.id.ll_room:
                presenter.transferDevice();
                break;
            case R.id.ll_chack_version:
                presenter.jumptoUpdata();
                break;
            case R.id.ll_reconfig:
                presenter.toConfigDeviceByType();
                break;
            case R.id.tv_delete:
                presenter.showWarnDialog();
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
        mSceneGridAdapter.setOnItemClickListener(this);
    }

    @Override
    public void setViewsByType(String deviceType) {
        switch (deviceType) {
            case DeviceTypeConstant.TYPE_AIRCONDITION:
                ivDeviceIcon.setImageResource(DeviceAirCon.getOpenIcon(0));
                break;
            case DeviceTypeConstant.TYPE_BULB:
                ivDeviceIcon.setImageResource(DeviceBulb.getOpenIcon(0));
                break;
            case DeviceTypeConstant.TYPE_PADDLE:
                ivDeviceIcon.setImageResource(DevicePlug.getOpenIcon(0));
                break;
            case DeviceTypeConstant.TYPE_THERMOSTAT:
                ivDeviceIcon.setImageResource(DeviceThermostat.getOpenIcon(0));
                break;
            case DeviceTypeConstant.TYPE_PANELSWITCH:
                ivDeviceIcon.setImageResource(DevicePanel.getOpenIcon(0));
                break;
            case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
                ivDeviceIcon.setImageResource(DeviceStripLights.getOpenIcon(0));
                break;
        }
    }

    @Override
    public void setDeviceName(String deviceName) {
        if (!TextUtils.isEmpty(deviceName)) {
            tvDeviceName.setText(deviceName);

        }
    }

    @Override
    public void updataList(List<ScenesBean.DataBean> data) {
        mSceneGridAdapter.replaceData(data);
    }

    @Override
    public void setRoomName(String roomName) {
        if (!TextUtils.isEmpty(roomName)) {
            tvRoomName.setText(roomName);
        }
    }

    @Override
    public void setDeviceId(String deviceId) {
        if (!TextUtils.isEmpty(deviceId)){
            tvDeviceId.setText(deviceId);
        }
    }

    @Override
    public void onError(String onError) {
        requestError(onError);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ScenesBean.DataBean dataBean = mSceneGridAdapter.getData().get(position);
        presenter.toSceneDetail(dataBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDevTransferBean(TransferDevMsg bean) {
        if (bean != null) {
            //获取列表设备列表
            try {
                if (!TextUtils.isEmpty(bean.roomName)) {
                    tvRoomName.setText(bean.roomName);
                }
                presenter.setRoomInfo(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
