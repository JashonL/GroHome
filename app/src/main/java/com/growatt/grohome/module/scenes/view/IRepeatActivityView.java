package com.growatt.grohome.module.scenes.view;

import com.growatt.grohome.base.BaseView;

import java.util.Map;

public interface IRepeatActivityView extends BaseView {
    void upLoop(String loopType,String loopValue);

    Map<String,String> getLoop();

}
