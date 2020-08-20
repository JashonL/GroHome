package com.growatt.grohome.module.config.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.config.DeviceConfigActivity;
import com.growatt.grohome.module.config.SelectConfigTypeActivity;
import com.growatt.grohome.module.config.view.IDeviceEZLightView;
import com.growatt.grohome.utils.ActivityUtils;

public class DeviceEZLightPresenter extends BasePresenter<IDeviceEZLightView> {

    private  String deviceType;
    private  String ssid;
    private  String password;

    public DeviceEZLightPresenter(IDeviceEZLightView baseView) {
        super(baseView);

    }

    public DeviceEZLightPresenter(Context context, IDeviceEZLightView baseView) {
        super(context, baseView);
        deviceType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_TYPE);
        ssid = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_SSID);
        password = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_PASSWORD);
    }



    public void toEcbindConfig() {
        Intent intent = new Intent(context, DeviceConfigActivity.class);
        intent.putExtra(GlobalConstant.WIFI_SSID, ssid);
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, password);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(SelectConfigTypeActivity.CONFIG_MODE, SelectConfigTypeActivity.EC_MODE);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,true);
    }
}
