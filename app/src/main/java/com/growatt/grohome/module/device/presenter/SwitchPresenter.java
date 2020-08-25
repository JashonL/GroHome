package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.PanelSwitchBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.TransferDevMsg;
import com.growatt.grohome.module.device.DeviceSettingActivity;
import com.growatt.grohome.module.device.DeviceTimingListActivity;
import com.growatt.grohome.module.device.EditNameActivity;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.view.ISwitchView;
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
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SwitchPresenter extends BasePresenter<ISwitchView> implements IDevListener, SendDpListener {

    private GroDeviceBean mGroDeviceBean;
    private String deviceId;
    private String devName;
    private String roomId;
    private String roomName;

    private PanelSwitchBean panelSwitchBean;
    private ITuyaDevice mTuyaDevice;
    private DeviceBean deviceBean;
    private List<String> nameList = new ArrayList<>();

    public SwitchPresenter(ISwitchView baseView) {
        super(baseView);
    }

    public SwitchPresenter(Context context, ISwitchView baseView) {
        super(context, baseView);
        deviceId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_ID);
        devName = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_NAME);
        roomId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.ROOM_ID);
        roomName = ((Activity) context).getIntent().getStringExtra(GlobalConstant.ROOM_NAME);
        String deviceJson = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_BEAN);
        if (!TextUtils.isEmpty(deviceJson)) {
            mGroDeviceBean = new Gson().fromJson(deviceJson, GroDeviceBean.class);
            deviceId = mGroDeviceBean.getDevId();
            devName = mGroDeviceBean.getName();
            roomId = String.valueOf(mGroDeviceBean.getRoomId());
            roomName = mGroDeviceBean.getRoomName();
        }
        if (!TextUtils.isEmpty(devName)) {
            baseView.setTitle(devName);
        }
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
        try {
            getDetailData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取面板详情
     *
     * @throws Exception
     */
    public void getDetailData() throws Exception {
        JSONObject requestJson = new JSONObject();
        requestJson.put("devId", deviceId);
        requestJson.put("lan", String.valueOf(CommentUtils.getLanguage()));
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.getSwitchDetail(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(bean);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        //解析数据
                        JSONObject panelObject = jsonObject.getJSONObject("data");
                        panelSwitchBean = new PanelSwitchBean();
                        Iterator<String> iterator = panelObject.keys();
                        int road = 0;
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            Object value = panelObject.get(key);
                            if ("devId".equals(key)) {
                                panelSwitchBean.setDevId((String) value);
                            } else if ("name".equals(key)) {
                                panelSwitchBean.setName((String) value);
                            } else if ("onoff".equals(key)) {
                                panelSwitchBean.setOnoff((Integer) value);
                            } else if ("online".equals(key)) {
                                panelSwitchBean.setOnline((Integer) value);
                            } else if (key.contains("code")) {
                                road++;
                            }
                        }
                        panelSwitchBean.setRoad(road);
                        devName = panelObject.getString("name");
                        baseView.setTitle(devName);
                        road = panelSwitchBean.getRoad();
                        for (int i = 0; i < road; i++) {
                            PanelSwitchBean.SwichBean swichBean = new PanelSwitchBean.SwichBean();
                            swichBean.setId(i + 1);
                            if (deviceBean != null) {
                                String onOff = String.valueOf(deviceBean.getDps().get(String.valueOf(i + 1)));
                                if (!TextUtils.isEmpty(onOff)) {
                                    swichBean.setOnOff("true".equals(onOff) ? 1 : 0);
                                }
                            }
                            String name = panelObject.getString("code" + swichBean.getId());
                            swichBean.setName(name);
                            swichBean.setCustomName(panelObject.getString("name" + swichBean.getId()));
                            nameList.add(name);
                            panelSwitchBean.addSwitchBean(swichBean);
                        }
                        List<PanelSwitchBean.SwichBean> beanList = panelSwitchBean.getBeanList();
                        panelSwitchBean.setSwitchNum(beanList.size());
                        baseView.freshData(beanList);
                    }
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


    /**
     * 跳转到设置
     */
    public void jumpSetting() {
        Intent intent = new Intent(context, DeviceSettingActivity.class);
        intent.putExtra(GlobalConstant.DEVICE_ID, deviceId);
        intent.putExtra(GlobalConstant.DEVICE_NAME, devName);
        intent.putExtra(GlobalConstant.ROOM_ID, roomId);
        intent.putExtra(GlobalConstant.ROOM_NAME, roomName);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, DeviceTypeConstant.TYPE_PANELSWITCH);
        String deviceJson = new Gson().toJson(mGroDeviceBean);
        intent.putExtra(GlobalConstant.DEVICE_BEAN, deviceJson);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /**
     * 跳转到定时
     */
    public void jumpTiming() {
        Intent intent = new Intent(context, DeviceTimingListActivity.class);
        intent.putExtra(GlobalConstant.DEVICE_ID, deviceId);
        intent.putExtra(GlobalConstant.DEVICE_NAME, devName);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, DeviceTypeConstant.TYPE_PANELSWITCH);
        String deviceJson = new Gson().toJson(mGroDeviceBean);
        intent.putExtra(GlobalConstant.DEVICE_BEAN, deviceJson);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /**
     * 跳转到修改名字
     */
    public void jumpEditName(int switchId) {
        Intent intent = new Intent(context, EditNameActivity.class);
        intent.putExtra(GlobalConstant.DEVICE_ID, deviceId);
        intent.putExtra(GlobalConstant.DEVICE_NAME, devName);
        intent.putExtra(GlobalConstant.DEVICE_SWITCH_ID, switchId);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, DeviceTypeConstant.TYPE_PANELSWITCH);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    /**
     * 操作所有开关
     *
     * @param isOpen 开或者关
     */
    public void allOnoff(boolean isOpen) {
        if (panelSwitchBean == null) {
            MyToastUtils.toast(R.string.m171_check_network);
            return;
        }
        if (deviceNotOnline()) {
            LinkedHashMap<String, Object> dpMap = new LinkedHashMap<>();
            for (int i = 0; i < panelSwitchBean.getRoad(); i++) {
                dpMap.put(String.valueOf(i + 1), isOpen);
            }
            TuyaApiUtils.sendCommand(dpMap, mTuyaDevice, this);
        }

    }


    /**
     * 操作单个开关
     */
    public void singleOnOff(String dpId) {
        try {
            if (panelSwitchBean == null) {
                MyToastUtils.toast(R.string.m171_check_network);
                return;
            }
            if (deviceNotOnline()) {
                String onOff = String.valueOf(deviceBean.getDps().get(dpId));
                boolean isOpen = "true".equals(onOff);
                TuyaApiUtils.sendCommand(dpId, !isOpen, mTuyaDevice, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void sendCommandSucces() {

    }

    @Override
    public void sendCommandError(String code, String error) {

    }

    @Override
    public void onDpUpdate(String devId, String dpStr) {
        if (devId.equals(deviceId)) {
            try {
                JSONObject object = new JSONObject(dpStr);
                Iterator iterator = object.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    boolean value;
                    value = object.getBoolean(key);
                    baseView.freshStatus(Integer.parseInt(key) - 1, value);
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

    public void destroyTuya() {
        if (mTuyaDevice != null) {
            mTuyaDevice.unRegisterDevListener();
            mTuyaDevice.onDestroy();
        }
    }

}
