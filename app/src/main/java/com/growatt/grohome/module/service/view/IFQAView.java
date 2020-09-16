package com.growatt.grohome.module.service.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.FqaBean;

import java.util.List;

public interface IFQAView extends BaseView {

    void onError(String onError);

    void updata(List<FqaBean> list);

}
