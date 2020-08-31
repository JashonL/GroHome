package com.growatt.grohome.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.growatt.grohome.R;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;

import java.util.List;

public class CircleDialogUtils {
    /**
     * 公共自定义弹框
     *
     * @return
     */
    public static DialogFragment showCommentBodyDialog(View bodyView, FragmentManager fragmentManager, OnCreateBodyViewListener listener) {
        DialogFragment bulbBodyDialog = new CircleDialog.Builder()
                .setBodyView(bodyView, listener)
                .setGravity(Gravity.BOTTOM)
                .setYoff(20)
                .show(fragmentManager);
        ;
        return bulbBodyDialog;
    }

    /**
     * 跳转到连接wifi界面
     */
    public static DialogFragment showSetWifiDialog(Context context, FragmentManager fragmentManager) {
        DialogFragment setWifiDialog = new CircleDialog.Builder()
                .setTitle(context.getString(R.string.m95_tips))
                .setText(context.getString(R.string.m96_wifi_not_connected))
                .setWidth(0.7f)
                .setPositive(context.getString(R.string.m97_to_connect), v -> {
                    ActivityUtils.gotoWiFiSetTingActivity((Activity) context);
                })
                .show(fragmentManager);
        return setWifiDialog;
    }


    /**
     * 跳转到连接5G提示
     */
    public static DialogFragment show5GHzDialog(Context context, FragmentManager fragmentManager, View.OnClickListener onNegativeListener, View.OnClickListener onPositiveListner) {
        DialogFragment _5GhzDialog = new CircleDialog.Builder()
                .setTitle(context.getString(R.string.m95_tips))
                .setText(context.getString(R.string.m99_connected_5ghz_wifi))
                .setWidth(0.7f)
                .setNegative(context.getString(R.string.m100_go_on), onNegativeListener)
                .setPositive(context.getString(R.string.m101_switch_wifi), onPositiveListner)
                .show(fragmentManager);
        return _5GhzDialog;
    }


    /**
     * 获取涂鸦token失败提示
     */
    public static DialogFragment showGetTuyaTokenFail(Context context, FragmentManager fragmentManager, String errorMsg) {
        DialogFragment getTokenFailDialog = new CircleDialog.Builder()
                .setTitle(context.getString(R.string.m95_tips))
                .setText(errorMsg)
                .setWidth(0.7f)
                .setPositive(context.getString(R.string.m90_ok), v -> {
                    ((FragmentActivity) context).finish();
                })
                .show(fragmentManager);
        return getTokenFailDialog;
    }


    /**
     * 配网失败
     *
     * @return
     */
    public static DialogFragment showFailConfig(View bodyView, FragmentManager fragmentManager, OnCreateBodyViewListener listener) {
        DialogFragment failConfigDialog = new CircleDialog.Builder()
                .setBodyView(bodyView, listener)
                .setGravity(Gravity.CENTER)
                .show(fragmentManager);
        ;
        return failConfigDialog;
    }


    /**
     * 取消配网
     */
    public static DialogFragment showCancelConfigDialog(Context context, FragmentManager fragmentManager, View.OnClickListener onNegativeListener, View.OnClickListener onPositiveListner) {
        DialogFragment cancelConfigDialog = new CircleDialog.Builder()
                .setTitle(context.getString(R.string.m95_tips))
                .setText(context.getString(R.string.m126_confirm_exit))
                .setWidth(0.7f)
                .setNegative(context.getString(R.string.m127_no), onNegativeListener)
                .setPositive(context.getString(R.string.m90_ok), onPositiveListner)
                .show(fragmentManager);
        return cancelConfigDialog;
    }

    /**
     * 场景颜色闪烁模式弹框
     */
    public static DialogFragment showSceneFlashMode(FragmentActivity activity, List<String> modes, OnLvItemClickListener listener) {
        DialogFragment flashModeDialog = new CircleDialog.Builder()
                .setTitle(activity.getString(R.string.m161_colour_flash_mode))
                .configTitle(params -> {
                    params.styleText = Typeface.BOLD;
                })
                .setItems(modes, listener)
                .setGravity(Gravity.BOTTOM)
                .show(activity.getSupportFragmentManager());
        return flashModeDialog;
    }


