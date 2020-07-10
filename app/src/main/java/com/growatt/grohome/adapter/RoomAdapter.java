package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.bean.RoomBean;

import java.util.List;

public class RoomAdapter extends BaseQuickAdapter<RoomBean, BaseViewHolder> {
    public RoomAdapter(int layoutResId, @Nullable List<RoomBean> data) {
        super(layoutResId, data);
    }

    public RoomAdapter(@Nullable List<RoomBean> data) {
        super(data);
    }

    public RoomAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RoomBean item) {

    }
}
