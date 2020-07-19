package com.growatt.grohome.module.home.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.HomeDeviceBean;
import com.growatt.grohome.module.home.view.IGrohomeView;
import com.growatt.grohome.tuya.TuyaApiUtils;

import org.json.JSONObject;

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


}
