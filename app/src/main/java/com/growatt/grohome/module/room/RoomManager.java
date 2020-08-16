package com.growatt.grohome.module.room;

import android.util.Log;

import androidx.annotation.NonNull;

import com.growatt.grohome.bean.HomeRoomBean;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

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
