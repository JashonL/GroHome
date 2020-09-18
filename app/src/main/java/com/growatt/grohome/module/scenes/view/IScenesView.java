package com.growatt.grohome.module.scenes.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.LogsSceneBean;
import com.growatt.grohome.bean.ScenesBean;

import java.util.List;

public interface IScenesView extends BaseView {
    void upDataLaunch(List<ScenesBean.DataBean> Data);

    void upDataSmart(List<ScenesBean.DataBean> Data);

    void launchTapToRunSuccess(ScenesBean.DataBean dataBean);

    void updataSuccess(int position,ScenesBean.DataBean dataBean);

    void updataLogs(List<LogsSceneBean>list);

    void onError(String onError);
}
