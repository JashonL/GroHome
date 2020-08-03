package com.growatt.grohome.module.scenes.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.scenes.view.IEffectivePeriodView;

public class EffectivePeriodPresenter extends BasePresenter<IEffectivePeriodView> {
    public EffectivePeriodPresenter(IEffectivePeriodView baseView) {
        super(baseView);
    }

    public EffectivePeriodPresenter(Context context, IEffectivePeriodView baseView) {
        super(context, baseView);
    }
}
