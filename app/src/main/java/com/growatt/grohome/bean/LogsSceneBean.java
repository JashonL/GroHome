package com.growatt.grohome.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class LogsSceneBean implements MultiItemEntity {

    private int itemType;

    private String  title;
    private String  cname;
    private String  runStatus;
    private String  runTime;
    private int   index;


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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
