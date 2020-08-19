package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.BulbSceneColourBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.customview.CircleView;

import java.util.Collection;
import java.util.List;

public class BulbSceneColourAdapter extends BaseMultiItemQuickAdapter<BulbSceneColourBean, BaseViewHolder> {

    private int nowSelectPosition=-1;


    public BulbSceneColourAdapter(@Nullable List<BulbSceneColourBean> data) {
        super(data);
        addItemType(GlobalConstant.STATUS_ITEM_DATA, R.layout.item_bulb_scene_colour);
        addItemType(GlobalConstant.STATUS_ITEM_OTHER, R.layout.item_bulb_scene_colour_add);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BulbSceneColourBean item) {
        if (item.getItemType()==GlobalConstant.STATUS_ITEM_DATA){
            CircleView view = helper.getView(R.id.view_color);
            if (item.isSelected()){
                view.setType(CircleView.CIRCLE_VIEW_TYPE_STROKE);
            }else {
                view.setType(CircleView.CIRCLE_VIEW_TYPE_SOLID);
            }
            boolean colour = item.isColour();
            if (colour){
                view.setColor(item.getColour());
            }else {
                view.setColor(item.getWhiteColor());

            }
        }
    }



    public int getNowSelectPosition() {
        return nowSelectPosition;
    }

    public void setNowSelectPosition(int position) {
        if (position >= getItemCount()) return;
        //去除其他item选择
        try {
            //不相等时才去除之前选中item以及赋值，防止重复操作
            if (this.nowSelectPosition != position) {
                if (this.nowSelectPosition >=0 && this.nowSelectPosition < getItemCount()) {
                    BulbSceneColourBean itemPre = getItem(nowSelectPosition);
                    assert itemPre != null;
                    itemPre.setSelected(false);
                }
                this.nowSelectPosition = position;
                BulbSceneColourBean itemNow = getItem(nowSelectPosition);
                //只有没被选中才刷新数据
                assert itemNow != null;
                if (!itemNow.isSelected()) {
                    itemNow.setSelected(true);
                    notifyDataSetChanged();
                }
            }else {
                BulbSceneColourBean itemNow = getItem(position);
                assert itemNow != null;
                itemNow.setSelected(false);
                nowSelectPosition=-1;
                notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void replaceData(@NonNull Collection<? extends BulbSceneColourBean> data) {
        super.replaceData(data);
        int nowPos = -1;
        if (nowSelectPosition >= 0 && nowSelectPosition < data.size()){
            nowPos = nowSelectPosition;
        }
        setNowSelectPosition(nowPos);
    }

}
