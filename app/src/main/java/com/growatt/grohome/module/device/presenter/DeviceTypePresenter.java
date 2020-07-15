package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.view.IDeviceTypeView;
import com.growatt.grohome.utils.ActivityUtils;

public class DeviceTypePresenter extends BasePresenter<IDeviceTypeView> {

    public static final String DEVICE_TYPE="deviceType";

    public DeviceTypePresenter(IDeviceTypeView baseView) {
        super(baseView);
    }

    public DeviceTypePresenter(Context context, IDeviceTypeView baseView) {
        super(context, baseView);
    }

    public void toConfigDeviceByType(String devType){
        Intent intent=new Intent(context, WiFiOptionsActivity.class);
        intent.putExtra(DEVICE_TYPE,devType);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,false);
    }
}
