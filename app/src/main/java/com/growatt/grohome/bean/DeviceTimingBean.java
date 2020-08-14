package com.growatt.grohome.bean;

import java.util.List;

public class DeviceTimingBean {


    private String devId;
    private String devType;
    private String timeValue;
    private String userId;
    private String loopType;
    private String loopValue;
    private String cKey;
    private String road;
    private String cValue;
    private long ctime;
    private int actNum;
    private String cid;
    private int status;

    private String name;


    private List<SwitchTimingBean> conf;

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

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoopType() {
        return loopType;
    }

    public void setLoopType(String loopType) {
        this.loopType = loopType;
    }

    public String getLoopValue() {
        return loopValue;
    }

    public void setLoopValue(String loopValue) {
        this.loopValue = loopValue;
    }

    public String getCKey() {
        return cKey;
    }

    public void setCKey(String cKey) {
        this.cKey = cKey;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getCValue() {
        return cValue;
    }

    public void setCValue(String cValue) {
        this.cValue = cValue;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public int getActNum() {
        return actNum;
    }

    public void setActNum(int actNum) {
        this.actNum = actNum;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getcKey() {
        return cKey;
    }

    public void setcKey(String cKey) {
        this.cKey = cKey;
    }

    public String getcValue() {
        return cValue;
    }

    public void setcValue(String cValue) {
        this.cValue = cValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SwitchTimingBean> getConf() {
        return conf;
    }

    public void setConf(List<SwitchTimingBean> conf) {
        this.conf = conf;
    }
}
