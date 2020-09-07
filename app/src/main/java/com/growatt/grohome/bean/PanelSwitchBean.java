package com.growatt.grohome.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/1/3.
 */

public class PanelSwitchBean implements Parcelable {
    private int switchNum;
    private String devId;
    private int online;
    private int onoff;
    private String name;
    private int road;
    private List<SwichBean> beanList;

    private SchemaDpdBean  dpd;

    public PanelSwitchBean() {
    }



    public static class SwichBean {
        private int onOff;
        private String name;
        private String customName;
        private int id;
        private boolean scenesConditionEnable;
        private String switchId;

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

        public String getCustomName() {
            return customName;
        }

        public void setCustomName(String customName) {
            this.customName = customName;
        }

        public boolean isScenesConditionEnable() {
            return scenesConditionEnable;
        }

        public void setScenesConditionEnable(boolean scenesConditionEnable) {
            this.scenesConditionEnable = scenesConditionEnable;
        }

        public String getSwitchId() {
            return switchId;
        }

        public void setSwitchId(String switchId) {
            this.switchId = switchId;
        }
    }


    protected PanelSwitchBean(Parcel in) {
        switchNum = in.readInt();
        devId = in.readString();
        online = in.readInt();
        onoff = in.readInt();
        name = in.readString();
        road = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(switchNum);
        dest.writeString(devId);
        dest.writeInt(online);
        dest.writeInt(onoff);
        dest.writeString(name);
        dest.writeInt(road);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PanelSwitchBean> CREATOR = new Creator<PanelSwitchBean>() {
        @Override
        public PanelSwitchBean createFromParcel(Parcel in) {
            return new PanelSwitchBean(in);
        }

        @Override
        public PanelSwitchBean[] newArray(int size) {
            return new PanelSwitchBean[size];
        }
    };

    public int getSwitchNum() {
        return switchNum;
    }

    public void setSwitchNum(int switchNum) {
        this.switchNum = switchNum;
    }



    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOnoff() {
        return onoff;
    }

    public void setOnoff(int onoff) {
        this.onoff = onoff;
    }

    public List<SwichBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<SwichBean> beanList) {
        this.beanList = beanList;
    }

    public void addSwitchBean(SwichBean bean){
        if (beanList==null)beanList=new ArrayList<>();
        beanList.add(bean);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoad() {
        return road;
    }

    public void setRoad(int road) {
        this.road = road;
    }

    public SchemaDpdBean getDpd() {
        return dpd;
    }

    public void setDpd(SchemaDpdBean dpd) {
        this.dpd = dpd;
    }
}
