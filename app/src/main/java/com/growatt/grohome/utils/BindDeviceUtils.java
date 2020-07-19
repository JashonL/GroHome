package com.growatt.grohome.utils;

import android.text.TextUtils;

import com.growatt.grohome.constants.GlobalConstant;
import com.tuya.smart.android.device.utils.WiFiUtil;
import com.tuya.smart.sdk.TuyaSdk;


public class BindDeviceUtils {

    public static boolean isAPMode() {
        String ssid = "";
        if (TextUtils.isEmpty(ssid)) {
            ssid = GlobalConstant.DEFAULT_COMMON_AP_SSID;
        }
        String currentSSID = WiFiUtil.getCurrentSSID(TuyaSdk.getApplication()).toLowerCase();
        return !TextUtils.isEmpty(currentSSID) && (currentSSID.startsWith(ssid.toLowerCase()) ||
                currentSSID.startsWith(GlobalConstant.DEFAULT_OLD_AP_SSID.toLowerCase()) ||
                currentSSID.contains(GlobalConstant.DEFAULT_KEY_AP_SSID.toLowerCase()));
    }
}
