package com.growatt.grohome.module.room.view;

import com.growatt.grohome.base.BaseView;

public interface IRoomAddView extends BaseView {
    void getPhotoSuccess(String path);

    void getPhotoFail(String message);

    void createRoomSuccess();

    void createRoomFail(String msg);

    void onError(String onError);
}
