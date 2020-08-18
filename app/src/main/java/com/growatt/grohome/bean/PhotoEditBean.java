package com.growatt.grohome.bean;

/**
 * Created by Administrator on 2019/9/17.
 */

public class PhotoEditBean {
    private String path;

    public PhotoEditBean(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
