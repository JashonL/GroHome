package com.growatt.grohome.module.device.manager;

import com.growatt.grohome.R;

public class DevicePanel extends BaseDevice {
    @Override
    public String getType() {
        return DeviceTypeConstant.TYPE_PANELSWITCH;
    }

    public static int getNameRes(){
        return R.string.m37_面板开关;
    }


    public static int getCloseIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_panel_off;
                break;
            case 1:
                res = R.drawable.device_card_panel_off;
                break;
            default:
                res = R.drawable.device_real_panel;

                break;
        }
        return res;
    }

    public static int getOpenIcon(int resIndex) {
        int res;
        switch (resIndex) {
            case 0:
                res = R.drawable.device_list_panel;
                break;
            case 1:
                res = R.drawable.device_card_panel;
                break;
            default:
                res = R.drawable.device_real_panel;
                break;
        }
        return res;
    }
}
