package com.growatt.grohome.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.constants.GlobalConstant;

import java.util.Collection;
import java.util.List;

public class SelectRoomAdapter extends BaseMultiItemQuickAdapter<HomeRoomBean, BaseViewHolder> {
    private int nowSelectPosition=-1;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SelectRoomAdapter(List<HomeRoomBean> data) {
        super(data);
        addItemType(GlobalConstant.STATUS_ON, R.layout.item_select_room);
        addItemType(GlobalConstant.STATUS_OFF, R.layout.item_select_room_off);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeRoomBean item) {
        helper.setText(R.id.tv_room_name,item.getName());
    }



    public int getNowSelectPosition() {
        return nowSelectPosition;
    }

    public void setNowSelectPosition(int position) {
        if (position==-1)return;
        if (position >= getItemCount()) return;
        //去除其他item选择
        try {
            //不相等时才去除之前选中item以及赋值，防止重复操作
            if (this.nowSelectPosition != position) {
                if (this.nowSelectPosition >=0 && this.nowSelectPosition < getItemCount()) {
                    HomeRoomBean itemPre = getItem(nowSelectPosition);
                    assert itemPre != null;
                    itemPre.setSelect(false);
                    itemPre.setItemType(0);
                }
                this.nowSelectPosition = position;
                HomeRoomBean itemNow = getItem(nowSelectPosition);
                //只有没被选中才刷新数据
                assert itemNow != null;
                if (!itemNow.isSelect()) {
                    itemNow.setSelect(true);
                    itemNow.setItemType(1);
                    notifyDataSetChanged();
                }
            }else {
                HomeRoomBean itemNow = getItem(position);
                assert itemNow != null;
                itemNow.setSelect(false);
                itemNow.setItemType(0);
                nowSelectPosition=-1;
                notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void replaceData(@NonNull Collection<? extends HomeRoomBean> data) {
        super.replaceData(data);
        int nowPos = -1;
        if (nowSelectPosition >= 0 && nowSelectPosition < data.size()){
            nowPos = nowSelectPosition;
        }
        setNowSelectPosition(nowPos);
    }

}
