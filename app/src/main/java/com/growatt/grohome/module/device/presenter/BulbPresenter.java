package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.BulbSceneBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.BulbSceneEditActivity;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.view.IBulbView;
import com.growatt.grohome.tuya.SendDpListener;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BulbPresenter extends BasePresenter<IBulbView> implements IDevListener, SendDpListener {

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


    //颜色
    private int mColor = Color.RED;
    //冷暖色
    private int mColourSatProgrees = 0;
    //亮度
    private int mColourValProgrees = 990;

    //白光
    private int mWhiteColor;
    //场景值
    private String sceneData;


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


    public List<BulbSceneBean> initScene() {
        String[] scenesName = new String[]{context.getString(R.string.m152_scene_night), context.getString(R.string.m153_scene_read), context.getString(R.string.m154_scene_meeting), context.getString(R.string.m155_scene_leisure),
                context.getString(R.string.m156_scene_soft), context.getString(R.string.m159_scence_rainbow), context.getString(R.string.m157_scene_shine), context.getString(R.string.m158_sence_gorgeous)};
        List<String> sceneCodeName = DeviceBulb.getSceneCodeName();
        List<String> sceneDefultValue = DeviceBulb.getSceneDefultValue();
        List<BulbSceneBean> sceneList = new ArrayList<>();
        for (int i = 0; i < scenesName.length; i++) {
            BulbSceneBean bulbSceneBean = new BulbSceneBean();
            bulbSceneBean.setId(sceneCodeName.get(i));
            bulbSceneBean.setName(scenesName[i]);
            bulbSceneBean.setSelected(i==0);
            bulbSceneBean.setSceneValue(sceneDefultValue.get(i));
            sceneList.add(bulbSceneBean);
        }
        return  sceneList;
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
            ((Activity) context).finish();
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
        if (!TextUtils.isEmpty(colour)) {
            int length = colour.length();
            if (length > 9) {
                float[] hsv = new float[3];
                String angle = colour.substring(0, 4);
                String sat = colour.substring(4, 8);
                String val = colour.substring(8, length);
                int mHue = CommentUtils.hexStringToInter(angle);
                int mSat = CommentUtils.hexStringToInter(sat);
                int mVal = CommentUtils.hexStringToInter(val);
                hsv[0] = (float) mHue;
                hsv[1] = (float) mSat / 1000f;
                hsv[2] = (float) (mVal - 10) / 990f;
                mColor = Color.HSVToColor(hsv);
                mColourSatProgrees = mSat;
                mColourValProgrees = mVal - 10;
                Log.i(TuyaApiUtils.TUYA_TAG, "mColourSatProgrees：" + mColourSatProgrees + "mColourValProgrees" + mColourValProgrees);
            }
        }
        baseView.setSatProgress(mColourSatProgrees);
        baseView.setVatProgress(mColourValProgrees);
        baseView.setColour(mColor);
        baseView.setColourMaskView(mColor);
        baseView.setControData(controdata);
        baseView.setCuntDown(countdown);
        baseView.setScene(scene);
        baseView.setMode(mode);
        baseView.setTemp(temp);

        int whiteBright = Integer.parseInt(bright);
        int whiteTemp = 1000 - Integer.parseInt(temp);
        float[] hsv = new float[3];
        hsv[0] = 42.3f;
        hsv[1] = (float) whiteTemp / 1000f;
        hsv[2] = (float) whiteBright / 1000f;
        mWhiteColor = Color.HSVToColor(hsv);
        baseView.setWhiteMaskView(mWhiteColor);
        baseView.setWhiteBgColor(mWhiteColor);
    }


    /**
     * 开关
     */
    public void bulbSwitch() {
        if (deviceNotOnline()) {
            boolean bulb_onoff = "true".equals(onOff);
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbSwitchLed(), !bulb_onoff, mTuyaDevice, this);
        }

    }

    /**
     * 设置模式
     *
     * @param mode
     */
    public void bulbMode(String mode) {
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbWorkMode(), mode, mTuyaDevice, this);
        }
    }


    /**
     * 设置亮度
     *
     * @param brightness
     */

    public void bulbBrightness(int brightness) {
        bright = String.valueOf(brightness);
        int whiteTemp = Integer.parseInt(temp);
        float[] hsv = new float[3];
        Color.colorToHSV(mWhiteColor, hsv);
        hsv[1] = (float) whiteTemp / 1000f;
        hsv[2] = (float) brightness / 1000f;
        int newColor = Color.HSVToColor(hsv);
        baseView.setWhiteBgColor(newColor);
        baseView.setWhiteMaskView(newColor);
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbBrightValue(), brightness, mTuyaDevice, this);
        }
    }


    /**
     * 设置冷暖值
     *
     * @param temper
     */

    public void bulbTemper(int temper) {
        temp = String.valueOf((1000 - temper));
        int whiteBright = Integer.parseInt(bright);
        float[] hsv = new float[3];
        Color.colorToHSV(mWhiteColor, hsv);
        hsv[1] = (float) (1000 - temper) / 1000f;
        hsv[2] = (float) whiteBright / 1000f;
        int newColor = Color.HSVToColor(hsv);
        baseView.setWhiteBgColor(newColor);
        baseView.setWhiteMaskView(newColor);
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbTempValue(), temper, mTuyaDevice, this);
        }
    }


    /**
     * 设置彩光
     */
    public void bulbColour(int color) {
        Log.i(TuyaApiUtils.TUYA_TAG, "颜色值：" + color);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        float mHue = hsv[0];
        float mSat = mColourSatProgrees;
        float mVal = mColourValProgrees + 10;
        Log.i(TuyaApiUtils.TUYA_TAG, "mHue：" + mHue + "mStat" + mSat + "mVal" + mVal);
        String angle = CommentUtils.integerToHexstring((int) mHue);
        String s = CommentUtils.integerToHexstring((int) mSat);
        String v = CommentUtils.integerToHexstring((int) mVal);
        colour = angle + s + v;
        mColor = color;
        Log.i(TuyaApiUtils.TUYA_TAG, colour);
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbColourData(), colour, mTuyaDevice, this);
        }
    }


    /**
     * 设置彩光饱亮度
     */
    public void bulbColourSat(int progress) {
        Log.i(TuyaApiUtils.TUYA_TAG, "颜色值：" + mColor);
        float[] hsv = new float[3];
        Color.colorToHSV(mColor, hsv);
        float mHue = hsv[0];
        float mVal = mColourValProgrees + 10;
        mColourSatProgrees = progress;
        Log.i(TuyaApiUtils.TUYA_TAG, "mHue：" + mHue + "mStat" + (float) progress + "mVal" + mVal);
        String angle = CommentUtils.integerToHexstring((int) mHue);
        String s = CommentUtils.integerToHexstring((progress));
        String v = CommentUtils.integerToHexstring((int) mVal);
        colour = angle + s + v;
        Log.i(TuyaApiUtils.TUYA_TAG, colour);

        hsv[1] = (float) progress / 1000f;
        hsv[2] = mVal / (1000);
        int newColor = Color.HSVToColor(hsv);
        baseView.setCenterColor(newColor);

        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbColourData(), colour, mTuyaDevice, this);
        }
    }


    /**
     * 设置彩光亮度
     */
    public void bulbColourVal(int progress) {
        Log.i(TuyaApiUtils.TUYA_TAG, "颜色值：" + mColor);
        float[] hsv = new float[3];
        Color.colorToHSV(mColor, hsv);
        float mHue = hsv[0];
        float mSat = mColourSatProgrees;
        mColourValProgrees = progress;
        Log.i(TuyaApiUtils.TUYA_TAG, "mHue：" + mHue + "mStat" + (float) progress + "mVal" + (float) progress);
        String angle = CommentUtils.integerToHexstring((int) mHue);
        String s = CommentUtils.integerToHexstring((int) mSat);
        String v = CommentUtils.integerToHexstring(progress + 10);
        colour = angle + s + v;

        hsv[1] = mSat / 1000f;
        hsv[2] = (float) progress / (990);
        int newColor = Color.HSVToColor(hsv);
        baseView.setCenterColor(newColor);

        Log.i(TuyaApiUtils.TUYA_TAG, colour);
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbColourData(), colour, mTuyaDevice, this);
        }
    }



    /**
     * 设置场景
     *
     * @param scene
     */
    public void bulbScene(String scene) {
        sceneData=scene;
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbSceneData(), scene, mTuyaDevice, this);
        }
    }

    /**
     * 判断是否在线
     *
     * @return
     */
    private boolean deviceNotOnline() {
        if (!deviceBean.getIsOnline()) {
            MyToastUtils.toast(R.string.m150_device_is_offline);
            return false;
        }
        return true;
    }


    public void toEditScene() {
        Intent intent = new Intent(context, BulbSceneEditActivity.class);
        intent.putExtra(DeviceBulb.BULB_SCENE_DATA,sceneData);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    @Override
    public void onDpUpdate(String devId, String dpStr) {
        Log.i(TuyaApiUtils.TUYA_TAG, "deviceId:" + devId + "responed:" + dpStr + "time：" + System.currentTimeMillis());
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
                        onOff = String.valueOf(switch_on);
                        baseView.setOnoff(String.valueOf(switch_on));
                    } else if (key.equals(DeviceBulb.getBulbWorkMode())) {
                        this.mode = object.optString(DeviceBulb.getBulbWorkMode());
                        baseView.setMode(mode);
                    }else if (key.equals(DeviceBulb.getBulbSceneData())){
                        this.sceneData = object.optString(DeviceBulb.getBulbSceneData());
                        baseView.setScene(sceneData);
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
        baseView.sendCommandError(code, error);
    }
}
