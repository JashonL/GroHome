package com.growatt.grohome.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DevicePanel;
import com.growatt.grohome.module.device.manager.DevicePlug;
import com.growatt.grohome.module.device.manager.DeviceThermostat;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;

import java.util.List;

/**
 * Created by Administrator on 2019/12/4.
 */

public class ScenesDivceListAdapter extends BaseQuickAdapter<SceneTaskBean, BaseViewHolder> {

    public ScenesDivceListAdapter(int layoutResId, @Nullable List<SceneTaskBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, SceneTaskBean item) {
        String devType = item.getDevType();
        StringBuilder setting = new StringBuilder();
        String linkType = item.getLinkType();
        ImageView icon= helper.getView(R.id.iv_icon);
        switch (devType) {
            case DeviceTypeConstant.TYPE_PADDLE:
                if (GlobalConstant.SCENE_DEVICE_OPEN.equals(linkType)) {
                    setting.append(mContext.getString(R.string.m240_on_off)).append(":").append(mContext.getString(R.string.m167_on));
                } else {
                    setting.append(mContext.getString(R.string.m240_on_off)).append(":").append(mContext.getString(R.string.m168_off));
                }
                icon.setImageResource(DevicePlug.getCloseIcon(1));
                break;
            case DeviceTypeConstant.TYPE_THERMOSTAT:
                if (GlobalConstant.SCENE_DEVICE_SET.equals(linkType)||GlobalConstant.SCENE_DEVICE_OPEN.equals(linkType)) {
                    double linkValue = item.getLinkValue();
                    setting.append(mContext.getString(R.string.m240_on_off)).append(":").append(mContext.getString(R.string.m167_on)).
                            append(",").append(mContext.getString(R.string.m215_setting_temperature)).append(":").append(linkValue).append("°");
                } else {
                    setting.append(mContext.getString(R.string.m240_on_off)).append(":").append(mContext.getString(R.string.m168_off));
                }
                icon.setImageResource(DeviceThermostat.getCloseIcon(1));
                break;
            case DeviceTypeConstant.TYPE_PANELSWITCH:
                String road = item.getRoad();
                char[] chars = road.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    String name=mContext.getString(R.string.m240_on_off)+ (i + 1);
                    if (String.valueOf(chars[i]).equals("1")) {
                        setting.append(name).append("-").append(mContext.getString(R.string.m167_on)).append(",");
                    } else {
                        setting.append(name).append("-").append(mContext.getString(R.string.m168_off)).append(",");
                    }
                }
                icon.setImageResource(DevicePanel.getCloseIcon(1));
                break;
        }
        String s = setting.toString();
        if (s.endsWith(",")) {
            s = s.substring(0, s.length() - 1);
        }
        helper.setText(R.id.tv_device_name, item.getDevName());
        helper.setText(R.id.tv_device_status, s);

    }
}