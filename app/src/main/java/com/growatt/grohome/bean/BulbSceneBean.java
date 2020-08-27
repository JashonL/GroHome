package com.growatt.grohome.bean;

public class BulbSceneBean {
    private String name;
    private boolean selected;
    private String id;
    private String sceneValue;
    private String musicOnoff;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSceneValue() {
        return sceneValue;
    }

    public void setSceneValue(String sceneValue) {
        this.sceneValue = sceneValue;
    }

    public String getMusicOnoff() {
        return musicOnoff;
    }

    public void setMusicOnoff(String musicOnoff) {
        this.musicOnoff = musicOnoff;
    }
}
