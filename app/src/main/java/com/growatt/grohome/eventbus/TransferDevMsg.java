package com.growatt.grohome.eventbus;

/**
 * 转移设备通知
 * Created by Administrator on 2019/5/15.
 */

public class TransferDevMsg {
    public String roomName;
    public String roomId;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
