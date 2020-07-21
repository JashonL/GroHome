package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.view.IBulbView;
import com.growatt.grohome.tuya.SendDpListener;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.json.JSONObject;

import java.util.Iterator;

public class BulbPresenter extends BasePresenter<IBulbView> implements IDevListener ,SendDpListener{

    private String deviceId;
    private String devName;
    private ITuyaDevice mTuyaDevice;
    private String onOff;
    private String bright;
    private String colour;
    private String controdata;
    private String countdown;
    private String scene;
    private String mode;
    private String temp;
    private DeviceBean deviceBean;

    public BulbPresenter(IBulbView baseView) {
        super(baseView);
    }

    public BulbPresenter(Context context, IBulbView baseView) {
        super(context, baseView);
        deviceId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_ID);
        devName = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_NAME);
        if (!TextUtils.isEmpty(devName)) {
            baseView.setDeviceTitle(devName);
        }
    }


    /**
     * 获取到设备操作类
     * 并获取数据，进行初始化
     */
    public void initDevice() {
        //先干掉之前的在重新获取，避免多次回调
        if (mTuyaDevice != null) {
            mTuyaDevice.unRegisterDevListener();
            mTuyaDevice.onDestroy();
        }
        if (!TextUtils.isEmpty(deviceId)) {
            mTuyaDevice = TuyaHomeSdk.newDeviceInstance(deviceId);
            mTuyaDevice.registerDevListener(this);
        }
        deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(deviceId);
        if (deviceBean == null) {
            MyToastUtils.toast(R.string.m149_device_does_not_exist);
            ((Activity)context).finish();
            return;
        }
        //获取开关状态
        onOff = String.valueOf(deviceBean.getDps().get(DeviceBulb.getBulbSwitchLed()));
        bright = String.valueOf(deviceBean.getDps().get(DeviceBulb.getBulbBrightValue()));
        colour = String.valueOf(deviceBean.getDps().get(DeviceBulb.getBulbColourData()));
        controdata = String.valueOf(deviceBean.getDps().get(DeviceBulb.getBulbControlData()));
        countdown = String.valueOf(deviceBean.getDps().get(DeviceBulb.getBulbCountdown()));
        scene = String.valueOf(deviceBean.getDps().get(DeviceBulb.getBulbSceneData()));
        mode = String.valueOf(deviceBean.getDps().get(DeviceBulb.getBulbWorkMode()));
        temp = String.valueOf(deviceBean.getDps().get(DeviceBulb.getBulbTempValue()));
        baseView.setOnoff(onOff);
        baseView.setBright(bright);
        baseView.setColour(colour);
        baseView.setControData(controdata);
        baseView.setCuntDown(countdown);
        baseView.setScene(scene);
        baseView.setMode(mode);
        baseView.setTemp(temp);

    }


    /**
     * 开关
     */
    public void bulbSwitch() {
        if (deviceNotOnline()){
            boolean bulb_onoff="true".equals(onOff);
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbSwitchLed(), !bulb_onoff,  mTuyaDevice, this);
        }

    }

    /**
     * 设置模式
     * @param mode
     */
    public void bulbMode(String mode){
        if (deviceNotOnline()){
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbWorkMode(), mode,  mTuyaDevice, this);
        }
    }


    /**
     * 设置亮度
     * @param brightness
     */

    public void bulbBrightness(int brightness){
        if (deviceNotOnline()){
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbBrightValue(), brightness,  mTuyaDevice, this);
        }
    }



    /**
     * 设置冷暖值
     * @param temper
     */

    public void bulbTemper(int temper){
        if (deviceNotOnline()){
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbTempValue(), temper,  mTuyaDevice, this);
        }
    }



    /**
     * 判断是否在线
     * @return
     */
    private boolean deviceNotOnline(){
        if (!deviceBean.getIsOnline()) {
            MyToastUtils.toast(R.string.m150_device_is_offline);
            return false;
        }
        return true;
    }



    @Override
    public void onDpUpdate(String devId, String dpStr) {
        if (devId.equals(deviceId)) {
            try {
                JSONObject object = new JSONObject(dpStr);
                Iterator iterator = object.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    if (key.equals(DeviceBulb.getBulbSwitchLed())) {
                        if (object.length() > 2) return;//自动上报，可能导致状态错误
                        //开关
                        boolean switch_on = object.optBoolean(DeviceBulb.getBulbSwitchLed());
                        onOff= String.valueOf(switch_on);
                        baseView.setOnoff(String.valueOf(switch_on));
                    }else if (key.equals(DeviceBulb.getBulbWorkMode())){
                        this.mode= object.optString(DeviceBulb.getBulbWorkMode());
                        baseView.setMode(mode);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRemoved(String devId) {

    }

    @Override
    public void onStatusChanged(String devId, boolean online) {

    }

    @Override
    public void onNetworkStatusChanged(String devId, boolean status) {

    }

    @Override
    public void onDevInfoUpdate(String devId) {

    }

    @Override
    public void sendCommandSucces() {
        baseView.sendCommandSucces();
    }

    @Override
    public void sendCommandError(String code, String error) {
        baseView.sendCommandError(code,error);
    }
}
