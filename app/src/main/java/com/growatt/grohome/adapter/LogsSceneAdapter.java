package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.LogsSceneBean;
import com.growatt.grohome.constants.GlobalConstant;

import java.util.List;

public class LogsSceneAdapter extends BaseMultiItemQuickAdapter<LogsSceneBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public LogsSceneAdapter(List<LogsSceneBean> data) {
        super(data);
        addItemType(GlobalConstant.STATUS_ITEM_OTHER, R.layout.item_scene_time);
        addItemType(GlobalConstant.STATUS_ITEM_DATA, R.layout.item_scenes_log);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LogsSceneBean item) {

    }
}
