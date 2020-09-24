package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceManager;
import com.growatt.grohome.module.device.view.IAllDeviceView;
import com.growatt.grohome.module.scenes.SceneConditionActivity;
import com.growatt.grohome.module.scenes.SceneTaskSettingActivity;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AllDevicePrensenter extends BasePresenter<IAllDeviceView> {
    private String devSelectType;

    public AllDevicePrensenter(IAllDeviceView baseView) {
        super(baseView);
    }

    public AllDevicePrensenter(Context context, IAllDeviceView baseView) {
        super(context, baseView);
        devSelectType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.SCENE_DEVICE_SELECT);
    }



    public void getDeviceList() {
        List<GroDeviceBean> deviceBeans = DeviceManager.getInstance().getDeviceBeans();
        if (deviceBeans != null) {
            baseView.setAllDeviceSuccess(deviceBeans);
        } else {
            try {
                getAlldevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void getAlldevice() throws Exception {
        String userServerUrl = GlobalConstant.HTTP_PREFIX + App.getUserBean().getUrl();
        JSONObject requestJson = new JSONObject();
        requestJson.put("userId", App.getUserBean().getAccountName());
        requestJson.put("cmd", "devList");
        requestJson.put("userServerId", "0");
        requestJson.put("userServerUrl", userServerUrl);
        requestJson.put("lan", String.valueOf(CommentUtils.getLanguage()));
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.getAllDevice(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                JSONObject obj;
                try {
                    obj = new JSONObject(bean);
                    int code = obj.getInt("code");
                    if (0 == code) {
                        JSONArray dataArray = obj.optJSONArray("data");
                        List<GroDeviceBean> data = new ArrayList<>();
                        if (dataArray != null) {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.optJSONObject(i);
                                GroDeviceBean deviceBean = new Gson().fromJson(jsonObject.toString(), GroDeviceBean.class);
                                data.add(deviceBean);
                            }
                        }

                        baseView.setAllDeviceSuccess(data);
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


    public void toSetDevice(GroDeviceBean deviceBean) {
        if (deviceBean == null) return;
        switch (devSelectType) {
            case GlobalConstant.SCENE_ADD_CONDITION:
                setSceneCondition(deviceBean);
                break;
            case GlobalConstant.SCENE_ADD_TASK:
                setSceneRunDevice(deviceBean);
                break;
        }
    }

    private void setSceneCondition(GroDeviceBean deviceBean) {
        if (deviceBean == null) return;
        String bean = new Gson().toJson(deviceBean);
        Intent intent = new Intent(context, SceneConditionActivity.class);
        intent.putExtra(GlobalConstant.DEVICE_BEAN, bean);
        intent.putExtra(GlobalConstant.SCENE_CREATE_OR_EDIT, GlobalConstant.SCENE_CREATE);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    private void setSceneRunDevice(GroDeviceBean deviceBean) {
        if (deviceBean == null) return;
        String bean = new Gson().toJson(deviceBean);
        Intent intent = new Intent(context, SceneTaskSettingActivity.class);
        intent.putExtra(GlobalConstant.DEVICE_BEAN, bean);
        intent.putExtra(GlobalConstant.SCENE_CREATE_OR_EDIT, GlobalConstant.SCENE_CREATE);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


}
