package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.bean.SelectRoomBean;

import java.util.List;

public class SelectRoomAdapter extends BaseQuickAdapter<SelectRoomBean, BaseViewHolder> {
    public SelectRoomAdapter(int layoutResId, @Nullable List<SelectRoomBean> data) {
        super(layoutResId, data);
    }

    public SelectRoomAdapter(@Nullable List<SelectRoomBean> data) {
        super(data);
    }

    public SelectRoomAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SelectRoomBean item) {

    }
}
