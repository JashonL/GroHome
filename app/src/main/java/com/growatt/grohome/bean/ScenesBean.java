package com.growatt.grohome.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2019/1/15.
 */

public class ScenesBean {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean  implements MultiItemEntity{
        private int itemType;

        private String loopType;
        private String loopValue;
        private String name;
        private long ctime;
        private int actNum;
        private String timeValue;
        private String userId;
        private String cid;
        private String status;
        private int onoff;
        private String icon;
        private String total;
        private String satisfy;
        private String isCondition;

        private boolean isTiming;

        private List<SceneTaskBean> conf;
        private List<SceneConditionBean> conditionconf;

        public DataBean() {
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getOnoff() {
            return onoff;
        }

        public void setOnoff(int onoff) {
            this.onoff = onoff;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }


        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public List<SceneTaskBean> getConf() {
            return conf;
        }

        public void setConf(List<SceneTaskBean> conf) {
            this.conf = conf;
        }

        public boolean isTimging() {
            return isTiming;
        }

        public void setTimging(boolean timging) {
            isTiming = timging;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public boolean isTiming() {
            return isTiming;
        }

        public void setTiming(boolean timing) {
            isTiming = timing;
        }

        public String getSatisfy() {
            return satisfy;
        }

        public void setSatisfy(String satisfy) {
            this.satisfy = satisfy;
        }

        public String getIsCondition() {
            return isCondition;
        }

        public void setIsCondition(String isCondition) {
            this.isCondition = isCondition;
        }

        public List<SceneConditionBean> getCondition() {
            return conditionconf;
        }

        public void setCondition(List<SceneConditionBean> condition) {
            this.conditionconf = condition;
        }



        @Override
        public int getItemType() {
            return itemType;
        }
    }
}
