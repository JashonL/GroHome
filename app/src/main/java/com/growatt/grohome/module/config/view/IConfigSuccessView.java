package com.growatt.grohome.module.config.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.HomeRoomBean;

import java.util.List;

public interface IConfigSuccessView extends BaseView {

    void upRoomList( List<HomeRoomBean> homeRoomList);

    void setDeviceName(String deviceName);
}
