package com.growatt.grohome.module.personal.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.personal.AboutActivity;
import com.growatt.grohome.module.personal.CacheClearActivity;
import com.growatt.grohome.module.personal.SetttingActivity;
import com.growatt.grohome.module.personal.view.IPersonalFragmentView;
import com.growatt.grohome.utils.ActivityUtils;
import com.hjq.toast.ToastUtils;

public class PersonalPresenter extends BasePresenter<IPersonalFragmentView> {

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

}
