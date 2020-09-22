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

    void onError(String errorMsg);

    void setSwitchRoad(List<ScenesRoadBean> beanList);

    void setSocketUi(String linkType);

    List<ScenesRoadBean> getData();

    boolean getSwitchChecked();

    boolean getModeChecked();

    boolean getBrightChecked();

    boolean getTempChecked();

    boolean getTimeChecked();


    String getModeValue();

    String getBrightValue();

    String getTempValue();

    String getTimeValue();


    void selectedMode(String mode);

    void setBright(String bright);

    void setTemp(String temp);

    void setCountDown(String countDown);
}
