package com.growatt.grohome.module.config.presenter;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.AllPermissionRequestCode;
import com.growatt.grohome.constants.DeviceConfigConstant;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.config.WiFiOptionsActivity;
import com.growatt.grohome.module.config.view.IBluetoothConfigView;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.Mydialog;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.tuya.smart.android.ble.api.ScanDeviceBean;
import com.tuya.smart.android.ble.api.TyBleScanResponse;

import pub.devrel.easypermissions.EasyPermissions;

public class BluetoothPresenter extends BasePresenter<IBluetoothConfigView> {

    private String scanJson;
    private DialogFragment dialogFragment;
    private String configType;

    public BluetoothPresenter(IBluetoothConfigView baseView) {
        super(baseView);
    }

    public BluetoothPresenter(Context context, IBluetoothConfigView baseView) {
        super(context, baseView);
        //先获取设备配网的类型
        configType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_CONFIG_TYPE);
    }


    //判断蓝牙是否打开
    public void isBleEnable() {
        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        if (blueadapter == null) {
            ((Activity) context).finish();
        } else {
            if (!blueadapter.isEnabled()) {//没打开
                showOpenBlueToothDialog();
            } else {
                checkLocationPermissions();
            }
        }
    }


    /**
     * 开启蓝牙弹框
     */
    private void showOpenBlueToothDialog() {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m313_wants_turn_bluetooth), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                bluetoothAdapter.enable();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFinish();
            }
        }, false);
    }



    /**
     * 关闭蓝牙弹框
     */

    private void showBlueToothClose() {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m314_bluetooth_not_turned_on), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOpenBlueToothDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFinish();
            }
        },false);
    }



    /**
     * 开启GPS弹框
     */

    private void showGpsDialog() {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m315_turn_on_gps), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ((Activity) context).startActivityForResult(intent, GlobalConstant.ACTION_LOCATION_CODE);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFinish();
            }
        }, false);
    }



    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) return;
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (action) {
                case BluetoothDevice.ACTION_ACL_CONNECTED://蓝牙设备已连接
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED://蓝牙设备已断开
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON://蓝牙正在打开
                            Mydialog.show(context);
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF://蓝牙正在关闭

                            break;
                        case BluetoothAdapter.STATE_OFF://蓝牙设备已关闭
                            showBlueToothClose();
                            break;
                        case BluetoothAdapter.STATE_ON://蓝牙设备已开启
                            checkLocationPermissions();
                            Mydialog.dissmiss();
                            break;
                    }
                    break;
            }
        }
    };

    public void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_TURNING_ON");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_TURNING_OFF");
        context.registerReceiver(mBroadcastReceiver, intentFilter);
    }


    public void unregisterBluetoothReceiver() {
        if (mBroadcastReceiver != null) {
            context.unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }


    public void checkLocationPermissions() {
        try {
            if (EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)) {//已经有权限
                gpsStatus();
            } else {
                EasyPermissions.requestPermissions((Activity) context, String.format("%s:%s", context.getString(R.string.m93_request_permission), context.getString(R.string.m94_location)), AllPermissionRequestCode.PERMISSION_LOCATION_CODE, Manifest.permission.ACCESS_FINE_LOCATION);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断Gps是否打开
     */
    private void gpsStatus() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm != null) {
            boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (ok) {
                TuyaApiUtils.blueToothScan(new TyBleScanResponse() {//开始扫描
                    @Override
                    public void onResult(ScanDeviceBean bean) {
                        scanJson = new Gson().toJson(bean);
                        showAddDialog();
                        Log.d("蓝牙扫描",bean.toString());
                    }
                });
            } else {
                showGpsDialog();
            }
        }
    }



    public void showAddDialog(){
        if (dialogFragment==null){
            View bodyView = LayoutInflater.from(context).inflate(R.layout.blue_find_device, null, false);
            dialogFragment = CircleDialogUtils.showCommentBodyDialog(bodyView, ((FragmentActivity) context).getSupportFragmentManager(), new OnCreateBodyViewListener() {
                @Override
                public void onCreateBodyView(View view) {
                    view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityFinish();
                        }
                    });

                    view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toConfig();
                        }
                    });
                }
            });
        }

    }






    public void toConfig() {
        Intent intent = new Intent(context, WiFiOptionsActivity.class);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, DeviceTypeConstant.TYPE_STRIP_LIGHTS);
        intent.putExtra(GlobalConstant.DEVICE_SCAN_BEAN,scanJson);
        intent.putExtra(GlobalConstant.DEVICE_CONFIG_TYPE,configType);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE, DeviceConfigConstant.BLUETOOTH_MODE);
        intent.putExtra(DeviceConfigConstant.CONFIG_MODE,DeviceConfigConstant.BLUETOOTH_MODE);
        context.startActivity(intent);
        ((FragmentActivity)context).finish();
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GlobalConstant.ACTION_LOCATION_CODE) {
            gpsStatus();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        TuyaApiUtils.blueToothStop();
    }
}
