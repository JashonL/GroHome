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

}
