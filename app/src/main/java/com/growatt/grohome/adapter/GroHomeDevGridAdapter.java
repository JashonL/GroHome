package com.growatt.grohome.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
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

public class GroHomeDevGridAdapter extends BaseMultiItemQuickAdapter<GroDeviceBean, BaseViewHolder> {

    final static int STATUS_ON = 1;
    final static int STATUS_OFF = 0;


    public GroHomeDevGridAdapter(@Nullable List<GroDeviceBean> data) {
        super(data);
        addItemType(STATUS_ON, R.layout.item_device_grid_open);
        addItemType(STATUS_OFF, R.layout.item_device_grid_close);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GroDeviceBean item) {
        //设备名称
        helper.setText(R.id.tv_device_name, item.getName());


        //房间
        String roomName = item.getRoomName();
        if (TextUtils.isEmpty(roomName)) roomName = "--";
        helper.setText(R.id.tv_device_room, roomName);


        //设备状态
        boolean deviceConfig = item.isDeviceConfig();
        int online = item.getOnline();
        if (deviceConfig){
            if (1==online){
                helper.setVisible(R.id.tv_device_status,false);
            }else {
                helper.setVisible(R.id.tv_device_status,true);
                helper.setText(R.id.tv_device_status, mContext.getString(R.string.m336_offline));
            }
        }else {//未配网
            helper.setVisible(R.id.tv_device_status,true);
            helper.setText(R.id.tv_device_status, mContext.getString(R.string.m335_no_network));
        }

        //设备类型设置图标
        ImageView deviceIcon = helper.getView(R.id.iv_device_icon);
        String devType = item.getDevType();
        if (item.getItemType() == STATUS_ON) {
            switch (devType) {
                case DeviceTypeConstant.TYPE_PANELSWITCH:
                    deviceIcon.setImageResource(DevicePanel.getOpenIcon(0));
                    break;
                case DeviceTypeConstant.TYPE_BULB:
                    deviceIcon.setImageResource(DeviceBulb.getOpenIcon(0));
                    break;
                case DeviceTypeConstant.TYPE_AIRCONDITION:
                    deviceIcon.setImageResource(DeviceAirCon.getOpenIcon(0));
                    break;
                case DeviceTypeConstant.TYPE_PADDLE:
                    deviceIcon.setImageResource(DevicePlug.getOpenIcon(0));
                    break;
                case DeviceTypeConstant.TYPE_THERMOSTAT:
                    deviceIcon.setImageResource(DeviceThermostat.getOpenIcon(0));
                    break;
                case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
                    deviceIcon.setImageResource(DeviceStripLights.getOpenIcon(0));
                    break;
            }
        } else {
            switch (devType) {
                case DeviceTypeConstant.TYPE_PANELSWITCH:
                    deviceIcon.setImageResource(DevicePanel.getCloseIcon(0));
                    break;
                case DeviceTypeConstant.TYPE_BULB:
                    deviceIcon.setImageResource(DeviceBulb.getCloseIcon(0));
                    break;

                case DeviceTypeConstant.TYPE_AIRCONDITION:
                    deviceIcon.setImageResource(DeviceAirCon.getCloseIcon(0));
                    break;
                case DeviceTypeConstant.TYPE_PADDLE:
                    deviceIcon.setImageResource(DevicePlug.getCloseIcon(0));
                    break;
                case DeviceTypeConstant.TYPE_THERMOSTAT:
                    deviceIcon.setImageResource(DeviceThermostat.getCloseIcon(0));
                    break;
                case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
                    deviceIcon.setImageResource(DeviceStripLights.getCloseIcon(0));
                    break;

            }
        }
        helper.addOnClickListener(R.id.card_item);
        helper.addOnClickListener(R.id.iv_onoff);
    }
}
