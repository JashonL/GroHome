package com.growatt.grohome.utils;


import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.growatt.grohome.R;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;

public class CircleDialogUtils {
    /**
     * 灯泡面板
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
      DialogFragment _5GhzDialog=  new CircleDialog.Builder()
                .setTitle(context.getString(R.string.m95_tips))
                .setText(context.getString(R.string.m99_connected_5ghz_wifi))
                .setWidth(0.7f)
                .setNegative(context.getString(R.string.m100_go_on),onNegativeListener)
                .setPositive(context.getString(R.string.m101_switch_wifi), onPositiveListner)
                .show(fragmentManager);
        return _5GhzDialog;
    }


    /**
     * 获取涂鸦token失败提示
     */
    public static DialogFragment showGetTuyaTokenFail(Context context, FragmentManager fragmentManager,String errorMsg) {
        DialogFragment getTokenFailDialog = new CircleDialog.Builder()
                .setTitle(context.getString(R.string.m95_tips))
                .setText(errorMsg)
                .setWidth(0.7f)
                .setPositive(context.getString(R.string.m90_ok), v -> {
                    ((FragmentActivity)context).finish();
                })
                .show(fragmentManager);
        return getTokenFailDialog;
    }


    /**
     * 配网失败
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
        DialogFragment cancelConfigDialog=  new CircleDialog.Builder()
                .setTitle(context.getString(R.string.m95_tips))
                .setText(context.getString(R.string.m126_confirm_exit))
                .setWidth(0.7f)
                .setNegative(context.getString(R.string.m127_no),onNegativeListener)
                .setPositive(context.getString(R.string.m90_ok), onPositiveListner)
                .show(fragmentManager);
        return cancelConfigDialog;
    }

}
