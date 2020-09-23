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
            String runStatus = item.getRunStatus();
            helper.setText(R.id.tv_title,title);
            helper.setText(R.id.tv_content,time);
            String dataType = item.getDataType();
            if (GlobalConstant.STRING_STATUS_ON.equals(runStatus)){//开启状态
                String status = mContext.getString(R.string.m328_starting);
                helper.setText(R.id.tv_title,status);
            }else {
                String status = mContext.getString(R.string.m329_terminated);
                helper.setText(R.id.tv_title,status);
            }
            switch (dataType){
                case GlobalConstant.SCENE_LOG_TYPE_HEADER:
                    helper.setVisible(R.id.line_half_top,false);
                    helper.setVisible(R.id.line_half_bottom,true);
                    break;
                case GlobalConstant.SCENE_LOG_TYPE_BODY:
                    helper.setVisible(R.id.line_half_top,true);
                    helper.setVisible(R.id.line_half_bottom,true);
                    break;
                case GlobalConstant.SCENE_LOG_TYPE_SINGLE:
                    helper.setVisible(R.id.line_half_top,false);
                    helper.setVisible(R.id.line_half_bottom,false);
                    break;
                case GlobalConstant.SCENE_LOG_TYPE_FOOT:
                    helper.setVisible(R.id.line_half_top,true);
                    helper.setVisible(R.id.line_half_bottom,false);
                    break;
            }
        }
    }
}
