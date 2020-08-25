package com.growatt.grohome.module.room.view;

import com.growatt.grohome.base.BaseView;

public interface IRoomImageView extends BaseView {

    void getPhotoSuccess(String path);

    void getPhotoFail(String message);

    void setImage(String path);

    void updateImageSuccess();

    void updateImageFail(String msg);

    void onError(String msg);
}
