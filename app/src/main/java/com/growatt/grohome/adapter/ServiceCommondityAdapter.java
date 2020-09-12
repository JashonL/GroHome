package com.growatt.grohome.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.CommondityBean;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.utils.GlideUtils;

import java.util.List;

public class ServiceCommondityAdapter extends BaseQuickAdapter<CommondityBean, BaseViewHolder> {
    public ServiceCommondityAdapter(int layoutResId, @Nullable List<CommondityBean> data) {
        super(layoutResId, data);
    }

    public ServiceCommondityAdapter(@Nullable List<CommondityBean> data) {
        super(data);
    }

    public ServiceCommondityAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CommondityBean item) {
        String deviceType = item.getDeviceType();
        ImageView ivPicture = helper.getView(R.id.iv_commondity_picture);
        switch (deviceType) {
            case DeviceTypeConstant.TYPE_PANELSWITCH:
                GlideUtils.showImageContext(mContext, R.drawable.banner_switch, R.drawable.banner_switch, R.drawable.banner_switch, ivPicture);
                break;
            case DeviceTypeConstant.TYPE_BULB:
                GlideUtils.showImageContext(mContext, R.drawable.banner_bulb, R.drawable.banner_bulb, R.drawable.banner_bulb, ivPicture);
                break;
            case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
                GlideUtils.showImageContext(mContext, R.drawable.banner_striplights, R.drawable.banner_striplights, R.drawable.banner_striplights, ivPicture);
                break;
            default:
                break;
        }
    }
}
