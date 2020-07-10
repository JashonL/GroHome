package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.bean.CommondityBean;

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

    }
}
