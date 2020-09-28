package com.growatt.grohome.module.home.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.HomeRoomBean;

import java.util.List;

public interface IGrohomeView extends BaseView {
        void setAllDeviceSuccess(List<GroDeviceBean>list);

        void onError(String onError);

        List<GroDeviceBean> getDeviceList();

        void upDataStatus(String devId, String value);

        void upDataOnline(String devId, boolean online);


        void setRoomListSuccess(List<HomeRoomBean> roomList);

        void deleteDevice(String deviceId);

}
