package com.growatt.grohome.constants;

public class GlobalConstant {

    //***********************退出APP时间*****************************
    public static final long APP_EXIT_LONG_TIME = 2000;
    //***********************涂鸦设备热点****************************
    public static final String DEFAULT_OLD_AP_SSID = "TuyaSmart";
    public static final String DEFAULT_COMMON_AP_SSID = "SmartLife";
    public static final String TY_ROUTER = "TY_ROUTER";
    public static final String DEFAULT_KEY_AP_SSID = "-TLinkAP-";

    //*********************Activity跳转传值**************************
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_PID= "device_pid";
    public static final String DEVICE_SWITCH_ID = "device_switch_id";
    public static final String DEVICE_TYPE = "device_type";
    public static final String COUNTRY = "country";
    public static final String ROOM_LIST = "room_list";
    public static final String ROOM_POSITION = "room_position";
    public static final String ROOM_BEAN = "room_bean";
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_NAME = "room_name";
    public static final String ACITION_DEVICE_TRANSFER = "device_transfer";
    public static final String ACITION_KEY = "action_key";
    public static final String DEVICE_ROAD = "road";
    public static final String DEVICE_CONFIG_TYPE = "config_type";
    public static final String DEVICE_SCAN_BEAN = "device_scan_bean";
    //**************************场景*****************************
    public static final int REQUEST_CODE_EDIT_SCENE_TIME = 102;
    public static final String SCENE_BEAN = "scene_bean";
    public static final String SCENE_TYPE = "scene_type";
    public static final String SCENE_NAME = "scene_name";
    public static final String SCENE_LUANCH_TAP_TO_RUN = "luanch_tap_to_run";
    public static final String SCENE_SMART = "smart";
    public static final String SCENE_DEVICE_SELECT = "scene_device_select";
    public static final String SCENE_ADD_TASK = "scene_task";
    public static final String SCENE_ADD_CONDITION = "scene_condition";
    public static final String DEVICE_BEAN = "device_bean";
    public static final String SCENE_DEVICE_OPEN = "open";
    public static final String SCENE_DEVICE_SET = "set";
    public static final String SCENE_DEVICE_SHUT = "shut";
    public static final String SCENE_CREATE_OR_EDIT = "scene_create_or_edit";
    public static final String SCENE_CREATE = "scene_create";
    public static final String SCENE_EDIT = "scene_edit";
    public static final String SCENE_TASK_BEAN = "scene_task_bean";
    public static final String SCENE_CONDITION_BEAN = "scene_condition_bean";
    public static final int SCENE_TYPE_LUANCH = 0;
    public static final int SCENE_TYPE_SMART= 1;


    //*****************************时间********************************
    public static final int REQUEST_CODE_SELECT_TIME = 100;
    public static final String TIME_START = "start_time";
    public static final String TIME_END = "start_end";
    public static final String TIME_VALUE = "start_value";
    public static final int REQUEST_CODE_SELECT_REPEAT = 101;
    public static final String TIME_LOOPTYPE = "start_looptype";
    public static final String TIME_LOOPVALUE = "start_loopvalue";
    public static final String SET_TIMEVALUE_OR_TIMEPERIOD = "set_timevalue_or_timeperiod";
    public static final String SET_TIMEVALUE = "timevalue";
    public static final String SET_TIMEPERIOD = "timeperiod";

    //************shareperferen保存常量********************************
    public static final String SP_REMMENER_PASSWORD="remmenber_password";
    public static final String SP_USER_NAME = "user_name";
    public static final String SP_USER_PASSWORD = "user_password";
    public static final String SP_AUTO_LOGIN="auto_login";
    //*************************国  家********************************
    public static final String STRING_CHINA_CHINESE = "中国";
    public static final String STRING_CHINA_ENLISH = "china";

    public static final String STRING_COUNTY_UNITEDKINGDOM="UnitedKingdom";
    public static final String STRING_COUNTY_UNITEDSTATES="UnitedStates";

    //**************************开关********************************
    public static final int STATUS_ON = 1;
    public static final int STATUS_OFF = 0;

    public static final String STRING_STATUS_ON = "1";
    public static final String STRING_STATUS_OFF = "0";

    //***************************彩灯场景*******************************
    public static final String BULB_ISWHITE="iswhite";
    public static final String BULB_SCENE_BEAN = "bulb_scene_bean";
    public static final String BULB_SCENE_BEAN_LIST = "bulb_scene_list";


