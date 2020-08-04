package com.growatt.grohome.module.scenes.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.SceneTaskBean;

import java.util.List;

public interface ISceneAddView extends BaseView {
    void setViewBySceneType(String type);
    String getName();
    void setSceneName(String name);
    String getSceneName();
    void deleteTaskDevice(int position);
    List<SceneTaskBean> getData();
    void addSceneResult(String msg);
}
