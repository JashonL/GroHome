package com.growatt.grohome.module.config.view;

import com.growatt.grohome.base.BaseView;

public interface IWiFiOptionsView extends BaseView {

    void setWifiSsid(String wifiSsid);

    String getWifissid();

    String getWifiPassWord();
}
