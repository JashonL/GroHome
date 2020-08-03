package com.growatt.grohome.module.scenes;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.SceneConditionAdapter;
import com.growatt.grohome.adapter.SceneTaskAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.scenes.presenter.SceneAddPresenter;
import com.growatt.grohome.module.scenes.view.ISceneAddView;

import java.util.ArrayList;

import butterknife.BindView;

public class SceneAddActivity extends BaseActivity<SceneAddPresenter> implements ISceneAddView {

    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.guideline_begin)
    Guideline guidelineBegin;
    @BindView(R.id.guideline_end)
    Guideline guidelineEnd;
    @BindView(R.id.tv_scene_name_title)
    AppCompatTextView tvSceneNameTitle;
    @BindView(R.id.tv_scene_name_value)
    AppCompatTextView tvSceneNameValue;
    @BindView(R.id.iv_name_more)
    ImageView ivNameMore;
    @BindView(R.id.card_view_name)
    CardView cardViewName;
    @BindView(R.id.tv_scene_condition_execution)
    AppCompatTextView tvSceneConditionExecution;
    @BindView(R.id.iv_onekey)
    ImageView ivOnekey;
    @BindView(R.id.tv_onekey)
    AppCompatTextView tvOnekey;
    @BindView(R.id.card_view_condition)
    CardView cardViewCondition;
    @BindView(R.id.tv_scene_task)
    AppCompatTextView tvSceneTask;
    @BindView(R.id.rlv_task_list)
    RecyclerView rlvTaskList;
    @BindView(R.id.rlv_condition)
    RecyclerView rlvCondition;
    @BindView(R.id.iv_task_add)
    ImageView ivTaskAdd;
    @BindView(R.id.card_view_task)
    CardView cardViewTask;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.condition_onkey_group)
    Group groupOnkey;
    @BindView(R.id.condition_compose_group)
    Group groupCompose;
    @BindView(R.id.card_effect_period)
    CardView cardEffectPeriod;


    private SceneTaskAdapter mSceneTaskAdapter;
    private SceneConditionAdapter mSceneConditionAdapter;

    @Override
    protected SceneAddPresenter createPresenter() {
        return new SceneAddPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scene_add;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);


        //条件列表
        rlvCondition.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSceneConditionAdapter = new SceneConditionAdapter(R.layout.item_scene_task, new ArrayList<>());
        View linkageEmpty = LayoutInflater.from(this).inflate(R.layout.device_empty_view, rlvCondition, false);
        TextView llAddConditionView = linkageEmpty.findViewById(R.id.tv_add);
        llAddConditionView.setOnClickListener(v -> {

        });
        mSceneConditionAdapter.setEmptyView(linkageEmpty);
        rlvCondition.setAdapter(mSceneConditionAdapter);


        //任务列表
        rlvTaskList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSceneTaskAdapter = new SceneTaskAdapter(R.layout.item_scene_task, new ArrayList<>());
        View taskEmpty = LayoutInflater.from(this).inflate(R.layout.device_empty_view, rlvCondition, false);
        TextView llAddTaskView = taskEmpty.findViewById(R.id.tv_add);
        llAddTaskView.setOnClickListener(v -> {

        });
        mSceneTaskAdapter.setEmptyView(taskEmpty);
        rlvTaskList.setAdapter(mSceneTaskAdapter);

    }

    @Override
    protected void initData() {
        presenter.getSceneType();
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

    @Override
    public void setViewBySceneType(String type) {
        if (GlobalConstant.SCENE_LUANCH_TAP_TO_RUN.equals(type)){
            tvTitle.setText(R.string.m81_launch_tap_to_run);
            groupOnkey.setVisibility(View.VISIBLE);
            groupCompose.setVisibility(View.GONE);
            cardEffectPeriod.setVisibility(View.GONE);
        }else {
            tvTitle.setText(R.string.m234_smart);
            groupOnkey.setVisibility(View.GONE);
            groupCompose.setVisibility(View.VISIBLE);
            cardEffectPeriod.setVisibility(View.VISIBLE);
        }
    }
}
