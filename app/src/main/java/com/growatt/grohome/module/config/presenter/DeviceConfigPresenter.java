package com.growatt.grohome.module.config.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.DeviceConfigConstant;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.DeviceAddOrDelMsg;
import com.growatt.grohome.module.config.ConfigErrorActivity;
import com.growatt.grohome.module.config.ConfigSuccessActivity;
import com.growatt.grohome.module.config.DeviceLightStatusActivity;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.config.model.DeviceBindModel;
import com.growatt.grohome.module.config.view.IDeviceConfigView;
import com.growatt.grohome.tuya.FamilyManager;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.tuya.smart.android.ble.api.ScanDeviceBean;
import com.tuya.smart.android.device.utils.WiFiUtil;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DeviceConfigPresenter extends BasePresenter<IDeviceConfigView> {
    private static final int MESSAGE_SHOW_SUCCESS_PAGE = 1001;
    private static final int MESSAGE_CONFIG_WIFI_OUT_OF_TIME = 0x16;
    private String deviceType;
    private String ssid;
    private String password;
    private String tuyaToken;
    private String deviceName;

    private int mConfigMode;
    private DeviceBindModel mModel;
    private boolean mBindDeviceSuccess;
    private int mTime;
    private boolean mStop;

    private String scanJson;
    private String configType;


    public DeviceConfigPresenter(IDeviceConfigView baseView) {
        super(baseView);
    }

    public DeviceConfigPresenter(Context context, IDeviceConfigView baseView) {
        super(context, baseView);
        initHandler(context);
        mModel = new DeviceBindModel(context, handler);
        //先获取设备配网的类型
        configType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_CONFIG_TYPE);

        mConfigMode = ((Activity) context).getIntent().getIntExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.EC_MODE);
        deviceType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_TYPE);
        ssid = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_SSID);
        password = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_PASSWORD);
        tuyaToken = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_TOKEN);
        scanJson = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_SCAN_BEAN);

    }


    public void startConfig() {
        if (TextUtils.isEmpty(ssid)) {
            return;
        }
        if (mConfigMode == DeviceConfigConstant.EC_MODE) {
            getTokenForConfigDevice();
        } else if (mConfigMode == DeviceConfigConstant.AP_MODE) {
            initConfigDevice(tuyaToken);
        } else {//蓝牙配网
            getTokenForConfigDevice();
        }


    }


    private void getTokenForConfigDevice() {
        long homeId = FamilyManager.getInstance().getCurrentHomeId();
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                initConfigDevice(token);
            }

            @Override
            public void onFailure(String s, String s1) {
                baseView.getTokenFail(s, s1);
            }
        });
    }


    private void initConfigDevice(String token) {
        if (mConfigMode == DeviceConfigConstant.EC_MODE) {
            mModel.setEC(ssid, password, token);
//            startSearch();
        } else if (mConfigMode == DeviceConfigConstant.AP_MODE) {
            mModel.setAP(ssid, password, token);
        }else {
            long homeId = TuyaApiUtils.getHomeId();
            if (!TextUtils.isEmpty(scanJson)) {
                ScanDeviceBean bean = new Gson().fromJson(scanJson, ScanDeviceBean.class);
                if (bean!=null){
                    Map<String,Object> wifiMap=new HashMap<>();
                    wifiMap.put("ssid",ssid);
                    wifiMap.put("password",password);
                    wifiMap.put("token",token);
                    mModel.setBlueTooth(homeId, bean.getUuid(), wifiMap);
                }
            }
        }
        startSearch();
    }

    public void startSearch() {
        mModel.start();
        baseView.showConnectPage();
        mBindDeviceSuccess = false;
        startLoop();
    }


    private void startLoop() {
        mTime = 0;
        mStop = false;
        handler.sendEmptyMessage(MESSAGE_CONFIG_WIFI_OUT_OF_TIME);
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_SHOW_SUCCESS_PAGE:
                String devId = msg.getData().getString(GlobalConstant.DEVICE_ID);
                String pId = msg.getData().getString(GlobalConstant.DEVICE_PID);
                deviceName = msg.getData().getString(GlobalConstant.DEVICE_NAME);
                baseView.showSuccessPage(devId, pId, deviceName);
                break;
            case MESSAGE_CONFIG_WIFI_OUT_OF_TIME:
                checkLoop();
                break;
            //网络错误异常情况
            case DeviceBindModel.WHAT_EC_GET_TOKEN_ERROR:            //获取token失败
                stopSearch();
                Result wifiError = (Result) msg.obj;
                baseView.showConfigFail(wifiError.getError(), wifiError.getError(), mConfigMode);
                break;
            //ec激活失败
            case DeviceBindModel.WHAT_EC_ACTIVE_ERROR:
                stopSearch();
                if (mBindDeviceSuccess) {
                    baseView.showBindDeviceSuccessFinalTip();
                    break;
                }
                Result ecActivityError = (Result) msg.obj;
                baseView.showConfigFail(ecActivityError.getError(), ecActivityError.getError(), mConfigMode);
                break;

            //AP激活失败
            case DeviceBindModel.WHAT_AP_ACTIVE_ERROR:
                stopSearch();
                if (mBindDeviceSuccess) {
                    baseView.showBindDeviceSuccessFinalTip();
                    break;
                }
                Result apActivityError = (Result) msg.obj;
                baseView.showConfigFail(apActivityError.getError(), apActivityError.getError(), mConfigMode);
                String currentSSID = WiFiUtil.getCurrentSSID(context);
//                if (BindDeviceUtils.isAPMode())
//                    WiFiUtil.removeNetwork(mContext, currentSSID);
                break;

            case DeviceBindModel.WHTA_BLUETOOTH_ACTIVE_ERROR:
                stopSearch();
                Result bleActivityError = (Result) msg.obj;
                baseView.showConfigFail(bleActivityError.getError(), bleActivityError.getError(), mConfigMode);
                break;

            case DeviceBindModel.WHAT_EC_ACTIVE_SUCCESS:  //EC激活成功
            case DeviceBindModel.WHAT_AP_ACTIVE_SUCCESS:  //AP激活成功
            case DeviceBindModel.WHTA_BLUETOOTH_ACTIVE_SUCCESS://蓝牙激活成功
                DeviceBean bean = (DeviceBean) ((Result) (msg.obj)).getObj();
                String deviceId = bean.getDevId();
                String productId = bean.getProductId();
                String devName = bean.getName();
                stopSearch();
                configSuccess(deviceId, productId, devName);
                break;

            case DeviceBindModel.WHAT_DEVICE_FIND:
                deviceFind((String) ((Result) (msg.obj)).getObj());
                break;
            case DeviceBindModel.WHAT_BIND_DEVICE_SUCCESS:
//                bindDeviceSuccess(/*((GwDevResp) ((Result) (msg.obj)).getObj()).getName()*/);
                bindDeviceSuccess();
                break;

        }
        return super.handleMessage(msg);
    }


    private void bindDeviceSuccess() {
        if (!mStop) {
            mBindDeviceSuccess = true;
            baseView.showBindDeviceSuccessTip();
            baseView.setAddDeviceName("");
        }
    }


    private void deviceFind(String gwId) {
        if (!mStop) {
            baseView.showDeviceFindTip(gwId);
        }
    }


    private void checkLoop() {
        if (mTime >= 100) {
            stopSearch();
            // mModel.configFailure();
            baseView.showConfigFail("", "", mConfigMode);
        } else {
            baseView.setConnectProgress(mTime++, 1000);
            handler.sendEmptyMessageDelayed(MESSAGE_CONFIG_WIFI_OUT_OF_TIME, 1000);
        }
    }


    //配网成功
    private void configSuccess(String devId, String pId, String devName) {
        stopSearch();
        baseView.showConfigSuccessTip();
        baseView.setConnectProgress(100, 800);
        Message msg = new Message();
        msg.what = MESSAGE_SHOW_SUCCESS_PAGE;
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.DEVICE_ID, devId);
        bundle.putString(GlobalConstant.DEVICE_PID, pId);
        bundle.putString(GlobalConstant.DEVICE_NAME, devName);
        msg.setData(bundle);
        handler.sendMessage(msg);
