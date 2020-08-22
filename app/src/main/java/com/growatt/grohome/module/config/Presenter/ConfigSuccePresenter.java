package com.growatt.grohome.module.config.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.DevEditNameBean;
import com.growatt.grohome.eventbus.TransferDevMsg;
import com.growatt.grohome.module.config.view.IConfigSuccessView;
import com.growatt.grohome.module.device.BulbActivity;
import com.growatt.grohome.module.device.SwitchActivity;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.room.RoomManager;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ConfigSuccePresenter extends BasePresenter<IConfigSuccessView> {

    private String deviceName;
    private String deviceType;
    private String deviceId;
    private String mRoomId;
    private String mRoomName;


    private ITuyaDevice mTuyaDevice;

    public ConfigSuccePresenter(IConfigSuccessView baseView) {
        super(baseView);
    }

    public ConfigSuccePresenter(Context context, IConfigSuccessView baseView) {
        super(context, baseView);
        deviceType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_TYPE);
        deviceId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_ID);
        deviceName = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_NAME);
        mRoomId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.ROOM_ID);
        mRoomName = ((Activity) context).getIntent().getStringExtra(GlobalConstant.ROOM_NAME);
        mTuyaDevice = TuyaHomeSdk.newDeviceInstance(deviceId);
    }


    /**
     * 设备改名
     */
    public void getDeviceData() {
        baseView.setDeviceName(deviceName);
    }


    /**
     * 设备改名
     */
    public void editName() {
        CircleDialogUtils.showCommentInputDialog((FragmentActivity) context, context.getString(R.string.m148_edit), deviceName,
                context.getString(R.string.m233_please_entry_name), true, new OnInputClickListener() {
                    @Override
                    public boolean onClick(String text, View v) {
                        try {
                            if (!TextUtils.isEmpty(text)) {
                                editNameByTuya(text);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
    }


    /**
     * 通过涂鸦修改设备名称
     */
    private void editNameByTuya(String name) {
        if (TextUtils.isEmpty(name)) return;
        mTuyaDevice.renameDevice(name, new IResultCallback() {
            @Override
            public void onError(String s, String s1) {
                MyToastUtils.toast(R.string.m201_fail);
            }

            @Override
            public void onSuccess() {
                try {
                    editNameByServer(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void editNameByServer(String reName) throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("devId", deviceId);
        requestJson.put("devType", deviceType);
        requestJson.put("name", reName);
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        Observable<String> stringObservable;
        if (DeviceTypeConstant.TYPE_PANELSWITCH.equals(deviceType)) {
            stringObservable = apiServer.updateSwitchName(body);
        } else {
            stringObservable = apiServer.editDevName(body);
        }
        addDisposable(stringObservable, new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String jsonBean) {
                try {
                    JSONObject object = new JSONObject(jsonBean);
                    int code = object.getInt("code");
                    if (code == 0) {
                        deviceName = reName;
                        baseView.setDeviceName(reName);
                        DevEditNameBean bean = new DevEditNameBean();
                        bean.setDevId(deviceId);
                        bean.setName(reName);
                        EventBus.getDefault().post(bean);
                        MyToastUtils.toast(R.string.m200_success);
                    } else {
                        MyToastUtils.toast(R.string.m201_fail);
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


    public void jumpToDeviceDetail() {
        if (TextUtils.isEmpty(deviceType)) return;
        switch (deviceType) {
            case DeviceTypeConstant.TYPE_PANELSWITCH:
                Intent intent1 = new Intent(context, SwitchActivity.class);
                intent1.putExtra(GlobalConstant.DEVICE_ID, deviceId);
                intent1.putExtra(GlobalConstant.DEVICE_NAME, deviceName);
                intent1.putExtra(GlobalConstant.ROOM_NAME, mRoomName);
                intent1.putExtra(GlobalConstant.ROOM_ID, mRoomId);
                ActivityUtils.startActivity((Activity) context, intent1, ActivityUtils.ANIMATE_FORWARD, true);
                break;
            case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
            case DeviceTypeConstant.TYPE_BULB:
                Intent intent2 = new Intent(context, BulbActivity.class);
                intent2.putExtra(GlobalConstant.DEVICE_ID, deviceId);
                intent2.putExtra(GlobalConstant.DEVICE_NAME, deviceName);
                intent2.putExtra(GlobalConstant.ROOM_NAME, mRoomName);
                intent2.putExtra(GlobalConstant.ROOM_ID, mRoomId);
                intent2.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
                ActivityUtils.startActivity((Activity) context, intent2, ActivityUtils.ANIMATE_FORWARD, true);
                break;
            default:
                MyToastUtils.toast(R.string.m275_function_is_not_ready);
                break;
        }
    }


    public void getRoomList() {
        List<HomeRoomBean> homeRoomList = RoomManager.getInstance().getHomeRoomList();
        if (homeRoomList != null) {
            baseView.upRoomList(homeRoomList);
        } else {
            try {
                getRoomListByServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取房间列表
     *
     * @throws Exception
     */
    public void getRoomListByServer() throws Exception {
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
                        baseView.upRoomList(roomList);
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
     * 转移设备
     *
     * @throws Exception
     */
    public void transferDevice(String roomId, String roomName) throws Exception {
        JSONObject requestJson = new JSONObject();
        requestJson.put("roomId", roomId);
        requestJson.put("cmd", "updateDeviceRoom");
        requestJson.put("devId", deviceId);
        requestJson.put("lan", CommentUtils.getLanguage());
        requestJson.put("devType", deviceType);
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.roomRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    mRoomId = roomId;
                    mRoomName = roomName;
                    TransferDevMsg transferBean = new TransferDevMsg();
                    transferBean.setRoomId(roomId);
                    transferBean.setRoomName(roomName);
                    EventBus.getDefault().post(bean);
                    jumpToDeviceDetail();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTuyaDevice != null) {
            mTuyaDevice.onDestroy();
        }
    }
}
