package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.scenes.view.IPeriodCommentSetView;

import org.greenrobot.eventbus.EventBus;

public class PeriodCommentSetPresenter extends BasePresenter<IPeriodCommentSetView> {
    public PeriodCommentSetPresenter(IPeriodCommentSetView baseView) {
        super(baseView);
    }

    public PeriodCommentSetPresenter(Context context, IPeriodCommentSetView baseView) {
        super(context, baseView);
    }


    public void getAlreadySet(){
        String startime = ((Activity) context).getIntent().getStringExtra(GlobalConstant.TIME_START);
        String endtime = ((Activity) context).getIntent().getStringExtra(GlobalConstant.TIME_END);
        baseView.upTime(startime,endtime);
    }



    public void selectResult(){
        String startTime = baseView.getStartTime();
        String endTime = baseView.getEndTime();
        Intent intent=new Intent();
        intent.putExtra(GlobalConstant.TIME_START,startTime);
        intent.putExtra(GlobalConstant.TIME_END,endTime);
        ((Activity)context).setResult(Activity.RESULT_OK,intent);
        ((Activity)context).finish();
    }




}
