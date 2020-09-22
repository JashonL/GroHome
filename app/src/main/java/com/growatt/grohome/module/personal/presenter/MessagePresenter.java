package com.growatt.grohome.module.personal.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.MessageBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.personal.view.IMessageView;
import com.growatt.grohome.utils.CommentUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MessagePresenter extends BasePresenter<IMessageView> {



    public MessagePresenter(IMessageView baseView) {
        super(baseView);
    }

    public MessagePresenter(Context context, IMessageView baseView) {
        super(context, baseView);
    }



    public void getDataByIntent(){
        String jsonobject = ((Activity) context).getIntent().getStringExtra(GlobalConstant.MESSAGELIST);
        if (!TextUtils.isEmpty(jsonobject)){
            parserJson(jsonobject);
        }
    }



    /**
     * 获取消息
     */
    public void getLoginRecord() throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("userId",  App.getUserBean().getAccountName());
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.getLoginRecord(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                parserJson(bean);
            }

            @Override
            public void onError(String msg) {
                baseView.onError(msg);
            }
        });
    }




    private void parserJson(String bean) {
        try {
            JSONObject obj = new JSONObject(bean);
            int code = obj.getInt("code");
            if (code == 0) {
                JSONArray array = obj.optJSONArray("data");
                List<MessageBean> newList=new ArrayList<>();
                if (array!=null){
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        MessageBean messageBean=new Gson().fromJson(object.toString(),MessageBean.class);
                        newList.add(messageBean);
                    }
                }
                baseView.setMessageData(newList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
