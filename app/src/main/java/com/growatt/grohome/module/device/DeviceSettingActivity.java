package com.growatt.grohome.module.device;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.device.presenter.DeviceSettingPresenter;
import com.growatt.grohome.module.device.view.IDeviceSettingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceSettingActivity extends BaseActivity<DeviceSettingPresenter> implements IDeviceSettingView {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_device_icon)
    ImageView ivDeviceIcon;
    @BindView(R.id.tv_scene_name)
    TextView tvSceneName;
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
    @BindView(R.id.rlv_launch_tap)
    RecyclerView rlvLaunchTap;

    @Override
    protected DeviceSettingPresenter createPresenter() {
        return new DeviceSettingPresenter(this, this);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView) .statusBarColor(R.color.white).init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_setting;
    }

    @Override
    protected void initViews() {
        tvTitle.setText("");
        toolbar.setNavigationIcon(R.drawable.icon_return);


    }

    @Override
    protected void initData() {
        presenter.getIntentData();
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
}
