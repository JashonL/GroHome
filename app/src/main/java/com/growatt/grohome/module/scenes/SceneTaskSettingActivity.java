package com.growatt.grohome.module.scenes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.HomeDeviceBean;
import com.growatt.grohome.module.scenes.presenter.SceneTaskPrensenter;
import com.growatt.grohome.module.scenes.view.ISceneTaskSettingView;

import java.util.List;

public class SceneTaskSettingActivity extends BaseActivity<SceneTaskPrensenter> implements ISceneTaskSettingView {

    @Override
    protected SceneTaskPrensenter createPresenter() {
        return new SceneTaskPrensenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }
}
