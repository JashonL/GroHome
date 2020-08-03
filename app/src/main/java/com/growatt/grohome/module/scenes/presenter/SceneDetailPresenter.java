package com.growatt.grohome.module.scenes.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.scenes.view.ISceneDetailView;

public class SceneDetailPresenter extends BasePresenter<ISceneDetailView> {

    public SceneDetailPresenter(ISceneDetailView baseView) {
        super(baseView);
    }

    public SceneDetailPresenter(Context context, ISceneDetailView baseView) {
        super(context, baseView);
    }
}
