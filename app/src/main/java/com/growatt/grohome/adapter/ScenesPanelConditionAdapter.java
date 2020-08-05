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

public class ScenesPanelConditionAdapter extends BaseQuickAdapter<ScenesRoadBean, BaseViewHolder> {


    public ScenesPanelConditionAdapter(int layoutResId, @Nullable List<ScenesRoadBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ScenesRoadBean item) {
        String customName = item.getName();
        int onOff = item.getOnOff();
        String statusOn= item.getStatusOn();
        String statusOff= item.getStatusOff();
        boolean scenesConditionEnable = item.isScenesConditionEnable();
        helper.setText(R.id.tv_switch_status,customName);
         if (onOff==1){
             helper.setText(R.id.tv_switch_onoff,statusOn);
             helper.setImageResource(R.id.iv_switch_onoff,R.drawable.scenes_on);
         }else {
             helper.setText(R.id.tv_switch_onoff,statusOff);
             helper.setImageResource(R.id.iv_switch_onoff,R.drawable.scenes_off);
         }
         helper.setImageResource(R.id.iv_switch_use,scenesConditionEnable?R.drawable.icon_sign_check:R.drawable.icon_sign_checkoff);
         helper.addOnClickListener(R.id.iv_switch_onoff);
         helper.addOnClickListener(R.id.iv_switch_use);
    }

}
