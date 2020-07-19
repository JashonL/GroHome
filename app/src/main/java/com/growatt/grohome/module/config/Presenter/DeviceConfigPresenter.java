package com.growatt.grohome.module.config.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.config.ConfigErrorActivity;
import com.growatt.grohome.module.config.DeviceConfigActivity;
import com.growatt.grohome.module.config.SelectConfigTypeActivity;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.config.model.DeviceBindModel;
import com.growatt.grohome.module.config.view.IDeviceConfigView;
import com.growatt.grohome.module.device.presenter.DeviceTypePresenter;
import com.growatt.grohome.tuya.FamilyManager;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.mylhyl.circledialog.CircleDialog;
import com.tuya.smart.android.device.utils.WiFiUtil;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.bean.DeviceBean;

public class DeviceConfigPresenter extends BasePresenter<IDeviceConfigView> {
    private static final int MESSAGE_SHOW_SUCCESS_PAGE = 1001;
    private static final int MESSAGE_CONFIG_WIFI_OUT_OF_TIME = 0x16;
    private  String deviceType;
    private  String ssid;
    private  String password;
    private  String tuyaToken;

    private  int mConfigMode;
    private DeviceBindModel mModel;
    private boolean mBindDeviceSuccess;
    private int mTime;
    private boolean mStop;

    public DeviceConfigPresenter(IDeviceConfigView baseView) {
        super(baseView);
    }

    public DeviceConfigPresenter(Context context, IDeviceConfigView baseView) {
        super(context, baseView);
        initHandler(context);
        mConfigMode = ((Activity) context).getIntent().getIntExtra(SelectConfigTypeActivity.CONFIG_MODE, SelectConfigTypeActivity.EC_MODE);
        deviceType = ((Activity) context).getIntent().getStringExtra(DeviceTypePresenter.DEVICE_TYPE);
        ssid = ((Activity) context).getIntent().getStringExtra(WiFiOptionsActivity.CONFIG_SSID);
        password = ((Activity) context).getIntent().getStringExtra(WiFiOptionsActivity.CONFIG_PASSWORD);
    }


    public void startConfig() {
        if (TextUtils.isEmpty(ssid)) {
            return;
        }
        if (TextUtils.isEmpty(tuyaToken)) {
            getTokenForConfigDevice();
        } else {
            initConfigDevice(tuyaToken);
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
                baseView.getTokenFail(s,s1);
            }
        });
    }


    private void initConfigDevice(String token) {
        if (mConfigMode == SelectConfigTypeActivity.EC_MODE) {
            mModel.setEC(ssid, password, token);
//            startSearch();
        } else if (mConfigMode == SelectConfigTypeActivity.AP_MODE) {
            mModel.setAP(ssid, password, token);
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
                String devId = msg.getData().getString("devId");
                String pId = msg.getData().getString("pId");
                String deviceName=msg.getData().getString("devName");
                baseView.showSuccessPage(devId,pId,deviceName);
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
                baseView.showFailurePage(mConfigMode);
                String currentSSID = WiFiUtil.getCurrentSSID(context);
//                if (BindDeviceUtils.isAPMode())
//                    WiFiUtil.removeNetwork(mContext, currentSSID);
                break;

            case DeviceBindModel.WHAT_EC_ACTIVE_SUCCESS:  //EC激活成功
            case DeviceBindModel.WHAT_AP_ACTIVE_SUCCESS:  //AP激活成功
                DeviceBean bean = (DeviceBean) ((Result) (msg.obj)).getObj();
                String deviceId = bean.getDevId();
                String productId=bean.getProductId();
                String devName=bean.getName();
                stopSearch();
                configSuccess(deviceId,productId,devName);
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
        if (mStop) return;
        if (mTime >= 100) {
            stopSearch();
            // mModel.configFailure();
        } else {
            baseView.setConnectProgress(mTime++, 1000);
            handler.sendEmptyMessageDelayed(MESSAGE_CONFIG_WIFI_OUT_OF_TIME, 1000);
        }
    }


    //配网成功
    private void configSuccess(String devId,String pId,String devName) {
        stopSearch();
        baseView.showConfigSuccessTip();
        baseView.setConnectProgress(100, 800);
        Message msg = new Message();
        msg.what = MESSAGE_SHOW_SUCCESS_PAGE;
        Bundle bundle = new Bundle();
        bundle.putString("devId", devId);
        bundle.putString("pId",pId);
        bundle.putString("devName",devName);
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
        intent.putExtra("type", deviceType);
        intent.putExtra("token", tuyaToken);
        intent.putExtra("ssid", ssid);
        intent.putExtra("password", password);
        intent.putExtra(SelectConfigTypeActivity.CONFIG_MODE, mConfigMode);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,true);
    }



    public void cancel() {
        CircleDialogUtils.showCancelConfigDialog(context, ((FragmentActivity) context).getSupportFragmentManager(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) context).finish();
            }
        }, v -> {});
    }

    @Override
    public void onDestroy() {
        handler.removeMessages(MESSAGE_CONFIG_WIFI_OUT_OF_TIME);
        handler.removeMessages(MESSAGE_SHOW_SUCCESS_PAGE);
        mModel.onDestroy();
        super.onDestroy();
    }


}
