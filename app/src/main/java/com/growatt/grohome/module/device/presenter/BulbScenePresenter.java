package com.growatt.grohome.module.device.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.module.device.view.IBulbSceneView;

public class BulbScenePresenter extends BasePresenter<IBulbSceneView> {
    public BulbScenePresenter(IBulbSceneView baseView) {
        super(baseView);
    }

    public BulbScenePresenter(Context context, IBulbSceneView baseView) {
        super(context, baseView);
    }
}
