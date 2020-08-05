package com.growatt.grohome.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PickViewUtils {


    /**
     * 弹出滚动选择器
     *
     * @param data     数据源
     * @param title    选择器标题
     */
    public static void showPickView(final Activity context, final List<String> data,OnOptionsSelectListener listener, String title) {
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(context,listener)
                .setTitleText(title)
                .setCancelText(App.getInstance().getString(R.string.m89_cancel))//取消按钮文字
                .setSubmitText(App.getInstance().getString(R.string.m90_ok))//确认按钮文字
                .setTitleBgColor(ContextCompat.getColor(context,R.color.white))
                .setTitleColor(ContextCompat.getColor(context,R.color.color_theme_green))
                .setSubmitColor(ContextCompat.getColor(context,R.color.color_theme_green))
                .setCancelColor(ContextCompat.getColor(context,R.color.color_theme_green))
                .setBgColor(ContextCompat.getColor(context,R.color.white))
                .setTitleSize(22)
                .setTextColorCenter(ContextCompat.getColor(context,R.color.color_theme_green))
                .build();
        pvOptions.setPicker(data);
        pvOptions.show();
    }


    /**
     * 弹出滚动选择器
     *
     * @param data     数据源
     * @param textView 显示的位置
     * @param title    选择器标题
     */
    public static void showPickView(final Activity context, final List<String> data, final TextView textView, String title, String label) {
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                final String tx = data.get(options1) + label;
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(tx);
                    }
                });
            }
        })
                .setTitleText(title)
                .setTitleBgColor(0xff032d3a)
                .setTitleColor(0xffffffff)
                .setSubmitColor(0xffffffff)
                .setCancelColor(0xff058ef0)
                .setBgColor(0xff032d3a)
                .setTitleSize(22)
                .setLabels(label, "", "")
                .setTextColorCenter(0xffffffff)
                .build();
        pvOptions.setPicker(data);
        pvOptions.show();
    }



    /**
     * 弹出时间选择器
     */
    public static void showTimePickView(Activity context, String selectDate,OnTimeSelectListener listener) throws Exception{
        Calendar  selectedDate = Calendar.getInstance();
        if (!TextUtils.isEmpty(selectDate)){
            String[] split = selectDate.split("[\\D]");
            if (split.length>1){
                int hour = Integer.parseInt(split[0]);
                int min = Integer.parseInt(split[1]);
                selectedDate.set(Calendar.HOUR_OF_DAY,hour);
                selectedDate.set(Calendar.MINUTE,min);
                selectedDate.set(Calendar.SECOND,0);
            }
        }
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH,-1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH,1);
        TimePickerView pvCustomTime = new TimePickerBuilder(context,listener)
                .setType(new boolean[]{false, false, false, true, true, false})// 默认全部显示
                .setCancelText(context.getString(R.string.m89_cancel))//取消按钮文字
                .setSubmitText(context.getString(R.string.m90_ok))//确认按钮文字
                .setContentTextSize(18)
                .setTitleSize(20)//标题文字大小
                .setTitleText(context.getString(R.string.m252_time))//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(ContextCompat.getColor(context,R.color.color_text_33))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(context,R.color.color_text_33))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(context,R.color.color_text_33))//取消按钮文字颜色
                .setTitleBgColor(ContextCompat.getColor(context,R.color.white))//标题背景颜色 Night mode
                .setBgColor(ContextCompat.getColor(context,R.color.white))//滚轮背景颜色 Night mode
                .setTextColorCenter(ContextCompat.getColor(context,R.color.color_text_33))
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("", "", "", context.getString(R.string.m249_hour), context.getString(R.string.m250_min), "")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvCustomTime.show();
    }

}
