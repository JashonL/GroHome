package com.growatt.grohome.bean;

public class DeviceTypeBean {
    private String type;
    private String name;
    private boolean isWiFi;
    private boolean isBluethooth;
    private String configType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWiFi() {
        return isWiFi;
    }

    public void setWiFi(boolean wiFi) {
        isWiFi = wiFi;
    }

    public boolean isBluethooth() {
        return isBluethooth;
    }

    public void setBluethooth(boolean bluethooth) {
        isBluethooth = bluethooth;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }
}
