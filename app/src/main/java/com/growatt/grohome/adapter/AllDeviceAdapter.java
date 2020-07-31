package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.bean.HomeDeviceBean;

import java.util.List;

public class AllDeviceAdapter extends BaseQuickAdapter<HomeDeviceBean, BaseViewHolder> {
    public AllDeviceAdapter(int layoutResId, @Nullable List<HomeDeviceBean> data) {
        super(layoutResId, data);
    }

    public AllDeviceAdapter(@Nullable List<HomeDeviceBean> data) {
        super(data);
    }

    public AllDeviceAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeDeviceBean item) {

    }
}
