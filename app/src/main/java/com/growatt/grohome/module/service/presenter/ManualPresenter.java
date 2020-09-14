package com.growatt.grohome.module.service.presenter;

import android.content.Context;

import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.http.API;
import com.growatt.grohome.module.service.view.IManualView;
import com.growatt.grohome.utils.CommentUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManualPresenter extends BasePresenter<IManualView> {


    public ManualPresenter(IManualView baseView) {
        super(baseView);
    }

    public ManualPresenter(Context context, IManualView baseView) {
        super(context, baseView);

    }



    /**
     * 从服务器获取使用手册
     */
    public void getImageList() {
        String userUrl = GlobalConstant.HTTP_PREFIX + App.getUserBean().getUrl() + API.GET_ADVERTISING_LIST + CommentUtils.getLocale()+"&kind=12";
        addDisposable(apiServer.getAdvertisingList(userUrl), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                if (bean.length() > 10) {
                    try {
                        JSONArray jsonArray = new JSONArray(bean);
                        List<String> bannerList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.get("name").toString();
                            String path = GlobalConstant.HTTP_PREFIX + App.getUserBean().getUrl() + "/" + name;
                            bannerList.add(path);
                        }
                        baseView.showManual(bannerList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String msg) {
                baseView.error(msg);
            }
        });

    }

}
