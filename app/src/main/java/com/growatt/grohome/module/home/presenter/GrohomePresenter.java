package com.growatt.grohome.module.home.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.HomeDeviceBean;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.BulbActivity;
import com.growatt.grohome.module.device.SwitchActivity;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DevicePlug;
import com.growatt.grohome.module.device.manager.DeviceThermostat;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.room.RoomListActivity;
import com.growatt.grohome.module.home.view.IGrohomeView;
import com.growatt.grohome.tuya.SendDpListener;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class GrohomePresenter extends BasePresenter<IGrohomeView> implements IDevListener, SendDpListener {

    /*用来操作涂鸦设备的集合*/
    private Map<String, ITuyaDevice> mTuyaDevices = new HashMap<>();
    private DeviceBean deviceBean;


    public GrohomePresenter(IGrohomeView baseView) {
        super(baseView);
    }

    public GrohomePresenter(Context context, IGrohomeView baseView) {
        super(context, baseView);
    }


    public void getAlldevice() throws Exception {
        JSONObject requestJson = new JSONObject();
        requestJson.put("userId", App.getUserBean().accountName);
        requestJson.put("cmd", "devList");
        requestJson.put("userServerId", "0");
        requestJson.put("userServerUrl", "http://server-cn.growatt.com/");
        requestJson.put("lan", String.valueOf(CommentUtils.getLanguage()));
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.getAllDevice(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                Log.i(TuyaApiUtils.TUYA_TAG, "请求成功：" + bean);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(bean);
                    int code = obj.getInt("code");
                    if (0 == code) {
                        HomeDeviceBean infoData = new Gson().fromJson(bean, HomeDeviceBean.class);
                        baseView.setAllDeviceSuccess(infoData);
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
     * 获取房间列表
     *
     * @throws Exception
     */
    public void getRoomList() throws Exception {
        JSONObject requestJson = new JSONObject();
        requestJson.put("userId", App.getUserBean().accountName);
        requestJson.put("cmd", "roomList");
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.roomRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                Log.i(TuyaApiUtils.TUYA_TAG, "请求成功：" + bean);
                try {
                    JSONObject obj = new JSONObject(bean);
                    int code = obj.getInt("code");
                    if (code == 0) {
                        JSONArray dataArray = obj.getJSONArray("data");
                        List<HomeRoomBean> roomList = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            HomeRoomBean roomBean = new Gson().fromJson(jsonObject.toString(), HomeRoomBean.class);
                            roomList.add(roomBean);
                        }
                        baseView.setRoomListSuccess(roomList);
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
     * 跳转到设备操作页面
     */
    public void jumpToRoom(String roomList,int position) {
        Intent intent = new Intent(context, RoomListActivity.class);
        intent.putExtra(GlobalConstant.ROOM_LIST,roomList);
        intent.putExtra(GlobalConstant.ROOM_POSITION,position);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }





    /**
     * 跳转到设备操作页面
     */
    public void jumpTodevice(HomeDeviceBean.DataBean bean) {
        String devType = bean.getDevType();
        Class clazz = null;
        if (DeviceTypeConstant.TYPE_PANELSWITCH.equals(devType)) {
            clazz = SwitchActivity.class;
        } else if (DeviceTypeConstant.TYPE_BULB.equals(devType)) {
            clazz = BulbActivity.class;
        }
        if (clazz == null) return;
        Intent intentThermostat = new Intent(context, clazz);
        intentThermostat.putExtra(GlobalConstant.DEVICE_ID, bean.getDevId());
        intentThermostat.putExtra(GlobalConstant.DEVICE_NAME, bean.getName());
        ActivityUtils.startActivity((Activity) context, intentThermostat, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /**
     * 获取设备的开关状态
     *
     * @param devId 设备id
     */
    public int initDevOnOff(String devType, String devId) {
        DeviceBean deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
        String onOff = "true";//设备的开关状态
        if (deviceBean != null) {
            switch (devType) {
                case DeviceTypeConstant.TYPE_PADDLE:
                    onOff = String.valueOf(deviceBean.getDps().get(DevicePlug.getPlugOnoff()));
                    break;
                case DeviceTypeConstant.TYPE_THERMOSTAT:
                    onOff = String.valueOf(deviceBean.getDps().get(DeviceThermostat.getSwitchThermostat()));
                    break;
                case DeviceTypeConstant.TYPE_PANELSWITCH:
                    onOff = String.valueOf(deviceBean.getDps().get("1"));//默认获取第一路的开关
                    break;
                case DeviceTypeConstant.TYPE_BULB:
                    onOff = String.valueOf(deviceBean.getDps().get(DeviceBulb.getBulbSwitchLed()));
                    break;
            }
        }
        return "true".equals(onOff) ? 1 : 0;
    }


    /**
     * 初始化设备状态变化的监听
     *
     * @param devId 设备id
     */
    public void initTuyaDevices(String devId) {
        ITuyaDevice iTuyaDevice = mTuyaDevices.get(devId);
        if (iTuyaDevice != null) {//先把对应的释放掉，再从新添加
            iTuyaDevice.unRegisterDevListener();
            iTuyaDevice.onDestroy();
            mTuyaDevices.remove(devId);
        }
        ITuyaDevice mTuyaDevice = TuyaHomeSdk.newDeviceInstance(devId);
        mTuyaDevice.registerDevListener(this);
        mTuyaDevices.put(devId, mTuyaDevice);
    }


    /**
     * 离开时，销毁所有的设备监听
     */
    private void destroyDevices() {
        Set<String> keys = mTuyaDevices.keySet();
        for (String id : keys) {
            ITuyaDevice iTuyaDevice = mTuyaDevices.get(id);
            if (iTuyaDevice != null) {
                iTuyaDevice.unRegisterDevListener();
                iTuyaDevice.onDestroy();
            }
        }
    }


    /**
     * 开关
     */
    public void deviceSwitch(String devId, String devType) {
        deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
        String onOff;
        boolean bulb_onoff;
        LinkedHashMap<String, Object> sendMap = new LinkedHashMap<>();
        if (deviceNotOnline()) {
            switch (devType) {
                case DeviceTypeConstant.TYPE_BULB:
                    onOff = String.valueOf(deviceBean.getDps().get(DeviceBulb.getBulbSwitchLed()));
                    bulb_onoff = "true".equals(onOff);
                    sendMap.put(DeviceBulb.getBulbSwitchLed(), !bulb_onoff);
                    break;
                case DeviceTypeConstant.TYPE_PADDLE:
                    onOff = String.valueOf(deviceBean.getDps().get(DevicePlug.getPlugOnoff()));
                    bulb_onoff = "true".equals(onOff);
                    sendMap.put(DeviceBulb.getBulbSwitchLed(), !bulb_onoff);
                    break;
                case DeviceTypeConstant.TYPE_THERMOSTAT:
                    onOff = String.valueOf(deviceBean.getDps().get(DeviceThermostat.getSwitchThermostat()));
                    bulb_onoff = "true".equals(onOff);
                    sendMap.put(DeviceBulb.getBulbSwitchLed(), !bulb_onoff);
                    break;
            }
            ITuyaDevice mTuyaDevice = mTuyaDevices.get(devId);
            if (mTuyaDevice == null) return;
            TuyaApiUtils.sendCommand(sendMap, mTuyaDevice, this);
        }

    }


    /**
     * 开关
     */
    public void deviceSwitch(String devId, int road, int onOff) {
        deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
        boolean bulb_onoff = onOff == 1;
        LinkedHashMap<String, Object> sendMap = new LinkedHashMap<>();
        if (deviceNotOnline()) {
            for (int i = 0; i < road; i++) {
                sendMap.put(String.valueOf(i + 1), !bulb_onoff);
            }
            ITuyaDevice mTuyaDevice = mTuyaDevices.get(devId);
            if (mTuyaDevice == null) return;
            TuyaApiUtils.sendCommand(sendMap, mTuyaDevice, this);
        }

    }


    /**
     * 判断是否在线
     *
     * @return
     */
    private boolean deviceNotOnline() {
        if (deviceBean == null) {
            MyToastUtils.toast(R.string.m149_device_does_not_exist);
            return false;
        }
        if (!deviceBean.getIsOnline()) {
            MyToastUtils.toast(R.string.m150_device_is_offline);
            return false;
        }
        return true;
    }

    @Override
    public void onDpUpdate(String devId, String dpStr) {
        Log.i(TuyaApiUtils.TUYA_TAG, "deviceId:" + devId + "responed:" + dpStr + "time：" + System.currentTimeMillis());
        try {
            List<HomeDeviceBean.DataBean> deviceList = baseView.getDeviceList();
            HomeDeviceBean.DataBean allDeviceBean = new HomeDeviceBean.DataBean();
            allDeviceBean.setDevId(devId);
            int allDevice = deviceList.indexOf(allDeviceBean);
            if (allDevice != -1) {
                String devType = deviceList.get(allDevice).getDevType();
                JSONObject object = new JSONObject(dpStr);
                Iterator iterator = object.keys();
                switch (devType) {
                    case DeviceTypeConstant.TYPE_PANELSWITCH:
                        try {
                            int road = deviceList.get(allDevice).getRoad();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                String value = String.valueOf(object.optBoolean(key));
                                if (Integer.parseInt(key) > road) return;
                                if ("true".equals(value)) {
                                    baseView.upDataStatus(devId, "1");
                                } else {
                                    baseView.upDataStatus(devId, "0");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case DeviceTypeConstant.TYPE_PADDLE:
                        try {
                            if (object.length() > 2) return;//自动上报，可能导致状态错误
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                if (DevicePlug.getPlugOnoff().equals(key)) {
                                    String value = String.valueOf(object.optBoolean(key));
                                    if ("true".equals(value)) {
                                        baseView.upDataStatus(devId, "1");
                                    } else {
                                        baseView.upDataStatus(devId, "0");
                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case DeviceTypeConstant.TYPE_THERMOSTAT:
                        try {
                            if (object.length() > 2) return;//自动上报，可能导致状态错误
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                if (DeviceThermostat.getSwitchThermostat().equals(key)) {
                                    String value = String.valueOf(object.optBoolean(key));
                                    if ("true".equals(value)) {
                                        baseView.upDataStatus(devId, "1");
                                    } else {
                                        baseView.upDataStatus(devId, "0");
                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case DeviceTypeConstant.TYPE_BULB:
                        try {
                            if (object.length() > 2) return;//自动上报，可能导致状态错误
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                if (DeviceBulb.getBulbSwitchLed().equals(key)) {
                                    String value = String.valueOf(object.optBoolean(key));
                                    if ("true".equals(value)) {
                                        baseView.upDataStatus(devId, "1");
                                    } else {
                                        baseView.upDataStatus(devId, "0");
                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
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
    public void onDestroy() {
        super.onDestroy();
        destroyDevices();
    }

    @Override
    public void sendCommandSucces() {

    }

    @Override
    public void sendCommandError(String code, String error) {

    }
}
