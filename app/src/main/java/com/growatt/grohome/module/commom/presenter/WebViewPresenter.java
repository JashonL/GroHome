package com.growatt.grohome.module.commom.presenter;

import android.app.Activity;
import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.commom.view.IWebViewView;

public class WebViewPresenter extends BasePresenter<IWebViewView> {

    private String url;

    public WebViewPresenter(IWebViewView baseView) {
        super(baseView);
    }

    public WebViewPresenter(Context context, IWebViewView baseView) {
        super(context, baseView);
        url=((Activity)context).getIntent().getStringExtra(GlobalConstant.WEB_URL);
    }

    public void showWeb(){
        baseView.openWebView(url);
    }
}
