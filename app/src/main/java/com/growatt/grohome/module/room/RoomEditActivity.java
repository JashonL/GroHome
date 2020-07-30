package com.growatt.grohome.module.room;

import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.room.presenter.RoomEditPresenter;
import com.growatt.grohome.module.room.view.IRoomEditView;

public class RoomEditActivity extends BaseActivity<RoomEditPresenter>implements IRoomEditView {
    @Override
    protected RoomEditPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
