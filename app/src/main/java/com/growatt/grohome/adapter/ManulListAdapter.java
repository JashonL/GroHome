package com.growatt.grohome.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.ManualBean;

import java.util.List;

public class ManulListAdapter extends BaseQuickAdapter<ManualBean, BaseViewHolder> {
    public ManulListAdapter(int layoutResId, @Nullable List<ManualBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ManualBean item) {
        String title = item.getTitle();
        if (!TextUtils.isEmpty(title)){
            helper.setText(R.id.tv_title,title);
        }

    }
}
