package com.growatt.grohome.bean;

import java.util.List;

public class BulbInfo {
    private List<ColourBean> mode;

    public List<ColourBean> getMode() {
        return mode;
    }

    public void setMode(List<ColourBean> mode) {
        this.mode = mode;
    }

    public static class ColourBean {
        private String color;
        private String name;
        private String numb;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumb() {
            return numb;
        }

        public void setNumb(String numb) {
            this.numb = numb;
        }
    }

}
