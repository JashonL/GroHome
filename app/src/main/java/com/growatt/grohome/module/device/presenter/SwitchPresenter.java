package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.PanelSwitchBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.view.ISwitchView;
import com.growatt.grohome.tuya.SendDpListener;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SwitchPresenter extends BasePresenter<ISwitchView> implements IDevListener , SendDpListener {
    private String deviceId;
    private String devName;

    private PanelSwitchBean panelSwitchBean;
    private ITuyaDevice mTuyaDevice;
    private DeviceBean deviceBean;
    private List<String> nameList = new ArrayList<>();

    public SwitchPresenter(ISwitchView baseView) {
        super(baseView);
    }

    public SwitchPresenter(Context context, ISwitchView baseView) {
        super(context, baseView);
        deviceId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_ID);
        devName = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_NAME);
        if (!TextUtils.isEmpty(devName)) {
            baseView.setTitle(devName);
        }
    }



    /**
     * 获取到设备操作类
     * 并获取数据，进行初始化
     */
    public void initDevice() {
        //先干掉之前的在重新获取，避免多次回调
        if (mTuyaDevice != null) {
            mTuyaDevice.unRegisterDevListener();
            mTuyaDevice.onDestroy();
        }
        if (!TextUtils.isEmpty(deviceId)) {
            mTuyaDevice = TuyaHomeSdk.newDeviceInstance(deviceId);
            mTuyaDevice.registerDevListener(this);
        }
        deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(deviceId);
        if (deviceBean == null) {
            MyToastUtils.toast(R.string.m149_device_does_not_exist);
            ((Activity) context).finish();
            return;
        }
        try {
            getDetailData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 获取面板详情
     * @throws Exception
     */
    public void getDetailData() throws Exception {
        JSONObject requestJson=new JSONObject();
        requestJson.put("devId", deviceId);
        requestJson.put("lan",String.valueOf(CommentUtils.getLanguage()));
        String s = requestJson.toString();
        RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.getSwitchDetail(body), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String bean) {
                Log.i(TuyaApiUtils.TUYA_TAG,"请求成功："+bean);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(bean);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        //解析数据
                        JSONObject panelObject = jsonObject.getJSONObject("data");
                        panelSwitchBean = new PanelSwitchBean();
                        Iterator<String> iterator = panelObject.keys();
                        int road = 0;
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            Object value = panelObject.get(key);
                            if ("devId".equals(key)) {
                                panelSwitchBean.setDevId((String) value);
                            } else if ("name".equals(key)) {
                                panelSwitchBean.setName((String) value);
                            } else if ("onoff".equals(key)) {
                                panelSwitchBean.setOnoff((Integer) value);
                            } else if ("online".equals(key)) {
                                panelSwitchBean.setOnline((Integer) value);
                            } else if (key.contains("code")) {
                                road++;
                            }
                        }
                        panelSwitchBean.setRoad(road);
                        devName = panelObject.getString("name");
                        baseView.setTitle(devName);
                        road = panelSwitchBean.getRoad();
                        for (int i = 0; i < road; i++) {
                            PanelSwitchBean.SwichBean swichBean = new PanelSwitchBean.SwichBean();
                            swichBean.setId(i + 1);
                            if (deviceBean != null) {
                                String onOff = String.valueOf(deviceBean.getDps().get(String.valueOf(i+1)));
                                if (!TextUtils.isEmpty(onOff)){
                                    swichBean.setOnOff("true".equals(onOff) ? 1 : 0);
                                }
                            }
                            String name = panelObject.getString("code" + swichBean.getId());
                            swichBean.setName(name);
                            swichBean.setCustomName(panelObject.getString("name" + swichBean.getId()));
                            nameList.add(name);
                            panelSwitchBean.addSwitchBean(swichBean);
                        }
                        List<PanelSwitchBean.SwichBean> beanList = panelSwitchBean.getBeanList();
                        panelSwitchBean.setSwitchNum(beanList.size());
                        baseView.freshData(beanList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String msg) {
            }
        });

    }


    /**
     * 操作所有开关
     *
     * @param isOpen 开或者关
     */
    public void allOnoff(boolean isOpen) {
        if (panelSwitchBean == null) {
            MyToastUtils.toast(R.string.m171_check_network);
            return;
        }
        if (deviceNotOnline()) {
            LinkedHashMap<String, Object> dpMap = new LinkedHashMap<>();
            for (int i = 0; i < panelSwitchBean.getRoad(); i++) {
                dpMap.put(String.valueOf(i+1), isOpen);
            }
            TuyaApiUtils.sendCommand(dpMap,mTuyaDevice,this);
        }

    }


    /**
     * 操作单个开关
     *
     */
    public void singleOnOff(String dpId) {
        try {
            if (panelSwitchBean == null) {
                MyToastUtils.toast(R.string.m171_check_network);
                return;
            }
            if (deviceNotOnline()){
                String onOff = String.valueOf(deviceBean.getDps().get(dpId));
                boolean isOpen= "true".equals(onOff);
                TuyaApiUtils.sendCommand(dpId,!isOpen,mTuyaDevice,this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断是否在线
     *
     * @return
     */
    private boolean deviceNotOnline() {
        if (!deviceBean.getIsOnline()) {
            MyToastUtils.toast(R.string.m150_device_is_offline);
            return false;
        }
        return true;
    }

    @Override
    public void sendCommandSucces() {

    }

    @Override
    public void sendCommandError(String code, String error) {

    }

    @Override
    public void onDpUpdate(String devId, String dpStr) {
        if (devId.equals(deviceId)) {
            try {
                JSONObject object = new JSONObject(dpStr);
                Iterator iterator = object.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    boolean value;
                    value = object.getBoolean(key);
                    baseView.freshStatus(Integer.parseInt(key)-1,value);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRemoved(String devId) {

    }

    @Override
    public void onStatusChanged(String devId, boolean online) {

    }

    @Override
    public void onNetworkStatusChanged(String devId, boolean status) {

    }

    @Override
    public void onDevInfoUpdate(String devId) {

    }

    public void destroyTuya(){
        if (mTuyaDevice != null) {
            mTuyaDevice.unRegisterDevListener();
            mTuyaDevice.onDestroy();
        }
    }

}
