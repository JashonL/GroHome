package com.growatt.grohome.module.scenes;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Guideline;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.scenes.presenter.SceneCustomPresenter;
import com.growatt.grohome.module.scenes.view.ISceneCustomizeView;

import butterknife.BindView;
import butterknife.OnClick;

public class SceneCustomizeActivity extends BaseActivity<SceneCustomPresenter> implements ISceneCustomizeView {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected SceneCustomPresenter createPresenter() {
        return new SceneCustomPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scene_customize;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);
        tvTitle.setText(R.string.m85_add_scenes);

    }

    @Override
    protected void initData() {

    }


    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @OnClick({R.id.card_launch_tap, R.id.card_condition})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.card_launch_tap:
                presenter.addScene(GlobalConstant.SCENE_LUANCH_TAP_TO_RUN);
                break;
            case R.id.card_condition:
                presenter.addScene(GlobalConstant.SCENE_SMART);
                break;
        }
    }
}
