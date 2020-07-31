package com.growatt.grohome.module.scenes.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.scenes.view.ISceneTaskSettingView;

public class SceneTaskPrensenter extends BasePresenter<ISceneTaskSettingView> {
    public SceneTaskPrensenter(ISceneTaskSettingView baseView) {
        super(baseView);
    }

    public SceneTaskPrensenter(Context context, ISceneTaskSettingView baseView) {
        super(context, baseView);
    }
}
