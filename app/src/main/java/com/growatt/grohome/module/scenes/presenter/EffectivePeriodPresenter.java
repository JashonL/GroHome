package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.scenes.PeriodCommentSetActivity;
import com.growatt.grohome.module.scenes.RepeatActivity;
import com.growatt.grohome.module.scenes.view.IEffectivePeriodView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.growatt.grohome.utils.PickViewUtils;
import com.growatt.grohome.utils.TimePickUtils;

import java.text.SimpleDateFormat;

import static android.app.Activity.RESULT_OK;

public class EffectivePeriodPresenter extends BasePresenter<IEffectivePeriodView> {

    private String startTime;//开始时间
    private String endTime;//结束时间
    private String timeValue;//定时

    private String loopType;//每天，单次
    private String loopValue;//用来判断周一到周五
    private String setType;


    public EffectivePeriodPresenter(IEffectivePeriodView baseView) {
        super(baseView);
    }

    public EffectivePeriodPresenter(Context context, IEffectivePeriodView baseView) {
        super(context, baseView);
    }

    public void getAlreadySet(){
        //获取已设置的时间
        startTime = ((Activity) context).getIntent().getStringExtra(GlobalConstant.TIME_START);
        endTime = ((Activity) context).getIntent().getStringExtra(GlobalConstant.TIME_END);
        timeValue = ((Activity) context).getIntent().getStringExtra(GlobalConstant.TIME_VALUE);
        baseView.upTimePeriod(startTime,endTime);

        //获取已设置的重复
        loopType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.TIME_LOOPTYPE);
        loopValue = ((Activity) context).getIntent().getStringExtra(GlobalConstant.TIME_LOOPVALUE);
        baseView.upLoop(loopType,loopValue);

        //根据需求隐藏控件
        setType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.SET_TIMEVALUE_OR_TIMEPERIOD);
        if (GlobalConstant.SET_TIMEVALUE.equals(setType)){
            baseView.hideTimePeriod();

        }else {
            baseView.hideTime();

        }
    }

    /**
     * 跳转设置时间段
     */
    public void selectTime() {
        Intent intent = new Intent(context, PeriodCommentSetActivity.class);
        intent.putExtra(GlobalConstant.TIME_START, startTime);
        intent.putExtra(GlobalConstant.TIME_END,endTime);
        ActivityUtils.startActivityForResult((Activity) context, intent, GlobalConstant.REQUEST_CODE_SELECT_TIME, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /**
     * 弹框设置时间
     */
    public void showTimePickView() {
        try {
            PickViewUtils.showTimePickView((Activity) context, timeValue, (date, v) -> {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", context.getResources().getConfiguration().locale);
                timeValue = sdf.format(date);
                baseView.upTimeValue(timeValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转重复设置
     */
    public void selectRepeat() {
        Intent intent = new Intent(context, RepeatActivity.class);
        intent.putExtra(GlobalConstant.TIME_LOOPTYPE, loopType);
        intent.putExtra(GlobalConstant.TIME_LOOPVALUE,loopValue);
        ActivityUtils.startActivityForResult((Activity) context, intent, GlobalConstant.REQUEST_CODE_SELECT_REPEAT, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode==RESULT_OK){
            if (requestCode== GlobalConstant.REQUEST_CODE_SELECT_TIME){
                startTime = data.getStringExtra(GlobalConstant.TIME_START);
                endTime = data.getStringExtra(GlobalConstant.TIME_END);
                baseView.upTimePeriod(startTime,endTime);
            }
            if (requestCode==GlobalConstant.REQUEST_CODE_SELECT_REPEAT){
                loopType = data.getStringExtra(GlobalConstant.TIME_LOOPTYPE);
                loopValue = data.getStringExtra(GlobalConstant.TIME_LOOPVALUE);
                baseView.upLoop(loopType,loopValue);
            }

        }
    }


    public void selectResult(){
        Intent intent=new Intent();
        if (TextUtils.isEmpty(loopType)){
            MyToastUtils.toast(R.string.m254_incomplete_settings);
            return;
        }
        if (GlobalConstant.SET_TIMEVALUE.equals(setType)){
            if (TextUtils.isEmpty(timeValue)){
                MyToastUtils.toast(R.string.m254_incomplete_settings);
                return;
            }

        }else {
            if (TextUtils.isEmpty(startTime)||TextUtils.isEmpty(endTime)){
                MyToastUtils.toast(R.string.m254_incomplete_settings);
                return;
            }
        }
        intent.putExtra(GlobalConstant.TIME_START,startTime);
        intent.putExtra(GlobalConstant.TIME_END,endTime);
        intent.putExtra(GlobalConstant.TIME_VALUE,timeValue);
        intent.putExtra(GlobalConstant.TIME_LOOPTYPE,loopType);
        intent.putExtra(GlobalConstant.TIME_LOOPVALUE,loopValue);
        ((Activity)context).setResult(Activity.RESULT_OK,intent);
        ((Activity)context).finish();
    }


}
