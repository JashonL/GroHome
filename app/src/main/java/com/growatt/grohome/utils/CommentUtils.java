package com.growatt.grohome.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import java.util.List;
import java.util.Objects;

public class CommentUtils {
    //判断WiFi是否打开
    public static boolean isWiFi(Context context) throws Exception{
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo==null)return false;
        return networkInfo.isConnected();
    }

    /**
     * 获取SSID
     *
     * @param activity 上下文
     * @return WIFI 的SSID
     */
    public static String getWiFiSsid(Activity activity) throws Exception{
        String ssid = null;
        ConnectivityManager manager = (ConnectivityManager)activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo.State wifi = Objects.requireNonNull(manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState();
        if (wifi ==null) {
            return null;
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            assert mWifiManager != null;
            WifiInfo info = mWifiManager.getConnectionInfo();
            return info.getSSID().replace("\"", "");
        } else {
            WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (mWifiManager != null) {
                WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
                int networkId = connectionInfo.getNetworkId();
                List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
                for (WifiConfiguration wificonf : configuredNetworks) {
                    if (wificonf.networkId == networkId) {
                        ssid = wificonf.SSID;
                        break;
                    }
                }
            }
            if (TextUtils.isEmpty(ssid)) return ssid;
            if (ssid.contains("\"")) {
                ssid = ssid.replace("\"", "");
            }
        }
        return ssid;
    }



    /**
     * 判断wifi是否是5G
     *
     * @param ssid    wifi名称
     * @param context 上下文
     * @return
     */

    public static boolean is5GHz(String ssid, Context context) {
        try {
            WifiManager wifiManger = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            assert wifiManger != null;
            WifiInfo wifiInfo = wifiManger.getConnectionInfo();
            if (wifiInfo != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int freq = wifiInfo.getFrequency();
                return freq > 4900 && freq < 5900;
            } else return ssid.toUpperCase().endsWith("5G");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
