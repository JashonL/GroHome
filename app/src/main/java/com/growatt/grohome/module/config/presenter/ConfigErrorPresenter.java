package com.growatt.grohome.module.config.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.DeviceConfigConstant;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.config.DeviceLightStatusActivity;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.config.view.IConfigErrorView;

public class ConfigErrorPresenter extends BasePresenter<IConfigErrorView> {
    private  String deviceType;
    private  String ssid;
    private  String password;
    private  String tuyaToken;
    private  int mConfigMode;
    private String configType;

    public ConfigErrorPresenter(IConfigErrorView baseView) {
        super(baseView);
    }

    public ConfigErrorPresenter(Context context, IConfigErrorView baseView) {
        super(context, baseView);
        //先获取设备配网的类型
        configType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_CONFIG_TYPE);

        deviceType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_TYPE);
        ssid = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_SSID);
        tuyaToken=((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_TOKEN);
        password = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_PASSWORD);
        mConfigMode = ((Activity) context).getIntent().getIntExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.EC_MODE);
    }


    public void setTisViews(){
        baseView.showTipsByMode(mConfigMode);
    }


    public void reTryConfig() {
        Class clazz;
        if (DeviceConfigConstant.BLUETOOTH_MODE==mConfigMode){
            clazz= DeviceLightStatusActivity.class;
        }else {
            clazz=WiFiOptionsActivity.class;
        }
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE,configType);

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
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE,configType);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.AP_MODE);
        context.startActivity(intent);
        ((FragmentActivity)context).finish();
    }

}
