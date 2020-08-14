package com.growatt.grohome.adapter;

import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.DeviceTimingBean;
import com.growatt.grohome.bean.SwitchTimingBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.utils.CommentUtils;

import java.util.List;

public class DeviceTimingAdapter extends BaseQuickAdapter<DeviceTimingBean, BaseViewHolder> {
    public DeviceTimingAdapter(int layoutResId, @Nullable List<DeviceTimingBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DeviceTimingBean item) {
        ImageView ivSwitch = helper.getView(R.id.iv_switch);

        int status = item.getStatus();
        if (status == 1) {
            ivSwitch.setImageResource(R.drawable.scenes_off);
        } else {
            ivSwitch.setImageResource(R.drawable.scenes_on);
        }

        LinearLayout llTextGroup = helper.getView(R.id.ll_text_group);
        String devType = item.getDevType();
        llTextGroup.removeAllViews();
        if (DeviceTypeConstant.TYPE_PANELSWITCH.equals(devType)) {
            List<SwitchTimingBean> conf = item.getConf();
            if (conf!=null){
                for (int i=0;i<conf.size();i++){
                    TextView tvOnoff = createOnoffView(mContext.getResources().getDimension(R.dimen.size_title_sp_14),R.color.color_text_33);
                    TextView tvTiming = createOnoffView(mContext.getResources().getDimension(R.dimen.size_title_sp_14),R.color.color_text_33);
                    llTextGroup.addView(tvOnoff);
                    llTextGroup.addView(tvTiming);
                    setTimingViews(conf.get(i),tvOnoff,tvTiming);
                }
            }

        } else {
            TextView tvOnoff = createOnoffView(mContext.getResources().getDimension(R.dimen.size_title_sp_14),R.color.color_text_33);
            TextView tvTiming = createOnoffView(mContext.getResources().getDimension(R.dimen.size_title_sp_14),R.color.color_text_33);
            llTextGroup.addView(tvOnoff);
            llTextGroup.addView(tvTiming);
            setTimingViews(item, tvOnoff, tvTiming);
        }
        helper.addOnClickListener(R.id.iv_switch);
    }




    private void setTimingViews(DeviceTimingBean timingBean, TextView tvOnoff, TextView tvTiming) {
        String timeValue = timingBean.getTimeValue();
        String key = timingBean.getcKey();
        String loopType = timingBean.getLoopType();
        String loopValue = timingBean.getLoopValue();
        String onOff;
        if (key.equals("open") || key.equals("set")) {
            onOff = mContext.getString(R.string.m167_on);
        } else {
            onOff = mContext.getString(R.string.m168_off);
        }
        String onOffTime = onOff + timeValue;
        tvOnoff.setText(onOffTime);
        StringBuilder loopStyle = new StringBuilder();
        if ("-1".equals(loopType)) {
            loopStyle = new StringBuilder(mContext.getString(R.string.m222_single));
        } else if ("0".equals(loopType)) {
            loopStyle = new StringBuilder(mContext.getString(R.string.m224_everyday));
        } else if ("1".equals(loopType)) {
            if (loopValue.equals("1111111")) {
                loopStyle = new StringBuilder(mContext.getString(R.string.m224_everyday));
            } else {
                char[] chars = loopValue.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (String.valueOf(chars[i]).equals("1")) {
                        String week = CommentUtils.getWeeks(mContext).get(i);
                        loopStyle.append(week).append(",");
                    }
                }
            }
        } else {
            loopStyle = new StringBuilder(mContext.getString(R.string.m235_not_set));
        }
        String cycleDay = loopStyle.toString();
        if (cycleDay.endsWith(",")) {
            cycleDay = cycleDay.substring(0, cycleDay.length() - 1);
        }
        String cValue = timingBean.getCValue();
        if (key.equals("set")) {
            tvTiming.setText(String.format("%1$s     %2$s" + GlobalConstant.TEMP_UNIT_CELSIUS, cycleDay, cValue));
        } else {
            tvTiming.setText(String.format("%1$s", cycleDay));
        }
    }



    /**
     * @param timingBean
     */
    private void setTimingViews(SwitchTimingBean timingBean, TextView tvOnoff, TextView tvTiming) {
        String timeValue = timingBean.getTimeValue();
        String key = timingBean.getcKey();
        String loopType = timingBean.getLoopType();
        String loopValue = timingBean.getLoopValue();
        String name = timingBean.getName();
        String onOff;
        if (key.equals("open") || key.equals("set")) {
            onOff = mContext.getString(R.string.m167_on);
        } else {
            onOff = mContext.getString(R.string.m168_off);
        }
        String onOffTime = onOff + timeValue;
        if(!TextUtils.isEmpty(name)){
            onOffTime = name+onOff + timeValue;
        }
        tvOnoff.setText(onOffTime);
        StringBuilder loopStyle = new StringBuilder();
        if ("-1".equals(loopType)) {
            loopStyle = new StringBuilder(mContext.getString(R.string.m222_single));
        } else if ("0".equals(loopType)) {
            loopStyle = new StringBuilder(mContext.getString(R.string.m224_everyday));
        } else if ("1".equals(loopType)) {
            if (loopValue.equals("1111111")) {
                loopStyle = new StringBuilder(mContext.getString(R.string.m224_everyday));
            } else {
                char[] chars = loopValue.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (String.valueOf(chars[i]).equals("1")) {
                        String week = CommentUtils.getWeeks(mContext).get(i);
                        loopStyle.append(week).append(",");
                    }
                }
            }
        } else {
            loopStyle = new StringBuilder(mContext.getString(R.string.m235_not_set));
        }
        String cycleDay = loopStyle.toString();
        if (cycleDay.endsWith(",")) {
            cycleDay = cycleDay.substring(0, cycleDay.length() - 1);
        }
        tvTiming.setText(String.format("%1$s", cycleDay));
    }



    private TextView createOnoffView(float size, int color) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textView.setTextColor(ContextCompat.getColor(mContext, color));
        textView.setLayoutParams(layoutParams);
        return textView;
    }

}
