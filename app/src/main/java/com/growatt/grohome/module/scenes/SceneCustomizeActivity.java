package com.growatt.grohome.module.scenes;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.scenes.presenter.SceneCustomPresenter;
import com.growatt.grohome.module.scenes.view.ISceneCustomizeView;
import com.growatt.grohome.module.scenes.view.ISceneTaskSettingView;

public class SceneCustomizeActivity extends BaseActivity<SceneCustomPresenter>implements ISceneCustomizeView {
    @Override
    protected SceneCustomPresenter createPresenter() {
        return new SceneCustomPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scene_customize;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
