package com.growatt.grohome.module.device.manager;

import com.growatt.grohome.R;

public class DeviceThermostat extends BaseDevice {

    /********************功能dpid**************************/
    public static final String THERMMOSTAT_ONOFF="101";//开关


    @Override
    public String getType() {
        return DeviceTypeConstant.TYPE_THERMOSTAT;
    }

    public static int getNameRes(){
        return R.string.m38_thermostat;
    }

    public static int getCloseIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_thermostat_off;
                break;
            case 1:
                res = R.drawable.device_card_thermostat_off;
                break;
            default:
                res = R.drawable.device_real_thermostat;

                break;
        }
        return res;
    }

    public static int getOpenIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_thermostat;
                break;

            case 1:
                res = R.drawable.device_card_thermostat;
                break;
            default:
                res = R.drawable.device_real_thermostat;
                break;
        }
        return res;
    }



    /**************************获取dpid*************************************/
    public static String getSwitchThermostat(){
        return THERMMOSTAT_ONOFF;
    }


}
