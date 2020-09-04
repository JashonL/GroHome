package com.growatt.grohome.bean;

import java.util.List;

public class SwitchDpBean {
    private String countdown;
    private String countdown_scale;
    private List<String> switchDpIds;

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }

    public List<String> getSwitchDpIds() {
        return switchDpIds;
    }

    public void setSwitchDpIds(List<String> switchDpIds) {
        this.switchDpIds = switchDpIds;
    }

    public String getCountdown_scale() {
        return countdown_scale;
    }

    public void setCountdown_scale(String countdown_scale) {
        this.countdown_scale = countdown_scale;
    }
}
