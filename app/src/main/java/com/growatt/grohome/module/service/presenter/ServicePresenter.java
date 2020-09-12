package com.growatt.grohome.module.service.presenter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.growatt.grohome.R;
import com.growatt.grohome.WebViewActivity;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.CommondityBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.http.API;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.service.view.IServiceFragmentView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServicePresenter extends BasePresenter<IServiceFragmentView> {

    List<CommondityBean> newList = new ArrayList<>();


    public ServicePresenter(IServiceFragmentView baseView) {
        super(baseView);
    }

    public ServicePresenter(Context context, IServiceFragmentView baseView) {
        super(context, baseView);
        String[] details = new String[]{context.getString(R.string.m289_schedule_detail), context.getString(R.string.m290_light_bulbs_detail),context.getString(R.string.m290_light_bulbs_detail)};
        String[] deviceTypes = new String[]{DeviceTypeConstant.TYPE_PANELSWITCH, DeviceTypeConstant.TYPE_BULB,DeviceTypeConstant.TYPE_STRIP_LIGHTS};

        String counrty = App.getUserBean().getCounrty();
        String[] webUrl;
        if (GlobalConstant.STRING_COUNTY_UNITEDKINGDOM.equals(counrty)){
            webUrl = new String[]{GlobalConstant.SWITCH_PANEL_WEB, GlobalConstant.LIGHT_BULB_UK_WEB,GlobalConstant.LIGHT_STRIP_UK_WEB};
        }else {
            webUrl = new String[]{GlobalConstant.SWITCH_PANEL_WEB, GlobalConstant.LIGHT_BULB_US_WEB,GlobalConstant.LIGHT_STRIP_US_WEB};
        }
        for (int i = 0; i < webUrl.length; i++) {
            CommondityBean commondityBean = new CommondityBean();
            commondityBean.setDetail(details[i]);
            commondityBean.setDeviceType(deviceTypes[i]);
            commondityBean.setUrl(webUrl[i]);
            newList.add(commondityBean);
        }
    }


    public List<CommondityBean> getNewList() {
        return newList;
    }

    public void setNewList(List<CommondityBean> newList) {
        this.newList = newList;
    }

    /**
     * 根据国家获取服务器
     */
    public void getAdvertisingList() {
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
                        baseView.setBannerList(bannerList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String msg) {
                baseView.onError(msg);
            }
        });

    }

    //方式一跳转网页
    public void openWebView(String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }


    //方式二以WebView的形式打开
    public void openWebViewActivity(String url){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(GlobalConstant.WEB_URL,url);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,false);
    }



}
