package com.growatt.grohome.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class BulbSceneColourBean implements MultiItemEntity {

    private int colour;
    private boolean isSelected;
    private boolean isColour;
    private float[] hsv;
    private int itemType;

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

    public void setColour(boolean colour) {
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

    @Override
    public int getItemType() {
        return itemType;
    }
}
