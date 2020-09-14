package com.growatt.grohome.module.service.presenter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.service.ManualActivity;
import com.growatt.grohome.module.service.view.IManualListView;
import com.growatt.grohome.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class ManualListPresenter extends BasePresenter<IManualListView> {
    private List<String> manulList=new ArrayList<>();

    public ManualListPresenter(IManualListView baseView) {
        super(baseView);
    }

    public ManualListPresenter(Context context, IManualListView baseView) {
        super(context, baseView);
        manulList.add(context.getString(R.string.m321_strip_user_manual));
        manulList.add(context.getString(R.string.m322_switch_user_manual));
        manulList.add(context.getString(R.string.m323_bulb_user_manual));
    }

    public List<String> getManulList() {
        return manulList;
    }


    public void toManualDetail(int position){
        String guide = "";
        switch (position){
            case 0:
                guide= GlobalConstant.MANUAL_STRIP_MANUAL;
                break;
            case 1:
                guide=GlobalConstant.MANUAL_BULB_MANUAL;
                break;
            case 2:
                guide=GlobalConstant.MANUAL_SWITCH_MANUAL;
                break;
        }
        Intent intent=new Intent(context, ManualActivity.class);
        intent.putExtra(GlobalConstant.MANUAL_GUIDE,guide);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,false);
    }

}
