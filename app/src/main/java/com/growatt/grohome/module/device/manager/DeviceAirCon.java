package com.growatt.grohome.module.device.manager;

import com.growatt.grohome.R;

public class DeviceAirCon extends BaseDevice {
    @Override
    public String getType() {
        return DeviceTypeConstant.TYPE_AIRCONDITION;
    }

    public static int getNameRes(){
        return R.string.m41_air_con;
    }

    public static int getCloseIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_aircon_off;
                break;
            case 1:
                res = R.drawable.device_card_aircon_off;
                break;
            default:
                res = R.drawable.device_real_aircon;

                break;
        }
        return res;
    }

    public static int getOpenIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_aircon_on;
                break;

            case 1:
                res = R.drawable.device_card_aircon;
                break;
            default:
                res = R.drawable.device_real_aircon;
                break;
        }
        return res;
    }
}
