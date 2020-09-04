package com.growatt.grohome.module.device.manager;

import com.growatt.grohome.R;
import com.growatt.grohome.bean.SwitchDpBean;

import java.util.HashMap;
import java.util.Map;

public class DevicePanel extends BaseDevice {

    public static Map<String, SwitchDpBean> sechMap = new HashMap<>();


    @Override
    public String getType() {
        return DeviceTypeConstant.TYPE_PANELSWITCH;
    }

    public static int getNameRes(){
        return R.string.m37_panel_switch;
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
            case 2:
                res=R.drawable.device_s_panel;
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
