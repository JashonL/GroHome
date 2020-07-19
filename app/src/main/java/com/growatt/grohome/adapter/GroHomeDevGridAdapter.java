package com.growatt.grohome.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.HomeDeviceBean;

import java.util.List;

public class GroHomeDevGridAdapter extends BaseMultiItemQuickAdapter<HomeDeviceBean.DataBean, BaseViewHolder> {

    public final static int STATUS_ON = 1;
    public final static int STATUS_OFF = 0;


    public GroHomeDevGridAdapter(@Nullable List<HomeDeviceBean.DataBean> data) {
        super(data);
        addItemType(STATUS_ON, R.layout.item_device_grid_open);
        addItemType(STATUS_OFF, R.layout.item_device_grid_close);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeDeviceBean.DataBean item) {
        helper.setText(R.id.tv_device_name,item.getName());
    }
}
