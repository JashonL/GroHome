package com.growatt.grohome.module.commom.presenter;

import android.app.Activity;
import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.commom.view.IReadFileView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadViewPresenter extends BasePresenter<IReadFileView> {

    private String fileName;

    public ReadViewPresenter(IReadFileView baseView) {
        super(baseView);
    }

    public ReadViewPresenter(Context context, IReadFileView baseView) {
        super(context, baseView);
        fileName = ((Activity) context).getIntent().getStringExtra(GlobalConstant.FQA_GUIDE);
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
