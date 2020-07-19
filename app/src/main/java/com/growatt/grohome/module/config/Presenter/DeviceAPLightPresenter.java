package com.growatt.grohome.module.config.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.config.ConnectHotsPotActivity;
import com.growatt.grohome.module.config.SelectConfigTypeActivity;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.config.view.IDeviceAPLightView;
import com.growatt.grohome.module.config.view.IDeviceEZLightView;
import com.growatt.grohome.module.device.presenter.DeviceTypePresenter;
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

    public DeviceAPLightPresenter(IDeviceAPLightView baseView) {
        super(baseView);
    }

    public DeviceAPLightPresenter(Context context, IDeviceAPLightView baseView) {
        super(context, baseView);
        deviceType = ((Activity) context).getIntent().getStringExtra(DeviceTypePresenter.DEVICE_TYPE);
        ssid = ((Activity) context).getIntent().getStringExtra(WiFiOptionsActivity.CONFIG_SSID);
        password = ((Activity) context).getIntent().getStringExtra(WiFiOptionsActivity.CONFIG_PASSWORD);
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
        Intent intent = new Intent(context, ConnectHotsPotActivity.class);
        intent.putExtra("ssid", ssid);
        intent.putExtra("password", password);
        intent.putExtra("token", tuyaToken);
        intent.putExtra(SelectConfigTypeActivity.CONFIG_MODE, SelectConfigTypeActivity.AP_MODE);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,false);
    }
}
