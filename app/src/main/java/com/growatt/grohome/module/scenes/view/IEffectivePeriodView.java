package com.growatt.grohome.module.scenes.view;

import com.growatt.grohome.base.BaseView;

public interface IEffectivePeriodView extends BaseView {
    void upTimePeriod(String startTime,String endTime);

    void upTimeValue(String timeValue);

    void upLoop(String looptype,String loopValue);

    void hideTime();

    void hideTimePeriod();


}
