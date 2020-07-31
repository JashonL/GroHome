package com.growatt.grohome.module.scenes.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.scenes.view.ISceneCustomizeView;

public class SceneCustomPresenter extends BasePresenter<ISceneCustomizeView> {
    public SceneCustomPresenter(ISceneCustomizeView baseView) {
        super(baseView);
    }

    public SceneCustomPresenter(Context context, ISceneCustomizeView baseView) {
        super(context, baseView);
    }
}
