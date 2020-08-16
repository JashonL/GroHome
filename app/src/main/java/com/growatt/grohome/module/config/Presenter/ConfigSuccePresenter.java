package com.growatt.grohome.module.config.Presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.module.config.view.IConfigSuccessView;
import com.growatt.grohome.module.room.RoomManager;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ConfigSuccePresenter extends BasePresenter<IConfigSuccessView> {

    private String deviceName;


    public ConfigSuccePresenter(IConfigSuccessView baseView) {
        super(baseView);
    }

    public ConfigSuccePresenter(Context context, IConfigSuccessView baseView) {
        super(context, baseView);
    }




    /**
     * 场景改名
     */
    public void editName() {
        CircleDialogUtils.showCommentInputDialog((FragmentActivity) context, context.getString(R.string.m148_edit), deviceName,
                context.getString(R.string.m233_please_entry_name), true, new OnInputClickListener() {
                    @Override
                    public boolean onClick(String text, View v) {
                        try {
                            if (!TextUtils.isEmpty(text)) {
                                baseView.setDeviceName(text);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
    }



    public void getRoomList(){
        List<HomeRoomBean> homeRoomList = RoomManager.getInstance().getHomeRoomList();
        if (homeRoomList==null){
            baseView.upRoomList(homeRoomList);
        }else {
            try {
                getRoomListByServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }



    /**
     * 获取房间列表
     *
     * @throws Exception
     */
    public void getRoomListByServer() throws Exception {
        JSONObject requestJson = new JSONObject();
        requestJson.put("userId", App.getUserBean().accountName);
        requestJson.put("cmd", "roomList");
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.roomRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject obj = new JSONObject(bean);
                    int code = obj.getInt("code");
                    if (code == 0) {
                        JSONArray dataArray = obj.getJSONArray("data");
                        List<HomeRoomBean> roomList = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            HomeRoomBean roomBean = new Gson().fromJson(jsonObject.toString(), HomeRoomBean.class);
                            roomList.add(roomBean);
                        }
                        RoomManager.getInstance().setHoomRoomList(roomList);
                        baseView.upRoomList(roomList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String msg) {

            }
        });

    }

}
