package com.growatt.grohome.module.personal.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.MessageBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.personal.AboutActivity;
import com.growatt.grohome.module.personal.CacheClearActivity;
import com.growatt.grohome.module.personal.MessageCenterActivity;
import com.growatt.grohome.module.personal.SetttingActivity;
import com.growatt.grohome.module.personal.view.IPersonalFragmentView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.hjq.toast.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PersonalPresenter extends BasePresenter<IPersonalFragmentView> {

    private String json;

    public PersonalPresenter(IPersonalFragmentView baseView) {
        super(baseView);
    }

    public PersonalPresenter(Context context, IPersonalFragmentView baseView) {
        super(context, baseView);
    }

    public void toUsercenter(){
        Intent intent=new Intent(context, SetttingActivity.class);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    public void clearCache(){
        Intent intent=new Intent(context, CacheClearActivity.class);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void about(){
        Intent intent=new Intent(context, AboutActivity.class);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void startGoogleHome(){
        PackageManager pm=context.getPackageManager();
        Intent intent=pm.getLaunchIntentForPackage("com.google.android.apps.chromecast.app");
        if (intent==null){
            ToastUtils.show(R.string.m308_application_not_installed);
            return;
        }
        context.startActivity(intent);
    }


    public void startAmazonAlexa(){
        PackageManager pm=context.getPackageManager();
        Intent intent=pm.getLaunchIntentForPackage("com.amazon.dee.app");
        if (intent==null){
            ToastUtils.show(R.string.m308_application_not_installed);
            return;
        }
        context.startActivity(intent);
    }



    /**
     * 获取消息
     */
    public void getLoginRecord() throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("userId", App.getUserBean().getAccountName());
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
//                baseView.onError(msg);
            }
        });
    }

    private void parserJson(String bean) {
        json=bean;
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
                baseView.setMessageCount(newList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void toMessageList(){
        Intent intent=new Intent(context, MessageCenterActivity.class);
        intent.putExtra(GlobalConstant.MESSAGELIST,json);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,false);
    }


}
