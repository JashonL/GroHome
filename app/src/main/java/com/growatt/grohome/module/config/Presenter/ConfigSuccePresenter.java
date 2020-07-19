package com.growatt.grohome.module.config.Presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.config.view.IConfigSuccessView;

public class ConfigSuccePresenter extends BasePresenter<IConfigSuccessView> {
    public ConfigSuccePresenter(IConfigSuccessView baseView) {
        super(baseView);
    }

    public ConfigSuccePresenter(Context context, IConfigSuccessView baseView) {
        super(context, baseView);
    }
}
