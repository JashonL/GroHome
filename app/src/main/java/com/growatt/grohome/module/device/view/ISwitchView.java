package com.growatt.grohome.module.device.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.PanelSwitchBean;

import java.util.List;

public interface ISwitchView extends BaseView {

    void onError(String onError);

    void setTitle(String name);

    void freshData(List<PanelSwitchBean.SwichBean> beanList);

    void freshStatus(int switchId,boolean onOff);
}
