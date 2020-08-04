package com.growatt.grohome.bean;

import java.util.List;

public class SceneTaskBean {
    private String devId;
    private String devType;
    private double linkValue;
    private String linkType;
    private String devName;
    private int order;
    private String road;
    private String subSwitchName;
    private String minTime;
    private String maxTime;
    private String connectorId;
    private int connectors;
    private String url;
    private List<String> switchNameList;
    private List<String> gunNameList;

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public double getLinkValue() {
        return linkValue;
    }

    public void setLinkValue(double linkValue) {
        this.linkValue = linkValue;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getSubSwitchName() {
        return subSwitchName;
    }

    public void setSubSwitchName(String subSwitchName) {
        this.subSwitchName = subSwitchName;
    }

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    public String getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId;
    }

    public int getConnectors() {
        return connectors;
    }

    public void setConnectors(int connectors) {
        this.connectors = connectors;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getSwitchNameList() {
        return switchNameList;
    }

    public void setSwitchNameList(List<String> switchNameList) {
        this.switchNameList = switchNameList;
    }

    public List<String> getGunNameList() {
        return gunNameList;
    }

    public void setGunNameList(List<String> gunNameList) {
        this.gunNameList = gunNameList;
    }
}
