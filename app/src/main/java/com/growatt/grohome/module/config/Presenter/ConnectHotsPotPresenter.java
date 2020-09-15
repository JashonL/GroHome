package com.growatt.grohome.module.config.Presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.DeviceConfigConstant;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.config.DeviceConfigActivity;
import com.growatt.grohome.module.config.view.IConnectHotsPotView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.BindDeviceUtils;

public class ConnectHotsPotPresenter extends BasePresenter<IConnectHotsPotView> {

    private  String deviceType;
    private  String ssid;
    private  String password;
    private  String tuyaToken;
    private String configType;//设备的配网类型

    public ConnectHotsPotPresenter(IConnectHotsPotView baseView) {
        super(baseView);
    }

    public ConnectHotsPotPresenter(Context context, IConnectHotsPotView baseView) {
        super(context, baseView);
        //先获取设备的配网类型
        configType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_CONFIG_TYPE);

        deviceType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_TYPE);
        ssid = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_SSID);
        password = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_PASSWORD);
        tuyaToken= ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_TOKEN);

    }


    public void registerBroadcastReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction("android.net.wifi.CONFIGURED_NETWORKS_CHANGE");
        filter.addAction("android.net.wifi.LINK_CONFIGURATION_CHANGED");
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        context.registerReceiver(mBroadcastReceiver, filter);
    }



    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                    if (baseView.getVisible()) return;
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.isAvailable() && networkInfo.isConnected()) {
                        boolean apMode = BindDeviceUtils.isAPMode();
                        if (apMode) {
                            toEcbindConfig();
                        }
                    }
                }
            }
        }
    };


    public void toEcbindConfig() {
        Intent intent = new Intent(context, DeviceConfigActivity.class);
        intent.putExtra(GlobalConstant.WIFI_SSID, ssid);
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, password);
        intent.putExtra(GlobalConstant.WIFI_TOKEN, tuyaToken);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.AP_MODE);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,true);
    }


    public void unRegisterWifiReceiver() throws Exception {
        if (mBroadcastReceiver != null) {
            context.unregisterReceiver(mBroadcastReceiver);
        }
    }

}
