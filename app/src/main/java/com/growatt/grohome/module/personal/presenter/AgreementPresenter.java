package com.growatt.grohome.module.personal.presenter;

import android.app.Activity;
import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.personal.view.IAgreementView;
import com.growatt.grohome.utils.CommentUtils;

import java.io.InputStream;

public class AgreementPresenter extends BasePresenter<IAgreementView> {

    public String contentType;

    private String filename;

    public AgreementPresenter(IAgreementView baseView) {
        super(baseView);
    }

    public AgreementPresenter(Context context, IAgreementView baseView) {
        super(context, baseView);
        contentType=((Activity)context).getIntent().getStringExtra(GlobalConstant.AGREEMENT_OR_POLICY);
    }



    public void getContent(){
        if (GlobalConstant.AGREEMENT.equals(contentType)){//用户协议
            if (CommentUtils.getLanguage() == 0){
                filename = "agreement_zh";
            }else {
                filename = "agreement_en";
            }


        }else {//隐私政策
            if (CommentUtils.getLanguage() == 0){
                filename = "policy_zh";
            }else {
                filename = "policy_en";
            }

        }
        try {
            InputStream inputStream = context.getAssets().open(filename);
            byte[] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            String resultString = new String(buffer,"utf-8");
            resultString = resultString.replace("\\n","\n");
            baseView.showContent(resultString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
