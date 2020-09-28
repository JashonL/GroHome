package com.growatt.grohome.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class LogsSceneBean implements MultiItemEntity {

    private int itemType;

    private String title;
    private String cname;
    private String runStatus;
    private String runTime;
    private String runType;
    private String dataType;


    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }


    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
