package com.growatt.grohome.module.device.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.ScenesBean;

import java.util.List;

public interface IDeviceSettingView extends BaseView {

    void setViewsByType(String deviceType);

    void setDeviceName(String deviceName);

    void updataList( List<ScenesBean.DataBean> data);

    void setRoomName(String roomName);

    void setDeviceId(String deviceId);

    void onError(String onError);
}
