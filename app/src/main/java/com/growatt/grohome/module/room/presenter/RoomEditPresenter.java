package com.growatt.grohome.module.room.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.room.view.IRoomEditView;

public class RoomEditPresenter extends BasePresenter<IRoomEditView> {
    public RoomEditPresenter(IRoomEditView baseView) {
        super(baseView);
    }

    public RoomEditPresenter(Context context, IRoomEditView baseView) {
        super(context, baseView);
    }
}
