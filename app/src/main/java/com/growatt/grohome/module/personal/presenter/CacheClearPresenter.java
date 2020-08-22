package com.growatt.grohome.module.personal.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.personal.view.ICacheClearView;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.GlideCacheUtil;
import com.growatt.grohome.utils.MyToastUtils;

public class CacheClearPresenter extends BasePresenter<ICacheClearView> {


    public CacheClearPresenter(ICacheClearView baseView) {
        super(baseView);
    }

    public CacheClearPresenter(Context context, ICacheClearView baseView) {
        super(context, baseView);

    }


    public void showClearDialog() {
        String size = GlideCacheUtil.getInstance().getCacheSize(context);
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m295_clear_cache), size, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlideCacheUtil.getInstance().clearImageAllCache(context);
                MyToastUtils.toast(R.string.m200_success);
                ((Activity) context).finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });

    }

}
