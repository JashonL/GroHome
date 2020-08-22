package com.growatt.grohome.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.module.device.manager.DeviceAirCon;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DevicePanel;
import com.growatt.grohome.module.device.manager.DevicePlug;
import com.growatt.grohome.module.device.manager.DeviceStripLights;
import com.growatt.grohome.module.device.manager.DeviceThermostat;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;

import java.util.List;

public class AllDeviceAdapter extends BaseQuickAdapter<GroDeviceBean, BaseViewHolder> {
    public AllDeviceAdapter(int layoutResId, @Nullable List<GroDeviceBean> data) {
        super(layoutResId, data);
    }

    public AllDeviceAdapter(@Nullable List<GroDeviceBean> data) {
        super(data);
    }

    public AllDeviceAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GroDeviceBean item) {
        //设备类型
        String devType = item.getDevType();
        String name = item.getName();

        //设置图标
        switch (devType) {
            case DeviceTypeConstant.TYPE_THERMOSTAT:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_THERMOSTAT;
                }
                helper.setImageResource(R.id.ivIcon, DeviceThermostat.getCloseIcon(1));
                break;
            case DeviceTypeConstant.TYPE_PADDLE:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_PADDLE;
                }
                helper.setImageResource(R.id.ivIcon, DevicePlug.getCloseIcon(1));
                break;
            case DeviceTypeConstant.TYPE_PANELSWITCH:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_PANELSWITCH;
                }
                helper.setImageResource(R.id.ivIcon, DevicePanel.getCloseIcon(1));
                break;

            case DeviceTypeConstant.TYPE_AIRCONDITION:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_AIRCONDITION;
                }
                helper.setImageResource(R.id.ivIcon, DeviceAirCon.getCloseIcon(1));
                break;

            case DeviceTypeConstant.TYPE_BULB:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_BULB;
                }
                helper.setImageResource(R.id.ivIcon, DeviceBulb.getCloseIcon(1));
                break;
            case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_STRIP_LIGHTS;
                }
                helper.setImageResource(R.id.ivIcon, DeviceStripLights.getCloseIcon(1));
                break;
            default:
                if (TextUtils.isEmpty(name)) {
                    name = "device";
                }
                helper.setImageResource(R.id.ivIcon, DeviceBulb.getCloseIcon(1));
                break;
        }
        helper.setText(R.id.tvDeviceName, name);
    }
}
