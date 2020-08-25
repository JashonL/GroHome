package com.growatt.grohome.module.device.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.SwitchTimingBean;

import java.util.List;

public interface ISwitchTimingView extends BaseView {

    void initViews(String type);

    void showViews(String action);

    void setDeviceName(String deviceName);

    void upData(List<SwitchTimingBean> newList);

    void setAllSelect(boolean allSelect);

    void allSwitchOpen();

    void allSwitchClose();

    List<SwitchTimingBean> getData();

    void  setAllOpen();

    void updataAdapter();

    void onError(String onError);
}
