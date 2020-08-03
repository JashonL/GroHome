package com.growatt.grohome.module.scenes.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.scenes.view.IRepeatActivityView;

public class RepeatPresenter extends BasePresenter<IRepeatActivityView> {
    public RepeatPresenter(IRepeatActivityView baseView) {
        super(baseView);
    }

    public RepeatPresenter(Context context, IRepeatActivityView baseView) {
        super(context, baseView);
    }
}
