package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.BulbSceneBean;
import com.growatt.grohome.bean.BulbSceneColourBean;
import com.growatt.grohome.module.device.manager.DeviceBulb;

import java.util.Collection;
import java.util.List;

public class BulbSceneColourAdapter extends BaseQuickAdapter<BulbSceneColourBean, BaseViewHolder> {

    private int nowSelectPosition=-1;

    public BulbSceneColourAdapter(int layoutResId, @Nullable List<BulbSceneColourBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BulbSceneColourBean item) {

    }


}
