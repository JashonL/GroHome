package com.growatt.grohome.module.scenes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.ScenesPanelAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.bean.ScenesRoadBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.scenes.presenter.SceneTaskPrensenter;
import com.growatt.grohome.module.scenes.view.ISceneTaskSettingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SceneTaskSettingActivity extends BaseActivity<SceneTaskPrensenter> implements ISceneTaskSettingView , BaseQuickAdapter.OnItemClickListener{

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
    @BindView(R.id.tvSocketOnoff)
    TextView tvSocketOnoff;
    @BindView(R.id.ivSocketSwitch)
    ImageView ivSocketSwitch;
    @BindView(R.id.clSocket)
    ConstraintLayout clSocket;
    @BindView(R.id.clThemostatStatus)
    ConstraintLayout clThemostatStatus;
    @BindView(R.id.v)
    View v;
    @BindView(R.id.clTemp)
    ConstraintLayout clTemp;
    @BindView(R.id.rvPanelSetting)
    RecyclerView rvPanelSetting;
    @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;

    private ScenesPanelAdapter mPanelAdapter;

    @Override
    protected SceneTaskPrensenter createPresenter() {
        return new SceneTaskPrensenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scene_task_setting;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);
        tvTitle.setText(R.string.m238_device_setting);

        //面板开关设置列表
        rvPanelSetting.setLayoutManager(new LinearLayoutManager(this));
        mPanelAdapter = new ScenesPanelAdapter(R.layout.item_sceneset_panel, new ArrayList<>());
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
        mPanelAdapter.setOnItemClickListener(this);
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
            clThemostatStatus.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
            clTemp.setVisibility(View.GONE);
            clSocket.setVisibility(View.GONE);
            //面板开关需要根据devid请求设备详情获取线路和名称
            try {
                presenter.getDetailData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            typeName=getString(R.string.m37_panel_switch);
        } else {
            clSocket.setVisibility(View.VISIBLE);
            clThemostatStatus.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
            clTemp.setVisibility(View.GONE);
            rvPanelSetting.setVisibility(View.GONE);
            typeName=getString(R.string.m39_bulb);
        }
        String deviceSet = String.format("%1$s %2$s", typeName, getString(R.string.m239_setting));
        tvDeviceTitle.setText(deviceSet);
    }

    @Override
    public void setViewsByTask(SceneTaskBean bean) {
        String devType = bean.getDevType();
        if (!TextUtils.isEmpty(bean.getDevName())) {
            tvDeviceName.setText(bean.getDevName());
        }
        String typeName;
        if (DeviceTypeConstant.TYPE_PANELSWITCH.equals(devType)) {//面板
            srlPull.setEnabled(true);
            rvPanelSetting.setVisibility(View.VISIBLE);
            clThemostatStatus.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
            clTemp.setVisibility(View.GONE);
            clSocket.setVisibility(View.GONE);
            //面板开关需要根据devid请求设备详情获取线路和名称
            try {
                presenter.getDetailData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            typeName=getString(R.string.m37_panel_switch);
        } else {
            clSocket.setVisibility(View.VISIBLE);
            clThemostatStatus.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
            clTemp.setVisibility(View.GONE);
            rvPanelSetting.setVisibility(View.GONE);
            typeName=getString(R.string.m39_bulb);
            setSocketUi(bean.getLinkType());
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
            tvSocketOnoff.setText(R.string.m167_on);
            ivSocketSwitch.setImageResource(R.drawable.scenes_on);
        } else {
            tvSocketOnoff.setText(R.string.m168_off);
            ivSocketSwitch.setImageResource(R.drawable.scenes_off);
        }
    }


    @OnClick({R.id.ivSocketSwitch, R.id.btnNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivSocketSwitch:
               presenter.setSocketOnoff();
                break;
            case R.id.btnNext:
                presenter.settingComplete();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<ScenesRoadBean> data = mPanelAdapter.getData();
        ScenesRoadBean scenesRoadBean = data.get(position);
        int onOff = scenesRoadBean.getOnOff();
        scenesRoadBean.setOnOff(onOff == 0 ? 1 : 0);
        mPanelAdapter.notifyDataSetChanged();
    }

}
