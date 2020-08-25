package com.growatt.grohome.module.login.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.CountryBean;

import java.util.List;

public interface ICountryListView extends BaseView {

    void updataList(List<CountryBean> newList);

    void onError(String error);
}
