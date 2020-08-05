package com.growatt.grohome.module.scenes.view;

import com.growatt.grohome.base.BaseView;

public interface IPeriodCommentSetView extends BaseView {

    void upTime(String startTime,String endTime);

    String getStartTime();

    String getEndTime();
}
