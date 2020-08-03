package com.growatt.grohome.module.scenes;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.scenes.presenter.SceneDetailPresenter;
import com.growatt.grohome.module.scenes.view.ISceneDetailView;

public class SceneDetailActivity extends BaseActivity<SceneDetailPresenter>implements ISceneDetailView {
    @Override
    protected SceneDetailPresenter createPresenter() {
        return new SceneDetailPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scene_smart;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
