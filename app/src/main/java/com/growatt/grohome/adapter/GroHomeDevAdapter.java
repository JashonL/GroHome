package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.bean.HomeDeviceBean;

import java.util.List;

public class GroHomeDevAdapter extends BaseQuickAdapter<HomeDeviceBean, BaseViewHolder> {
    public GroHomeDevAdapter(int layoutResId, @Nullable List<HomeDeviceBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeDeviceBean item) {

    }
}
