package com.growatt.grohome.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;

import java.util.List;

/**
 * Created by Administrator on 2019/1/29.
 */

public class RightMenuAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public RightMenuAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_menu_item,item);
    }




}
