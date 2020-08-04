package com.growatt.grohome.module.scenes.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.bean.ScenesRoadBean;

import java.util.List;

public interface ISceneTaskSettingView extends BaseView {
    void setViewsByDevice(GroDeviceBean bean);

    void setViewsByTask(SceneTaskBean bean);

    void setSwitchRoad(List<ScenesRoadBean> beanList);

    List<ScenesRoadBean> getData();

    void setSceneName(String sceneName);

    void freshStop();

    void setSocketUi(String linkType);

}
