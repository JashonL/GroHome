package com.growatt.grohome.module.scenes;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.SceneConditionAdapter;
import com.growatt.grohome.adapter.SceneTaskAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.SceneConditionBean;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.constants.GlobalVariable;
import com.growatt.grohome.module.scenes.presenter.SceneAddPresenter;
import com.growatt.grohome.module.scenes.view.ISceneAddView;
import com.growatt.grohome.utils.MyToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SceneAddActivity extends BaseActivity<SceneAddPresenter> implements ISceneAddView, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_scene_name_value)
    AppCompatTextView tvSceneNameValue;
    @BindView(R.id.rlv_task_list)
    RecyclerView rlvTaskList;
    @BindView(R.id.rlv_condition)
    RecyclerView rlvCondition;
    @BindView(R.id.condition_onkey_group)
    Group groupOnkey;
    @BindView(R.id.condition_compose_group)
    Group groupCompose;
    @BindView(R.id.card_effect_period)
    CardView cardEffectPeriod;
    @BindView(R.id.iv_condition_add)
    ImageView ivConditionAdd;
    @BindView(R.id.tv_execution_met)
    TextView tvEexecutionMet;


    private SceneTaskAdapter mSceneTaskAdapter;
    private SceneConditionAdapter mSceneConditionAdapter;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }


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
        mSceneConditionAdapter = new SceneConditionAdapter(R.layout.item_scene_condition, new ArrayList<>());
   /*     View linkageEmpty = LayoutInflater.from(this).inflate(R.layout.device_empty_view, rlvCondition, false);
        TextView llAddConditionView = linkageEmpty.findViewById(R.id.tv_add);
        llAddConditionView.setOnClickListener(v -> {
            presenter.addCondition(GlobalConstant.SCENE_ADD_CONDITION);
        });
        mSceneConditionAdapter.setEmptyView(linkageEmpty);*/
        rlvCondition.setAdapter(mSceneConditionAdapter);

        //任务列表
        rlvTaskList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSceneTaskAdapter = new SceneTaskAdapter(R.layout.item_scene_task, new ArrayList<>());
