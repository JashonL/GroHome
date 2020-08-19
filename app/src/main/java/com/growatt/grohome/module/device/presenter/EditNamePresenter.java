package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.view.IEditNameView;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaDevice;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditNamePresenter extends BasePresenter<IEditNameView> {

    private String devId;
    private String devName;
    private int switchId;

    private ITuyaDevice mTuyaDevice;

    public EditNamePresenter(IEditNameView baseView) {
        super(baseView);
    }

    public EditNamePresenter(Context context, IEditNameView baseView) {
        super(context, baseView);
    }


    public void getData() {
        devId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_ID);
        devName = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_NAME);
        switchId = ((Activity) context).getIntent().getIntExtra(GlobalConstant.DEVICE_SWITCH_ID, 0);
        baseView.setName(devName);
        mTuyaDevice = TuyaHomeSdk.newDeviceInstance(devId);
    }


    public void editNameByServer() throws JSONException {
        String name = baseView.getName();
        if (TextUtils.isEmpty(name)){
            MyToastUtils.toast(R.string.m233_please_entry_name);
            return;
        }
        JSONObject requestJson = new JSONObject();
        requestJson.put("devId", devId);
        requestJson.put("name", name);
        requestJson.put("switchId", switchId);
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.updateOnoffName(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String jsonBean) {
                try {
                    JSONObject object = new JSONObject(jsonBean);
                    int code = object.getInt("code");
                    if (code == 0) {
                        MyToastUtils.toast(R.string.m200_success);
                        ((Activity) context).finish();
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTuyaDevice != null) {
            mTuyaDevice.onDestroy();
        }
    }
}
