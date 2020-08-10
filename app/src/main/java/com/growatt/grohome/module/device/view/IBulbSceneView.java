package com.growatt.grohome.module.device.view;

import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.BulbSceneColourBean;

import java.util.List;

public interface IBulbSceneView extends BaseView {

    void setViewById(int id);

    void setSceneName(String name);

    void setMode(int mode);

    void setSpeed(int speed);

    void setColous(List<BulbSceneColourBean> colourBeans);

    void updataSelected();

}
