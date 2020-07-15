package com.growatt.grohome.eventbus;

/**
 * 家庭能管设备添加或删除通用通知
 * Created by Administrator on 2019/5/15.
 */

public class DeviceAddOrDelMsg {
    public static final int DELETE_DEV=1;
    public static final int ADD_DEV=2;
    private String devId;
    private String devType;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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
}
