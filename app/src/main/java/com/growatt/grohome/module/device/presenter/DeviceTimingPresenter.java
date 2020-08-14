package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.growatt.grohome.adapter.DeviceTimingAdapter;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.DeviceTimingBean;
import com.growatt.grohome.bean.ScenesBean;
import com.growatt.grohome.bean.SwitchTimingBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.DeviceTimingMsg;
import com.growatt.grohome.module.device.DeviceTimingSetActivity;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.view.IDeviceTimingView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DeviceTimingPresenter extends BasePresenter<IDeviceTimingView> {

    private String mDevName;
    private String mDevId;
    private String devType;
    private String tempUnit;//当前温标
    private String mode;//当前模式
    private boolean config = false;
    private String roomId;
    private String roomName;


    public DeviceTimingPresenter(IDeviceTimingView baseView) {
        super(baseView);
    }

    public DeviceTimingPresenter(Context context, IDeviceTimingView baseView) {
        super(context, baseView);
        mDevName = ((Activity) context).getIntent().getStringExtra("devName");
        mDevId = ((Activity) context).getIntent().getStringExtra("devId");
        devType = ((Activity) context).getIntent().getStringExtra("deviceType");
        tempUnit = ((Activity) context).getIntent().getStringExtra("unit");
        mode = ((Activity) context).getIntent().getStringExtra("mode");
        config = ((Activity) context).getIntent().getBooleanExtra("config", false);
        roomId = ((Activity) context).getIntent().getStringExtra("roomId");
        roomName = ((Activity) context).getIntent().getStringExtra("roomName");
        if (TextUtils.isEmpty(tempUnit)) tempUnit = GlobalConstant.TEMP_UNIT_CELSIUS;
        if (TextUtils.isEmpty(mode)) mode = GlobalConstant.MODE_SMART;
    }


    public void refresh() throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("userId", App.getUserBean().getAccountName());
        requestJson.put("cmd", "selectSmartTask");
        requestJson.put("devId", mDevId);
        requestJson.put("devType", devType);
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String jsonBean) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonBean);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray array = jsonObject.optJSONArray("data");
                        if (array != null) {
                            List<DeviceTimingBean> datas = new ArrayList();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject beanObject = array.getJSONObject(i);
                                DeviceTimingBean bean = new Gson().fromJson(beanObject.toString(), DeviceTimingBean.class);
                                datas.add(bean);
                            }
                            baseView.updata(datas);
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


    /**
     * 跳转到设置详情，需要传
     * 名字，定时时间，重复，开关，设备id,是否设置了定时
     */
    public void jumpToSwitchTimgSet() {
        Intent intent = new Intent(context, DeviceTimingSetActivity.class);
        intent.putExtra(GlobalConstant.ACTION, GlobalConstant.ADD);
        intent.putExtra(GlobalConstant.DEVICE_NAME, mDevName);
        intent.putExtra(GlobalConstant.DEVICE_ID, mDevId);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, devType);
        intent.putExtra(GlobalConstant.TEMP_MODE,mode);
        intent.putExtra(GlobalConstant.TEMP_UNIT, tempUnit);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /**
     * 跳转到设置详情，需要传
     * 名字，定时时间，重复，开关，设备id,是否设置了定时
     */
    public void jumpToSwitchTimgSet(DeviceTimingBean bean) {
        String timingBean = null;
        if (bean != null) {
            timingBean = new Gson().toJson(bean);
        }
        Intent intent = new Intent(context, DeviceTimingSetActivity.class);
        intent.putExtra(GlobalConstant.ACTION, GlobalConstant.EDIT);
        intent.putExtra(GlobalConstant.DEVICE_NAME,mDevName);
        intent.putExtra(GlobalConstant.TEMP_MODE,mode);
        intent.putExtra(GlobalConstant.TEMP_UNIT, tempUnit);
        intent.putExtra(GlobalConstant.TIMING_BEAN, timingBean);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void editTiming(DeviceTimingBean bean) throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("cid", bean.getCid());
        requestJson.put("userId",App.getUserBean().getAccountName());
        requestJson.put("devId", mDevId);
        requestJson.put("devType", bean.getDevType());
        requestJson.put("loopType", bean.getLoopType());
        requestJson.put("loopValue", bean.getLoopValue());
        requestJson.put("cKey", bean.getcKey());
        requestJson.put("status", bean.getStatus());
        requestJson.put("timeValue", bean.getTimeValue());
        requestJson.put("cmd", "updateSmartTask");
        requestJson.put("lan", CommentUtils.getLanguage());
        requestJson.put("road", bean.getRoad());
        if (!TextUtils.isEmpty(bean.getcValue())){
            requestJson.put("cValue", bean.getcValue());
        }
        JSONArray jsonArray = new JSONArray();
        List<SwitchTimingBean> conf = bean.getConf();
        if (conf.size()>0) {
            for (SwitchTimingBean bean1 : conf) {
                String json = new Gson().toJson(bean1);
                JSONObject deviceJson = new JSONObject(json);
                jsonArray.put(deviceJson);
            }

            requestJson.put("conf", jsonArray);
        }

        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String jsonBean) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonBean);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        String msg = jsonObject.getString("data");
                        MyToastUtils.toast(msg);
                        baseView.upList();
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

}
