package com.growatt.grohome.module.scenes.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.scenes.view.ISceneLaunchTapView;

public class SceneLaunchTapPresenter extends BasePresenter<ISceneLaunchTapView> {
    public SceneLaunchTapPresenter(ISceneLaunchTapView baseView) {
        super(baseView);
    }

    public SceneLaunchTapPresenter(Context context, ISceneLaunchTapView baseView) {
        super(context, baseView);
    }
}
