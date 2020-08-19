package com.growatt.grohome.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.growatt.grohome.R;
import com.maning.mndialoglibrary.MProgressDialog;


public class Mydialog {
	/**
	 * 默认延时消失时间
	 */
	private static final int DEFAULT_DELAY_MILLIS = 30000;
	private static Handler mDelayHandler = new Handler();
	private static Runnable runnableDelay = MProgressDialog::dismissProgress;
	public static void show(@NonNull Activity act, String msg){
		if (TextUtils.isEmpty(msg)){
			msg = act.getString(R.string.m291_logding);
		}
		MProgressDialog.showProgress(act,msg);
	}
	public static void show(@NonNull Activity act){
		MProgressDialog.showProgress(act,act.getString(R.string.m291_logding));
	}
	public static void show(@NonNull Activity act, int msg){
		MProgressDialog.showProgress(act,act.getString(R.string.m291_logding));
	}
	public static void show(@NonNull Context act){
		MProgressDialog.showProgress(act,act.getString(R.string.m291_logding));
	}
	public static void show(@NonNull Context act, String msg){
		if (TextUtils.isEmpty(msg)){
			msg = act.getString(R.string.m291_logding);
		}
		MProgressDialog.showProgress(act,msg);
	}
	public static void show(@NonNull Context act, int msg){
		MProgressDialog.showProgress(act,act.getString(R.string.m291_logding));
	}


	public static void dissmiss(){
		MProgressDialog.dismissProgress();
	}

	/**
	 * 延时消失dialog
	 * @param delayMillis:时间：毫秒
	 * @param act
	 */
	public static void showDelayDismissDialog(long delayMillis ,@NonNull Context act){
		MProgressDialog.showProgress(act,act.getString(R.string.m291_logding));
		delayDismissDialog(delayMillis);
	}


	public static void delayDismissDialog(long delayMillis) {
		mDelayHandler.removeCallbacks(runnableDelay);
		mDelayHandler.postDelayed(runnableDelay,delayMillis);
	}
}
