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
import com.growatt.grohome.module.config.DeviceConfigActivity;
import com.growatt.grohome.module.config.SelectConfigTypeActivity;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.config.view.IConnectHotsPotView;
import com.growatt.grohome.module.device.presenter.DeviceTypePresenter;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.BindDeviceUtils;

public class ConnectHotsPotPresenter extends BasePresenter<IConnectHotsPotView> {

    private  String deviceType;
    private  String ssid;
    private  String password;
    private  String tuyaToken;

    public ConnectHotsPotPresenter(IConnectHotsPotView baseView) {
        super(baseView);
    }

    public ConnectHotsPotPresenter(Context context, IConnectHotsPotView baseView) {
        super(context, baseView);
        deviceType = ((Activity) context).getIntent().getStringExtra(DeviceTypePresenter.DEVICE_TYPE);
        ssid = ((Activity) context).getIntent().getStringExtra(WiFiOptionsActivity.CONFIG_SSID);
        password = ((Activity) context).getIntent().getStringExtra(WiFiOptionsActivity.CONFIG_PASSWORD);
    }


    public void registerBroadcastReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
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
        intent.putExtra("ssid", ssid);
        intent.putExtra("password", password);
        intent.putExtra("token", tuyaToken);
        intent.putExtra(SelectConfigTypeActivity.CONFIG_MODE, SelectConfigTypeActivity.AP_MODE);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,false);
    }


    public void unRegisterWifiReceiver() throws Exception {
        if (mBroadcastReceiver != null) {
            context.unregisterReceiver(mBroadcastReceiver);
        }
    }

}
