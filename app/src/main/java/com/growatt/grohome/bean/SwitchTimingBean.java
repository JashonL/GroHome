package com.growatt.grohome.bean;

public class SwitchTimingBean {

    private String loopType;
    private String loopValue;
    private String timeValue;//执行时间
    private String cKey;
    private String cValue;
    private String road;
    private String name;
    private String status;

    public SwitchTimingBean() {
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

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
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

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
