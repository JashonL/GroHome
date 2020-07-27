package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.PanelSwitchBean;

import java.util.List;

public class SwitchAdapter extends BaseQuickAdapter<PanelSwitchBean.SwichBean, BaseViewHolder> {
    public SwitchAdapter(int layoutResId, @Nullable List<PanelSwitchBean.SwichBean> data) {
        super(layoutResId, data);
    }

    public SwitchAdapter(@Nullable List<PanelSwitchBean.SwichBean> data) {
        super(data);
    }

    public SwitchAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PanelSwitchBean.SwichBean item) {
        String name = item.getCustomName();
        int onOff = item.getOnOff();
        helper.setText(R.id.tv_name,name);
        if (onOff==1){//开启
            helper.setBackgroundRes(R.id.cl_parent,R.drawable.panel_switch_background);
            helper.setText(R.id.tv_status,mContext.getString(R.string.m167_on));
            helper.setImageResource(R.id.iv_switch_icon,R.drawable.wallswitch_on);
            helper.setImageResource(R.id.iv_setting,R.drawable.wallswitch_edit);
            helper.setTextColor(R.id.tv_name,mContext.getResources().getColor(R.color.white));
            helper.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.white));
        }else {//关闭
            helper.setBackgroundRes(R.id.cl_parent,R.drawable.panel_switch_background);
            helper.setText(R.id.tv_status,mContext.getString(R.string.m168_off));
            helper.setImageResource(R.id.iv_switch_icon,R.drawable.wallswitch_off);
            helper.setImageResource(R.id.iv_setting,R.drawable.wallswitch_edit);
            helper.setTextColor(R.id.tv_name,mContext.getResources().getColor(R.color.white));
            helper.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.white));
        }
        helper.addOnClickListener(R.id.cl_setting);
    }
}
