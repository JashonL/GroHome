package com.growatt.grohome.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.FqaBean;

import java.util.List;

public class FqaAdapter extends BaseQuickAdapter<FqaBean, BaseViewHolder> {
    public FqaAdapter(int layoutResId, @Nullable List<FqaBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FqaBean item) {
        if (!TextUtils.isEmpty(item.getTitle())){
            helper.setText(R.id.tv_title,item.getTitle());

        }
    }
}
