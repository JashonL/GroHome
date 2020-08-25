package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.PanelSwitchBean;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.bean.ScenesRoadBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.scenes.view.ISceneTaskSettingView;
import com.growatt.grohome.utils.CommentUtils;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SceneTaskPrensenter extends BasePresenter<ISceneTaskSettingView> {

    //APP中的设备
    private GroDeviceBean groDeviceBean;

    //场景实例
    private SceneTaskBean mSceneTaskBean;

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


    public SceneTaskPrensenter(ISceneTaskSettingView baseView) {
        super(baseView);
    }

    public SceneTaskPrensenter(Context context, ISceneTaskSettingView baseView) {
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
            devName=groDeviceBean.getName();
            devType=groDeviceBean.getDevType();
            deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
            baseView.setViewsByDevice(groDeviceBean);
        } else {
            String taskJson = ((Activity) context).getIntent().getStringExtra(GlobalConstant.SCENE_TASK_BEAN);
            if (TextUtils.isEmpty(taskJson)) return;
            mSceneTaskBean = new Gson().fromJson(taskJson, SceneTaskBean.class);
            linkType = mSceneTaskBean.getLinkType();
            roadset = mSceneTaskBean.getRoad();
            devId = mSceneTaskBean.getDevId();
            devName=mSceneTaskBean.getDevName();
            devType=mSceneTaskBean.getDevType();
            deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
            baseView.setViewsByTask(mSceneTaskBean);
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
                            String onOff="";
                            if (deviceBean != null) {
                                 onOff = String.valueOf(deviceBean.getDps().get(String.valueOf(i + 1)));
                            }
                            if (GlobalConstant.SCENE_EDIT.equals(createOrEdit)) {
                                onOff = String.valueOf(roadset.charAt(i));
                            }
                            if (!TextUtils.isEmpty(onOff)) {
                                swichBean.setOnOff("true".equals(onOff) ? 1 : 0);
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
               baseView.onError(msg);
            }
        });

    }


    public void settingComplete() {
        SceneTaskBean bean = new SceneTaskBean();
        bean.setDevId(devId);
        bean.setDevName(devName);
        bean.setDevType(devType);
        switch (devType) {
            case DeviceTypeConstant.TYPE_PANELSWITCH:
                StringBuilder road = new StringBuilder();
                StringBuilder subSwitchName = new StringBuilder();
                List<ScenesRoadBean> data = baseView.getData();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getOnOff() == 1) {
                        road.append("1");
                    } else {
                        road.append("0");
                    }
                    subSwitchName.append(data.get(i).getName()).append(",");
                }
                String s = subSwitchName.toString();
                if (s.endsWith(",")) {
                    s = s.substring(0, s.length() - 1);
                }
                bean.setSubSwitchName(s);
                String switchSetting = road.toString();
                bean.setRoad(switchSetting);
                bean.setSwitchNameList(nameList);
                break;
            case DeviceTypeConstant.TYPE_BULB:
                bean.setLinkType(linkType);
                break;

            default:

                break;
        }
        EventBus.getDefault().post(bean);
        ((Activity) context).finish();
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
}
