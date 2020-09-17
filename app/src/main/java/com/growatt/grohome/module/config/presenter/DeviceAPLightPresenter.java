package com.growatt.grohome.module.config.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.DeviceConfigConstant;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.config.ConnectHotsPotActivity;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.config.view.IDeviceAPLightView;
import com.growatt.grohome.tuya.FamilyManager;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;

public class DeviceAPLightPresenter extends BasePresenter<IDeviceAPLightView> {

    private  String deviceType;
    private  String ssid;
    private  String password;
    private  String tuyaToken;
    private String configType;

    public DeviceAPLightPresenter(IDeviceAPLightView baseView) {
        super(baseView);
    }

    public DeviceAPLightPresenter(Context context, IDeviceAPLightView baseView) {
        super(context, baseView);
        deviceType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_TYPE);
        ssid = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_SSID);
        password = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_PASSWORD);
        configType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_CONFIG_TYPE);
    }



    public void getTokenForConfigDevice() {
        baseView.showDialog();
        long homeId = FamilyManager.getInstance().getCurrentHomeId();
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                baseView.dissmissDialog();
                tuyaToken = token;
                toApConfig();
            }

            @Override
            public void onFailure(String s, String s1) {
                baseView.dissmissDialog();
                String errorMsg = s + ":" + s1;
                CircleDialogUtils.showGetTuyaTokenFail(context,((FragmentActivity)context).getSupportFragmentManager(),errorMsg);
            }
        });
    }


    private void toApConfig() {
        Class clazz;
        if (DeviceConfigConstant.CONFIG_WIFI_BLUETHOOTH.equals(configType)){
            clazz= WiFiOptionsActivity.class;
        }else {
            clazz= ConnectHotsPotActivity.class;
        }
        Intent intent = new Intent(context, clazz);
        intent.putExtra(GlobalConstant.WIFI_SSID, ssid);
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, password);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.WIFI_TOKEN, tuyaToken);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE,configType);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.AP_MODE);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,true);
    }
}
