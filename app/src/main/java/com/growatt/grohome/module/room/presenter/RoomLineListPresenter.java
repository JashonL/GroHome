package com.growatt.grohome.module.room.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.TransferDevMsg;
import com.growatt.grohome.module.room.RoomAddActivity;
import com.growatt.grohome.module.room.RoomManager;
import com.growatt.grohome.module.room.view.IRoomLineListView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RoomLineListPresenter extends BasePresenter<IRoomLineListView> {


    private String DeviceId;
    private String deviceType;
    private String currentRoomId;

    public RoomLineListPresenter(IRoomLineListView baseView) {
        super(baseView);
    }

    public RoomLineListPresenter(Context context, IRoomLineListView baseView) {
        super(context, baseView);
        DeviceId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_ID);
        deviceType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_TYPE);
        currentRoomId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.ROOM_ID);
    }


    public void getRoomList() {
        List<HomeRoomBean> homeRoomList = RoomManager.getInstance().getHomeRoomList();
        if (homeRoomList != null) {
            for (int i = 0; i < homeRoomList.size(); i++) {
                HomeRoomBean roomBean = homeRoomList.get(i);
                int cid = roomBean.getCid();
                roomBean.setSelect(false);
                if (String.valueOf(cid).equals(currentRoomId)) {
                    roomBean.setSelect(true);
                }
            }
            baseView.updata(homeRoomList);
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
                            int cid = roomBean.getCid();
                            roomBean.setSelect(false);
                            if (currentRoomId.equals(String.valueOf(cid))) {
                                roomBean.setSelect(true);
                            }
                            roomList.add(roomBean);
                        }
                        baseView.updata(roomList);
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
    public void transferDevice(String roomId,String roomName) throws Exception {
        if (roomId.equals(currentRoomId)) {
            ((Activity) context).finish();
            return;
        }
        JSONObject requestJson = new JSONObject();
        requestJson.put("roomId", roomId);
        requestJson.put("cmd", "updateDeviceRoom");
        requestJson.put("devId", DeviceId);
        requestJson.put("lan", CommentUtils.getLanguage());
        requestJson.put("devType", deviceType);
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.roomRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject obj = new JSONObject(bean);
                    int code = obj.getInt("code");
                    if (code == 0) {
                        TransferDevMsg transferBean = new TransferDevMsg();
                        transferBean.setRoomId(currentRoomId);
                        transferBean.setRoomName(roomName);
                        EventBus.getDefault().post(transferBean);
                        baseView.transferSuccess();
                    } else {
                        String data = obj.getString("data");
                        baseView.transferFail(data);
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
     * 添加房间
     */
    public void addRoom() {
        Intent intent = new Intent(context, RoomAddActivity.class);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


}
