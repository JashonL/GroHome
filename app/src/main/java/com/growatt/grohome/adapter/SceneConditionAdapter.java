package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.bean.SceneConditionBean;
import com.growatt.grohome.bean.SceneTaskBean;

import java.util.List;

public class SceneConditionAdapter extends BaseQuickAdapter<SceneConditionBean, BaseViewHolder> {
    public SceneConditionAdapter(int layoutResId, @Nullable List<SceneConditionBean> data) {
        super(layoutResId, data);
    }

    public SceneConditionAdapter(@Nullable List<SceneConditionBean> data) {
        super(data);
    }

    public SceneConditionAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SceneConditionBean item) {

    }
}
