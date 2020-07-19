package com.growatt.grohome.bean;

/**
 * Created by Administrator on 2019/12/14.
 */

public class SchemaDpdBean {

    /**
     * 插座
     * current : 2
     * power : 4
     * onoff : 1
     * voltage : 3
     */
    private String current;
    private String power;
    private String onoff;
    private String voltage;
    private String current_scale;
    private String voltage_scale;
    private String power_scale;


    /**
     * 温控器
     */
    private String power1;//开关
    private String settemp;//设置温度
    private String controlmode;//控制模式
    private String sensorerror;//传感器故障
    private String roomtemper;//room当前温度
    private String floortemper;//floor当前温度
    private String temperunitchange;//温标切换
    private String lock;//童锁
    private String timeholiday;//假日模式时间设置
    private String event_adaptive_set;//周程序编程模式选择/自适应开关设置
    private String semchange;//传感器切换
    private String roomtempercomp;//room当前温度补偿
    private String floortempercomp;//floor当前温度补偿
    private String temperdiff;//温度回差
    private String reset;//恢复出厂设置
    private String powerset;//功率设置
    private String heatertime;//加热时间
    private String work_status;//工作状态
    private String week_program;//周程序
    private String floorlimit;//地板温度限制
    private String inquire;//查询
    private String settemp_scale;
    private String roomtemper_scale;
    private String floortemper_scale;
    private String timeholiday_scale;
    private String event_adaptive_set_scale;
    private String roomtempercomp_scale;
    private String floortempercomp_scale;
    private String powerset_scale;
    private String heatertime_scale;
    private String floorlimit_scale;

    /**
     *面板开关
     */

    private String switch_1;
    private String switch_2;
    private String switch_3;
    private String switch_4;
    private String switch_5;




    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getOnoff() {
        return onoff;
    }

