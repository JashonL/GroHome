package com.growatt.grohome.module.scenes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.Group;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.SceneConditionAdapter;
import com.growatt.grohome.adapter.SceneTaskAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.scenes.presenter.SceneDetailPresenter;
import com.growatt.grohome.module.scenes.view.ISceneDetailView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SceneDetailActivity extends BaseActivity<SceneDetailPresenter> implements ISceneDetailView {
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
    @BindView(R.id.iv_condition_add)
    ImageView ivConditionAdd;
    @BindView(R.id.barrier_condition)
    Barrier barrierCondition;
    @BindView(R.id.v_condition_diver)
    View vConditionDiver;
    @BindView(R.id.condition_onkey_group)
    Group conditionOnkeyGroup;
    @BindView(R.id.iv_onekey)
    ImageView ivOnekey;
    @BindView(R.id.tv_onekey)
    AppCompatTextView tvOnekey;
    @BindView(R.id.condition_compose_group)
    Group conditionComposeGroup;
    @BindView(R.id.tv_execution_met)
    AppCompatTextView tvExecutionMet;
    @BindView(R.id.iv_execution_pull)
    TextView ivExecutionPull;
    @BindView(R.id.rlv_condition)
    RecyclerView rlvCondition;
    @BindView(R.id.card_view_condition)
    CardView cardViewCondition;
    @BindView(R.id.tv_scene_task)
    AppCompatTextView tvSceneTask;
    @BindView(R.id.iv_task_add)
    ImageView ivTaskAdd;
    @BindView(R.id.barrier_task)
    Barrier barrierTask;
    @BindView(R.id.v_task_diver)
    View vTaskDiver;
    @BindView(R.id.rlv_task_list)
    RecyclerView rlvTaskList;
    @BindView(R.id.card_view_task)
    CardView cardViewTask;
    @BindView(R.id.tv_effect_period_title)
    AppCompatTextView tvEffectPeriodTitle;
    @BindView(R.id.tv_effect_period_value)
    AppCompatTextView tvEffectPeriodValue;
    @BindView(R.id.iv_effect_period_more)
    ImageView ivEffectPeriodMore;
    @BindView(R.id.card_effect_period)
    CardView cardEffectPeriod;
    @BindView(R.id.v_space)
    View vSpace;
    @BindView(R.id.btn_save)
    Button btnSave;

    @Override
    protected SceneDetailPresenter createPresenter() {
        return new SceneDetailPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scene_smart;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);


     /*   //条件列表
        rlvCondition.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSceneConditionAdapter = new SceneConditionAdapter(R.layout.item_scene_condition, new ArrayList<>());
        View linkageEmpty = LayoutInflater.from(this).inflate(R.layout.device_empty_view, rlvCondition, false);
        TextView llAddConditionView = linkageEmpty.findViewById(R.id.tv_add);
        llAddConditionView.setOnClickListener(v -> {
            presenter.addCondition(GlobalConstant.SCENE_ADD_CONDITION);
        });
        mSceneConditionAdapter.setEmptyView(linkageEmpty);
        rlvCondition.setAdapter(mSceneConditionAdapter);

        //任务列表
        rlvTaskList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSceneTaskAdapter = new SceneTaskAdapter(R.layout.item_scene_task, new ArrayList<>());
        View taskEmpty = LayoutInflater.from(this).inflate(R.layout.device_empty_view, rlvCondition, false);
        TextView llAddTaskView = taskEmpty.findViewById(R.id.tv_add);
        llAddTaskView.setOnClickListener(v -> {
            presenter.selectDevice(GlobalConstant.SCENE_ADD_TASK);
        });
        mSceneTaskAdapter.setEmptyView(taskEmpty);
        rlvTaskList.setAdapter(mSceneTaskAdapter);*/
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setViewBySceneType(String type) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setSceneName(String name) {

    }

    @Override
    public String getSceneName() {
        return null;
    }

    @Override
    public void deleteTaskDevice(int position) {

    }

    @Override
    public List<SceneTaskBean> getData() {
        return null;
    }

    @Override
    public void addSceneResult(String msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
