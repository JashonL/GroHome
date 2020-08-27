package com.growatt.grohome.module.room.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.BulbActivity;
import com.growatt.grohome.module.device.SwitchActivity;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DevicePlug;
import com.growatt.grohome.module.device.manager.DeviceStripLights;
import com.growatt.grohome.module.device.manager.DeviceThermostat;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.room.RoomAddActivity;
import com.growatt.grohome.module.room.RoomEditActivity;
import com.growatt.grohome.module.room.RoomManager;
import com.growatt.grohome.module.room.view.IRoomListView;
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

public class RoomListPresenter extends BasePresenter<IRoomListView> implements IDevListener, SendDpListener {
    /*用来操作涂鸦设备的集合*/
    private Map<String, ITuyaDevice> mTuyaDevices = new HashMap<>();

    private DeviceBean deviceBean;

    private HomeRoomBean mCurrenRoom;

    private int currentPosition;

    public RoomListPresenter(IRoomListView baseView) {
        super(baseView);
    }

    public RoomListPresenter(Context context, IRoomListView baseView) {
        super(context, baseView);
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
                        RoomManager.getInstance().setHoomRoomList(roomList);
                        baseView.updata(roomList,currentPosition);
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



    public void setmCurrenRoom(HomeRoomBean mCurrenRoom) {
        this.mCurrenRoom = mCurrenRoom;
    }


    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * 添加房间
     */
    public void addRoom() {
        Intent intent = new Intent(context, RoomAddActivity.class);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void parserData() {
        String roomDataJson = ((Activity) context).getIntent().getStringExtra(GlobalConstant.ROOM_LIST);
        int position = ((Activity) context).getIntent().getIntExtra(GlobalConstant.ROOM_POSITION, 0);
        List<HomeRoomBean> roomList = new ArrayList<>();
        if (!TextUtils.isEmpty(roomDataJson)) {
            try {
                JSONArray jsonArray = new JSONArray(roomDataJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HomeRoomBean roomBean = new Gson().fromJson(jsonObject.toString(), HomeRoomBean.class);
                    roomList.add(roomBean);
                }
                baseView.updata(roomList, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 跳转到设备操作页面
     */
    public void jumpTodevice(GroDeviceBean bean) {
        String devType = bean.getDevType();
        Class clazz;
        if (DeviceTypeConstant.TYPE_PANELSWITCH.equals(devType)) {
            clazz = SwitchActivity.class;
        } else if (DeviceTypeConstant.TYPE_BULB.equals(devType)||DeviceTypeConstant.TYPE_STRIP_LIGHTS.equals(devType)) {
            clazz = BulbActivity.class;
        }else {
            clazz = BulbActivity.class;
        }
        Intent intent = new Intent(context, clazz);
        intent.putExtra(GlobalConstant.DEVICE_ID, bean.getDevId());
        intent.putExtra(GlobalConstant.DEVICE_NAME, bean.getName());
        String deviceBean = new Gson().toJson(bean);
        intent.putExtra(GlobalConstant.DEVICE_BEAN,deviceBean);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }



    /**
     * 跳转到设备操作页面
     */
    public void jumpEditRoom() {
        if (mCurrenRoom==null)return;
        String roomJson = new Gson().toJson(mCurrenRoom);
        Intent intent = new Intent(context, RoomEditActivity.class);
        intent.putExtra(GlobalConstant.ROOM_BEAN, roomJson);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }



    /**
     * 获取设备的开关状态
     *
     * @param devId 设备id
     */
    public int initDevOnOff(String devType, String devId) {
        DeviceBean deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
        String onOff = "false";//设备的开关状态
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
                case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
                    onOff = String.valueOf(deviceBean.getDps().get(DeviceStripLights.getBulbSwitchLed()));
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
                case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
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
    public void sendCommandSucces() {

    }

    @Override
    public void sendCommandError(String code, String error) {

    }

    @Override
    public void onDpUpdate(String devId, String dpStr) {
        try {
            List<GroDeviceBean> data = baseView.getData();
            GroDeviceBean allDeviceBean = new GroDeviceBean();
            allDeviceBean.setDevId(devId);
            int allDevice = data.indexOf(allDeviceBean);
            if (allDevice != -1) {
                String devType = data.get(allDevice).getDevType();
                JSONObject object = new JSONObject(dpStr);
                Iterator iterator = object.keys();
                switch (devType) {
                    case DeviceTypeConstant.TYPE_PANELSWITCH:
                        try {
                            int road = data.get(allDevice).getRoad();
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
                    case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
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
}
