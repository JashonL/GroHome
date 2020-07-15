package com.growatt.grohome.module.device.manager;

import com.growatt.grohome.R;

public class DeviceStripLights extends BaseDevice {
    @Override
    public String getType() {
        return DeviceTypeConstant.TYPE_PADDLE;
    }

    public static int getNameRes(){
        return R.string.m40_light_strip;
    }

    public static int getOpenIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_strip_off;
                break;
            case 1:
                res = R.drawable.device_card_strip_off;
                break;
            case 2:
                res = R.drawable.device_real_strip;
                break;
            case 3:
                res = R.drawable.device_real_strip_c;
                break;
            default:
                res = R.drawable.device_real_strip;
                break;
        }
        return res;
    }

    public static int getCloseIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_strip;
                break;
            case 1:
                res = R.drawable.device_card_strip;
                break;
            case 2:
                res = R.drawable.device_real_strip;
                break;
            case 3:
                res = R.drawable.device_real_strip_c;
                break;
            default:
                res = R.drawable.device_real_strip;
                break;
        }
        return res;
    }
}
