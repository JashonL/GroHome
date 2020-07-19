package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.HomeDeviceBean;

import java.util.List;

import static com.growatt.grohome.adapter.GroHomeDevGridAdapter.STATUS_OFF;
import static com.growatt.grohome.adapter.GroHomeDevGridAdapter.STATUS_ON;

public class GroHomeDevLineAdapter extends BaseMultiItemQuickAdapter<HomeDeviceBean.DataBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GroHomeDevLineAdapter(List<HomeDeviceBean.DataBean> data) {
        super(data);
        addItemType(STATUS_ON, R.layout.item_device_line_open);
        addItemType(STATUS_OFF, R.layout.item_device_line_close);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeDeviceBean.DataBean item) {
        helper.setText(R.id.tv_device_name,item.getName());
    }
}
