package com.growatt.grohome.module.device.manager;

import com.growatt.grohome.R;

public class DevicePlug extends BaseDevice {
    @Override
    public String getType() {
        return DeviceTypeConstant.TYPE_PADDLE;
    }

    public static int getNameRes(){
        return R.string.m36_插座;
    }

    public static int getOpenIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_plug_off;
                break;
            case 1:
                res= R.drawable.device_card_plug_off;
                break;

            default:
                res=R.drawable.device_real_plug;
                break;
        }
        return res;
    }

    public static int getCloseIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_plug_on;
                break;
            case 1:
                res=R.drawable.device_card_plug;
                break;
            default:
                res=R.drawable.device_real_plug;
                break;
        }
        return res;
    }
}
