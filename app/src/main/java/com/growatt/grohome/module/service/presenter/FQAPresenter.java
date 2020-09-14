package com.growatt.grohome.module.service.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.service.CommonProblemActivity;
import com.growatt.grohome.module.service.view.IFQAView;
import com.growatt.grohome.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class FQAPresenter extends BasePresenter<IFQAView> {

    private List<String>fqaList=new ArrayList<>();


    public FQAPresenter(IFQAView baseView) {
        super(baseView);
    }

    public FQAPresenter(Context context, IFQAView baseView) {
        super(context, baseView);
        fqaList.add(context.getString(R.string.m318_how_to_distinguish));
        fqaList.add(context.getString(R.string.m319_how_to_reset_device));
        fqaList.add(context.getString(R.string.m320_how_to_config_separate));
    }

    public List<String> getFqaList() {
        return fqaList;
    }



    public void toFqaDetail(int position){
        String guide = "";
        switch (position){
            case 0:
                guide=GlobalConstant.FQA_WIFI_CONFIG;
                break;
            case 1:
                guide=GlobalConstant.FQA_CONFIG_ERROR;
                break;
            case 2:
                guide=GlobalConstant.FQA_WIFI_SEPARATE;
                break;
        }
        Intent intent=new Intent(context, CommonProblemActivity.class);
        intent.putExtra(GlobalConstant.FQA_GUIDE,guide);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_FORWARD,false);
    }

}
