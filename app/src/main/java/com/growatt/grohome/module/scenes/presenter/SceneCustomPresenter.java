package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.scenes.SceneAddActivity;
import com.growatt.grohome.module.scenes.view.ISceneCustomizeView;
import com.growatt.grohome.utils.ActivityUtils;

public class SceneCustomPresenter extends BasePresenter<ISceneCustomizeView> {
    public SceneCustomPresenter(ISceneCustomizeView baseView) {
        super(baseView);
    }

    public SceneCustomPresenter(Context context, ISceneCustomizeView baseView) {
        super(context, baseView);
    }

    public void addScene(String type) {
        Intent intent = new Intent(context, SceneAddActivity.class);
        intent.putExtra(GlobalConstant.SCENE_TYPE, type);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }

}
