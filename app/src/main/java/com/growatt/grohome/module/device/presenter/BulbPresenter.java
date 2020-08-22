package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.BulbSceneBean;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.TransferDevMsg;
import com.growatt.grohome.module.device.BulbSceneEditActivity;
import com.growatt.grohome.module.device.DeviceSettingActivity;
import com.growatt.grohome.module.device.DeviceTimingListActivity;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.view.IBulbView;
import com.growatt.grohome.tuya.SendDpListener;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.LogUtil;
import com.growatt.grohome.utils.MyToastUtils;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class BulbPresenter extends BasePresenter<IBulbView> implements IDevListener, SendDpListener {


    private GroDeviceBean mGroDeviceBean;
    private String deviceId;
    private String deviceType;
    private String devName;
    private String roomId;
    private String roomName;
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
    private DialogFragment dialogFragment;


    public BulbPresenter(IBulbView baseView) {
        super(baseView);
    }

    public BulbPresenter(Context context, IBulbView baseView) {
        super(context, baseView);
        deviceId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_ID);
        deviceType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_TYPE);
        devName = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_NAME);
        roomId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.ROOM_ID);
        roomName = ((Activity) context).getIntent().getStringExtra(GlobalConstant.ROOM_NAME);
        String deviceJson = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_BEAN);
        if (!TextUtils.isEmpty(deviceJson)) {
            mGroDeviceBean = new Gson().fromJson(deviceJson, GroDeviceBean.class);
            deviceId = mGroDeviceBean.getDevId();
            deviceType = mGroDeviceBean.getDevType();
            devName = mGroDeviceBean.getName();
            roomId = String.valueOf(mGroDeviceBean.getRoomId());
            roomName = mGroDeviceBean.getRoomName();
        }
        if (!TextUtils.isEmpty(devName)) {
            baseView.setDeviceTitle(devName);
        }

        baseView.setViewsByDeviceType(deviceType);
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
            bulbSceneBean.setSelected(i == 0);
            bulbSceneBean.setSceneValue(sceneDefultValue.get(i));
            sceneList.add(bulbSceneBean);
        }
        return sceneList;
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
        //设备不在线
        if (!deviceNotOnline()) {
            baseView.deviceOnline(false);
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
        if (!CommentUtils.isStringEmpty(colour)) {
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

        //从服务器获取场景
        try {
            requestBulbScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 服务器获取场景
     */
    public void requestBulbScene() throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("devId", deviceId);
        requestJson.put("devType", DeviceTypeConstant.TYPE_BULB);
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.getBulbInfo(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject obj = new JSONObject(bean);
                    int code = obj.getInt("code");
                    List<BulbSceneBean> sceneList = new ArrayList<>();
                    if (code == 0) {
                        JSONObject data = obj.getJSONObject("data");
                        JSONArray modeArray = data.getJSONArray("mode");
                        for (int i = 0; i < modeArray.length(); i++) {
                            BulbSceneBean sceneBean = new BulbSceneBean();
                            JSONObject modeObjcte = modeArray.getJSONObject(i);
                            sceneBean.setId(modeObjcte.optString("numb", ""));
                            sceneBean.setName(modeObjcte.optString("name", ""));
                            sceneBean.setSelected(false);
                            sceneBean.setSceneValue(modeObjcte.optString("color", ""));
                            sceneList.add(sceneBean);
                        }
                    }
                    if (sceneList.size() > 0) {
                        if (!TextUtils.isEmpty(scene)) {
                            String number = scene.substring(0, 2);
                            int id = Integer.parseInt(number);
                            sceneList.get(id).setSceneValue(scene);
                        }
                        Collections.sort(sceneList, new Comparator<BulbSceneBean>() {
                            @Override
                            public int compare(BulbSceneBean o1, BulbSceneBean o2) {
                                return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
                            }
                        });
                        baseView.upDataSceneList(sceneList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String msg) {

            }
        });
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
        if (deviceNotOnline()) {
            bright = String.valueOf(brightness);
            int whiteTemp = Integer.parseInt(temp);
            float[] hsv = new float[3];
            Color.colorToHSV(mWhiteColor, hsv);
            hsv[1] = (float) whiteTemp / 1000f;
            hsv[2] = (float) brightness / 1000f;
            int newColor = Color.HSVToColor(hsv);
            baseView.setWhiteBgColor(newColor);
            baseView.setWhiteMaskView(newColor);
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbBrightValue(), brightness, mTuyaDevice, this);
        }
    }


    /**
     * 设置冷暖值
     *
     * @param temper
     */

    public void bulbTemper(int temper) {
        if (deviceNotOnline()) {
            temp = String.valueOf((1000 - temper));
            int whiteBright = Integer.parseInt(bright);
            float[] hsv = new float[3];
            Color.colorToHSV(mWhiteColor, hsv);
            hsv[1] = (float) (1000 - temper) / 1000f;
            hsv[2] = (float) whiteBright / 1000f;
            int newColor = Color.HSVToColor(hsv);
            baseView.setWhiteBgColor(newColor);
            baseView.setWhiteMaskView(newColor);
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbTempValue(), temper, mTuyaDevice, this);
        }
    }


    /**
     * 设置彩光
     */
    public void bulbColour(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        float mHue = hsv[0];
        float mSat = mColourSatProgrees;
        float mVal = mColourValProgrees + 10;
        String angle = CommentUtils.integerToHexstring((int) mHue, 4);
        String s = CommentUtils.integerToHexstring((int) mSat, 4);
        String v = CommentUtils.integerToHexstring((int) mVal, 4);
        colour = angle + s + v;
        mColor = color;
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbColourData(), colour, mTuyaDevice, this);
        }
    }


    /**
     * 设置彩光饱亮度
     */
    public void bulbColourSat(int progress) {
        float[] hsv = new float[3];
        Color.colorToHSV(mColor, hsv);
        float mHue = hsv[0];
        float mVal = mColourValProgrees + 10;
        mColourSatProgrees = progress;
        String angle = CommentUtils.integerToHexstring((int) mHue, 4);
        String s = CommentUtils.integerToHexstring((progress), 4);
        String v = CommentUtils.integerToHexstring((int) mVal, 4);
        colour = angle + s + v;

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
        float[] hsv = new float[3];
        Color.colorToHSV(mColor, hsv);
        float mHue = hsv[0];
        float mSat = mColourSatProgrees;
        mColourValProgrees = progress;
        String angle = CommentUtils.integerToHexstring((int) mHue, 4);
        String s = CommentUtils.integerToHexstring((int) mSat, 4);
        String v = CommentUtils.integerToHexstring((progress + 10), 4);
        colour = angle + s + v;

        hsv[1] = mSat / 1000f;
        hsv[2] = (float) progress / (990);
        int newColor = Color.HSVToColor(hsv);
        baseView.setCenterColor(newColor);

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
        this.scene = scene;
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


    /**
     * 设置倒计时
     */
    public void bulbCountdown() {
        View bodyView = LayoutInflater.from(context).inflate(R.layout.bulb_dialog_time_select, null, false);
        dialogFragment = CircleDialogUtils.showCommentBodyDialog(bodyView, ((FragmentActivity) context).getSupportFragmentManager(), view -> {
            List<String> hours = CommentUtils.getHours();
            List<String> mins = CommentUtils.getMins();
            WheelView wheelHour = view.findViewById(R.id.wheel_hour);
            WheelView wheelMin = view.findViewById(R.id.wheel_min);
            CheckBox cbStatus = view.findViewById(R.id.cb_checked);
            cbStatus.setChecked(true);
            int hour = 0;
            int min = 0;
            if (!TextUtils.isEmpty(countdown) && !"0".equals(countdown)) {
                int time = Integer.parseInt(countdown);
                hour = time / (60 * 60);
                min = (time % (60 * 60)) / (60);
            }

            //初始化时间选择器
            wheelHour.setCyclic(true);
            wheelHour.isCenterLabel(true);
            wheelHour.setAdapter(new ArrayWheelAdapter<>(hours));
            wheelHour.setCurrentItem(hour);
            wheelHour.setTextColorCenter(ContextCompat.getColor(context, R.color.white));
            wheelMin.setCyclic(true);
            wheelMin.isCenterLabel(true);
            wheelMin.setAdapter(new ArrayWheelAdapter<>(mins));
            wheelMin.setCurrentItem(min);
            wheelMin.setTextColorCenter(ContextCompat.getColor(context, R.color.white));

            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogFragment.dismiss();
                }
            });

            view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cbStatus.isChecked()) {
                        int hour = wheelHour.getCurrentItem();
                        int min = wheelMin.getCurrentItem();
                        countdown = String.valueOf(hour * 3600 + min * 60);
                        dialogFragment.dismiss();
                    } else {
                        countdown = String.valueOf(0);
                    }
                    bulbCountDown();
                }
            });
        });
    }


    /**
     * 倒计时
     */
    private void bulbCountDown() {
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbCountdown(), Integer.parseInt(countdown), mTuyaDevice, this);
        }
    }

    public void toEditScene() {
        BulbSceneBean sceneBean = baseView.getSceneBean();
        List<BulbSceneBean> sceneList = baseView.getSceneList();
        if (sceneBean == null) return;
        String beanJson = new Gson().toJson(sceneBean);
        String beanListJson = new Gson().toJson(sceneList);
        Intent intent = new Intent(context, BulbSceneEditActivity.class);
        intent.putExtra(GlobalConstant.BULB_SCENE_BEAN, beanJson);
        intent.putExtra(GlobalConstant.BULB_SCENE_BEAN_LIST, beanListJson);
        intent.putExtra(GlobalConstant.DEVICE_ID, deviceId);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /**
     * 跳转到定时
     */
    public void jumpTiming() {
        Intent timingIntent = new Intent(context, DeviceTimingListActivity.class);
        timingIntent.putExtra(GlobalConstant.DEVICE_ID, deviceId);
        timingIntent.putExtra(GlobalConstant.DEVICE_NAME, devName);
        timingIntent.putExtra(GlobalConstant.DEVICE_TYPE, DeviceTypeConstant.TYPE_BULB);
        ActivityUtils.startActivity((Activity) context, timingIntent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /**
     * 跳转到设置
     */
    public void jumpSetting() {
        Intent intent = new Intent(context, DeviceSettingActivity.class);
        intent.putExtra(GlobalConstant.DEVICE_ID, deviceId);
        intent.putExtra(GlobalConstant.DEVICE_NAME, devName);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, DeviceTypeConstant.TYPE_BULB);
        String deviceJson = new Gson().toJson(mGroDeviceBean);
        intent.putExtra(GlobalConstant.DEVICE_BEAN, deviceJson);
        intent.putExtra(GlobalConstant.ROOM_ID, roomId);
        intent.putExtra(GlobalConstant.ROOM_NAME, roomName);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void setDevName(String name) {
        devName = name;
        if (mGroDeviceBean != null) {
            mGroDeviceBean.setName(name);
        }
    }

    public void setRoomInfo(TransferDevMsg bean) {
        roomName = bean.getRoomName();
        roomId = bean.getRoomId();
        if (mGroDeviceBean != null) {
            mGroDeviceBean.setRoomId(Integer.parseInt(bean.getRoomId()));
            mGroDeviceBean.setRoomName(bean.getRoomName());
        }
    }


    @Override
    public void onDpUpdate(String devId, String dpStr) {
        LogUtil.d(dpStr);
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
                    } else if (key.equals(DeviceBulb.getBulbSceneData())) {
                        this.scene = object.optString(DeviceBulb.getBulbSceneData());
                        baseView.setScene(scene);
                    } else if (key.equals(DeviceBulb.getBulbCountdown())) {
                        this.countdown = object.optString(DeviceBulb.getBulbCountdown());
                        baseView.setCuntDown(countdown);
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

    public void destroyTuya() {
        if (mTuyaDevice != null) {
            mTuyaDevice.unRegisterDevListener();
            mTuyaDevice.onDestroy();
        }
    }
}
