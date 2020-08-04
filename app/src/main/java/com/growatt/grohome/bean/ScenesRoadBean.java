package com.growatt.grohome.bean;

/**
 * Created by Administrator on 2020/4/8.
 */

public class ScenesRoadBean {

    private int onOff;
    private String name;
    private int id;
    private boolean scenesConditionEnable;
    private String title;
    private String statusOn;
    private String statusOff;

    public int getOnOff() {
        return onOff;
    }

    public void setOnOff(int onOff) {
        this.onOff = onOff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isScenesConditionEnable() {
        return scenesConditionEnable;
    }

    public void setScenesConditionEnable(boolean scenesConditionEnable) {
        this.scenesConditionEnable = scenesConditionEnable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatusOn() {
        return statusOn;
    }

    public void setStatusOn(String statusOn) {
        this.statusOn = statusOn;
    }

    public String getStatusOff() {
        return statusOff;
    }

    public void setStatusOff(String statusOff) {
        this.statusOff = statusOff;
    }
}
