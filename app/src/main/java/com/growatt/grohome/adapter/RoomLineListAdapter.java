package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.HomeRoomBean;

import java.util.List;

public class RoomLineListAdapter extends BaseQuickAdapter<HomeRoomBean, BaseViewHolder> {
    public RoomLineListAdapter(int layoutResId, @Nullable List<HomeRoomBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeRoomBean item) {
        helper.setText(R.id.tv_room_name,item.getName());
    }
}
