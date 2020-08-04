package com.growatt.grohome.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.ScenesRoadBean;

import java.util.List;

/**
 * Created by Administrator on 2019/1/19.
 */

public class ScenesPanelAdapter extends BaseQuickAdapter<ScenesRoadBean, BaseViewHolder> {
    public ScenesPanelAdapter(int layoutResId, @Nullable List<ScenesRoadBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ScenesRoadBean item) {
        String customName = item.getName();
        int onOff = item.getOnOff();
        helper.setText(R.id.tvSwitchName,customName);
        String statusOn= item.getStatusOn();
        String statusOff= item.getStatusOff();
         if (onOff==1){
             helper.setText(R.id.tvOnOffValue,statusOn);
             helper.setImageResource(R.id.ivSocketSwitch,R.drawable.scenes_on);
         }else {
             helper.setText(R.id.tvOnOffValue,statusOff);
             helper.setImageResource(R.id.ivSocketSwitch,R.drawable.scenes_off);
         }
    }

}
