package com.growatt.grohome.module.scenes;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.SceneTaskAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.scenes.presenter.SceneLaunchTapPresenter;
import com.growatt.grohome.module.scenes.view.ISceneLaunchTapView;

import java.util.ArrayList;

public class SceneLaunchTapActivity extends BaseActivity<SceneLaunchTapPresenter>implements ISceneLaunchTapView {

    private SceneTaskAdapter mSceneTaskAdapter;

    @Override
    protected SceneLaunchTapPresenter createPresenter() {
        return new SceneLaunchTapPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scene_launch_tap;
    }

    @Override
    protected void initViews() {
        mSceneTaskAdapter=new SceneTaskAdapter(R.layout.item_scene_task,new ArrayList<>());
    }

    @Override
    protected void initData() {

    }
}
