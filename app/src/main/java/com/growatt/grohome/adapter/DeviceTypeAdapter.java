package com.growatt.grohome.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.DeviceTypeBean;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DevicePanel;
import com.growatt.grohome.module.device.manager.DevicePlug;
import com.growatt.grohome.module.device.manager.DeviceStripLights;
import com.growatt.grohome.module.device.manager.DeviceThermostat;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;

import java.util.List;

public class DeviceTypeAdapter extends BaseQuickAdapter<DeviceTypeBean, BaseViewHolder> {
    public DeviceTypeAdapter(int layoutResId, @Nullable List<DeviceTypeBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, DeviceTypeBean item) {
        helper.setText(R.id.tv_type_name, item.getName());
        helper.addOnClickListener(R.id.cl_content_background);
        //设置图标和背景
        String type = item.getType();
        View view = helper.getView(R.id.cl_content_background);
        ImageView ivIcon = helper.getView(R.id.iv_device_icon);
        setViewsByType(type,view,ivIcon);

        helper.setVisible(R.id.iv_config_bluethooth,item.isBluethooth());
        helper.setVisible(R.id.iv_config_wifi,item.isWiFi());
    }



    private void setViewsByType(String type, View view, ImageView ivIcon) {
        switch (type) {
            case DeviceTypeConstant.TYPE_THERMOSTAT:
                ivIcon.setImageResource(DeviceThermostat.getCloseIcon(3));
                view.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_add_thermostat_background));
                break;
            case DeviceTypeConstant.TYPE_PADDLE:
                ivIcon.setImageResource(DevicePlug.getCloseIcon(3));
                view.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_add_plug_background));
                break;
            case DeviceTypeConstant.TYPE_PANELSWITCH:
                ivIcon.setImageResource(DevicePanel.getCloseIcon(3));
                view.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_add_switch_background));
                break;
            case DeviceTypeConstant.TYPE_BULB:
                ivIcon.setImageResource(DeviceBulb.getCloseIcon(3));
                view.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_add_bulb_background));
                break;
            case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
                ivIcon.setImageResource(DeviceStripLights.getCloseIcon(3));
                view.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_add_strip_background));
                break;
        }
    }
}
