package com.growatt.grohome.module.config.Presenter;

import android.content.Context;
import android.content.Intent;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.config.DeviceConfigActivity;
import com.growatt.grohome.module.config.SelectConfigTypeActivity;
import com.growatt.grohome.module.config.view.IDeviceLightStatusView;
import com.growatt.grohome.module.config.view.IWiFiOptionsView;
import com.mylhyl.circledialog.CircleDialog;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;

public class DeviceLightStatusPresenter extends BasePresenter<IDeviceLightStatusView> {

    public DeviceLightStatusPresenter(IDeviceLightStatusView baseView) {
        super(baseView);
    }

    public DeviceLightStatusPresenter(Context context, IDeviceLightStatusView baseView) {
        super(context, baseView);
    }


/*    public void toEcbindConfig() {
        Intent intent1 = new Intent(context, DeviceConfigActivity.class);
        intent1.putExtra("ssid", ssid);
        intent1.putExtra("password", password);
        intent1.putExtra(DeviceConfigActivity.CONFIG_MODE, DeviceConfigActivity.EC_MODE);
        jumpTo(intent1, true);
    }*/



/*    public void getTokenForConfigDevice() {
        Mydialog.Show(this);
        long homeId = FamilyManager.getInstance().getCurrentHomeId();
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                Mydialog.Dismiss();
                DeviceLightStatusActivity.this.token = token;
                toApConfig();
            }

            @Override
            public void onFailure(String s, String s1) {
                Mydialog.Dismiss();
                String errorMsg = s + ":" + s1;
                new CircleDialog.Builder()
                        .setTitle(getString(R.string.温馨提示))
                        .setText(errorMsg)
                        .setWidth(0.7f)
                        .setPositive(getString(R.string.all_ok), v -> {
                            finish();
                        })
                        .show(getSupportFragmentManager());
            }
        });
    }



    public void toLightReset() {
        Intent intent1 = new Intent(this, LightResetActivity.class);
        intent1.putExtra("isRenew", isRenew);
        intent1.putExtra("type", addType);
        intent1.putExtra("roomId", mRoomId);
        intent1.putExtra("roomName", roomName);
        intent1.putExtra("ssid", ssid);
        intent1.putExtra("password", password);
        jumpTo(intent1, false);
    }


    public void toHostGuide() {
        Intent intent1 = new Intent(this, HostPostGuideActivity.class);
        intent1.putExtra("isRenew", isRenew);
        intent1.putExtra("type", addType);
        intent1.putExtra("roomId", mRoomId);
        intent1.putExtra("roomName", roomName);
        intent1.putExtra("ssid", ssid);
        intent1.putExtra("password", password);
        jumpTo(intent1, false);
    }*/


}