/*        View taskEmpty = LayoutInflater.from(this).inflate(R.layout.device_empty_view, rlvCondition, false);
        TextView llAddTaskView = taskEmpty.findViewById(R.id.tv_add);
        llAddTaskView.setOnClickListener(v -> {
            presenter.selectDevice(GlobalConstant.SCENE_ADD_TASK);
        });
        mSceneTaskAdapter.setEmptyView(taskEmpty);*/
        rlvTaskList.setAdapter(mSceneTaskAdapter);

    }

    @Override
    protected void initData() {
        presenter.getSceneType();
        EventBus.getDefault().register(this);
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
        mSceneTaskAdapter.setOnItemChildClickListener(this);
        mSceneConditionAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void setViewBySceneType(String type) {
        if (GlobalConstant.SCENE_LUANCH_TAP_TO_RUN.equals(type)) {
            tvTitle.setText(R.string.m81_launch_tap_to_run);
            groupOnkey.setVisibility(View.VISIBLE);
            groupCompose.setVisibility(View.GONE);
            cardEffectPeriod.setVisibility(View.GONE);
            ivConditionAdd.setVisibility(View.GONE);
        } else {
            tvTitle.setText(R.string.m234_smart);
            groupOnkey.setVisibility(View.GONE);
            groupCompose.setVisibility(View.VISIBLE);
            cardEffectPeriod.setVisibility(View.GONE);
            ivConditionAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String getName() {
        return tvSceneNameValue.getText().toString();
    }

    @Override
    public void setSceneName(String name) {
        tvSceneNameValue.setText(name);
    }

    @Override
    public String getSceneName() {
        return tvSceneNameValue.getText().toString();
    }

    @Override
    public void deleteTaskDevice(int position) {
        mSceneTaskAdapter.remove(position);
    }

    @Override
    public void deleteCondition(int position) {
        mSceneConditionAdapter.remove(position);
    }

    @Override
    public List<SceneConditionBean> getConditionBean() {
        return mSceneConditionAdapter.getData();
    }

    @Override
    public List<SceneTaskBean> getData() {
        return mSceneTaskAdapter.getData();
    }

    @Override
    public void addSceneResult(String msg) {
        MyToastUtils.toast(msg);
    }

    @Override
    public void setConditionMet(int satisfy) {
        switch (satisfy) {
            case 0:
                tvEexecutionMet.setText(R.string.m217_all_conditions_are_met);
                break;
            case 1:
                tvEexecutionMet.setText(R.string.m218_any_conditions_is_met);
                break;
        }
    }

    @Override
    public void onError(String onError) {
        requestError(onError);
    }


    @OnClick({R.id.card_view_name, R.id.iv_task_add, R.id.btn_save, R.id.iv_condition_add, R.id.tv_execution_met})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.card_view_name:
                presenter.showInuptNameDialog();
                break;
            case R.id.btn_save:
                try {
                    presenter.upScenesData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_task_add:
                List<SceneConditionBean> data = mSceneConditionAdapter.getData();
                List<String> deviceIds = new ArrayList<>();
                for (SceneConditionBean bean : data) {
                    String id = bean.getDevId();
                    deviceIds.add(id);
                }
                presenter.selectDevice(GlobalConstant.SCENE_ADD_TASK, deviceIds);
                break;
            case R.id.iv_condition_add:
                List<SceneTaskBean> data1 = mSceneTaskAdapter.getData();
                List<String> deviceIds1 = new ArrayList<>();
                for (SceneTaskBean bean : data1) {
                    String id = bean.getDevId();
                    deviceIds1.add(id);
                }
                presenter.addCondition(GlobalConstant.SCENE_ADD_CONDITION, deviceIds1);
                break;
            case R.id.iv_execution_pull:
            case R.id.tv_execution_met:
                presenter.selectConditionMet();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventScenes(@NonNull SceneTaskBean bean) {
        setRvDevice(bean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventScenesConditionBean(@NonNull SceneConditionBean bean) {
        setRvCondition(bean);
    }


    //执行条件

    private void setRvCondition(SceneConditionBean conditionBean) {
        if (GlobalVariable.filterEnable) {
            List<SceneTaskBean> data = mSceneTaskAdapter.getData();
            for (SceneTaskBean bean : data) {
                if (!"time".equals(bean.getDevType())) {
                    if (bean.getDevId().equals(conditionBean.getDevId())) {
                        MyToastUtils.toast(R.string.m251_device_already_task);
                        return;
                    }
                }
            }
        }
        String devType = conditionBean.getDevType();
        String devIdBack = conditionBean.getDevId();
        List<SceneConditionBean> data = mSceneConditionAdapter.getData();
        if (data.size() <= 0) {
            mSceneConditionAdapter.addData(conditionBean);
        }
        if ("time".equals(devType)) {
            int index = -1;
            for (int i = 0; i < data.size(); i++) {
                if ("time".equals(data.get(i).getDevType())) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                mSceneConditionAdapter.addData(conditionBean);
            } else {
                data.set(index, conditionBean);
                mSceneConditionAdapter.notifyDataSetChanged();
            }
        } else {
            int index = -1;
            for (int i = 0; i < data.size(); i++) {
                String devId = data.get(i).getDevId();
                if (devIdBack.equals(devId)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                data.set(index, conditionBean);
                mSceneConditionAdapter.notifyDataSetChanged();
            } else {
                mSceneConditionAdapter.addData(conditionBean);
            }
        }
    }


    //执行任务
    private void setRvDevice(SceneTaskBean deviceBean) {
        String devIdBack = deviceBean.getDevId();
        List<SceneTaskBean> data = mSceneTaskAdapter.getData();
        if (data.size() <= 0) {
            mSceneTaskAdapter.addData(deviceBean);
        } else {
            int index = -1;
            for (int i = 0; i < data.size(); i++) {
                String devId = data.get(i).getDevId();
                if (devIdBack.equals(devId)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                data.set(index, deviceBean);
                mSceneTaskAdapter.notifyDataSetChanged();
            } else {
                mSceneTaskAdapter.addData(deviceBean);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mSceneTaskAdapter) {
            SceneTaskBean sceneTaskBean = mSceneTaskAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.iv_edit:
                    presenter.toEditConfig(sceneTaskBean);
                    break;
                case R.id.iv_delete:
                    presenter.deleteDevice(position);
                    break;
            }
        }

        if (adapter == mSceneConditionAdapter) {
            SceneConditionBean conditionBean = mSceneConditionAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.iv_edit:
                    presenter.toEditCondition(conditionBean);
                    break;
                case R.id.iv_delete:
                    presenter.deleteCondition(position);
                    break;
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GlobalConstant.REQUEST_CODE_EDIT_SCENE_TIME) {
                SceneConditionBean scenesConditionBean = new SceneConditionBean();
                scenesConditionBean.setLinkType(data.getStringExtra(GlobalConstant.TIME_LOOPTYPE));
                scenesConditionBean.setLinkValue(data.getStringExtra(GlobalConstant.TIME_LOOPVALUE));
                scenesConditionBean.setTimeValue(data.getStringExtra(GlobalConstant.TIME_VALUE));
                scenesConditionBean.setDevType("time");
                setRvCondition(scenesConditionBean);
            }
        }
    }
}
