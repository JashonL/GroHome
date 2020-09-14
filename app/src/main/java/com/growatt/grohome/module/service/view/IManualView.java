package com.growatt.grohome.module.service.view;

import com.growatt.grohome.base.BaseView;

import java.util.List;

public interface IManualView extends BaseView {

    void showManual(List<String> imageUrls);

    void error(String msg);

}
