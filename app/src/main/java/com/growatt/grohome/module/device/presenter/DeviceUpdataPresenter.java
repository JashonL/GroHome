package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.view.IDeviceUpdataView;
import com.growatt.grohome.utils.MyToastUtils;
import com.mylhyl.circledialog.CircleDialog;
import com.tuya.smart.android.device.bean.UpgradeInfoBean;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IGetOtaInfoCallback;
import com.tuya.smart.sdk.api.IOtaListener;
import com.tuya.smart.sdk.api.ITuyaOta;
import com.tuya.smart.sdk.bean.OTAErrorMessageBean;

import java.util.List;

public class DeviceUpdataPresenter extends BasePresenter<IDeviceUpdataView> implements IOtaListener {
    private String mDevId;
    private String nDevName;
    private int upgradeStatus;

    private ITuyaOta iTuyaOta;

    private CircleDialog.Builder builder;

    public DeviceUpdataPresenter(IDeviceUpdataView baseView) {
        super(baseView);
    }

    public DeviceUpdataPresenter(Context context, IDeviceUpdataView baseView) {
        super(context, baseView);
    }

    public void getIntentData() {
        Intent intent = ((Activity) context).getIntent();
        mDevId = intent.getStringExtra(GlobalConstant.DEVICE_ID);
        nDevName = intent.getStringExtra(GlobalConstant.DEVICE_NAME);
        baseView.setName(nDevName);
        initDevice();
    }

    /**
     * 获取到设备操作类
     */
    private void initDevice() {
        iTuyaOta = TuyaHomeSdk.newOTAInstance(mDevId);
        iTuyaOta.setOtaListener(this);
        checkUpgradeInfo();
    }




    /**
     * 检测更新
     */
    private void checkUpgradeInfo() {
        //获取固件升级信息
        iTuyaOta.getOtaInfo(new IGetOtaInfoCallback() {
            @Override
            public void onSuccess(List<UpgradeInfoBean> list) {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        UpgradeInfoBean dev = list.get(i);
                        if (dev != null) {
                            String currentVersion = context.getString(R.string.m271_current_version) +":"+"V"+ dev.getCurrentVersion();
                            baseView.currentVersion(currentVersion);
                            //upgradeStatus - 0:无新版本 1:有新版本 2:在升级中
                            upgradeStatus = dev.getUpgradeStatus();
                            if (upgradeStatus==1){//有更新
                                String version = context.getString(R.string.m271_new_version)+":"+"V" + dev.getVersion();
                                baseView.newVersion(version);
                                break;
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(String s, String s1) {
                MyToastUtils.toast(R.string.m201_fail);
                baseView.setlastVersion();
            }
        });


    }



    public void toUpGradeNow() {
        iTuyaOta.startOta();
    }


    private void showProgress(){
        builder = new CircleDialog.Builder();
        builder.setCancelable(false).setCanceledOnTouchOutside(true)
                .configDialog(params -> params.backgroundColor = ContextCompat.getColor(context,R.color.color_theme_green))
                .setTitle(context.getString(R.string.m272_firmware_update))
                .show(((FragmentActivity)context).getSupportFragmentManager());
    }

    @Override
    public void onSuccess(int otaType) {
        MyToastUtils.toast(R.string.m200_success);
        ((Activity)context).finish();
    }

    @Override
    public void onFailure(int otaType, String code, String error) {
        MyToastUtils.toast(R.string.m201_fail);
    }

    @Override
    public void onFailureWithText(int otaType, String code, OTAErrorMessageBean messageBean) {

    }

    @Override
    public void onProgress(int otaType, int progress) {
        if (builder==null){
            showProgress();
        }else {
            builder.setProgress(100, progress).refresh();
        }
    }

    @Override
    public void onTimeout(int otaType) {
        MyToastUtils.toast(R.string.m273_time_out);
    }
}
