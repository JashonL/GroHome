package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.BulbSceneBean;
import com.growatt.grohome.module.device.manager.DeviceBulb;

import java.util.Collection;
import java.util.List;

public class BulbSceneAdapter extends BaseQuickAdapter<BulbSceneBean, BaseViewHolder> {

    private int nowSelectPosition=-1;

    public BulbSceneAdapter(int layoutResId, @Nullable List<BulbSceneBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BulbSceneBean item) {
        helper.setText(R.id.tv_scene_name,item.getName());
        helper.setVisible(R.id.iv_scene_select, item.isSelected());
        String id = item.getId();
        switch (id){
            case DeviceBulb.BULB_SCENE_NIGHT:
                helper.setImageResource(R.id.iv_scene_pic,R.drawable.sence_night);
                break;
            case DeviceBulb.BULB_SCENE_READ:
                helper.setImageResource(R.id.iv_scene_pic,R.drawable.sence_read);
                break;
            case DeviceBulb.BULB_SCENE_MEETING:
                helper.setImageResource(R.id.iv_scene_pic,R.drawable.sence_meeting);
                break;
            case DeviceBulb.BULB_SCENE_LEISURE:
                helper.setImageResource(R.id.iv_scene_pic,R.drawable.sence_leisure);
                break;
            case DeviceBulb.BULB_SCENE_SOFT:
                helper.setImageResource(R.id.iv_scene_pic,R.drawable.sence_soft);
                break;
            case DeviceBulb.BULB_SCENE_SHINE:
                helper.setImageResource(R.id.iv_scene_pic,R.drawable.sence_shine);
                break;
            case DeviceBulb.BULB_SCENE_GORGEOUS:
                helper.setImageResource(R.id.iv_scene_pic,R.drawable.sence_gorgeous);
                break;
            case DeviceBulb.BULB_SCENE_RAINBOW:
                helper.setImageResource(R.id.iv_scene_pic,R.drawable.sence_rainbow);
                break;
        }
    }


    public void setNowSelectPosition(int position) {
        if (position >= getItemCount()) return;
        //去除其他item选择
        try {
            //不相等时才去除之前选中item以及赋值，防止重复操作
            if (this.nowSelectPosition != position) {
                if (this.nowSelectPosition >=0 && this.nowSelectPosition < getItemCount()) {
                    BulbSceneBean itemPre = getItem(nowSelectPosition);
                    assert itemPre != null;
                    itemPre.setSelected(false);
                }

                this.nowSelectPosition = position;
            }
            BulbSceneBean itemNow = getItem(nowSelectPosition);
            //只有没被选中才刷新数据
            assert itemNow != null;
            if (!itemNow.isSelected()) {
                itemNow.setSelected(true);
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void replaceData(@NonNull Collection<? extends BulbSceneBean> data) {
        super.replaceData(data);
        int nowPos = 0;
        if (nowSelectPosition >= 0 && nowSelectPosition < data.size()){
            nowPos = nowSelectPosition;
        }
        setNowSelectPosition(nowPos);
    }
}
