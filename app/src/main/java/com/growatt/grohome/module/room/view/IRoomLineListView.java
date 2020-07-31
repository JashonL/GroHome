package com.growatt.grohome.module.room.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.HomeRoomBean;

import java.util.List;

public interface IRoomLineListView extends BaseView {

    void updata(List<HomeRoomBean> roomList);

    void transferSuccess();

    void transferFail(String msg);
}
