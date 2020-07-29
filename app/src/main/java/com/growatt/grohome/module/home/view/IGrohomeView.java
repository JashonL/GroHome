package com.growatt.grohome.module.home.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.HomeDeviceBean;

import java.util.List;

public interface IGrohomeView extends BaseView {
        void getAllDeviceSuccess(HomeDeviceBean bean);

        List<HomeDeviceBean.DataBean> getDeviceList();

        void upDataStatus(String devId, String value);
}
