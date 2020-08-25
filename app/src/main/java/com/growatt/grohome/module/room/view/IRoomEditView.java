package com.growatt.grohome.module.room.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.HomeRoomBean;

public interface IRoomEditView extends BaseView {

    void upRoomData(HomeRoomBean bean);

    void editNameSuccess(String name);

    void onError(String msg);

    void editNameFail(String msg);

    void deleteRoomSuccess();

    void deleteDeviceSuccess(String deviceId);

}
