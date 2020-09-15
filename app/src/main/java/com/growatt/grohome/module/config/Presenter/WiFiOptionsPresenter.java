package com.growatt.grohome.module.config.Presenter;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.AllPermissionRequestCode;
import com.growatt.grohome.constants.DeviceConfigConstant;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.config.ConnectHotsPotActivity;
import com.growatt.grohome.module.config.DeviceConfigActivity;
import com.growatt.grohome.module.config.DeviceLightStatusActivity;
import com.growatt.grohome.module.config.view.IWiFiOptionsView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;

import pub.devrel.easypermissions.EasyPermissions;

public class WiFiOptionsPresenter extends BasePresenter<IWiFiOptionsView> {

    private String deviceType;

    private String scanJson;

    private String configType;//设备的配网类型
    private DialogFragment dialogFragment;

    private String tuyaToken;
    private int mConfigMode;


    public WiFiOptionsPresenter(IWiFiOptionsView baseView) {
        super(baseView);
    }

    public WiFiOptionsPresenter(Context context, IWiFiOptionsView baseView) {
        super(context, baseView);
        //先获取设备的配网类型
        configType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_CONFIG_TYPE);

        deviceType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_TYPE);
        tuyaToken = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_TOKEN);
        mConfigMode = ((Activity) context).getIntent().getIntExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.EC_MODE);
    }


    public void registerBroadcastReceiver() {
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
                switch (action) {
                    case ConnectivityManager.CONNECTIVITY_ACTION:
//                    case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    case "android.net.wifi.CONFIGURED_NETWORKS_CHANGE":
                    case "android.net.wifi.LINK_CONFIGURATION_CHANGED":
                    case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
                        checkWifiNetworkStatus();
                        break;
                }
            }
        }
    };


    public void checkWifiNetworkStatus() {
        try {
            if (CommentUtils.isWiFi(context)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//8.1
                    if (EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        gpsStatus();
                    } else {
                        EasyPermissions.requestPermissions((Activity) context, String.format("%s:%s", context.getString(R.string.m93_request_permission), context.getString(R.string.m94_location)), AllPermissionRequestCode.PERMISSION_LOCATION_CODE, Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                } else {
                    String ssid = CommentUtils.getWiFiSsid((Activity) context);
                    baseView.setWifiSsid(ssid);
                }
            } else {
                baseView.setWifiSsid("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void unRegisterWifiReceiver() throws Exception {
        if (mBroadcastReceiver != null) {
            context.unregisterReceiver(mBroadcastReceiver);
        }
    }


    public void showNoNetworkDialog(FragmentManager fragmentManager) {
        CircleDialogUtils.showSetWifiDialog(context, fragmentManager);
    }


    public void show5gNetworkDialog(FragmentManager fragmentManager) {
        CircleDialogUtils.show5GHzDialog(context, fragmentManager, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toConfigByType();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.gotoWiFiSetTingActivity((Activity) context);
            }
        });
    }


    public void goNext() {
        String password = baseView.getWifiPassWord();
        String ssid = baseView.getWifissid();
        if (TextUtils.isEmpty(password)) {
            MyToastUtils.toast(R.string.m98_enter_wifi_password);
            return;
        }
        if (TextUtils.isEmpty(ssid)) {
            MyToastUtils.toast(R.string.m45_enter_wifi_name);
            return;
        }
        try {
            if (!CommentUtils.isWiFi(context)) {
                showNoNetworkDialog(((FragmentActivity) context).getSupportFragmentManager());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CommentUtils.is5GHz(ssid, context)) {
            show5gNetworkDialog(((FragmentActivity) context).getSupportFragmentManager());
            return;
        }

        toConfigByType();

    }

    private void toConfigByType() {
        if (DeviceConfigConstant.CONFIG_WIFI_BLUETHOOTH.equals(configType)) {
            scanJson = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_SCAN_BEAN);
            if (mConfigMode == DeviceConfigConstant.EC_MODE) {
                toEcbindConfig();
            } else if (mConfigMode == DeviceConfigConstant.AP_MODE) {
                toApConfig();
            } else {
                toBlueToothConfig();
            }
        } else {
            toConfig();
        }
    }


    public void toConfig() {
        Intent intent = new Intent(context, DeviceLightStatusActivity.class);
        intent.putExtra(GlobalConstant.WIFI_SSID, baseView.getWifissid());
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, baseView.getWifiPassWord());
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE, configType);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    private void toApConfig() {
        Intent intent = new Intent(context, ConnectHotsPotActivity.class);
        intent.putExtra(GlobalConstant.WIFI_SSID, baseView.getWifissid());
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, baseView.getWifiPassWord());
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.WIFI_TOKEN, tuyaToken);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE, configType);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.AP_MODE);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void toEcbindConfig() {
        Intent intent = new Intent(context, DeviceConfigActivity.class);
        intent.putExtra(GlobalConstant.WIFI_SSID, baseView.getWifissid());
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, baseView.getWifiPassWord());
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.WIFI_TOKEN, tuyaToken);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE, configType);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.EC_MODE);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void toBlueToothConfig() {
        Intent intent = new Intent(context, DeviceConfigActivity.class);
        intent.putExtra(GlobalConstant.WIFI_SSID, baseView.getWifissid());
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, baseView.getWifiPassWord());
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.BLUETOOTH_MODE);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.DEVICE_SCAN_BEAN, scanJson);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE, configType);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /**
     * 判断Gps是否打开
     */
    private void gpsStatus() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm != null) {
            boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (ok) {
                try {
                    String ssid = CommentUtils.getWiFiSsid((Activity) context);
                    baseView.setWifiSsid(ssid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showGpsDialog();
            }
        }
    }


    /**
     * 开启GPS弹框
     */

    private void showGpsDialog() {
        if (dialogFragment == null) {
            dialogFragment = CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m315_turn_on_gps), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    ((Activity) context).startActivityForResult(intent, GlobalConstant.ACTION_LOCATION_CODE);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityFinish();
                }
            }, false);
        }

    }


}
