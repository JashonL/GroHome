package com.growatt.grohome.module.device;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.DeviceTypeAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.DeviceTypeBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DevicePanel;
import com.growatt.grohome.module.device.manager.DevicePlug;
import com.growatt.grohome.module.device.manager.DeviceStripLights;
import com.growatt.grohome.module.device.manager.DeviceThermostat;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.presenter.DeviceTypePresenter;
import com.growatt.grohome.module.device.view.IDeviceTypeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DeviceTypeActivity extends BaseActivity<DeviceTypePresenter> implements IDeviceTypeView {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rlv_device)
    RecyclerView mRlvdevice;

    /*设备部分*/
    private DeviceTypeAdapter mDeviceTypeAdapter;

    @Override
    protected DeviceTypePresenter createPresenter() {
        return new DeviceTypePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_type;
    }

    @Override
    protected void initViews() {
        //头部toolBar
        tvTitle.setVisibility(View.GONE);
//        toolbar.setTitle(R.string.m35_添加设备);
        tvTitle.setText(R.string.m35_添加设备);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        //列表
        mDeviceTypeAdapter = new DeviceTypeAdapter(R.layout.item_device_type, new ArrayList<>());
        mRlvdevice.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRlvdevice.setAdapter(mDeviceTypeAdapter);
        mRlvdevice.addItemDecoration(new LinearDivider(this, LinearLayoutManager.HORIZONTAL, 30, ContextCompat.getColor(this, R.color.nocolor)));
    }

    @Override
    protected void initData() {
        List<DeviceTypeBean> newList = new ArrayList<>();
        String[] typeArray = new String[]{DeviceTypeConstant.TYPE_PADDLE, DeviceTypeConstant.TYPE_PANELSWITCH, DeviceTypeConstant.TYPE_THERMOSTAT, DeviceTypeConstant.TYPE_BULB, DeviceTypeConstant.TYPE_STRIP_LIGHTS};
        String[] nameArray = new String[]{getString(DevicePlug.getNameRes()), getString(DevicePanel.getNameRes()), getString(DeviceThermostat.getNameRes()), getString(DeviceBulb.getNameRes()), getString(DeviceStripLights.getNameRes())};
        for (int i = 0; i < typeArray.length; i++) {
            DeviceTypeBean bean = new DeviceTypeBean();
            bean.setBluethooth(false);
            bean.setWiFi(true);
            bean.setType(typeArray[i]);
            bean.setName(nameArray[i]);
            newList.add(bean);
        }
        mDeviceTypeAdapter.replaceData(newList);
    }


}
