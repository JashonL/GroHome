package com.growatt.grohome.module.room;

import com.growatt.grohome.bean.HomeRoomBean;

import java.util.List;

public class RoomManager {

    public static final String TAG = RoomManager.class.getSimpleName();

    private static volatile RoomManager instance;

    private List<HomeRoomBean> roomBeans;


    private RoomManager() {
    }

    public static RoomManager getInstance() {
        if (null == instance) {
            synchronized (RoomManager.class) {
                if (null == instance) {
                    instance = new RoomManager();
                }
            }
        }
        return instance;
    }

    public void setHoomRoomList(List<HomeRoomBean> roomBeans) {
        if (null == roomBeans) {
            return;
        }
        this.roomBeans=roomBeans;
    }




    public List<HomeRoomBean> getHomeRoomList() {
      return roomBeans;
    }







}
