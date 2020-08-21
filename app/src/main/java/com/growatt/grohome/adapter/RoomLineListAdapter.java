package com.growatt.grohome.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.utils.GlideUtils;

import java.util.List;

public class RoomLineListAdapter extends BaseQuickAdapter<HomeRoomBean, BaseViewHolder> {
    private int nowSelect = -1;

    public RoomLineListAdapter(int layoutResId, @Nullable List<HomeRoomBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeRoomBean item) {
        //设置房间图片
        ImageView ivRoom = helper.getView(R.id.iv_room_img);
        String imgPath = item.getCdn();
        GlideUtils.showImageContext(mContext, imgPath, ivRoom);
        //设置房间名
        helper.setText(R.id.tv_now_text, item.getName());
        //显示当前选中
        View vNow = helper.getView(R.id.iv_now_icon);
        if (item.isSelect()) {
            vNow.setVisibility(View.VISIBLE);
            nowSelect = helper.getAdapterPosition();
        } else {
            vNow.setVisibility(View.GONE);
        }
    }

    public HomeRoomBean getNowItem() {
        int pos = nowSelect;
        if (pos < 0 || pos > getItemCount() - 1) pos = 0;
        return getItem(pos);
    }

    public void setSelectItem(int postion) {
        if (postion < 0 || postion > getItemCount() - 1) return;
        for (int i = 0; i < getItemCount(); i++) {
            HomeRoomBean item = getItem(i);
            item.setSelect(false);
        }
        HomeRoomBean item = getItem(postion);
        item.setSelect(true);
        nowSelect = postion;
        notifyDataSetChanged();
    }
}
