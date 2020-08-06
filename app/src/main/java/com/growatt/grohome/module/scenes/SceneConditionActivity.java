package com.growatt.grohome.module.scenes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.ScenesPanelAdapter;
import com.growatt.grohome.adapter.ScenesPanelConditionAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.SceneConditionBean;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.bean.ScenesRoadBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.scenes.presenter.SceneConditionPresenter;
import com.growatt.grohome.module.scenes.view.ISceneConditionView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SceneConditionActivity extends BaseActivity<SceneConditionPresenter> implements ISceneConditionView, BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvDeviceTitle)
    TextView tvDeviceTitle;
    @BindView(R.id.tvSceneName)
    TextView tvSceneName;
    @BindView(R.id.tvDeviceName)
    TextView tvDeviceName;
    @BindView(R.id.gl_begin)
    Guideline glBegin;
    @BindView(R.id.gl_end)
    Guideline glEnd;
    @BindView(R.id.gp_switch)
    Group gpSwitch;
    @BindView(R.id.tv_switch_use)
    TextView tvSwitchUse;
    @BindView(R.id.cb_switch_use)
    CheckBox cbSwitchUse;
    @BindView(R.id.v_switch_divider)
    View vSwitchDivider;
    @BindView(R.id.gp_switch_status)
    Group gpSwitchStatus;
    @BindView(R.id.tv_switch_status)
    TextView tvSwitchStatus;
    @BindView(R.id.tv_switch_onoff)
    TextView tvSwitchOnoff;
    @BindView(R.id.iv_switch_onoff)
    ImageView ivSwitchOnoff;
    @BindView(R.id.v_switch_margin_divider)
    View vSwitchMarginDivider;
    @BindView(R.id.gp_temp)
    Group gpTemp;
    @BindView(R.id.tv_temp_use)
    TextView tvTempUse;
    @BindView(R.id.cb_temp_use)
    CheckBox cbTempUse;
    @BindView(R.id.v_temp_divider)
    View vTempDivider;
    @BindView(R.id.v_temp_click)
    View vTempClick;
    @BindView(R.id.tv_temp_status)
    TextView tvTempStatus;
    @BindView(R.id.tv_temp_onoff)
    TextView tvTempOnoff;
    @BindView(R.id.tv_temp_value)
    TextView tvTempValue;
    @BindView(R.id.iv_temp_more)
    ImageView ivTempMore;
    @BindView(R.id.rv_panel_setting)
    RecyclerView rvPanelSetting;
    @BindView(R.id.cl_setting_parent)
    ConstraintLayout clSettingParent;
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;

    private ScenesPanelConditionAdapter mPanelAdapter;

    @Override
    protected SceneConditionPresenter createPresenter() {
        return new SceneConditionPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scene_condition;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);
        tvTitle.setText(R.string.m238_device_setting);


        //面板开关设置列表
        rvPanelSetting.setLayoutManager(new LinearLayoutManager(this));
        mPanelAdapter = new ScenesPanelConditionAdapter(R.layout.item_panel_condition, new ArrayList<>());
        rvPanelSetting.setAdapter(mPanelAdapter);

        srlPull.setColorSchemeColors(ContextCompat.getColor(this, R.color.color_theme_green));
        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.getDetailData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        srlPull.setEnabled(false);
    }

    @Override
    protected void initData() {
        presenter.getIntent();
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
        mPanelAdapter.setOnItemChildClickListener(this);
    }


    @Override
    public void setViewsByDevice(GroDeviceBean bean) {
        if (!TextUtils.isEmpty(bean.getName())) {
            tvDeviceName.setText(bean.getName());
        }
        String devType = bean.getDevType();
        String typeName;
        if (DeviceTypeConstant.TYPE_PANELSWITCH.equals(devType)) {//面板
            srlPull.setEnabled(true);
            rvPanelSetting.setVisibility(View.VISIBLE);
            gpSwitch.setVisibility(View.GONE);
            gpSwitchStatus.setVisibility(View.GONE);
            gpTemp.setVisibility(View.GONE);

            //面板开关需要根据devid请求设备详情获取线路和名称
            try {
                presenter.getDetailData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            typeName=getString(R.string.m37_panel_switch);
        } else {
            gpSwitch.setVisibility(View.VISIBLE);
            gpSwitchStatus.setVisibility(View.VISIBLE);
            rvPanelSetting.setVisibility(View.GONE);
            gpTemp.setVisibility(View.GONE);
            typeName=getString(R.string.m39_bulb);
        }
        String deviceSet = String.format("%1$s %2$s", typeName, getString(R.string.m239_setting));
        tvDeviceTitle.setText(deviceSet);
    }

    @Override
    public void setViewsByTask(SceneConditionBean bean) {
        String devType = bean.getDevType();
        if (!TextUtils.isEmpty(bean.getDevName())) {
            tvDeviceName.setText(bean.getDevName());
        }
        String typeName;
        if (DeviceTypeConstant.TYPE_PANELSWITCH.equals(devType)) {//面板
            srlPull.setEnabled(true);
            rvPanelSetting.setVisibility(View.VISIBLE);
            gpSwitch.setVisibility(View.GONE);
            gpSwitchStatus.setVisibility(View.GONE);
            gpTemp.setVisibility(View.GONE);

            //面板开关需要根据devid请求设备详情获取线路和名称
            try {
                presenter.getDetailData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            typeName=getString(R.string.m37_panel_switch);
        } else {
            gpSwitch.setVisibility(View.VISIBLE);
            gpSwitchStatus.setVisibility(View.VISIBLE);
            rvPanelSetting.setVisibility(View.GONE);
            gpTemp.setVisibility(View.GONE);
            typeName=getString(R.string.m39_bulb);
        }
        String deviceSet = String.format("%1$s %2$s", typeName, getString(R.string.m239_setting));
        tvDeviceTitle.setText(deviceSet);
    }




    @Override
    public void setSwitchRoad(List<ScenesRoadBean> beanList) {
        mPanelAdapter.replaceData(beanList);
    }

    @Override
    public List<ScenesRoadBean> getData() {
        return mPanelAdapter.getData();
    }

    @Override
    public boolean getSwitchChecked() {
        return cbSwitchUse.isChecked();
    }

    @Override
    public boolean getTempChecked() {
        return cbTempUse.isChecked();
    }

    @Override
    public void setSceneName(String sceneName) {
        if (!TextUtils.isEmpty(sceneName)) {
            tvSceneName.setText(sceneName);
        }
    }

    @Override
    public void freshStop() {
        if (srlPull!=null){
            srlPull.setRefreshing(false);
        }
    }

    @Override
    public void setSocketUi(String linkType) {
        if (GlobalConstant.SCENE_DEVICE_OPEN.equals(linkType)) {
            tvSwitchOnoff.setText(R.string.m167_on);
            ivSwitchOnoff.setImageResource(R.drawable.scenes_on);
        } else {
            tvSwitchOnoff.setText(R.string.m168_off);
            ivSwitchOnoff.setImageResource(R.drawable.scenes_off);
        }
    }


    @OnClick({R.id.tv_switch_onoff, R.id.btnNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_switch_onoff:
                presenter.setSocketOnoff();
                break;
            case R.id.btnNext:
                presenter.settingComplete();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
      /*  List<ScenesRoadBean> data = mPanelAdapter.getData();
        ScenesRoadBean scenesRoadBean = data.get(position);
        int onOff = scenesRoadBean.getOnOff();
        scenesRoadBean.setOnOff(onOff == 0 ? 1 : 0);
        mPanelAdapter.notifyDataSetChanged();
*/

        List<ScenesRoadBean> data = mPanelAdapter.getData();
        ScenesRoadBean swichBean = data.get(position);

        switch (view.getId()){
            case R.id.iv_switch_use:
                boolean scenesConditionEnable = swichBean.isScenesConditionEnable();
                swichBean.setScenesConditionEnable(!scenesConditionEnable);
                mPanelAdapter.notifyDataSetChanged();
                break;
            case R.id.iv_switch_onoff:
                int onOff = swichBean.getOnOff();
                swichBean.setOnOff(onOff == 0?1:0);
                mPanelAdapter.notifyDataSetChanged();
                break;
        }
    }


}
