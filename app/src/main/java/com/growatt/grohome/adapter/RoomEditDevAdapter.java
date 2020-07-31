package com.growatt.grohome.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

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

public class RoomEditDevAdapter extends BaseQuickAdapter<GroDeviceBean, BaseViewHolder> {
    public RoomEditDevAdapter(int layoutResId, @Nullable List<GroDeviceBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, GroDeviceBean item) {

        String name = item.getName();
        TextView tvName=helper.getView(R.id.tvDeviceName);
        helper.addOnClickListener(R.id.tvTransfer);
        helper.addOnClickListener(R.id.tvDelete);

        String devType = item.getDevType();
        ImageView ivDevIcon = helper.getView(R.id.ivIcon);
        //设置图标
        switch (devType) {
            case DeviceTypeConstant.TYPE_THERMOSTAT:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_THERMOSTAT;
                }
                ivDevIcon.setImageResource(DeviceThermostat.getCloseIcon(1));
                break;
            case DeviceTypeConstant.TYPE_PADDLE:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_PADDLE;
                }
                ivDevIcon.setImageResource(DevicePlug.getCloseIcon(1));
                break;

            case DeviceTypeConstant.TYPE_BULB:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_BULB;
                }
                ivDevIcon.setImageResource(DeviceBulb.getCloseIcon(1));
                break;

            case DeviceTypeConstant.TYPE_AIRCONDITION:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_AIRCONDITION;
                }
                ivDevIcon.setImageResource(DeviceAirCon.getCloseIcon(1));
                break;
            case DeviceTypeConstant.TYPE_PANELSWITCH:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_PANELSWITCH;
                }
                ivDevIcon.setImageResource(DevicePanel.getCloseIcon(1));
                break;
            case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
                if (TextUtils.isEmpty(name)) {
                    name = DeviceTypeConstant.TYPE_STRIP_LIGHTS;
                }
                ivDevIcon.setImageResource(DeviceStripLights.getCloseIcon(1));
                break;

        }
        tvName.setText(name);
    }
}
