package com.growatt.grohome.module.room.presenter;

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
import com.growatt.grohome.eventbus.DeviceAddOrDelMsg;
import com.growatt.grohome.eventbus.HomeRoomEvent;
import com.growatt.grohome.module.device.DeviceTypeActivity;
import com.growatt.grohome.module.room.RoomImageActivity;
import com.growatt.grohome.module.room.RoomLineListActivity;
import com.growatt.grohome.module.room.view.IRoomEditView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RoomEditPresenter extends BasePresenter<IRoomEditView> {

    private HomeRoomBean mRoomBean;

    private String roomName = "";

    private String roomJson;

    private int mTransPosition;

    public RoomEditPresenter(IRoomEditView baseView) {
        super(baseView);
    }

    public RoomEditPresenter(Context context, IRoomEditView baseView) {
        super(context, baseView);

    }


    public void getData() {
        roomJson = ((Activity) context).getIntent().getStringExtra(GlobalConstant.ROOM_BEAN);
        if (!TextUtils.isEmpty(roomJson)) {
            this.mRoomBean = new Gson().fromJson(roomJson, HomeRoomBean.class);
            baseView.upRoomData(mRoomBean);
            roomName = mRoomBean.getName();
        }
    }


    public void editRoomName() {
        CircleDialogUtils.showCommentInputDialog((FragmentActivity) context, context.getString(R.string.m148_edit), roomName,
                context.getString(R.string.m189_enter_room_name_tips), true, new OnInputClickListener() {
                    @Override
                    public boolean onClick(String text, View v) {
                        try {
                            requestEditName(text);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
    }


    public void requestEditName(String name) throws JSONException {
        if (TextUtils.isEmpty(name)) {
            MyToastUtils.toast(R.string.m189_enter_room_name_tips);
            return;
        }
        JSONObject requestJson = new JSONObject();
        requestJson.put("roomId", mRoomBean.getCid());
        requestJson.put("cmd", "updateRoom");
        requestJson.put("roomName", name);
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.editRoomName(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String data = obj.getString("data");
                    if (code == 0) {
                        HomeRoomEvent roomEvent = new HomeRoomEvent();
                        roomEvent.setRoomId(mRoomBean.getCid());
                        roomEvent.setName(name);
                        EventBus.getDefault().postSticky(roomEvent);
                        baseView.editNameSuccess(name);
                    } else {
                        baseView.editNameFail(data);
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


    public void updateImage() {
        Intent intent = new Intent(context, RoomImageActivity.class);
        intent.putExtra(GlobalConstant.ROOM_BEAN, roomJson);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void deleteRoom() {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m209_delete_room), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    requestDeleteRoom();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 删除设备
     */

    public void deleteDevice(String deviceId, String deviceType) {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m210_delete_device), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    requestDeleteDevice(deviceId, deviceType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void setmTransPosition(int mTransPosition) {
        this.mTransPosition = mTransPosition;
    }

    public int getmTransPosition() {
        return mTransPosition;
    }

    /**
     * 转移设备
     */

    public void transferDevice(String deviceId, String deviceType) {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m294_transfer_device), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    requestTransferDevice(deviceId, deviceType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 转移设备
     */
    public void requestTransferDevice(String deviceId, String deviceType) throws JSONException {
        Intent intent = new Intent(context, RoomLineListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(GlobalConstant.ROOM_ID, String.valueOf(mRoomBean.getCid()));
        intent.putExtra(GlobalConstant.ROOM_NAME, roomName);
        intent.putExtra(GlobalConstant.DEVICE_ID, deviceId);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.ACITION_KEY, GlobalConstant.ACITION_DEVICE_TRANSFER);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /**
     * 删除设备
     */
    public void requestDeleteDevice(String deviceId, String deviceType) throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("devId", deviceId);
        requestJson.put("devType", deviceType);
        requestJson.put("userId", App.getUserBean().getAccountName());
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.removeDevice(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject obj = new JSONObject(bean);
                    int code = obj.getInt("code");
                    if (code == 0) {
                        baseView.deleteDeviceSuccess(deviceId);
                        //通知首页更新
                        DeviceAddOrDelMsg deviceAddOrDelMsg = new DeviceAddOrDelMsg();
                        deviceAddOrDelMsg.setType(DeviceAddOrDelMsg.DELETE_DEV);
                        deviceAddOrDelMsg.setDevType(deviceType);
                        deviceAddOrDelMsg.setDevId(deviceId);
                        EventBus.getDefault().post(deviceAddOrDelMsg);
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
     * 删除房间
     */
    public void requestDeleteRoom() throws JSONException {
        if (mRoomBean == null) return;
        JSONObject requestJson = new JSONObject();
        requestJson.put("roomId", String.valueOf(mRoomBean.getCid()));
        requestJson.put("cmd", "deleteRoom");
        requestJson.put("userId", App.getUserBean().getAccountName());
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
                        baseView.deleteRoomSuccess();
                        HomeRoomEvent roomEvent = new HomeRoomEvent();
                        roomEvent.setRoomId(mRoomBean.getCid());
                        EventBus.getDefault().post(roomEvent);
                        ((Activity) context).finish();
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
     * 去添加设备
     */
    public void toAddDevice() {
        Intent intent = new Intent(context, DeviceTypeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("isRoom", true);
        intent.putExtra("roomId", String.valueOf(mRoomBean.getCid()));
        intent.putExtra("roomName", roomName);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }


}
