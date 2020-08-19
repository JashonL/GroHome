package com.growatt.grohome.module.config;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.eventbus.DeviceAddOrDelMsg;
import com.growatt.grohome.module.config.Presenter.DeviceLightStatusPresenter;
import com.growatt.grohome.module.config.view.IDeviceLightStatusView;
import com.yechaoa.yutils.YUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceLightStatusActivity extends BaseActivity<DeviceLightStatusPresenter> implements IDeviceLightStatusView, Toolbar.OnMenuItemClickListener, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    private MenuItem switchItem;
    private TextView tvSwitchItem;

    @Override
    protected DeviceLightStatusPresenter createPresenter() {
        return new DeviceLightStatusPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_light_status;
    }

    @Override
    protected void initViews() {
        //初始化头部
        tvTitle.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.inflateMenu(R.menu.menu_device_light_status);
        switchItem = toolbar.getMenu().findItem(R.id.action_switch_text);
        switchItem.setActionView(R.layout.menu_config_switch);
        tvSwitchItem=switchItem.getActionView().findViewById(R.id.tv_config_mode);
        toolbar.setOnMenuItemClickListener(this);
        //默认不勾选
        cbFlashStatus.setChecked(false);
        btnNext.setEnabled(false);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        //默认EC模式
        ecMode = SelectConfigTypeActivity.EC_MODE;
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
        toolbar.setNavigationOnClickListener(v -> finish());
        cbFlashStatus.setOnCheckedChangeListener(this);
        tvSwitchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.toSwitchMode();
            }
        });
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch_pic:
            case R.id.action_switch_text:
                presenter.toSwitchMode();
                break;
        }
        return true;
    }



    @OnClick({R.id.tv_device_reset, R.id.iv_toguide, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (ecMode == SelectConfigTypeActivity.EC_MODE) {
                    presenter.toEcbindConfig();
                } else {
                    presenter.getTokenForConfigDevice();
                }
                break;
            case R.id.iv_toguide:
            case R.id.tv_device_reset:
                if (ecMode == SelectConfigTypeActivity.EC_MODE) {
                    presenter.toLightReset();
                } else {
                    presenter.toHostGuide();
                }
                break;
        }
    }



    private void showAnim() {
        vBackground.clearAnimation();
        if (ecMode == SelectConfigTypeActivity.EC_MODE) {
            tvSubTitle.setText(R.string.m49_flash_fast_set);
            tvLightFlash.setText(R.string.m52_make_sure_flashing_fast);
            switchItem.setTitle(R.string.m105_ez_mode);
            tvSwitchItem.setText(R.string.m105_ez_mode);
            mFastLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.image_device_light_on), 500);
            mFastLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.image_device_light_off), 500);
            mFastLightAnimation.setOneShot(false);
            vBackground.setImageDrawable(mFastLightAnimation);
            mFastLightAnimation.start();
        } else {
            tvSubTitle.setText(R.string.m120_flashing_slowly_set);
            tvLightFlash.setText(R.string.m121_make_sure_flashing_slowly);
            switchItem.setTitle(R.string.m102_ap_mode);
            tvSwitchItem.setText(R.string.m102_ap_mode);
            mSlowLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.image_device_light_on), 1500);
            mSlowLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.image_device_light_off), 1500);
            mSlowLightAnimation.setOneShot(false);
            vBackground.setImageDrawable(mSlowLightAnimation);
            mSlowLightAnimation.start();
        }
    }


    @Override
    public void showDialog() {
        YUtils.showLoading("");
    }

    @Override
    public void dissmissDialog() {
        YUtils.dismissLoading();
    }

    @Override
    public void getTuyaTokenSuccess() {

    }


    @Override
    public void getTuyaTokenFails(String errorCode, String errorMsg) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == START_FOR_RESULT_CONFIG) {
                if (resultCode == RESULT_OK) {
                ecMode = data.getIntExtra(SelectConfigTypeActivity.CONFIG_MODE, SelectConfigTypeActivity.EC_MODE);
                showAnim();
            }
        }

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
