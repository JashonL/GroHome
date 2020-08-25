package com.growatt.grohome.module.service.view;


import com.growatt.grohome.base.BaseView;

import java.util.List;

public interface IServiceFragmentView extends BaseView {

    void setBannerList(List<String> bannerList);

    void onError(String onError);

}
