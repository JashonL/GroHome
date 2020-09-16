package com.growatt.grohome.module.service.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.ManualBean;

import java.util.List;

public interface IManualListView extends BaseView {

    void updata(List<ManualBean>list);

    void onError(String onError);
}
