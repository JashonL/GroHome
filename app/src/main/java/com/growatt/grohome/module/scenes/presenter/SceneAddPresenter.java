package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.scenes.view.ISceneAddView;

public class SceneAddPresenter extends BasePresenter<ISceneAddView> {
    private  String sceneType;

    public SceneAddPresenter(ISceneAddView baseView) {
        super(baseView);
    }

    public SceneAddPresenter(Context context, ISceneAddView baseView) {
        super(context, baseView);
        sceneType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.SCENE_TYPE);
    }

    public void getSceneType(){
        baseView.setViewBySceneType(sceneType);
    }
}
