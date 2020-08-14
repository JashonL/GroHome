package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.DeviceTimingBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.view.IDeviceSettingView;

import java.util.List;

public class DeviceSettingPresenter extends BasePresenter<IDeviceSettingView> {
    private String name;
    private String deviceType;
    private String devId;
    private String roomName;
    private List<String> nameList;
    private int used;
    private int roomId;

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
        roomName = intent.getStringExtra(GlobalConstant.ROOM_NAME);
        roomId = intent.getIntExtra(GlobalConstant.ROOM_ID, -1);
        if (deviceType.equals(DeviceTypeConstant.TYPE_PANELSWITCH))
            nameList=intent.getStringArrayListExtra(GlobalConstant.NAME_LIST);
    }


}