//        mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_SUCCESS_PAGE, 1000);
    }


    //暂停配网
    private void stopSearch() {
        mStop = true;
        handler.removeMessages(MESSAGE_CONFIG_WIFI_OUT_OF_TIME);
        mModel.cancel();
    }


    /**
     * 跳转到配网失败界面
     */
    public void dialogFail() {
        Intent intent = new Intent(context, ConfigErrorActivity.class);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.WIFI_TOKEN, tuyaToken);
        intent.putExtra(GlobalConstant.WIFI_SSID, ssid);
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, password);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE, configType);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, mConfigMode);
        intent.putExtra(GlobalConstant.DEVICE_SCAN_BEAN, scanJson);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }

    public void reTryConfig() {
        Class clazz;
        if (DeviceConfigConstant.BLUETOOTH_MODE==mConfigMode){
            clazz= DeviceLightStatusActivity.class;
        }else {
            clazz=WiFiOptionsActivity.class;
        }
        Intent intent = new Intent(context, clazz);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.WIFI_TOKEN, tuyaToken);
        intent.putExtra(GlobalConstant.WIFI_SSID, ssid);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, password);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE, configType);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, mConfigMode);
        intent.putExtra(GlobalConstant.DEVICE_SCAN_BEAN, scanJson);
        context.startActivity(intent);
        ((FragmentActivity) context).finish();
    }


    public void cancel() {
        CircleDialogUtils.showCancelConfigDialog(context, ((FragmentActivity) context).getSupportFragmentManager(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, v -> {
            ((FragmentActivity) context).finish();
        });
    }

    /**
     * 向服务器请求添加设备
     *
     * @throws Exception
     */
    public void addDevice(String pid, String devId) throws Exception {
        JSONObject requestJson = new JSONObject();
        requestJson.put("uid", App.getUserBean().accountName);
        requestJson.put("pid", pid);
        requestJson.put("devId", devId);
        requestJson.put("countryCode", App.getUserBean().getUserTuyaCode());
        int serverId = 1;
        if (App.getUserBean().getUserTuyaCode().equals(GlobalConstant.CHINA_AREA_CODE)) {
            serverId = 0;
        }
        requestJson.put("deviceServerAddress", serverId);
        requestJson.put("devType", deviceType);
        requestJson.put("lan", String.valueOf(CommentUtils.getLanguage()));
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.addDeviceNew(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject object = new JSONObject(bean);
                    int code = object.getInt("code");
                    if (code == 0) {
                        DeviceAddOrDelMsg deviceAddOrDelMsg = new DeviceAddOrDelMsg();
                        deviceAddOrDelMsg.setDevType(deviceType);
                        deviceAddOrDelMsg.setType(DeviceAddOrDelMsg.ADD_DEV);
                        EventBus.getDefault().post(deviceAddOrDelMsg);
                        Intent intent = new Intent(context, ConfigSuccessActivity.class);
                        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
                        intent.putExtra(GlobalConstant.DEVICE_ID, devId);
                        intent.putExtra(GlobalConstant.DEVICE_NAME, deviceName);
                        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, true);
                    }
                    String data = object.optString("data");
                    MyToastUtils.toast(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String msg) {
                baseView.onError(msg);
            }
        });

    }


    @Override
    public void onDestroy() {
        handler.removeMessages(MESSAGE_CONFIG_WIFI_OUT_OF_TIME);
        handler.removeMessages(MESSAGE_SHOW_SUCCESS_PAGE);
        mModel.onDestroy();
        super.onDestroy();
    }


}
