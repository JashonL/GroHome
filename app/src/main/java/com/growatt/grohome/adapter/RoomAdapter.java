package com.growatt.grohome.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DevicePanel;
import com.growatt.grohome.module.device.manager.DevicePlug;
import com.growatt.grohome.module.device.manager.DeviceStripLights;
import com.growatt.grohome.module.device.manager.DeviceThermostat;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.utils.GlideUtils;

import java.util.List;

public class RoomAdapter extends BaseQuickAdapter<HomeRoomBean, BaseViewHolder> {

    public RoomAdapter(int layoutResId, @Nullable List<HomeRoomBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeRoomBean item) {
        helper.setText(R.id.tv_room_name, item.getName());
        LinearLayout deviceTypeGroup = helper.getView(R.id.ll_type);
        deviceTypeGroup.setTag(getParentPosition(item));
        //设置图片信息
        String imgPath = item.getCdn();
        GlideUtils.showImageContext(mContext, R.drawable.home_keting, R.drawable.home_keting, imgPath, helper.getView(R.id.iv_room_pic));
        List<GroDeviceBean> devList = item.getDevList();
        deviceTypeGroup.removeAllViews();
        if (devList != null && devList.size() > 0) {
            int size = devList.size();
            helper.setVisible(R.id.tv_count, true);
            helper.setText(R.id.tv_count, "+" + size);
            int res=0;
            for (int i = 0; i < size; i++) {
                GroDeviceBean deviceBean = devList.get(i);
                String devType = deviceBean.getDevType();
                switch (devType) {
                    case DeviceTypeConstant.TYPE_BULB:
                        res= DeviceBulb.getCloseIcon(4);
                        break;
                    case DeviceTypeConstant.TYPE_PADDLE:
                        res= DevicePlug.getCloseIcon(2);
                        break;
                    case DeviceTypeConstant.TYPE_PANELSWITCH:
                        res= DevicePanel.getCloseIcon(2);
                        break;
                    case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
                        res= DeviceStripLights.getCloseIcon(4);
                        break;
                    case DeviceTypeConstant.TYPE_THERMOSTAT:
                        res= DeviceThermostat.getCloseIcon(2);
                        break;
                }
                setImageType(mContext, deviceTypeGroup, res);
            }

        } else {
            helper.setVisible(R.id.tv_count, false);
        }


    }


    private void setImageType(Context context, LinearLayout llType, int res) {
        if (res==0)return;
        ImageView ivType = new ImageView(context);
        int width = context.getResources().getDimensionPixelSize(R.dimen.dp_24);
        int height = context.getResources().getDimensionPixelSize(R.dimen.dp_24);
        int marginLeft = context.getResources().getDimensionPixelSize(R.dimen.dp_10);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        if (llType.getChildCount() != 0)
            layoutParams.setMargins(marginLeft, 0, 0, 0);
        ivType.setLayoutParams(layoutParams);
        ivType.setImageResource(res);
        ivType.setScaleType(ImageView.ScaleType.FIT_XY);
        llType.addView(ivType);
    }
}
