package com.growatt.grohome.module.config;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.constants.DeviceConfigConstant;
import com.growatt.grohome.eventbus.DeviceAddOrDelMsg;
import com.growatt.grohome.module.config.Presenter.DeviceLightStatusPresenter;
import com.growatt.grohome.module.config.view.IDeviceLightStatusView;
import com.growatt.grohome.utils.Mydialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceLightStatusActivity extends BaseActivity<DeviceLightStatusPresenter> implements IDeviceLightStatusView, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.tv_sub_title)
    TextView tvSubTitle;
    @BindView(R.id.v_background)
    ImageView vBackground;
    @BindView(R.id.cb_flash_status)
    CheckBox cbFlashStatus;
    @BindView(R.id.tv_light_flash)
    TextView tvLightFlash;
    @BindView(R.id.btn_next)
    Button btnNext;


    private AnimationDrawable mFastLightAnimation;
    private AnimationDrawable mSlowLightAnimation;

    //跳转选择配网模式
    public static final int START_FOR_RESULT_CONFIG = 100;

    private int ecMode;


    @Override
    protected DeviceLightStatusPresenter createPresenter() {
        return new DeviceLightStatusPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_light_status;
    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }


    @Override
    protected void initViews() {
        //初始化头部
        tvMode.setText(R.string.m105_ez_mode);

        //默认不勾选
        cbFlashStatus.setChecked(false);
        btnNext.setEnabled(false);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        //默认EC模式
        ecMode = DeviceConfigConstant.EC_MODE;
        //动画
        mFastLightAnimation = new AnimationDrawable();
        mSlowLightAnimation = new AnimationDrawable();
        showAnim();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDevList(DeviceAddOrDelMsg msg) {
        if (msg.getType() == DeviceAddOrDelMsg.ADD_DEV) finish();
    }


    @Override
    protected void initListener() {
        super.initListener();
        cbFlashStatus.setOnCheckedChangeListener(this);
    }


    @OnClick({R.id.iv_return, R.id.tv_mode, R.id.iv_switch, R.id.tv_device_reset, R.id.iv_toguide, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                finish();
                break;
            case R.id.btn_next:
                if (ecMode == DeviceConfigConstant.EC_MODE) {
                    presenter.toEcbindConfig();
                } else if (ecMode==DeviceConfigConstant.AP_MODE){
                    presenter.getTokenForConfigDevice();
                }else {
                    presenter.toBluetoothConfig();
                }
                break;
            case R.id.iv_toguide:
            case R.id.tv_device_reset:
                if (ecMode == DeviceConfigConstant.EC_MODE||ecMode==DeviceConfigConstant.BLUETOOTH_MODE) {
                    presenter.toLightReset();
                } else {
                    presenter.toHostGuide();
                }
                break;
            case R.id.tv_mode:
            case R.id.iv_switch:
                presenter.showRightMenu(tvMode);
                break;
        }
    }


    private void showAnim() {
        vBackground.clearAnimation();
        if (ecMode == DeviceConfigConstant.AP_MODE) {
            tvSubTitle.setText(R.string.m120_flashing_slowly_set);
            tvLightFlash.setText(R.string.m121_make_sure_flashing_slowly);
            tvMode.setText(R.string.m102_ap_mode);
            mSlowLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.image_device_light_on), 1500);
            mSlowLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.image_device_light_off), 1500);
            mSlowLightAnimation.setOneShot(false);
            vBackground.setImageDrawable(mSlowLightAnimation);
            mSlowLightAnimation.start();
        } else if (ecMode==DeviceConfigConstant.EC_MODE){
            tvSubTitle.setText(R.string.m49_flash_fast_set);
            tvLightFlash.setText(R.string.m52_make_sure_flashing_fast);
            tvMode.setText(R.string.m105_ez_mode);
            mFastLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.image_device_light_on), 500);
            mFastLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.image_device_light_off), 500);
            mFastLightAnimation.setOneShot(false);
            vBackground.setImageDrawable(mFastLightAnimation);
            mFastLightAnimation.start();
        }else if (ecMode==DeviceConfigConstant.BLUETOOTH_MODE){
            tvSubTitle.setText(R.string.m49_flash_fast_set);
            tvLightFlash.setText(R.string.m52_make_sure_flashing_fast);
            tvMode.setText(R.string.m119_Bluetooth);
            mFastLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.image_device_light_on), 500);
            mFastLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.image_device_light_off), 500);
            mFastLightAnimation.setOneShot(false);
            vBackground.setImageDrawable(mFastLightAnimation);
            mFastLightAnimation.start();
        }
    }


    @Override
    public void showDialog() {
        Mydialog.show(this, "");
    }

    @Override
    public void dissmissDialog() {
        Mydialog.dissmiss();
    }

    @Override
    public void setMode(int mode) {
        ecMode = mode;
        showAnim();
    }

    @Override
    public void getTuyaTokenSuccess() {

    }


    @Override
    public void getTuyaTokenFails(String errorCode, String errorMsg) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mFastLightAnimation != null && mFastLightAnimation.isRunning()) {
            mFastLightAnimation.stop();
        }
        if (mSlowLightAnimation != null && mSlowLightAnimation.isRunning()) {
            mSlowLightAnimation.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFastLightAnimation != null && !mFastLightAnimation.isRunning()) {
            mFastLightAnimation.start();
        }
        if (mSlowLightAnimation != null && !mSlowLightAnimation.isRunning()) {
            mSlowLightAnimation.start();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mFastLightAnimation != null && mFastLightAnimation.isRunning()) {
            mFastLightAnimation.stop();
        }
        if (mSlowLightAnimation != null && mSlowLightAnimation.isRunning()) {
            mSlowLightAnimation.stop();
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cb_flash_status) {
            if (isChecked) {
                btnNext.setEnabled(true);
            } else {
                btnNext.setEnabled(false);
            }
        }
    }

}
