package com.growatt.grohome.adapter;


import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.SceneBulbSetInfo;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DevicePanel;
import com.growatt.grohome.module.device.manager.DevicePlug;
import com.growatt.grohome.module.device.manager.DeviceStripLights;
import com.growatt.grohome.module.device.manager.DeviceThermostat;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;

import java.util.List;

public class SceneTaskAdapter extends BaseQuickAdapter<SceneTaskBean, BaseViewHolder> {
    public SceneTaskAdapter(int layoutResId, @Nullable List<SceneTaskBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, SceneTaskBean item) {
        String devType = item.getDevType();
        String devName = item.getDevName();
        String linkType = item.getLinkType();
        helper.setText(R.id.tv_device_name, devName);
        StringBuilder setting = new StringBuilder();
        switch (devType) {
            case DeviceTypeConstant.TYPE_THERMOSTAT:
                helper.setImageResource(R.id.iv_device_icon, DeviceThermostat.getCloseIcon(1));
                if (GlobalConstant.SCENE_DEVICE_SHUT.equals(linkType)) {
                    setting.append(mContext.getString(R.string.m240_on_off)).append(":").append(mContext.getString(R.string.m168_off));
                } else {
                    double linkValue = item.getLinkValue();
                    setting.append(mContext.getString(R.string.m240_on_off)).append(":").append(mContext.getString(R.string.m167_on)).append(",")
                            .append(mContext.getString(R.string.m241_setting_temperature)).append(":").append(linkValue).append("℃");
                }
                break;

            case DeviceTypeConstant.TYPE_PADDLE:
                helper.setImageResource(R.id.iv_device_icon, DevicePlug.getCloseIcon(1));
                if (GlobalConstant.SCENE_DEVICE_SHUT.equals(linkType)) {
                    setting.append(mContext.getString(R.string.m240_on_off)).append(":").append(mContext.getString(R.string.m168_off));
                } else {
                    setting.append(mContext.getString(R.string.m240_on_off)).append(":").append(mContext.getString(R.string.m167_on));
                }
                break;
            case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
            case DeviceTypeConstant.TYPE_BULB:
                String[] modes = new String[]{mContext.getString(R.string.m306_white), mContext.getString(R.string.m307_colour), mContext.getString(R.string.m10_scenes)};
                if (DeviceTypeConstant.TYPE_BULB.equals(devType)) {
                    helper.setImageResource(R.id.iv_device_icon, DeviceBulb.getCloseIcon(1));
                } else {
                    helper.setImageResource(R.id.iv_device_icon, DeviceStripLights.getCloseIcon(1));
                }
                if (GlobalConstant.SCENE_DEVICE_SHUT.equals(linkType)) {
                    setting.append(mContext.getString(R.string.m240_on_off)).append(":").append(mContext.getString(R.string.m168_off)).append(",");
                } else {
                    setting.append(mContext.getString(R.string.m240_on_off)).append(":").append(mContext.getString(R.string.m167_on)).append(",");
                }
                SceneBulbSetInfo setInfo = item.getSetInfo();
                if (setInfo != null) {
                    String bright = setInfo.getBright();
                    String countdown = setInfo.getCountdown();
                    String mode = setInfo.getMode();
                    String temp = setInfo.getTemp();
                    if (!TextUtils.isEmpty(bright)) {
                        String[] s = bright.split("_");
                        int value = 0;
                        int enable=0;
                        try {
                            enable = Integer.parseInt(s[0]);
                            value = Integer.parseInt(s[2]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (enable!=0){
                            setting.append(mContext.getString(R.string.m91_bright_ness)).append(":").append(value).append(",");
                        }
                    }
                    if (!TextUtils.isEmpty(countdown)) {
                        String[] s = countdown.split("_");
                        int value = 0;
                        int enable=0;
                        try {
                            enable = Integer.parseInt(s[0]);
                            value = Integer.parseInt(s[2]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (enable!=0) {
                            setting.append(mContext.getString(R.string.m145_left_time)).append(":").append(value).append(mContext.getString(R.string.m303_second)).append(",");
                        }
                    }
                    if (!TextUtils.isEmpty(mode)) {
                        String[] s = mode.split("_");
                        int index = 0;
                        int enable=0;
                        try {
                            enable = Integer.parseInt(s[0]);
                            index = Integer.parseInt(s[2]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (enable!=0&&index>0){
                            setting.append(mContext.getString(R.string.m298_mode)).append(":").append(modes[index - 1]).append(",");
                        }
                    }
                    if (!TextUtils.isEmpty(temp)) {
                        String[] s = temp.split("_");
                        int value = 0;
                        int enable=0;
                        try {
                            enable = Integer.parseInt(s[0]);
                            value = Integer.parseInt(s[2]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (enable!=0){
                            setting.append(mContext.getString(R.string.m92_colour_temp)).append(":").append(value).append(",");
                        }
                    }
                }
                break;
            case DeviceTypeConstant.TYPE_PANELSWITCH:
                String road = item.getRoad();
                if (TextUtils.isEmpty(road)) return;
                char[] chars = road.toCharArray();
                List<String> nameList = item.getSwitchNameList();
                for (int i = 0; i < chars.length; i++) {
                    String name = mContext.getString(R.string.m240_on_off) + (i + 1);
                    if (nameList != null && nameList.size() >= chars.length) {
                        name = nameList.get(i);
                    }
                    if (String.valueOf(chars[i]).equals("1")) {
                        setting.append(name).append("-").append(mContext.getString(R.string.m167_on)).append(",");
                    } else {
                        setting.append(name).append("-").append(mContext.getString(R.string.m168_off)).append(",");
                    }
                }
                helper.setImageResource(R.id.iv_device_icon, DevicePanel.getCloseIcon(1));
                break;
        }
        String s = setting.toString();
        if (s.endsWith(",")) {
            s = s.substring(0, s.length() - 1);
        }
        helper.setText(R.id.tv_device_task, s);
        helper.addOnClickListener(R.id.iv_edit);
        helper.addOnClickListener(R.id.iv_delete);
    }
}
