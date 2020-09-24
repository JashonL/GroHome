package com.growatt.grohome.module.device.manager;

import com.growatt.grohome.bean.GroDeviceBean;

import java.util.List;

public class DeviceManager {

    private static volatile DeviceManager instance;

    private List<GroDeviceBean> deviceBeans;

    private DeviceManager() {
    }

    public static DeviceManager getInstance() {
        if (null == instance) {
            synchronized (DeviceManager.class) {
                if (null == instance) {
                    instance = new DeviceManager();
                }
            }
        }
        return instance;
    }


    public void setDeviceBeans(List<GroDeviceBean> deviceBeans) {
        if (null == deviceBeans) {
            return;
        }
        this.deviceBeans=deviceBeans;
    }




    public List<GroDeviceBean> getDeviceBeans() {
        return deviceBeans;
    }

}
