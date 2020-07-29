package com.growatt.grohome.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.DeviceBean;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.utils.GlideUtils;

import java.util.List;

public class RoomAdapter extends BaseQuickAdapter<HomeRoomBean, BaseViewHolder> {

    public RoomAdapter(int layoutResId, @Nullable List<HomeRoomBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeRoomBean item) {
        helper.setText(R.id.tv_room_name,item.getName());
        LinearLayout deviceTypeGroup = helper.getView(R.id.ll_type);
        //设置图片信息
        String imgPath = item.getCdn();
        ImageView imageView = helper.getView(R.id.iv_room_pic);
        GlideUtils.showImageContext(mContext, R.drawable.home_keting, R.drawable.home_keting, imgPath, imageView,50);
        List<DeviceBean> devList = item.getDevList();
        if (devList!=null&&devList.size()>0){
            int size = devList.size();
            helper.setText(R.id.tv_count,"+"+size);
            for (int i = 0; i < size; i++) {

            }

        }else {
            helper.setVisible(R.id.tv_count,false);
        }



    }
}
