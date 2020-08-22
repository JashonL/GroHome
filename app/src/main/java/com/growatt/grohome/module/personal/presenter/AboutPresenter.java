package com.growatt.grohome.module.personal.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.growatt.grohome.R;
import com.growatt.grohome.WebViewActivity;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.http.API;
import com.growatt.grohome.module.personal.view.IAboutView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;

import org.json.JSONObject;

public class AboutPresenter extends BasePresenter<IAboutView> {

    public AboutPresenter(IAboutView baseView) {
        super(baseView);
    }

    public AboutPresenter(Context context, IAboutView baseView) {
        super(context, baseView);
    }

    public void getVersionName() {
        String verSionName = context.getString(R.string.m271_current_version) + "    " + CommentUtils.getVerSionName(context);
        baseView.setVersionName(verSionName);
        baseView.setAppicon();
    }


    /**
     * 获取客服电话
     */
    public void getCustomerPhone() {
        String userUrl = GlobalConstant.HTTP_PREFIX + App.getUserBean().getUrl() + API.GET_SERVICE_PHONENUM + CommentUtils.getLocale() + "&kind=12&type=2";
        addDisposable(apiServer.getServicePhoneNum(userUrl), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String num = jsonObject.optString("servicePhoneNum");
                    String email = jsonObject.optString("serviceEmail");
                    baseView.setEmail(email);
                    baseView.setPhone(num);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
            }
        });
    }


    public void toWebSite() {
        Intent intent = new Intent(context, WebViewActivity.class);
        if (0==CommentUtils.getLanguage()){
            intent.putExtra(GlobalConstant.WEB_URL,GlobalConstant.COMPANY_WEBSITE_CN);
        }else {
            intent.putExtra(GlobalConstant.WEB_URL,GlobalConstant.COMPANY_WEBSITE_EN);
        }
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }
}
