package com.growatt.grohome.module.device;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.presenter.BulbPresenter;
import com.growatt.grohome.module.device.view.IBulbView;
import com.growatt.grohome.utils.MyToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BulbActivity extends BaseActivity<BulbPresenter> implements IBulbView , SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_white_light)
    ImageView ivWhiteLight;
    @BindView(R.id.iv_colour_light)
    ImageView ivColourLight;
    @BindView(R.id.iv_scenec_light)
    ImageView ivScenecLight;
    @BindView(R.id.v_bulb_backgroud_white)
    View vBulbBackgroudWhite;
    @BindView(R.id.iv_bulb_white)
    ImageView ivBulbWhite;
    @BindView(R.id.iv_brightness_white_left)
    ImageView ivBrightnessWhiteLeft;
    @BindView(R.id.seek_brightness_white)
    AppCompatSeekBar seekBrightnessWhite;
    @BindView(R.id.iv_brightness_right_white)
    ImageView ivBrightnessRightWhite;
    @BindView(R.id.iv_temp_left_white)
    ImageView ivTempLeftWhite;
    @BindView(R.id.seek_temp_white)
    AppCompatSeekBar seekTempWhite;
    @BindView(R.id.iv_temp_right_white)
    ImageView ivTempRightWhite;
    @BindView(R.id.v_bulb_background_colour)
    View vBulbBackgroundColour;
    @BindView(R.id.iv_bulb_colour)
    ImageView ivBulbColour;
    @BindView(R.id.iv_brightness_left_colour)
    ImageView ivBrightnessLeftColour;
    @BindView(R.id.seek_brightness_colour)
    AppCompatSeekBar seekBrightnessColour;
    @BindView(R.id.iv_brightness_right_colour)
    ImageView ivBrightnessRightColour;
    @BindView(R.id.iv_temp_left_colour)
    ImageView ivTempLeftColour;
    @BindView(R.id.seek_temp_colour)
    AppCompatSeekBar seekTempColour;
    @BindView(R.id.iv_temp_right_colour)
    ImageView ivTempRightColour;
    @BindView(R.id.v_bulb_background_scene)
    View vBulbBackgroundScene;
    @BindView(R.id.iv_bulb_scene)
    ImageView ivBulbScene;
    @BindView(R.id.tv_scene_mode)
    TextView tvSceneMode;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.rlv_scene)
    RecyclerView rlvScene;
    @BindView(R.id.rl_device_menu)
    FrameLayout rlDeviceMenu;
    @BindView(R.id.v_bottom_background)
    View vBottomBackground;
    @BindView(R.id.tv_leftdown)
    TextView tvLeftdown;
    @BindView(R.id.imb_switch)
    ImageButton imbSwitch;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.layout_white)
    View whiteClude;
    @BindView(R.id.layout_colour)
    View colourClude;
    @BindView(R.id.layout_scene)
    View sceneClude;



    @Override
    protected BulbPresenter createPresenter() {
        return new BulbPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bulb;
    }

    @Override
    protected void initViews() {
        showViewsByTab(DeviceBulb.BULB_MODE_WHITE);
        imbSwitch.setBackgroundResource(R.drawable.icon_on);
    }

    @Override
    protected void initData() {
        presenter.initDevice();
    }

    @Override
    public void setDeviceTitle(String devName) {
        tvTitle.setText(devName);
    }

    @Override
    public void setOnoff(String onoff) {
        if ("true".equals(onoff)) {//状态开启
            imbSwitch.setBackgroundResource(R.drawable.icon_on);
        } else {//关闭
            imbSwitch.setBackgroundResource(R.drawable.icon_off);
        }
    }

    @Override
    public void setBright(String bright) {
        if (!TextUtils.isEmpty(bright)){
            try {
                int brightValue = Integer.parseInt(bright)-10;
                seekBrightnessWhite.setProgress(brightValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setColour(String colour) {

    }

    @Override
    public void set(String controdata) {

    }

    @Override
    public void setCuntDown(String countdown) {

    }

    @Override
    public void setScene(String scene) {

    }

    @Override
    public void setMode(String mode) {
        showViewsByTab(mode);
    }

    @Override
    public void setTemp(String temp) {
        if (!TextUtils.isEmpty(temp)){
            try {
                int brightValue = Integer.parseInt(temp);
                seekTempWhite.setProgress(brightValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setControData(String controdata) {

    }

    @Override
    public void sendCommandSucces() {

    }

    @Override
    public void sendCommandError(String code, String error) {
        String msg = getString(R.string.m151_send_dps_failed)+":"+code+"--->"+error;
        MyToastUtils.toast(msg);
    }

    public void showViewsByTab(String mode) {
        if (DeviceBulb.BULB_MODE_WHITE.equals(mode)){//白光模式
            ivWhiteLight.setSelected(true);
            ivColourLight.setSelected(false);
            ivScenecLight.setSelected(false);
            whiteClude.setVisibility(View.VISIBLE);
            colourClude.setVisibility(View.GONE);
            sceneClude.setVisibility(View.GONE);
        }else if (DeviceBulb.BULB_MODE_COLOUR.equals(mode)){//彩光模式
            ivWhiteLight.setSelected(false);
            ivColourLight.setSelected(true);
            ivScenecLight.setSelected(false);
            whiteClude.setVisibility(View.GONE);
            colourClude.setVisibility(View.VISIBLE);
            sceneClude.setVisibility(View.GONE);
        }else {
            ivWhiteLight.setSelected(false);
            ivColourLight.setSelected(false);
            ivScenecLight.setSelected(true);
            whiteClude.setVisibility(View.GONE);
            colourClude.setVisibility(View.GONE);
            sceneClude.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.iv_white_light, R.id.iv_colour_light, R.id.iv_scenec_light,R.id.imb_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_white_light:
                presenter.bulbMode(DeviceBulb.BULB_MODE_WHITE);
                break;
            case R.id.iv_colour_light:
                presenter.bulbMode(DeviceBulb.BULB_MODE_COLOUR);
                break;
            case R.id.iv_scenec_light:
                presenter.bulbMode(DeviceBulb.BULB_MODE_SCENE);
                break;
            case R.id.imb_switch:
                presenter.bulbSwitch();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        seekTempWhite.setOnSeekBarChangeListener(this);
        seekBrightnessWhite.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar==seekBrightnessWhite){
            presenter.bulbBrightness(progress+10);
        }
        if (seekBar==seekTempWhite){
            int mProgree=seekTempWhite.getMax()-progress;
            presenter.bulbTemper(mProgree);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