    //**************************item类型添加或是其他********************************
    public static final int STATUS_ITEM_DATA= 1;
    public static final int STATUS_ITEM_OTHER = 0;



    //******************************温度符号******************************
    public static final String TEMP_UNIT = "temp_unit";
    public static final String TEMP_UNIT_CELSIUS = "℃";
    public static final String TEMP_UNIT_FAHRENHEIT = "℉";

    //**************************温控器场景*********************************
    public static final String TEMP_MODE="temp_mode";
    public static final String MODE_HOLIDAY="holiday";
    public static final String MODE_SMART="smart";
    public static final String MODE_HOLD="hold";

    //*************************添加还是编辑******************************
    public static final String EDIT="edit";
    public static final String ADD="add";
    public static final String ACTION="action";
    //**************************定时***********************************
    public static final String TIMING_BEAN="timing_bean";
    public static final String SWTICH_TIMING_BEAN="switch_timing_bean";
    public static final String TIMING_CKEY="timing_ckey";

    //***********************名字集合*********************
    public static final String NAME_LIST="name_list";

    //**************请求修改邮箱********************************
    public static final int REQUEST_CODE_EDIT_EMAIL = 103;

    public static final String IMAGE_FILE_LOCATION = "grohome/avatar.png";

    //*********************http***************************
    public static final String HTTP_PREFIX = "http://";
    //***********************设备相关网页******************************
    public static final String SWITCH_PANEL_WEB="https://www.amazon.com/dp/B08BLCJB2C";

    public static final String LIGHT_BULB_US_WEB="https://www.amazon.com/dp/B08F4VBGZY?ref=myi_title_dp";
    public static final String LIGHT_BULB_UK_WEB="https://www.amazon.co.uk/dp/B08F9RL1SJ?ref=myi_title_dp";

    public static final String LIGHT_STRIP_US_WEB="https://www.amazon.com/dp/B08GCLY2LX?ref=myi_title_dp";
    public static final String LIGHT_STRIP_UK_WEB="https://www.amazon.co.uk/dp/B08GFR2VQT?ref=myi_title_dp";

    public static final String WEB_URL="web_url";

    //***********************区号***********************
    public static final String CHINA_AREA_CODE = "86";
    public static final String EUROPE_AREA_CODE = "33";
    //***********************wifi***********************
    public static final String WIFI_SSID = "wifi_ssid";
    public static final String WIFI_PASSWORD = "wifi_password";
    public static final String WIFI_TOKEN = "wifi_token";
    //***********************官网********************
    public static final String COMPANY_WEBSITE_CN = "http://www.growatt.com/";
    public static final String COMPANY_WEBSITE_EN = "http://ginverter.com/";

    //************************用户协议***********************
    public static final String AGREEMENT_OR_POLICY="agreement_or_policy";
    public static final String AGREEMENT="agreement";
    public static final String POLICY="policy";

    //************************打开系统设置*******************
    public static final int ACTION_LOCATION_CODE=104;

    //************************操作手册**********************
    public static final String FQA_GUIDE="fqa_guide";
    public static final String MANUAL_GUIDE_TITLE="manual_guide_title";
    public static final String MANUAL_GUIDE_CONTENT="manual_guide_content";
    //*************************本地文件名称**********************
    public static final String PRIVACY_POLICY_CN="file:///android_asset/privacyPolicy_cn.html";
    public static final String PRIVACY_POLICY_EN="file:///android_asset/privacyPolicy_en.html";

    public static final String RESETDEVICE_CN="file:///android_asset/resetDevice_cn.html";
    public static final String RESETDEVICE_EN="file:///android_asset/resetDevice_en.html";

    public static final String ROUTER_SETTING_METHOD_CN="file:///android_asset/routerSettingMethod_cn.html";
    public static final String ROUTER_SETTING_METHOD_EN="file:///android_asset/routerSettingMethod_en.html";

    public static final String SPLIT_FREQUENCY_BANDS_CN="file:///android_asset/splitFrequencyBands_cn.html";
    public static final String SPLIT_FREQUENCY_BANDS_EN="file:///android_asset/splitFrequencyBands_en.html";

    public static final String USER_AGREEMENT_CN="file:///android_asset/userAgreement_cn.html";
    public static final String USER_AGREEMENT_EN="file:///android_asset/userAgreement_en.html";
}
