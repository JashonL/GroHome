package com.growatt.grohome.module.service.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.service.view.IManualView;

public class ManualPresenter extends BasePresenter<IManualView> {
    public ManualPresenter(IManualView baseView) {
        super(baseView);
    }

    public ManualPresenter(Context context, IManualView baseView) {
        super(context, baseView);
    }
}
