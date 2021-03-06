package com.growatt.grohome.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class GroDeviceBean implements MultiItemEntity {

    //socket
    private String devId;
    private String devType;
    private float current;
    private String name;
    private int deviceOnline;
    private String power;
    private int roomId;
    private String roomName;
    private int deviceOnoff;
    private float voltage;
    private String dayEle;
    private boolean isDeviceConfig;

    //thermostat
    private String roomTemp;
    private String setTemp;
    private float floorTempComp;
    private float roomTempComp;
    private float floorTemp;
    private float ele;
    private int used;

    //switch
    private int road;

    //ammeter
    private double totalEle;
    private double curr_power;
    private boolean isExistNetwork;
    private String sensor;
    private String currentNew;
    private String monthEle;
    private String time;

    //chager
    private String prefix;
    private String host;

    //shineBoost
    private String temperature;

    private String isConfHave;

    private SchemaDpdBean  dpd;

    public GroDeviceBean() {
    }

    public GroDeviceBean(String devId) {
        this.devId = devId;
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

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOnline() {
        return deviceOnline;
    }

    public void setOnline(int online) {
        this.deviceOnline = online;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getOnoff() {
        return deviceOnoff;
    }

    public void setOnoff(int onoff) {
        this.deviceOnoff = onoff;
    }

    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }


    public String getRoomTemp() {
        return roomTemp;
    }

    public void setRoomTemp(String roomTemp) {
        this.roomTemp = roomTemp;
    }

    public String getSetTemp() {
        return setTemp;
    }

    public void setSetTemp(String setTemp) {
        this.setTemp = setTemp;
    }

    public float getFloorTempComp() {
        return floorTempComp;
    }

    public void setFloorTempComp(float floorTempComp) {
        this.floorTempComp = floorTempComp;
    }

    public float getRoomTempComp() {
        return roomTempComp;
    }

    public void setRoomTempComp(float roomTempComp) {
        this.roomTempComp = roomTempComp;
    }

    public float getFloorTemp() {
        return floorTemp;
    }

    public void setFloorTemp(float floorTemp) {
        this.floorTemp = floorTemp;
    }

    public float getEle() {
        return ele;
    }

    public void setEle(float ele) {
        this.ele = ele;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }


    public int getRoad() {
        return road;
    }

    public void setRoad(int road) {
        this.road = road;
    }

    public double getTotalEle() {
        return totalEle;
    }

    public void setTotalEle(double totalEle) {
        this.totalEle = totalEle;
    }


    public double getCurr_power() {
        return curr_power;
    }

    public void setCurr_power(double curr_power) {
        this.curr_power = curr_power;
    }

    public boolean isExistNetwork() {
        return isExistNetwork;
    }

    public void setExistNetwork(boolean existNetwork) {
        isExistNetwork = existNetwork;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getCurrentNew() {
        return currentNew;
    }

    public void setCurrentNew(String currentNew) {
        this.currentNew = currentNew;
    }

    public String getDayEle() {
        return dayEle;
    }

    public void setDayEle(String dayEle) {
        this.dayEle = dayEle;
    }


    public String getMonthEle() {
        return monthEle;
    }

    public void setMonthEle(String monthEle) {
        this.monthEle = monthEle;
    }

    public SchemaDpdBean getDpd() {
        return dpd;
    }

    public void setDpd(SchemaDpdBean dpd) {
        this.dpd = dpd;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }


    public String getIsConfHave() {
        return isConfHave;
    }

    public void setIsConfHave(String isConfHave) {
        this.isConfHave = isConfHave;
    }

    public boolean isDeviceConfig() {
        return isDeviceConfig;
    }

    public void setDeviceConfig(boolean deviceConfig) {
        isDeviceConfig = deviceConfig;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GroDeviceBean)) throw new ClassCastException("类型错误");
        GroDeviceBean p = (GroDeviceBean) obj;
        return this.devId.equals(p.devId);
    }

    @Override
    public int getItemType() {
        return deviceOnoff;
    }
}
