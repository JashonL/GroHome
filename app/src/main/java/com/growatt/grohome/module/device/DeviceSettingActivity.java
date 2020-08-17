package com.growatt.grohome.module.device;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.DeviceTimingAdapter;
import com.growatt.grohome.adapter.SceneGridAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.ScenesBean;
import com.growatt.grohome.customview.GridDivider;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.device.manager.DeviceAirCon;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DevicePanel;
import com.growatt.grohome.module.device.manager.DevicePlug;
import com.growatt.grohome.module.device.manager.DeviceThermostat;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.presenter.DeviceSettingPresenter;
import com.growatt.grohome.module.device.view.IDeviceSettingView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.iv_scene_edit)
    ImageView ivSceneEdit;
    @BindView(R.id.v_divider_room)
    View vDividerRoom;
    @BindView(R.id.iv_room_more)
    ImageView ivRoomMore;
    @BindView(R.id.ll_room)
    LinearLayout llRoom;
    @BindView(R.id.v_divider_sn)
    View vDividerSn;
    @BindView(R.id.iv_sn_more)
    ImageView ivSnMore;
    @BindView(R.id.ll_sn)
    LinearLayout llSn;
    @BindView(R.id.v_divider_chack_version)
    View vDividerChackVersion;
    @BindView(R.id.iv_chack_version)
    ImageView ivChackVersion;
    @BindView(R.id.ll_chack_version)
    LinearLayout llChackVersion;
    @BindView(R.id.v_divider_reconfig)
    View vDividerReconfig;
    @BindView(R.id.iv_reconfig)
    ImageView ivReconfig;
    @BindView(R.id.ll_reconfig)
    LinearLayout llReconfig;
    @BindView(R.id.tv_third_party)
    AppCompatTextView tvThirdParty;
    @BindView(R.id.icon_alexa)
    ImageView iconAlexa;
    @BindView(R.id.tv_alexa)
    AppCompatTextView tvAlexa;
    @BindView(R.id.cl_alexa)
    ConstraintLayout clAlexa;
    @BindView(R.id.icon_google)
    ImageView iconGoogle;
    @BindView(R.id.tv_google)
    AppCompatTextView tvGoogle;
    @BindView(R.id.cl_google)
    ConstraintLayout clGoogle;
    @BindView(R.id.card_view_third)
    CardView cardViewThird;
    @BindView(R.id.tv_add_scenes)
    AppCompatTextView tvAddScenes;
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
        tvTitle.setText("");
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
        presenter.getIntentData();
        try {
            presenter.getSceneById();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.tv_device_name, R.id.ll_room, R.id.ll_chack_version, R.id.ll_reconfig, R.id.tv_delete})
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ScenesBean.DataBean dataBean = mSceneGridAdapter.getData().get(position);
        presenter.toSceneDetail(dataBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
