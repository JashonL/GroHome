package com.growatt.grohome.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.SwitchTimingBean;
import com.growatt.grohome.utils.CommentUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/1/3.
 */

public class PanelTimingAdapter extends BaseQuickAdapter<SwitchTimingBean, BaseViewHolder> {
    private Context context;

    public PanelTimingAdapter(Context context, int layoutResId, @Nullable List<SwitchTimingBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SwitchTimingBean item) {
        helper.setText(R.id.tvName, item.getName());
        TextView tvTiming = helper.getView(R.id.tvTiming);
        ImageView ivSwitch = helper.getView(R.id.ivSwitch);
        setTimingViews(item, tvTiming, ivSwitch);
        helper.addOnClickListener(R.id.ivSwitch);
    }


    /**
     * 设置总开关
     *
     * @param timingBean
     */
    private void setTimingViews(SwitchTimingBean timingBean, TextView tvTiming, ImageView ivSwitch) {
        String status = timingBean.getStatus();
        String timeValue = timingBean.getTimeValue();
        String key = timingBean.getcKey();
        String loopType = timingBean.getLoopType();
        String loopValue = timingBean.getLoopValue();
        if ("0".equals(status)) {
            ivSwitch.setImageResource(R.drawable.scenes_on);
        } else {
            ivSwitch.setImageResource(R.drawable.scenes_off);
        }
        if (!TextUtils.isEmpty(timeValue)) {
            String onOff;
            if (key.equals("open")) {
                onOff = context.getString(R.string.m167_on);
            } else {
                onOff = context.getString(R.string.m168_off);
            }
            StringBuilder loopStyle = new StringBuilder();
            if ("-1".equals(loopType)) {
                loopStyle = new StringBuilder(context.getString(R.string.m222_single));
            } else if ("0".equals(loopType)) {
                loopStyle = new StringBuilder(context.getString(R.string.m224_everyday));
            } else if ("1".equals(loopType)){
                if (loopValue.equals("1111111")) {
                    loopStyle = new StringBuilder(context.getString(R.string.m224_everyday));
                } else {
                    char[] chars = loopValue.toCharArray();
                    for (int i = 0; i < chars.length; i++) {
                        if (String.valueOf(chars[i]).equals("1")) {
                            String week = CommentUtils.getWeeks(mContext).get(i);
                            loopStyle.append(week).append(",");
                        }
                    }
                }
            }else {
                loopStyle = new StringBuilder("");
            }
            String cycleDay = loopStyle.toString();
            if (cycleDay.endsWith(",")) {
                cycleDay = cycleDay.substring(0, cycleDay.length() - 1);
            }
            String timingStyle = String.format("%1$s-%2$s-%3$s", timeValue, onOff, cycleDay);
            tvTiming.setText(timingStyle);
        } else {
            tvTiming.setText(R.string.m235_not_set);
        }
    }
}
