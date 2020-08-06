package com.growatt.grohome.module.scenes.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.SceneConditionBean;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.bean.ScenesBean;

import java.util.List;

public interface ISceneDetailView extends BaseView {

    void setViewBySceneType(String type);

    void updataViews(ScenesBean.DataBean dataBean);

    String getName();

    void setSceneName(String name);

    String getSceneName();

    void deleteTaskDevice(int position);

    void deleteCondition(int position);

    List<SceneConditionBean> getConditionBean();

    List<SceneTaskBean> getData();

    void addSceneResult(String msg);

    void setConditionMet(int satisfy);
}
