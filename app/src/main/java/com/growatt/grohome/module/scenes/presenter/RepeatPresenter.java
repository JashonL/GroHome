package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.scenes.view.IRepeatActivityView;

import java.util.Map;

public class RepeatPresenter extends BasePresenter<IRepeatActivityView> {

    private String loopType;//每天，单次
    private String loopValue;//用来判断周一到周五

    public RepeatPresenter(IRepeatActivityView baseView) {
        super(baseView);
    }

    public RepeatPresenter(Context context, IRepeatActivityView baseView) {
        super(context, baseView);
    }


    public void getAlreadySet(){
        //获取已设置的重复
        loopType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.TIME_LOOPTYPE);
        loopValue = ((Activity) context).getIntent().getStringExtra(GlobalConstant.TIME_LOOPVALUE);
        baseView.upLoop(loopType,loopValue);
    }


    public String getLoopType() {
        return loopType;
    }

    public String getLoopValue() {
        return loopValue;
    }

    public void selectResult(){
        Map<String, String> loop = baseView.getLoop();
        loopType = loop.get(GlobalConstant.TIME_LOOPTYPE);
        loopValue = loop.get(GlobalConstant.TIME_LOOPVALUE);
        Intent intent=new Intent();
        intent.putExtra(GlobalConstant.TIME_LOOPTYPE,loopType);
        intent.putExtra(GlobalConstant.TIME_LOOPVALUE,loopValue);
        ((Activity)context).setResult(Activity.RESULT_OK,intent);
        ((Activity)context).finish();
    }

}
