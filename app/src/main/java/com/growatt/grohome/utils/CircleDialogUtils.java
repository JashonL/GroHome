package com.growatt.grohome.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.growatt.grohome.R;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;

import java.util.List;

public class CircleDialogUtils {
    /**
     * 灯泡面板
     *
     * @return
     */
    public static DialogFragment showBulbWhiteMode(View bodyView, FragmentManager fragmentManager, OnCreateBodyViewListener listener) {
        DialogFragment bulbWhiteMode = new CircleDialog.Builder()
                .setBodyView(bodyView, listener)
                .setGravity(Gravity.BOTTOM)
                .setYoff(20)
                .show(fragmentManager);
        ;
        return bulbWhiteMode;
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
     * 跳转到连接5G提示
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
                .setGravity(Gravity.CENTER)
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
        DialogFragment inputDialog = new CircleDialog.Builder()
                .setTitle(title)
                .setText(text)
                .setGravity(Gravity.CENTER)
                .setPositive(activity.getString(R.string.m90_ok), listener)
                .setNegative(activity.getString(R.string.m89_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show(activity.getSupportFragmentManager());
        return inputDialog;
    }


    /**
     * 公共复选框
     *
     * @param activity
     * @return
     */
    public static DialogFragment showCommentItemDialog(FragmentActivity activity, String title,List<String> items ,int gravity,OnLvItemClickListener listener) {
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

}