    public void setOnoff(String onoff) {
        this.onoff = onoff;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getCurrent_scale() {
        return current_scale;
    }

    public void setCurrent_scale(String current_scale) {
        this.current_scale = current_scale;
    }

    public String getVoltage_scale() {
        return voltage_scale;
    }

    public void setVoltage_scale(String voltage_scale) {
        this.voltage_scale = voltage_scale;
    }

    public String getPower_scale() {
        return power_scale;
    }

    public void setPower_scale(String power_scale) {
        this.power_scale = power_scale;
    }


    public String getPower1() {
        return power1;
    }

    public void setPower1(String power1) {
        this.power1 = power1;
    }

    public String getSettemp() {
        return settemp;
    }

    public void setSettemp(String settemp) {
        this.settemp = settemp;
    }

    public String getControlmode() {
        return controlmode;
    }

    public void setControlmode(String controlmode) {
        this.controlmode = controlmode;
    }

    public String getSensorerror() {
        return sensorerror;
    }

    public void setSensorerror(String sensorerror) {
        this.sensorerror = sensorerror;
    }

    public String getRoomtemper() {
        return roomtemper;
    }

    public void setRoomtemper(String roomtemper) {
        this.roomtemper = roomtemper;
    }

    public String getFloortemper() {
        return floortemper;
    }

    public void setFloortemper(String floortemper) {
        this.floortemper = floortemper;
    }

    public String getTemperunitchange() {
        return temperunitchange;
    }

    public void setTemperunitchange(String temperunitchange) {
        this.temperunitchange = temperunitchange;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getTimeholiday() {
        return timeholiday;
    }

    public void setTimeholiday(String timeholiday) {
        this.timeholiday = timeholiday;
    }

    public String getEvent_adaptive_set() {
        return event_adaptive_set;
    }

    public void setEvent_adaptive_set(String event_adaptive_set) {
        this.event_adaptive_set = event_adaptive_set;
    }

    public String getSemchange() {
        return semchange;
    }

    public void setSemchange(String semchange) {
        this.semchange = semchange;
    }

    public String getRoomtempercomp() {
        return roomtempercomp;
    }

    public void setRoomtempercomp(String roomtempercomp) {
        this.roomtempercomp = roomtempercomp;
    }

    public String getFloortempercomp() {
        return floortempercomp;
    }

    public void setFloortempercomp(String floortempercomp) {
        this.floortempercomp = floortempercomp;
    }

    public String getTemperdiff() {
        return temperdiff;
    }

    public void setTemperdiff(String temperdiff) {
        this.temperdiff = temperdiff;
    }

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public String getPowerset() {
        return powerset;
    }

    public void setPowerset(String powerset) {
        this.powerset = powerset;
    }

    public String getHeatertime() {
        return heatertime;
    }

    public void setHeatertime(String heatertime) {
        this.heatertime = heatertime;
    }

    public String getWork_status() {
        return work_status;
    }

    public void setWork_status(String work_status) {
        this.work_status = work_status;
    }

    public String getWeek_program() {
        return week_program;
    }

    public void setWeek_program(String week_program) {
        this.week_program = week_program;
    }

    public String getFloorlimit() {
        return floorlimit;
    }

    public void setFloorlimit(String floorlimit) {
        this.floorlimit = floorlimit;
    }

    public String getInquire() {
        return inquire;
    }

    public void setInquire(String inquire) {
        this.inquire = inquire;
    }


    public String getSettemp_scale() {
        return settemp_scale;
    }

    public void setSettemp_scale(String settemp_scale) {
        this.settemp_scale = settemp_scale;
    }

    public String getRoomtemper_scale() {
        return roomtemper_scale;
    }

    public void setRoomtemper_scale(String roomtemper_scale) {
        this.roomtemper_scale = roomtemper_scale;
    }

    public String getFloortemper_scale() {
        return floortemper_scale;
    }

    public void setFloortemper_scale(String floortemper_scale) {
        this.floortemper_scale = floortemper_scale;
    }

    public String getTimeholiday_scale() {
        return timeholiday_scale;
    }

    public void setTimeholiday_scale(String timeholiday_scale) {
        this.timeholiday_scale = timeholiday_scale;
    }

    public String getEvent_adaptive_set_scale() {
        return event_adaptive_set_scale;
    }

    public void setEvent_adaptive_set_scale(String event_adaptive_set_scale) {
        this.event_adaptive_set_scale = event_adaptive_set_scale;
    }

    public String getRoomtempercomp_scale() {
        return roomtempercomp_scale;
    }

    public void setRoomtempercomp_scale(String roomtempercomp_scale) {
        this.roomtempercomp_scale = roomtempercomp_scale;
    }

    public String getFloortempercomp_scale() {
        return floortempercomp_scale;
    }

    public void setFloortempercomp_scale(String floortempercomp_scale) {
        this.floortempercomp_scale = floortempercomp_scale;
    }

    public String getPowerset_scale() {
        return powerset_scale;
    }

    public void setPowerset_scale(String powerset_scale) {
        this.powerset_scale = powerset_scale;
    }

    public String getHeatertime_scale() {
        return heatertime_scale;
    }

    public void setHeatertime_scale(String heatertime_scale) {
        this.heatertime_scale = heatertime_scale;
    }

    public String getFloorlimit_scale() {
        return floorlimit_scale;
    }

    public void setFloorlimit_scale(String floorlimit_scale) {
        this.floorlimit_scale = floorlimit_scale;
    }


    public String getSwitch_1() {
        return switch_1;
    }

    public void setSwitch_1(String switch_1) {
        this.switch_1 = switch_1;
    }

    public String getSwitch_2() {
        return switch_2;
    }

    public void setSwitch_2(String switch_2) {
        this.switch_2 = switch_2;
    }

    public String getSwitch_3() {
        return switch_3;
    }

    public void setSwitch_3(String switch_3) {
        this.switch_3 = switch_3;
    }

    public String getSwitch_4() {
        return switch_4;
    }

    public void setSwitch_4(String switch_4) {
        this.switch_4 = switch_4;
    }

    public String getSwitch_5() {
        return switch_5;
    }

    public void setSwitch_5(String switch_5) {
        this.switch_5 = switch_5;
    }
}
