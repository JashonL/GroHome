package com.growatt.grohome.module.config.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.config.view.ISelectConfigView;

import static android.app.Activity.RESULT_OK;

public class SelectConfigPresenter extends BasePresenter<ISelectConfigView> {

    public SelectConfigPresenter(ISelectConfigView baseView) {
        super(baseView);
    }

    public SelectConfigPresenter(Context context, ISelectConfigView baseView) {
        super(context, baseView);
    }

    public void selectMode(int mode) {
        Intent intent = new Intent();
        intent.putExtra("mode", mode);
        ((Activity)context).setResult(RESULT_OK, intent);
        baseView.resultSelectMode();
    }
}
