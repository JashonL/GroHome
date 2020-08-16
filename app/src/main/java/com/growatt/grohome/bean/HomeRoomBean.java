package com.growatt.grohome.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created：2018/10/16 on 9:27
 * Author:gaideng on dg
 * Description:智慧家庭
 */

public class HomeRoomBean implements MultiItemEntity {

    private int itemType;

    //是否选中
    private boolean isSelect;
    //房间id
    private int cid;
    //房间名
    private String name;
    //房间图片id
    private String images;
    //房间图片
    private String cdn;
    //房间设备
    private List<GroDeviceBean>devList;


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getCdn() {
        return cdn;
    }

    public void setCdn(String cdn) {
        this.cdn = cdn;
    }

    public List<GroDeviceBean> getDevList() {
        return devList;
    }

    public void setDevList(List<GroDeviceBean> devList) {
        this.devList = devList;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
