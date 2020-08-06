package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.PanelSwitchBean;
import com.growatt.grohome.bean.SceneConditionBean;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.bean.ScenesRoadBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.scenes.view.ISceneConditionView;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.scene.SceneCondition;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SceneConditionPresenter extends BasePresenter<ISceneConditionView> {

    //APP中的设备
    private GroDeviceBean groDeviceBean;

    //场景实例
    private SceneConditionBean mSceneCondition;

    //面板开关
    private PanelSwitchBean panelSwitchBean;
    private List<String> nameList = new ArrayList<>();
    //涂鸦设备
    private DeviceBean deviceBean;
    private String devId;
    private String devName;
    private String devType;
    private String sceneName;
    private String linkType = GlobalConstant.SCENE_DEVICE_OPEN;
    private String roadset;
    private String createOrEdit;

    public SceneConditionPresenter(ISceneConditionView baseView) {
        super(baseView);
    }

    public SceneConditionPresenter(Context context, ISceneConditionView baseView) {
        super(context, baseView);
    }


    public void getIntent() {
        sceneName = ((Activity) context).getIntent().getStringExtra(GlobalConstant.SCENE_NAME);
        baseView.setSceneName(sceneName);

        createOrEdit = ((Activity) context).getIntent().getStringExtra(GlobalConstant.SCENE_CREATE_OR_EDIT);
        if (GlobalConstant.SCENE_CREATE.equals(createOrEdit)) {
            String beanJson = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_BEAN);
            if (TextUtils.isEmpty(beanJson)) return;
            groDeviceBean = new Gson().fromJson(beanJson, GroDeviceBean.class);
            devId = groDeviceBean.getDevId();
            devName = groDeviceBean.getName();
            devType = groDeviceBean.getDevType();
            deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
            baseView.setViewsByDevice(groDeviceBean);
        } else {
            String taskJson = ((Activity) context).getIntent().getStringExtra(GlobalConstant.SCENE_CONDITION_BEAN);
            if (TextUtils.isEmpty(taskJson)) return;
            mSceneCondition = new Gson().fromJson(taskJson, SceneConditionBean.class);
            linkType = mSceneCondition.getLinkType();
            roadset = mSceneCondition.getRoad();
            devId = mSceneCondition.getDevId();
            devName = mSceneCondition.getDevName();
            devType = mSceneCondition.getDevType();
            deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
            baseView.setViewsByTask(mSceneCondition);
        }

    }


    /**
     * 获取面板详情
     *
     * @throws Exception
     */
    public void getDetailData() throws Exception {
        JSONObject requestJson = new JSONObject();
        requestJson.put("devId", devId);
        requestJson.put("lan", String.valueOf(CommentUtils.getLanguage()));
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.getSwitchDetail(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                baseView.freshStop();
                Log.i(TuyaApiUtils.TUYA_TAG, "请求成功：" + bean);
                JSONObject jsonObject;
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
                        List<ScenesRoadBean> beanList = new ArrayList<>();
                        for (int i = 0; i < road; i++) {
                            ScenesRoadBean swichBean = new ScenesRoadBean();
                            swichBean.setId(i + 1);
                            String onOff;
                            if (deviceBean != null) {
                                onOff = String.valueOf(deviceBean.getDps().get(String.valueOf(i + 1)));
                                swichBean.setOnOff("true".equals(onOff) ? 1 : 0);
                            }
                            if (GlobalConstant.SCENE_EDIT.equals(createOrEdit)) {
                                if (TextUtils.isEmpty(roadset) || i >= roadset.length()) {
                                    return;
                                }
                                String switch_onOff = String.valueOf(roadset.charAt(i));
                                switch (switch_onOff) {
                                    case "1":
                                        swichBean.setOnOff(1);
                                        swichBean.setScenesConditionEnable(true);
                                        break;
                                    case "0":
                                        swichBean.setOnOff(0);
                                        swichBean.setScenesConditionEnable(true);
                                        break;
                                    case "2":
                                        swichBean.setOnOff(0);
                                        swichBean.setScenesConditionEnable(false);
                                        break;
                                }
                            }
                            String name = panelObject.getString("code" + swichBean.getId());
                            swichBean.setName(name);
                            nameList.add(name);
                            beanList.add(swichBean);
                        }
                        panelSwitchBean.setSwitchNum(beanList.size());
                        baseView.setSwitchRoad(beanList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String msg) {
                baseView.freshStop();
            }
        });

    }


    /**
     * 设置插座的开关
     */
    public void setSocketOnoff() {
        if (GlobalConstant.SCENE_DEVICE_OPEN.equals(linkType)) {
            linkType = GlobalConstant.SCENE_DEVICE_SHUT;
        } else {
            linkType = GlobalConstant.SCENE_DEVICE_OPEN;
        }
        baseView.setSocketUi(linkType);
    }


    public void settingComplete() {
        SceneConditionBean bean = new SceneConditionBean();
        bean.setDevId(devId);
        bean.setDevName(devName);
        bean.setDevType(devType);
        switch (devType) {
            case DeviceTypeConstant.TYPE_THERMOSTAT:
            case DeviceTypeConstant.TYPE_AIRCONDITION:
                if (!baseView.getSwitchChecked() && !baseView.getTempChecked()) {
                    MyToastUtils.toast(R.string.m256_choose_at_leat_one);
                    return;
                }
                if (baseView.getSwitchChecked()) {
                    bean.setLinkType(linkType);
                }
                break;
            case DeviceTypeConstant.TYPE_PADDLE:
                bean.setLinkType(linkType);
                break;
            case DeviceTypeConstant.TYPE_PANELSWITCH:
                StringBuilder road = new StringBuilder();
                StringBuilder subSwitchName = new StringBuilder();
                List<ScenesRoadBean> data = baseView.getData();
                boolean isConditionSelect = false;
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isScenesConditionEnable()) {
                        isConditionSelect = true;
                        if (data.get(i).getOnOff() == 1) {
                            road.append("1");
                        } else {
                            road.append("0");
                        }
                    } else {
                        road.append("2");
                    }
                    subSwitchName.append(data.get(i).getName()).append(",");
                }
                String s = subSwitchName.toString();
                if (s.endsWith(",")) {
                    s = s.substring(0, s.length() - 1);
                }
                bean.setSubSwitchName(s);
                bean.setRoad(road.toString());
                bean.setSwitchNameList(nameList);
                if (!isConditionSelect) {
                    MyToastUtils.toast(R.string.m256_choose_at_leat_one);
                    return;
                }
                break;
            default:

                break;
        }
        EventBus.getDefault().post(bean);
        ((Activity) context).finish();

    }

}
