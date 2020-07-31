package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.bean.SceneTaskBean;

import java.util.List;

public class SceneTaskAdapter extends BaseQuickAdapter<SceneTaskBean, BaseViewHolder> {
    public SceneTaskAdapter(int layoutResId, @Nullable List<SceneTaskBean> data) {
        super(layoutResId, data);
    }

    public SceneTaskAdapter(@Nullable List<SceneTaskBean> data) {
        super(data);
    }

    public SceneTaskAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SceneTaskBean item) {

    }
}
