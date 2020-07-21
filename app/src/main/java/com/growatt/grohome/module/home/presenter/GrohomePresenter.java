package com.growatt.grohome.module.home.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.HomeDeviceBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.BulbActivity;
import com.growatt.grohome.module.home.view.IGrohomeView;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.ActivityUtils;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class GrohomePresenter extends BasePresenter<IGrohomeView> {
    public GrohomePresenter(IGrohomeView baseView) {
        super(baseView);
    }

    public GrohomePresenter(Context context, IGrohomeView baseView) {
        super(context, baseView);
    }


    public void getAlldevice() throws Exception {
        JSONObject requestJson=new JSONObject();
        requestJson.put("userId", App.getUserBean().accountName);
        requestJson.put("cmd", "devList");
        requestJson.put("userServerId", "0");
        requestJson.put("userServerUrl", "http://server-cn.growatt.com/");
        requestJson.put("lan","0");
        String s = requestJson.toString();
        RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.getAllDevice(body), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String bean) {
                Log.i(TuyaApiUtils.TUYA_TAG,"请求成功："+bean);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(bean);
                    int code = obj.getInt("code");
                    if (0==code){
                        HomeDeviceBean infoData = new Gson().fromJson(bean, HomeDeviceBean.class);
                        baseView.getAllDeviceSuccess(infoData);
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
     *
     */
    public void jumpTodevice(HomeDeviceBean.DataBean bean) {
        Intent intentThermostat = new Intent(context, BulbActivity.class);
        intentThermostat.putExtra(GlobalConstant.DEVICE_ID, bean.getDevId());
        intentThermostat.putExtra(GlobalConstant.DEVICE_NAME, bean.getName());
        ActivityUtils.startActivity((Activity) context,intentThermostat,ActivityUtils.ANIMATE_FORWARD,false);
    }


}
