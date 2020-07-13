package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.bean.LaunchSceneBean;

import java.util.List;

public class LaunchTapAdapter extends BaseQuickAdapter<LaunchSceneBean, BaseViewHolder> {
    public LaunchTapAdapter(int layoutResId, @Nullable List<LaunchSceneBean> data) {
        super(layoutResId, data);
    }

    public LaunchTapAdapter(@Nullable List<LaunchSceneBean> data) {
        super(data);
    }

    public LaunchTapAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LaunchSceneBean item) {

    }
}
