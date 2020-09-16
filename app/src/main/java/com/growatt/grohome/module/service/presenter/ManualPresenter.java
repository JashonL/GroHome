package com.growatt.grohome.module.service.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.service.view.IManualView;

import java.util.Arrays;
import java.util.List;

public class ManualPresenter extends BasePresenter<IManualView> {

    private String content;
    public String title;

    public ManualPresenter(IManualView baseView) {
        super(baseView);
    }

    public ManualPresenter(Context context, IManualView baseView) {
        super(context, baseView);
        content=((Activity)context).getIntent().getStringExtra(GlobalConstant.MANUAL_GUIDE_TITLE);
        title=((Activity)context).getIntent().getStringExtra(GlobalConstant.MANUAL_GUIDE_CONTENT);
    }


    /**
     * 从服务器获取使用手册
     */
    public void getImageList() {
        if (!TextUtils.isEmpty(content)){
            String[] split = content.split(";");
            List<String> bannerList = Arrays.asList(split);
            baseView.showManual(bannerList);
        }
    }

}
