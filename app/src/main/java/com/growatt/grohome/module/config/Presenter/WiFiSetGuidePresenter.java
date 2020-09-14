package com.growatt.grohome.module.config.Presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.config.view.IWiFiSetGuideView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WiFiSetGuidePresenter extends BasePresenter<IWiFiSetGuideView> {
    public WiFiSetGuidePresenter(IWiFiSetGuideView baseView) {
        super(baseView);
    }

    public WiFiSetGuidePresenter(Context context, IWiFiSetGuideView baseView) {
        super(context, baseView);
    }


    public void readFile() throws Exception{
        //获取文件
        InputStream inputStream= context.getAssets().open("wificonfig_en");
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
