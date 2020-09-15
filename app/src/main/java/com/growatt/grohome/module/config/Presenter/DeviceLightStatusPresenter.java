package com.growatt.grohome.module.config.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.RightMenuAdapter;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.DeviceConfigConstant;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.config.ConfigBlueToothActivity;
import com.growatt.grohome.module.config.ConnectHotsPotActivity;
import com.growatt.grohome.module.config.DeviceAPLightActivity;
import com.growatt.grohome.module.config.DeviceConfigActivity;
import com.growatt.grohome.module.config.DeviceEZLightActivity;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.config.view.IDeviceLightStatusView;
import com.growatt.grohome.tuya.FamilyManager;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommonPopupWindow;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;

import java.util.Arrays;
import java.util.List;

import static com.growatt.grohome.module.config.DeviceLightStatusActivity.START_FOR_RESULT_CONFIG;

public class DeviceLightStatusPresenter extends BasePresenter<IDeviceLightStatusView> {

    private String deviceType;
    private String ssid;
    private String password;
    private String tuyaToken;
    private String configType;


    private CommonPopupWindow modeWindow;
    private String[] modeArray;
    private RightMenuAdapter modeAdapter;


    public DeviceLightStatusPresenter(IDeviceLightStatusView baseView) {
        super(baseView);
    }

    public DeviceLightStatusPresenter(Context context, IDeviceLightStatusView baseView) {
        super(context, baseView);
        //先获取设备配网的类型
        configType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_CONFIG_TYPE);


        deviceType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_TYPE);
        String defalut = context.getString(R.string.m105_ez_mode);

        if (DeviceConfigConstant.CONFIG_WIFI_BLUETHOOTH.equals(configType)) {
            modeArray = new String[]{defalut, context.getString(R.string.m102_ap_mode), context.getString(R.string.m119_Bluetooth)};
        } else {
            ssid = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_SSID);
            password = ((Activity) context).getIntent().getStringExtra(GlobalConstant.WIFI_PASSWORD);
            modeArray = new String[]{defalut, context.getString(R.string.m102_ap_mode)};
        }
    }


    public void toEcbindConfig() {
        Class clazz;
        if (DeviceConfigConstant.CONFIG_WIFI_BLUETHOOTH.equals(configType)){
            clazz= WiFiOptionsActivity.class;
        }else {
            clazz=DeviceConfigActivity.class;
        }
        Intent intent = new Intent(context, clazz);
        intent.putExtra(GlobalConstant.WIFI_SSID, ssid);
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, password);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.WIFI_TOKEN, tuyaToken);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE,configType);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.EC_MODE);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void toBluetoothConfig() {
        Intent intent = new Intent(context, ConfigBlueToothActivity.class);
        intent.putExtra(GlobalConstant.WIFI_SSID, ssid);
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, password);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.WIFI_TOKEN, tuyaToken);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE,configType);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.EC_MODE);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    private void toApConfig() {
        Class clazz;
        if (DeviceConfigConstant.CONFIG_WIFI_BLUETHOOTH.equals(configType)){
            clazz= WiFiOptionsActivity.class;
        }else {
            clazz=ConnectHotsPotActivity.class;
        }

        Intent intent = new Intent(context, clazz);
        intent.putExtra(GlobalConstant.WIFI_SSID, ssid);
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, password);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.WIFI_TOKEN, tuyaToken);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE,configType);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.AP_MODE);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    public void getTokenForConfigDevice() {
        baseView.showDialog();
        long homeId = FamilyManager.getInstance().getCurrentHomeId();
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                baseView.dissmissDialog();
                tuyaToken = token;
                toApConfig();
            }

            @Override
            public void onFailure(String s, String s1) {
                baseView.dissmissDialog();
                String errorMsg = s + ":" + s1;
                CircleDialogUtils.showGetTuyaTokenFail(context, ((FragmentActivity) context).getSupportFragmentManager(), errorMsg);
            }
        });
    }


    public void toLightReset() {
        Intent intent = new Intent(context, DeviceEZLightActivity.class);
        intent.putExtra(GlobalConstant.WIFI_SSID, ssid);
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, password);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE,configType);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void toHostGuide() {
        Intent intent = new Intent(context, DeviceAPLightActivity.class);
        intent.putExtra(GlobalConstant.WIFI_SSID, ssid);
        intent.putExtra(GlobalConstant.WIFI_PASSWORD, password);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE,configType);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void toSwitchMode() {
        Intent intent = new Intent(context, DeviceConfigConstant.class);
        ActivityUtils.startActivityForResult((Activity) context, intent, START_FOR_RESULT_CONFIG, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /*模式弹框*/
    public void showRightMenu(View dropView) {
        if (modeWindow == null) {
            modeWindow = new CommonPopupWindow(context, R.layout.pop_menu_list, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) {
                @Override
                protected void initView() {
                    List<String> list = Arrays.asList(modeArray);
                    View view = getContentView();
                    RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    modeAdapter = new RightMenuAdapter(R.layout.item_right_menu, list);
                    recyclerView.setAdapter(modeAdapter);
                    modeAdapter.setOnItemClickListener((adapter1, view1, position) -> {
                        baseView.setMode(position);
                        modeWindow.getPopupWindow().dismiss();
                    });
                }

                @Override
                protected void initEvent() {
                }
            };
        }
        int[] location = new int[2];
        dropView.getLocationOnScreen(location);
        modeWindow.showAsDropDown(dropView, 0, 0);
    }


}
