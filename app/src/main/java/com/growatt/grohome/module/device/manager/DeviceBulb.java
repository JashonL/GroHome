package com.growatt.grohome.module.device.manager;

import com.growatt.grohome.R;

import java.util.HashMap;
import java.util.Map;

public class DeviceBulb extends BaseDevice {

    public static final String BULB_SWITCH_LED="20";//开关
    public static final String BULB_WORK_MODE="21";//模式
    public static final String BULB_BRIGHT_VALUE="22";//亮度值
    public static final String BULB_TEMP_VALUE="23";//冷暖值
    public static final String BULB_COLOUR_DATA="24";//彩光
    public static final String BULB_SCENE_DATA="25";//场景
    public static final String BULB_COUNTDOWN="26";//倒计时剩余时间
    public static final String BULB_CONTROL_DATA="28";//调节


    public static final String BULB_MODE_WHITE="white";//彩光
    public static final String BULB_MODE_COLOUR="colour";//场景
    public static final String BULB_MODE_SCENE="scene";//倒计时剩余时间
    public static final String BULB_MODE_MUSIC="music";//调节


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


    public static String getBulbSwitchLed(){
        return BULB_SWITCH_LED;
    }

    public static String getBulbWorkMode() {
        return BULB_WORK_MODE;
    }

    public static String getBulbBrightValue() {
        return BULB_BRIGHT_VALUE;
    }

    public static String getBulbTempValue() {
        return BULB_TEMP_VALUE;
    }

    public static String getBulbColourData() {
        return BULB_COLOUR_DATA;
    }

    public static String getBulbSceneData() {
        return BULB_SCENE_DATA;
    }

    public static String getBulbCountdown() {
        return BULB_COUNTDOWN;
    }

    public static String getBulbControlData() {
        return BULB_CONTROL_DATA;
    }
}
