package com.growatt.grohome.module.home.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.HomeDeviceBean;

public interface IGrohomeView extends BaseView {
        void getAllDeviceSuccess(HomeDeviceBean bean);
}
