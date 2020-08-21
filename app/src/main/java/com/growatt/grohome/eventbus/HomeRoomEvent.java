package com.growatt.grohome.eventbus;

/**
 * Created：2018/10/17 on 9:54
 * Author:gaideng on dg
 * Description:房间相关Eventbus
 */

public class HomeRoomEvent {
    private String name;

    private int roomId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
