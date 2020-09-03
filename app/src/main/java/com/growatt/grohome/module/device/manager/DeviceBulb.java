package com.growatt.grohome.module.device.manager;

import com.growatt.grohome.R;
import com.growatt.grohome.utils.CommentUtils;

import java.util.ArrayList;
import java.util.List;

public class DeviceBulb extends BaseDevice {

    /********************功能dpid**************************/
    public static final String BULB_SWITCH_LED="20";//开关
    public static final String BULB_WORK_MODE="21";//模式
    public static final String BULB_BRIGHT_VALUE="22";//亮度值
    public static final String BULB_TEMP_VALUE="23";//冷暖值
    public static final String BULB_COLOUR_DATA="24";//彩光
    public static final String BULB_SCENE_DATA="25";//场景
    public static final String BULB_COUNTDOWN="26";//倒计时剩余时间
    public static final String BULB_CONTROL_DATA="28";//调节

    /************************模式**************************/
    public static final String BULB_MODE_WHITE="white";//彩光
    public static final String BULB_MODE_COLOUR="colour";//场景
    public static final String BULB_MODE_SCENE="scene";//倒计时剩余时间
    public static final String BULB_MODE_MUSIC="music";//音乐

    /************************场景序号****************************/
    public static final String BULB_SCENE_NIGHT="0";//night
    public static final String BULB_SCENE_READ="1";//read
    public static final String BULB_SCENE_MEETING="2";//meeting
    public static final String BULB_SCENE_LEISURE="3";//leisure
    public static final String BULB_SCENE_SOFT="4";//soft
    public static final String BULB_SCENE_RAINBOW="5";//rainbow
    public static final String BULB_SCENE_SHINE="6";//shine
    public static final String BULB_SCENE_GORGEOUS="7";//gorgeous
    /**************************场景相关常量*********************************/
    public static final String BULB_SCENE_WHITE_DEFAULT_SPEED="0e0d";
    public static final String BULB_SCENE_WHITE_DEFAULT_SPACE="000000000000";
    public static final String BULB_SCENE_COLOUR_DEFAULT_SPACE="00000000";
    public static final String BULB_SCENE_WHITE_STATIC="00";
    public static final String BULB_SCENE_SPEED_MAX="6464";
    public static final String BULB_SCENE_SPEED_MIN="2828";


    @Override
    public String getType() {
        return DeviceTypeConstant.TYPE_PADDLE;
    }

    public static int getNameRes(){
        return R.string.m39_bulb;
    }

    public static int  getCloseIcon (int resIndex) {
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
            case 4:
                res=R.drawable.device_s_bulb;
                break;
            default:
                res = R.drawable.device_real_bulb;
                break;
        }
        return res;
    }

    public static int getOpenIcon(int resIndex) {
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

   /**************************获取dpid*************************************/
    public static String getBulbSwitchLed(){return BULB_SWITCH_LED;}

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



    /*********************默认场景***********************/
    public static List<String> getSceneCodeName() {
        List<String>codes=new ArrayList<>();
        codes.add(BULB_SCENE_NIGHT);
        codes.add(BULB_SCENE_READ);
        codes.add(BULB_SCENE_MEETING);
        codes.add(BULB_SCENE_LEISURE);
        codes.add(BULB_SCENE_SOFT);
        codes.add(BULB_SCENE_RAINBOW);
        codes.add(BULB_SCENE_SHINE);
        codes.add(BULB_SCENE_GORGEOUS);
        return codes;
    }

    public static List<Integer> getSceneDefultPicRes() {
        List<Integer>res=new ArrayList<>();
        res.add(R.drawable.sence_night);
        res.add(R.drawable.sence_read);
        res.add(R.drawable.sence_meeting);
        res.add(R.drawable.sence_leisure);
        res.add(R.drawable.sence_soft);
        res.add(R.drawable.sence_rainbow);
        res.add(R.drawable.sence_shine);
        res.add(R.drawable.sence_gorgeous);
        return res;
    }

    public static List<String> getSceneDefultValue() {
        List<String>codes=new ArrayList<>();
        codes.add("000e0d0000000000000000c80000");
        codes.add("010e0d0000000000000003e801f4");
        codes.add("020e0d0000000000000003e803e8");
        codes.add("030e0d0000000000000001f401f4");
        codes.add("04464602007803e803e800000000464602007803e8000a00000000");
        codes.add("05464601000003e803e800000000464601007803e803e80000000046460100f003e803e800000000464601003d03e803e80000000046460100ae03e803e800000000464601011303e803e800000000");
        codes.add("06464601000003e803e800000000464601007803e803e80000000046460100f003e803e800000000");
        codes.add("07464602000003e803e800000000464602007803e803e80000000046460200f003e803e800000000464602003d03e803e80000000046460200ae03e803e800000000464602011303e803e800000000");
        return codes;
    }



    //默认night值
    public static String defultSceneNight(){
        return "000e0d0000000000000000c80000";
    }


    //默认Read值
    public static String defultSceneRead(){
        return "010e0d0000000000000003e801f4";
    }


    //默认Meeting值
    public static String defultSceneMeeting(){
        return "020e0d0000000000000003e803e8";
    }


    //默认Sure值
    public static String defultSceneLeiSure(){
        return "030e0d0000000000000001f401f4";
    }


    //默认soft值
    public static String defultSceneSoft(){
        return "04464602007803e803e800000000464602007803e8000a00000000";
    }


    //默认Rainbow值
    public static String defultSceneRainbow(){
        return "05464601000003e803e800000000464601007803e803e80000000046460100f003e803e800000000464601003d03e803e80000000046460100ae03e803e800000000464601011303e803e800000000";
    }


    //默认Shine值
    public static String defultSceneShine(){
        return "06464601000003e803e800000000464601007803e803e80000000046460100f003e803e800000000";
    }


    //默认Gorgeous值
    public static String defultSceneGorgeous(){
        return "07464602000003e803e800000000464602007803e803e80000000046460200f003e803e800000000464602003d03e803e80000000046460200ae03e803e800000000464602011303e803e800000000";
    }


    public static int getSpeedMax(){
        return CommentUtils.hexStringToInter(BULB_SCENE_SPEED_MAX);
    }


    public static int getSpeedMin(){
        return CommentUtils.hexStringToInter(BULB_SCENE_SPEED_MIN);
    }
}
