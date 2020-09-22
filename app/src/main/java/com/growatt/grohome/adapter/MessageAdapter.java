package com.growatt.grohome.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.MessageBean;
import com.growatt.grohome.utils.CommentUtils;

import java.util.List;

public class MessageAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    public MessageAdapter(int layoutResId, @Nullable List<MessageBean> data) {
        super(layoutResId, data);
    }

    public MessageAdapter(@Nullable List<MessageBean> data) {
        super(data);
    }

    public MessageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MessageBean item) {
        if (!TextUtils.isEmpty(item.getTitle())){
            helper.setText(R.id.tv_title,item.getTitle());
        }
        if (!TextUtils.isEmpty(item.getTime())){
            helper.setText(R.id.tv_time,item.getTime());
        }
        int language = CommentUtils.getLanguage();

        if (language==0){
            String msgContentCN = item.getMsgContentCN();
            if (!TextUtils.isEmpty(msgContentCN)){
                helper.setText(R.id.tv_content,msgContentCN);
            }
        }else {
            String msgContentEN = item.getMsgContentEN();
            if (!TextUtils.isEmpty(msgContentEN)){
                helper.setText(R.id.tv_content,msgContentEN);
            }
        }
    }
}
