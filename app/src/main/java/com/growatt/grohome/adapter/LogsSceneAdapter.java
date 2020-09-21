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
        int itemType = item.getItemType();
        if (itemType == GlobalConstant.STATUS_ITEM_OTHER) {
            String runTime = item.getRunTime();
            String[] time = runTime.split("[\\D]");
            String year = time[0];
            String month = time[1];
            String day = time[2];
            helper.setText(R.id.tv_day,day);
            helper.setText(R.id.tv_month,month);
            helper.setText(R.id.tv_year,year);

        } else {
            String title = item.getCname();
            String time=item.getRunTime();
            helper.setText(R.id.tv_title,title);
            helper.setText(R.id.tv_content,time);
        }
    }
}
