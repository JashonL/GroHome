package com.growatt.grohome.module.device.manager;

import com.growatt.grohome.R;

public class DevicePlug extends BaseDevice {
    /********************功能dpid**************************/
    public static final String PLUG_ONOFF="101";//开关


    @Override
    public String getType() {
        return DeviceTypeConstant.TYPE_PADDLE;
    }

    public static int getNameRes(){
        return R.string.m36_socket;
    }

    public static int getCloseIcon (int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_plug_off;
                break;
            case 1:
                res= R.drawable.device_card_plug_off;
                break;
            case 2:
                res=R.drawable.device_s_plug;
                break;
            default:
                res=R.drawable.device_real_plug;
                break;
        }
        return res;
    }

    public static int getOpenIcon(int resIndex) {
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


    public static String getPlugOnoff() {
        return PLUG_ONOFF;
    }
}
