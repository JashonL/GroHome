package com.growatt.grohome.module.service.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.service.view.IFQAView;

import java.util.ArrayList;
import java.util.List;

public class FQAPresenter extends BasePresenter<IFQAView> {

    private List<String>fqaList=new ArrayList<>();


    public FQAPresenter(IFQAView baseView) {
        super(baseView);
    }

    public FQAPresenter(Context context, IFQAView baseView) {
        super(context, baseView);

    }



}
