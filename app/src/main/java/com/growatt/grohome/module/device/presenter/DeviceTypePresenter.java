package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.DeviceConfigConstant;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.config.DeviceLightStatusActivity;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.device.view.IDeviceTypeView;
import com.growatt.grohome.utils.ActivityUtils;

public class DeviceTypePresenter extends BasePresenter<IDeviceTypeView> {

    public DeviceTypePresenter(IDeviceTypeView baseView) {
        super(baseView);
    }

    public DeviceTypePresenter(Context context, IDeviceTypeView baseView) {
        super(context, baseView);
    }

    public void toConfigDeviceByType(String devType, String configType) {
        Class clazz;
        if (DeviceConfigConstant.CONFIG_WIFI_BLUETHOOTH.equals(configType)) {
            clazz = DeviceLightStatusActivity.class;
        } else {
            clazz = WiFiOptionsActivity.class;
        }
        Intent intent = new Intent(context, clazz);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE, configType);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, devType);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }
}
