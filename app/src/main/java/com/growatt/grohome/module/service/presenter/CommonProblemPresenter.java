package com.growatt.grohome.module.service.presenter;

import android.app.Activity;
import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.service.view.ICommonProblemView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommonProblemPresenter extends BasePresenter<ICommonProblemView> {

    private String fileName;

    public CommonProblemPresenter(ICommonProblemView baseView) {
        super(baseView);
    }

    public CommonProblemPresenter(Context context, ICommonProblemView baseView) {
        super(context, baseView);
        String fqaguide = ((Activity) context).getIntent().getStringExtra(GlobalConstant.FQA_GUIDE);
        if (GlobalConstant.FQA_WIFI_SEPARATE.equals(fqaguide)){
            fileName="wifiseparate";
        }else if (GlobalConstant.FQA_CONFIG_ERROR.equals(fqaguide)){
            fileName="config_error";
        }else {
            fileName="wificonfig_en";
        }
    }



    public void readFile() throws Exception{
        //获取文件
        InputStream inputStream= context.getAssets().open(fileName);
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(inputStream);//转成字符流
        BufferedReader bufferedReader = new BufferedReader(isr);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\r\n");
        }
        baseView.showConfigText(stringBuilder.toString());
        bufferedReader.close();
    }
}
