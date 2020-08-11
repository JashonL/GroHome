package com.growatt.grohome.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class BulbSceneColourBean implements MultiItemEntity {

    private int colour;
    private boolean isSelected;
    private boolean isColour;//彩色的
    private float[] hsv;//彩色
    private int itemType;

    private float[]whiteHsv;//白光
    private int whiteColor;//彩色的

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isColour() {
        return isColour;
    }

    public void setIsColour(boolean colour) {
        isColour = colour;
    }

    public float[] getHsv() {
        return hsv;
    }

    public void setHsv(float[] hsv) {
        this.hsv = hsv;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public float[] getWhiteHsv() {
        return whiteHsv;
    }

    public void setWhiteHsv(float[] whiteHsv) {
        this.whiteHsv = whiteHsv;
    }

    public int getWhiteColor() {
        return whiteColor;
    }

    public void setWhiteColor(int whiteColor) {
        this.whiteColor = whiteColor;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
