package com.growatt.grohome.module.scenes;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.ScenesPanelConditionAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.SceneConditionBean;
import com.growatt.grohome.bean.ScenesRoadBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.scenes.presenter.SceneConditionPresenter;
import com.growatt.grohome.module.scenes.view.ISceneConditionView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SceneConditionActivity extends BaseActivity<SceneConditionPresenter> implements ISceneConditionView, BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
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
    @BindView(R.id.cb_switch_use)
    CheckBox cbSwitchUse;
    @BindView(R.id.tv_switch_onoff)
    TextView tvSwitchOnoff;
    @BindView(R.id.iv_switch_onoff)
    ImageView ivSwitchOnoff;
    @BindView(R.id.tv_bulb_mode_value)
    TextView tvBulbModeValue;
    @BindView(R.id.tv_bulb_bright_value)
    TextView tvBulbBrightValue;
    @BindView(R.id.cb_temp_use)
    CheckBox cbTempUse;
    @BindView(R.id.tv_bulb_temp_value)
    TextView tvBulbTempValue;
    @BindView(R.id.tv_bulb_time_value)
    TextView tvBulbTimeValue;
    @BindView(R.id.rv_panel_setting)
    RecyclerView rvPanelSetting;
    @BindView(R.id.layout_switch)
    ConstraintLayout layoutSwitch;
    @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;
    @BindView(R.id.layout_bulb)
    ConstraintLayout layoutBulb;
    @BindView(R.id.cb_mode_use)
    CheckBox cbModeUse;
    @BindView(R.id.cb_bright_use)
    CheckBox cbBrightUse;
    @BindView(R.id.cb_time_use)
    CheckBox cbTimeUse;


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
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
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
            layoutBulb.setVisibility(View.GONE);
            layoutSwitch.setVisibility(View.VISIBLE);
            //面板开关需要根据devid请求设备详情获取线路和名称
            try {
                presenter.getDetailData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            typeName = getString(R.string.m37_panel_switch);
        } else {
            layoutBulb.setVisibility(View.VISIBLE);
            layoutSwitch.setVisibility(View.GONE);
            typeName = getString(R.string.m39_bulb);
            if (DeviceTypeConstant.TYPE_STRIP_LIGHTS.equals(devType)){
                typeName = getString(R.string.m40_light_strip);
            }
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
            layoutBulb.setVisibility(View.GONE);
            layoutSwitch.setVisibility(View.VISIBLE);

            //面板开关需要根据devid请求设备详情获取线路和名称
            try {
                presenter.getDetailData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            typeName = getString(R.string.m37_panel_switch);
        } else {
            layoutBulb.setVisibility(View.VISIBLE);
            layoutSwitch.setVisibility(View.GONE);
            typeName = getString(R.string.m39_bulb);
            if (DeviceTypeConstant.TYPE_STRIP_LIGHTS.equals(devType)){
                typeName = getString(R.string.m40_light_strip);
            }
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
    public boolean getModeChecked() {
        return cbModeUse.isChecked();
    }

    @Override
    public boolean getBrightChecked() {
        return cbBrightUse.isChecked();
    }

    @Override
    public boolean getTempChecked() {
        return cbTempUse.isChecked();
    }

    @Override
    public boolean getTimeChecked() {
        return cbTimeUse.isChecked();
    }



    @Override
    public void selectedMode(String mode) {
        if (!TextUtils.isEmpty(mode)){
            cbModeUse.setChecked(true);
            tvBulbModeValue.setText(mode);
        }else {
            cbModeUse.setChecked(false);
            tvBulbModeValue.setText("");
        }
    }

    @Override
    public void setBright(String bright) {
        if (!TextUtils.isEmpty(bright)){
            cbBrightUse.setChecked(true);
            tvBulbBrightValue.setText(bright);
        }else {
            cbBrightUse.setChecked(false);
            tvBulbBrightValue.setText("");
        }
    }

    @Override
    public void setTemp(String temp) {
        if (!TextUtils.isEmpty(temp)){
            cbTempUse.setChecked(true);
            tvBulbTempValue.setText(temp);
        }else {
            cbTempUse.setChecked(false);
            tvBulbTempValue.setText("");
        }
    }

    @Override
    public void setCountDown(String countDown) {
        if (!TextUtils.isEmpty(countDown)){
            cbTimeUse.setChecked(true);
            tvBulbTimeValue.setText(countDown);
        }else {
            cbTimeUse.setChecked(false);
            tvBulbTimeValue.setText("");
        }
    }

    @Override
    public void setSceneName(String sceneName) {
        if (!TextUtils.isEmpty(sceneName)) {
            tvSceneName.setText(sceneName);
        }
    }

    @Override
    public void freshStop() {
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
    }

    @Override
    public void onError(String errorMsg) {
        freshStop();
        requestError(errorMsg);
    }

    @Override
    public void setSocketUi(String linkType) {
        if (TextUtils.isEmpty(linkType)){
            cbSwitchUse.setChecked(false);
        }else {
            cbSwitchUse.setChecked(true);
        }
        if (GlobalConstant.SCENE_DEVICE_OPEN.equals(linkType)) {
            tvSwitchOnoff.setText(R.string.m167_on);
            ivSwitchOnoff.setImageResource(R.drawable.scenes_on);
        } else {
            tvSwitchOnoff.setText(R.string.m168_off);
            ivSwitchOnoff.setImageResource(R.drawable.scenes_off);
        }
    }


    @OnClick({R.id.iv_switch_onoff, R.id.btnNext,
            R.id.tv_bulb_mode_title, R.id.tv_bulb_mode_value, R.id.iv_mode_more,
            R.id.tv_bulb_bright_title, R.id.tv_bulb_bright_value, R.id.iv_bright_more,
            R.id.tv_bulb_time_title, R.id.tv_bulb_time_value, R.id.iv_time_more,
            R.id.tv_bulb_temp_title, R.id.tv_bulb_temp_value, R.id.iv_temp_more
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch_onoff:
                presenter.setSocketOnoff();
                break;
            case R.id.btnNext:
                presenter.settingComplete();
                break;
            case R.id.tv_bulb_mode_title:
            case R.id.iv_mode_more:
            case R.id.tv_bulb_mode_value:
                presenter.setDeviceMode();
                break;
            case R.id.tv_bulb_bright_title:
            case R.id.tv_bulb_bright_value:
            case R.id.iv_bright_more:
                presenter.setBrightValue();
                break;
            case R.id.tv_bulb_time_title:
            case R.id.tv_bulb_time_value:
            case R.id.iv_time_more:
                presenter.setTime();
                break;
            case R.id.tv_bulb_temp_title:
            case R.id.tv_bulb_temp_value:
            case R.id.iv_temp_more:
                presenter.setTempValue();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        List<ScenesRoadBean> data = mPanelAdapter.getData();
        ScenesRoadBean swichBean = data.get(position);

        switch (view.getId()) {
            case R.id.iv_switch_use:
                boolean scenesConditionEnable = swichBean.isScenesConditionEnable();
                swichBean.setScenesConditionEnable(!scenesConditionEnable);
                mPanelAdapter.notifyDataSetChanged();
                break;
            case R.id.iv_switch_onoff:
                int onOff = swichBean.getOnOff();
                swichBean.setOnOff(onOff == 0 ? 1 : 0);
                mPanelAdapter.notifyDataSetChanged();
                break;
        }
    }


}
