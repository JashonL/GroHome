package com.growatt.grohome.module.home.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.HomeDeviceBean;
import com.growatt.grohome.bean.HomeRoomBean;

import java.util.List;

public interface IGrohomeView extends BaseView {
        void setAllDeviceSuccess(HomeDeviceBean bean);

        void onError(String onError);

        List<HomeDeviceBean.DataBean> getDeviceList();

        void upDataStatus(String devId, String value);

        void setRoomListSuccess(List<HomeRoomBean> roomList);
}
