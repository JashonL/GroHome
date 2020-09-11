package com.growatt.grohome.module.config.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.config.DeviceLightStatusActivity;
import com.growatt.grohome.module.config.SelectConfigTypeActivity;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.config.view.IConfigErrorView;

public class ConfigErrorPresenter extends BasePresenter<IConfigErrorView> {
    private  String deviceType;
    private  String ssid;
    private  String password;
    private  String tuyaToken;
    private  int mConfigMode;

    public ConfigErrorPresenter(IConfigErrorView baseView) {
        super(baseView);
    }

    public ConfigErrorPresenter(Context context, IConfigErrorView baseView) {
        super(context, baseView);
        deviceType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_TYPE);
        ssid = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_SSID);
        password = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_PASSWORD);
        mConfigMode = ((Activity) context).getIntent().getIntExtra(SelectConfigTypeActivity.CONFIG_MODE, SelectConfigTypeActivity.EC_MODE);
    }


    public void setTisViews(){
        baseView.showTipsByMode(mConfigMode);
    }


    public void reTryConfig() {
        Intent intent = new Intent(context, WiFiOptionsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        context.startActivity(intent);
        ((FragmentActivity)context).finish();
    }


    public void toApConfig() {
        Intent intent = new Intent(context, DeviceLightStatusActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(GlobalConstant.WIFI_SSID, ssid);
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, password);
        intent.putExtra(GlobalConstant.WIFI_TOKEN, tuyaToken);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(SelectConfigTypeActivity.CONFIG_MODE, SelectConfigTypeActivity.AP_MODE);
        context.startActivity(intent);
        ((FragmentActivity)context).finish();
    }

}
