package com.growatt.grohome.module.service.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.growatt.grohome.module.commom.WebViewActivity;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.FqaBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.service.view.IFQAView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FQAPresenter extends BasePresenter<IFQAView> {

    public FQAPresenter(IFQAView baseView) {
        super(baseView);
    }

    public FQAPresenter(Context context, IFQAView baseView) {
        super(context, baseView);
    }

    public void getFqaList() {

        String lan= String.valueOf(CommentUtils.getLanguage());
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("lan",lan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.methodFile(body), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = jsonObject.optJSONArray("data");
                        List<FqaBean>newList=new ArrayList<>();
                        if (data!=null){
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.optJSONObject(i);
                                FqaBean bean=new Gson().fromJson(object.toString(),FqaBean.class);
                                newList.add(bean);
                            }
                        }
                        baseView.updata(newList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
//                baseView.onError(msg);
            }
        });
    }



    public void toFqaDetail(String url){
        Intent intent=new Intent(context, WebViewActivity.class);
        intent.putExtra(GlobalConstant.WEB_URL,url);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,false);
    }

}