    /**
     * 场景颜色闪烁模式弹框
     */
    public static DialogFragment showTakePictureDialog(FragmentActivity activity, List<String> modes, OnLvItemClickListener listener) {
        DialogFragment takePictureDialog = new CircleDialog.Builder()
                .setTitle(activity.getString(R.string.m80_selection))
                .setItems(modes, listener)
                .setGravity(Gravity.BOTTOM)
                .setNegative(activity.getString(R.string.m89_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show(activity.getSupportFragmentManager());
        return takePictureDialog;
    }


    /**
     * 公共输入框
     *
     * @param activity
     * @return
     */
    public static DialogFragment showCommentInputDialog(FragmentActivity activity, String title, String text, String hint, boolean showKeyboard, OnInputClickListener listener) {
        DialogFragment inputDialog = new CircleDialog.Builder()
                .setCanceledOnTouchOutside(true)
                .setCancelable(true)
                .setTitle(title)
                .setInputText(text)
                .setInputHint(hint)
                .setGravity(Gravity.CENTER)
                .setInputShowKeyboard(showKeyboard)
                .setInputCounter(1000, (maxLen, currentLen) -> "")
                .setPositiveInput(activity.getString(R.string.m90_ok), listener)
                .configInput(params -> {
//                            params.isCounterAllEn = true;
                    params.padding = new int[]{5, 5, 5, 5};
                    params.strokeColor = ContextCompat.getColor(activity, R.color.black_999999);
//                                params.inputBackgroundResourceId = R.drawable.bg_input;
//                                params.gravity = Gravity.CENTER;
                    //密码
//                                params.inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
//                                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
                    //文字加粗
//            params.styleText = Typeface.BOLD;
                })
                .setNegative(activity.getString(R.string.m89_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show(activity.getSupportFragmentManager());
        return inputDialog;


    }


    /**
     * 公共提示框
     *
     * @param activity
     * @return
     */
    public static DialogFragment showCommentDialog(FragmentActivity activity, String title, String text, View.OnClickListener listener) {
        return showCommentDialog(activity, title, text, listener,null);
    }



    /**
     * 公共提示框
     *
     * @param activity
     * @return
     */
    public static DialogFragment showCommentDialog(FragmentActivity activity, String title, String text, View.OnClickListener posiListener,View.OnClickListener negativeListener) {
        DialogFragment inputDialog = new CircleDialog.Builder()
                .setTitle(title)
                .setText(text)
                .setGravity(Gravity.CENTER)
                .setPositive(activity.getString(R.string.m90_ok), posiListener)
                .setNegative(activity.getString(R.string.m89_cancel), negativeListener)
                .show(activity.getSupportFragmentManager());
        return inputDialog;
    }


    /**
     * 公共复选框
     *
     * @param activity
     * @return
     */
    public static DialogFragment showCommentItemDialog(FragmentActivity activity, String title, List<String> items, int gravity, OnLvItemClickListener listener) {
        DialogFragment itemsSelectDialog = new CircleDialog.Builder()
                .setTitle(title)
                .configTitle(params -> {
                    params.styleText = Typeface.BOLD;
                })
                .setItems(items, listener)
                .configItems(params -> {
                    params.dividerHeight = 0;
                    params.textColor = ContextCompat.getColor(activity, R.color.color_text_33);
                })
                .setGravity(gravity)
                .show(activity.getSupportFragmentManager());

        return itemsSelectDialog;
    }


    /**
     * 公共复选框,没有标题
     *
     * @param activity
     * @return
     */
    public static DialogFragment showCommentItemDialog(FragmentActivity activity, List<String> items, int gravity, OnLvItemClickListener listener) {
        DialogFragment itemsSelectDialog = new CircleDialog.Builder()
                .configTitle(params -> {
                    params.styleText = Typeface.BOLD;
                })
                .setItems(items, listener)
                .configItems(params -> {
                    params.dividerHeight = 0;
                    params.textColor = ContextCompat.getColor(activity, R.color.color_text_33);
                })
                .setGravity(gravity)
                .show(activity.getSupportFragmentManager());

        return itemsSelectDialog;
    }


    /**
     * 公共自定义框
     *
     * @return
     */
    public static DialogFragment showCommentBodyView(Context context, View bodyView, String title, FragmentManager fragmentManager, OnCreateBodyViewListener listener, View.OnClickListener positiveListner) {
        DialogFragment commentBodyDialog = new CircleDialog.Builder()
                .setTitle(title)
                .setBodyView(bodyView, listener)
                .setGravity(Gravity.CENTER)
                .setPositive(context.getString(R.string.m90_ok), positiveListner)
                .setNegative(context.getString(R.string.m89_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show(fragmentManager);
        ;
        return commentBodyDialog;
    }


    /**
     * 公共自定义框
     *
     * @return
     */
    public static DialogFragment showCommentBodyViewNoCancel(Context context, View bodyView, String title, FragmentManager fragmentManager, OnCreateBodyViewListener listener, View.OnClickListener positiveListner) {
        DialogFragment commentBodyDialog = new CircleDialog.Builder()
                .setTitle(title)
                .setBodyView(bodyView, listener)
                .setGravity(Gravity.CENTER)
                .setPositive(context.getString(R.string.m90_ok), positiveListner)
                .show(fragmentManager);
        ;
        return commentBodyDialog;
    }



    /**
     * 公共复选择框
     *
     * @return
     */
    public static DialogFragment showSelectItemDialog(FragmentActivity context,  String title, RecyclerView.Adapter adapter,  RecyclerView.LayoutManager layoutManager,View.OnClickListener positiveListner) {
        DialogFragment selectItemDialog = new CircleDialog.Builder()
                .setTitle(title)
                .setItems(adapter,layoutManager)
                .setPositive(context.getString(R.string.m90_ok), positiveListner)
                .setNegative(context.getString(R.string.m89_cancel),null)
                .show(context.getSupportFragmentManager());
        ;
        return selectItemDialog;
    }


    /**
     * 时间选择框
     * @param context 上下文
     * @param hour  当前时间：时
     * @param min 当前时间：分
     * @param fragmentManager  fragmentManager
     * @param listener 回调监听
     * @return
     */

    public static DialogFragment showWhiteTimeSelect(Context context, int hour, int min, FragmentManager fragmentManager,boolean isShowStatus,timeSelectedListener listener) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_time_select, null, false);
        DialogFragment bulbBodyDialog = new CircleDialog.Builder()
                .setBodyView(contentView, new OnCreateBodyViewListener() {
                    @Override
                    public void onCreateBodyView(View view) {
                        List<String> hours = CommentUtils.getHours();
                        List<String> mins = CommentUtils.getMins();
                        WheelView wheelHour = view.findViewById(R.id.wheel_hour);
                        WheelView wheelMin = view.findViewById(R.id.wheel_min);
                        //初始化时间选择器
                        wheelHour.setCyclic(true);
                        wheelHour.isCenterLabel(true);
                        wheelHour.setAdapter(new ArrayWheelAdapter<>(hours));
                        wheelHour.setCurrentItem(hour);
                        wheelHour.setTextColorCenter(ContextCompat.getColor(context, R.color.color_text_00));
                        wheelMin.setCyclic(true);
                        wheelMin.isCenterLabel(true);
                        wheelMin.setAdapter(new ArrayWheelAdapter<>(mins));
                        wheelMin.setCurrentItem(min);
                        wheelMin.setTextColorCenter(ContextCompat.getColor(context, R.color.color_text_00));

                        Group status = view.findViewById(R.id.group_status);
                        status.setVisibility(isShowStatus?View.VISIBLE:View.GONE);
                        CheckBox cbStatus = view.findViewById(R.id.cb_checked);
                        cbStatus.setChecked(true);

                        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.cancle();
                            }
                        });

                        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int hour = wheelHour.getCurrentItem();
                                int min = wheelMin.getCurrentItem();
                                boolean checked = cbStatus.isChecked();
                                listener.ok(checked,hour,min);
                            }
                        });
                    }
                })
                .setGravity(Gravity.BOTTOM)
                .setYoff(20)
                .show(fragmentManager);
        ;
        return bulbBodyDialog;
    }




    public interface timeSelectedListener {
        void cancle();
        void ok(boolean status,int hour,int min);
    }

}
