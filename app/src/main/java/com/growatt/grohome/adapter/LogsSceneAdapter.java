package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.bean.LogsSceneBean;

import java.util.List;

public class LogsSceneAdapter extends BaseQuickAdapter<LogsSceneBean, BaseViewHolder> {
    public LogsSceneAdapter(int layoutResId, @Nullable List<LogsSceneBean> data) {
        super(layoutResId, data);
    }

    public LogsSceneAdapter(@Nullable List<LogsSceneBean> data) {
        super(data);
    }

    public LogsSceneAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LogsSceneBean item) {

    }
}
