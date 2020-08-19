package com.growatt.grohome.module.scenes.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.SceneConditionBean;
import com.growatt.grohome.bean.ScenesRoadBean;

import java.util.List;

public interface ISceneConditionView extends BaseView {

    void setSceneName(String sceneName);

    void setViewsByDevice(GroDeviceBean bean);

    void setViewsByTask(SceneConditionBean bean);

    void freshStop();

    void setSwitchRoad(List<ScenesRoadBean> beanList);

    void setSocketUi(String linkType);

    List<ScenesRoadBean> getData();

    boolean getSwitchChecked();


    boolean getTempChecked();
}
