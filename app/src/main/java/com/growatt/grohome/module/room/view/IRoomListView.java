package com.growatt.grohome.module.room.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.HomeRoomBean;

import java.util.List;

public interface IRoomListView extends BaseView {

    void updata(List<HomeRoomBean> roomList, int position);

    void setSelected(int position);

    List<GroDeviceBean> getData();

    void upDataStatus(String devId, String value);
}
