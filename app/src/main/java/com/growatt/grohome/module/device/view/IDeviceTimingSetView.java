package com.growatt.grohome.module.device.view;

import com.growatt.grohome.base.BaseView;

public interface IDeviceTimingSetView extends BaseView {
    void showViews(String action);

    void initViews(String type);

    void setOnoffViews(String onoff);

    void setValue(String cValue);

    void setRepeate(String repeate);

    void setTimeValue(String timeValue);

    String getViewsTimeValue();

    String getViewsRepeat();

    String getViewsonffValue();

}
