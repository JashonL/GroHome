package com.growatt.grohome.module.device.manager;

import com.growatt.grohome.R;

public class DeviceBulb extends BaseDevice {
    @Override
    public String getType() {
        return DeviceTypeConstant.TYPE_PADDLE;
    }

    public static int getNameRes(){
        return R.string.m39_bulb;
    }

    public static int getOpenIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_bulb_off;
                break;
            case 1:
                res = R.drawable.device_card_bulb_off;
                break;
            case 2:
                res = R.drawable.device_real_bulb;
                break;
            case 3:
                res = R.drawable.device_real_bulb_c;
                break;
            default:
                res = R.drawable.device_real_bulb;
                break;
        }
        return res;
    }

    public static int getCloseIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_bulb;
                break;
            case 1:
                res = R.drawable.device_card_bulb;
                break;
            case 2:
                res = R.drawable.device_real_bulb;
                break;
            case 3:
                res = R.drawable.device_real_bulb_c;
                break;
            default:
                res = R.drawable.device_real_bulb;
                break;
        }
        return res;
    }
}
