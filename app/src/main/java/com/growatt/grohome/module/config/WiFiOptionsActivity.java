package com.growatt.grohome.module.config;

import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.config.Presenter.WiFiOptionsPresenter;
import com.growatt.grohome.module.config.view.IWiFiOptionsView;
import com.growatt.grohome.utils.ActivityUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WiFiOptionsActivity extends BaseActivity<WiFiOptionsPresenter> implements IWiFiOptionsView {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_ssid)
    EditText edtSsid;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_switch_password)
    ImageView ivSwitchPassword;


    private boolean passwordOn = false;

    public static final String  CONFIG_PASSWORD="wifi_password";
    public static final String  CONFIG_SSID="wifi_ssid";


    @Override
    protected WiFiOptionsPresenter createPresenter() {
        return new WiFiOptionsPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.acitivity_wifi_options;
    }

    @Override
    protected void initViews() {
        //初始化头部
        tvTitle.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.icon_return);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.registerBroadcastReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            presenter.unRegisterWifiReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setWifiSsid(String wifissid) {
        if (TextUtils.isEmpty(wifissid)) {
            edtSsid.setFocusable(true);
            edtSsid.setFocusableInTouchMode(true);
            edtSsid.requestFocus();
        } else {
            //密码输入获取焦点
            etPassword.setFocusable(true);
            etPassword.setFocusableInTouchMode(true);
            etPassword.requestFocus();
        }
        edtSsid.setText(wifissid);
    }

    @Override
    public String getWifissid() {
        return edtSsid.getText().toString();
    }

    @Override
    public String getWifiPassWord() {
        return etPassword.getText().toString();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        presenter.checkWifiNetworkStatus();
    }

    @OnClick({R.id.iv_switch_password, R.id.btn_next,R.id.iv_switch_wifi,R.id.tv_setwifi_guide,R.id.iv_toguide})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch_password:
                clickPasswordSwitch();
                break;
            case R.id.btn_next:
                presenter.goNext();
                break;
            case R.id.iv_switch_wifi:
               ActivityUtils.gotoWiFiSetTingActivity(this);
                break;
            case R.id.iv_toguide:
            case R.id.tv_setwifi_guide:
                ActivityUtils.gotoActivity(this,WiFiSetGuideActivity.class,ActivityUtils.ANIMATE_FORWARD,false);
                break;
        }
    }


    public void clickPasswordSwitch() {
        passwordOn = !passwordOn;
        if (passwordOn) {
            ivSwitchPassword.setImageResource(R.drawable.icon_signin_see);
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            ivSwitchPassword.setImageResource(R.drawable.icon_signin_see);
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        if (etPassword.getText().length() > 0) {
            etPassword.setSelection(etPassword.getText().length());
        }
    }

}
