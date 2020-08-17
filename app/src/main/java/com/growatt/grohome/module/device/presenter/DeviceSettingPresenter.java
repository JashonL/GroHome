package com.growatt.grohome.module.device.presenter;

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
import com.growatt.grohome.bean.DeviceTimingBean;
import com.growatt.grohome.bean.ScenesBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.DeviceTimingMsg;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.device.DeviceSettingActivity;
import com.growatt.grohome.module.device.DeviceUpdataActivity;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.view.IDeviceSettingView;
import com.growatt.grohome.module.room.RoomLineListActivity;
import com.growatt.grohome.module.scenes.SceneDetailActivity;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DeviceSettingPresenter extends BasePresenter<IDeviceSettingView> {
    private String name;
    private String deviceType;
    private String devId;
    private String roomName;
    private List<String> nameList;
    private int used;
    private int roomId;

    private ITuyaDevice mTuyaDevice;

    public DeviceSettingPresenter(IDeviceSettingView baseView) {
        super(baseView);


    }

    public DeviceSettingPresenter(Context context, IDeviceSettingView baseView) {
        super(context, baseView);

    }

    public void getIntentData() {
        Intent intent = ((Activity) context).getIntent();
        name = intent.getStringExtra(GlobalConstant.DEVICE_NAME);
        deviceType = intent.getStringExtra(GlobalConstant.DEVICE_TYPE);
        devId = intent.getStringExtra(GlobalConstant.DEVICE_ID);
        mTuyaDevice = TuyaHomeSdk.newDeviceInstance(devId);
        roomName = intent.getStringExtra(GlobalConstant.ROOM_NAME);
        roomId = intent.getIntExtra(GlobalConstant.ROOM_ID, -1);
        if (deviceType.equals(DeviceTypeConstant.TYPE_PANELSWITCH)){
            nameList = intent.getStringArrayListExtra(GlobalConstant.NAME_LIST);
        }
        baseView.setViewsByType(deviceType);
    }


    /**
     * 场景改名
     */
    public void editName() {
        CircleDialogUtils.showCommentInputDialog((FragmentActivity) context, context.getString(R.string.m148_edit), name,
                context.getString(R.string.m233_please_entry_name), true, new OnInputClickListener() {
                    @Override
                    public boolean onClick(String text, View v) {
                        try {

                            if (!TextUtils.isEmpty(text)) {
                                editNameByTuya(text);
                                baseView.setDeviceName(text);
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
        requestJson.put("devId", devId);
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
                        baseView.setDeviceName(reName);
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




    /**
     * 转移设备
     */
    public void transferDevice() {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m207_transfer), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RoomLineListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(GlobalConstant.ROOM_ID, roomId);
                intent.putExtra(GlobalConstant.ROOM_NAME, roomName);
                intent.putExtra(GlobalConstant.DEVICE_ID, devId);
                intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
                intent.putExtra(GlobalConstant.ACITION_KEY, GlobalConstant.ACITION_DEVICE_TRANSFER);
                ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, true);
            }
        });

    }


    /**
     * 转移设备
     */
    public void jumptoUpdata() {
        Intent timingIntent = new Intent(context, DeviceUpdataActivity.class);
        timingIntent.putExtra(GlobalConstant.DEVICE_ID, devId);
        timingIntent.putExtra(GlobalConstant.DEVICE_NAME, name);
        timingIntent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        ActivityUtils.startActivity((Activity) context, timingIntent, ActivityUtils.ANIMATE_FORWARD, false);

    }

    /**
     * 设备配网
     */
    public void toConfigDeviceByType(){
        Intent intent=new Intent(context, WiFiOptionsActivity.class);
        intent.putExtra(GlobalConstant.DEVICE_TYPE,deviceType);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,false);
    }



    public void showWarnDialog(){
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m206_delete), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteDevice();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public void deleteDevice() throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("devId", devId);
        requestJson.put("devType", deviceType);
        requestJson.put("userId", App.getUserBean().getAccountName());
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.deleteDevice(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String jsonBean) {
                JSONObject obj;
                try {
                    obj = new JSONObject(jsonBean);
                    int code = obj.getInt("code");
                    if (code == 0) {
                        ((Activity)context).finish();
                    } else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }


    public void getSceneById() throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("devId", devId);
        requestJson.put("cmd", "selectSceneByDevId");
        requestJson.put("userId", App.getUserBean().getAccountName());
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.deleteDevice(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String jsonBean) {
                try {
                    JSONObject object = new JSONObject(jsonBean);
                    int code = object.getInt("code");
                    if (code == 0) {
                        ScenesBean scenesBean = new Gson().fromJson(jsonBean, ScenesBean.class);
                        if (scenesBean != null) {
                            List<ScenesBean.DataBean> data = scenesBean.getData();
                            if (data != null) {
                                for (ScenesBean.DataBean bean : data) {
                                    String status = bean.getStatus();
                                    if (TextUtils.isEmpty(bean.getIsCondition()))
                                        bean.setIsCondition("1");
                                    if ("0".equals(bean.getIsCondition())) {
                                        bean.setItemType(0);
                                    } else {
                                        bean.setItemType(1);
                                    }
                                }
                                baseView.updataList(data);
                            }
                        }
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


    public void toSceneDetail(ScenesBean.DataBean dataBean) {
        String beanJson = new Gson().toJson(dataBean);
        Intent intent = new Intent(context, SceneDetailActivity.class);
        intent.putExtra(GlobalConstant.SCENE_BEAN, beanJson);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTuyaDevice != null) {
            mTuyaDevice.onDestroy();
        }
    }
}
