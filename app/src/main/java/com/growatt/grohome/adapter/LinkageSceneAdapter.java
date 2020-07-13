package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.bean.LinkageSceneBean;

import java.util.List;

public class LinkageSceneAdapter extends BaseQuickAdapter<LinkageSceneBean, BaseViewHolder> {
    public LinkageSceneAdapter(int layoutResId, @Nullable List<LinkageSceneBean> data) {
        super(layoutResId, data);
    }

    public LinkageSceneAdapter(@Nullable List<LinkageSceneBean> data) {
        super(data);
    }

    public LinkageSceneAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LinkageSceneBean item) {

    }
}
