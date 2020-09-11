package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.DeviceTimingBean;
import com.growatt.grohome.bean.PanelSwitchBean;
import com.growatt.grohome.bean.SwitchTimingBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.DeviceTimingMsg;
import com.growatt.grohome.module.device.DeviceTimingSetActivity;
import com.growatt.grohome.module.device.view.ISwitchTimingView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class SwitchTimingPresenter extends BasePresenter<ISwitchTimingView> {

    private String mAction;

    private String mDevId;//设备id
    private String name;//线路名称
    private String timeValue;//定时时间
    private String cKey;//开启或者关闭
    private String loopType;//每天，单次
    private String loopValue;//用来判断周一到周五
    private String[] onOffArray;
    public static final int REQUEST_SET_REPEAT_CODE = 10000;
    private String deviceType;
    private String road;
    private String cid;
    private String cValue;
    private List<String> temps = new ArrayList<>();
    private String tempUnit;//当前温标
    private String mode;//当前模式
    private DialogFragment dialogFragment;
    private PanelSwitchBean panelSwitchBean;
    private List<SwitchTimingBean> conf;
    public int allStatus = 1;
    private String deviceName;
    private String status = "0";


    public SwitchTimingPresenter(ISwitchTimingView baseView) {
        super(baseView);
    }

    public SwitchTimingPresenter(Context context, ISwitchTimingView baseView) {
        super(context, baseView);
    }


    public void getIntentData() {
        Intent intent = ((Activity) context).getIntent();
        mAction = intent.getStringExtra(GlobalConstant.ACTION);
        baseView.showViews(mAction);
        if (GlobalConstant.ADD.equals(mAction)) {
            deviceType = intent.getStringExtra(GlobalConstant.DEVICE_TYPE);
            name = intent.getStringExtra(GlobalConstant.DEVICE_NAME);
            mDevId = intent.getStringExtra(GlobalConstant.DEVICE_ID);
            tempUnit = intent.getStringExtra(GlobalConstant.TEMP_UNIT);
            mode = intent.getStringExtra(GlobalConstant.TEMP_MODE);
            baseView.initViews(deviceType);
        } else {
            String timingBean = intent.getStringExtra(GlobalConstant.TIMING_BEAN);
            if (!TextUtils.isEmpty(timingBean)) {
                DeviceTimingBean deviceTimingBean = new Gson().fromJson(timingBean, DeviceTimingBean.class);
                cid = deviceTimingBean.getCid();
                cKey = deviceTimingBean.getCKey();
                mDevId = deviceTimingBean.getDevId();
                deviceType = deviceTimingBean.getDevType();
                name = deviceTimingBean.getName();
                road = deviceTimingBean.getRoad();
                timeValue = deviceTimingBean.getTimeValue();
                loopType = deviceTimingBean.getLoopType();
                loopValue = deviceTimingBean.getLoopValue();
                cValue = deviceTimingBean.getcValue();
                tempUnit = intent.getStringExtra(GlobalConstant.TEMP_UNIT);
                mode = intent.getStringExtra(GlobalConstant.TEMP_MODE);
                baseView.initViews(deviceType);
                conf = deviceTimingBean.getConf();
            }
        }

    }


    /**
     * 获取面板详情
     *
     * @throws Exception
     */
    public void getDetailData() throws Exception {
        JSONObject requestJson = new JSONObject();
        requestJson.put("devId", mDevId);
        requestJson.put("lan", String.valueOf(CommentUtils.getLanguage()));
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.getSwitchDetail(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        //解析数据
                        JSONObject panelObject = jsonObject.getJSONObject("data");
                        panelSwitchBean = new PanelSwitchBean();
                        Iterator<String> iterator = panelObject.keys();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            Object value = panelObject.get(key);
                            switch (key) {
                                case "devId":
                                    panelSwitchBean.setDevId((String) value);
                                    break;
                                case "name":
                                    panelSwitchBean.setName((String) value);
                                    break;
                                case "onoff":
                                    panelSwitchBean.setOnoff((Integer) value);
                                    break;
                                case "online":
                                    panelSwitchBean.setOnline((Integer) value);
                                    break;
                                case "road":
                                    panelSwitchBean.setRoad((Integer) value);
                                    break;
                            }
                        }
                        deviceName = panelObject.getString("name");
                        baseView.setDeviceName(deviceName);
                        int roads = panelSwitchBean.getRoad();
                        for (int i = 0; i < roads; i++) {
                            PanelSwitchBean.SwichBean swichBean = new PanelSwitchBean.SwichBean();
                            swichBean.setId(i + 1);
                            swichBean.setOnOff(panelObject.getInt("onoff" + swichBean.getId()));
                            swichBean.setName(panelObject.getString("code" + swichBean.getId()));
                            swichBean.setCustomName(panelObject.getString("name" + swichBean.getId()));
                            panelSwitchBean.addSwitchBean(swichBean);
                        }
                        showList();
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


    private void showList() {
        //分路
        List<PanelSwitchBean.SwichBean> beanList = panelSwitchBean.getBeanList();
        List<SwitchTimingBean> newList = new ArrayList<>();
        for (int i = 0; i < beanList.size(); i++) {
            SwitchTimingBean switchTimingBean = new SwitchTimingBean();
            switchTimingBean.setName(beanList.get(i).getName());
            switchTimingBean.setStatus("1");//默认未设置
            switchTimingBean.setRoad(String.valueOf(i + 1));
            newList.add(switchTimingBean);
        }

        if (!TextUtils.isEmpty(cid)) {//编辑
            int count = 0;
            if (conf != null) {
                for (int i = 0; i < conf.size(); i++) {
                    SwitchTimingBean switchTimingBean = conf.get(i);
                    String road = switchTimingBean.getRoad();
                    int roadId = Integer.parseInt(road) - 1;
                    if (roadId <= newList.size()) {
                        SwitchTimingBean switchTimingBean1 = newList.get(roadId);
                        String status = switchTimingBean.getStatus();
                        if ("0".equals(status)) {
                            count++;
                        }
                        switchTimingBean1.setStatus(status);
                        switchTimingBean1.setcKey(switchTimingBean.getcKey());
                        switchTimingBean1.setcValue(switchTimingBean.getcValue());
                        switchTimingBean1.setLoopType(switchTimingBean.getLoopType());
                        switchTimingBean1.setLoopValue(switchTimingBean.getLoopValue());
                        switchTimingBean1.setTimeValue(switchTimingBean.getTimeValue());
                    }
                }
            }

            baseView.setAllSelect(count == 1);
            if (count == 0) {
                allStatus = 1;
            } else {
                allStatus = 0;
            }
        }
        baseView.upData(newList);
    }


    /**
     * 跳转到设置详情，需要传
     * 名字，定时时间，重复，开关，设备id,是否设置了定时
     */
    public void jumpToSwitchTimgSet(SwitchTimingBean bean) {
        String timingBean = null;
        if (bean != null) {
            timingBean = new Gson().toJson(bean);
        }
        Intent intent = new Intent(context, DeviceTimingSetActivity.class);
        intent.putExtra(GlobalConstant.ACTION, GlobalConstant.EDIT);
        intent.putExtra(GlobalConstant.DEVICE_NAME, deviceName);
        intent.putExtra(GlobalConstant.TEMP_MODE, mode);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.TEMP_UNIT, tempUnit);
        intent.putExtra(GlobalConstant.TIMING_BEAN, timingBean);
        ActivityUtils.startActivityForResult((Activity) context, intent, GlobalConstant.REQUEST_CODE_SELECT_TIME, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /**
     * 跳转到设置详情，需要传
     * 名字，定时时间，重复，开关，设备id,是否设置了定时
     */
    public void jumpToSwitchTimgSet(int road) {
        Intent intent = new Intent(context, DeviceTimingSetActivity.class);
        intent.putExtra(GlobalConstant.ACTION, GlobalConstant.ADD);
        intent.putExtra(GlobalConstant.DEVICE_NAME, deviceName);
        intent.putExtra(GlobalConstant.DEVICE_ID, mDevId);
        intent.putExtra(GlobalConstant.DEVICE_TYPE, deviceType);
        intent.putExtra(GlobalConstant.DEVICE_ROAD, road);
        ActivityUtils.startActivityForResult((Activity) context, intent, GlobalConstant.REQUEST_CODE_SELECT_TIME, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void setAllSwitch() {
        if (allStatus == 0) {
            baseView.allSwitchClose();
        } else {
            baseView.allSwitchOpen();
        }
    }


    public void savaTiming() {
        if (GlobalConstant.ADD.equals(mAction)) {
            try {
                addTiming();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                editTiming();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 设备修改定时
     */
    private void editTiming() throws Exception {
        List<SwitchTimingBean> conf = baseView.getData();
        if (conf.isEmpty()) {
            MyToastUtils.toast(R.string.m261_timing_not_set);
            return;
        }

        List<SwitchTimingBean> setConf = new ArrayList<>();
        for (int i = 0; i < conf.size(); i++) {
            SwitchTimingBean switchTimingBean = conf.get(i);
            if (TextUtils.isEmpty(switchTimingBean.getTimeValue())) continue;
            setConf.add(switchTimingBean);
        }
        if (setConf.size() == 0) {
            MyToastUtils.toast(R.string.m261_timing_not_set);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cid", cid);
        jsonObject.put("userId", App.getUserBean().getAccountName());
        jsonObject.put("devId", mDevId);
        jsonObject.put("devType", deviceType);
        jsonObject.put("loopType", loopType);
        jsonObject.put("loopValue", loopValue);
        jsonObject.put("status", status);
        jsonObject.put("cKey", cKey);
        jsonObject.put("timeValue", timeValue);
        jsonObject.put("cmd", "updateSmartTask");
        jsonObject.put("lan", String.valueOf(CommentUtils.getLanguage()));
        JSONArray jsonArray = new JSONArray();
        if (setConf.size() > 0) {
            for (SwitchTimingBean bean : setConf) {
                String json = new Gson().toJson(bean);
                JSONObject deviceJson = new JSONObject(json);
                jsonArray.put(deviceJson);
            }

            jsonObject.put("conf", jsonArray);
        }

        String json = jsonObject.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("data");
                    MyToastUtils.toast(msg);
                    if (code == 0) {
                        EventBus.getDefault().post(new DeviceTimingMsg());
                        ((Activity) context).finish();
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


    /**
     * 设备添加定时
     */
    private void addTiming() throws Exception {
        List<SwitchTimingBean> conf = baseView.getData();
        if (conf.isEmpty()) {
            MyToastUtils.toast(R.string.m261_timing_not_set);
            return;
        }

        List<SwitchTimingBean> setConf = new ArrayList<>();
        for (int i = 0; i < conf.size(); i++) {
            SwitchTimingBean switchTimingBean = conf.get(i);
            if (TextUtils.isEmpty(switchTimingBean.getTimeValue())) continue;
            setConf.add(switchTimingBean);
        }
        if (setConf.size() == 0) {
            MyToastUtils.toast(R.string.m261_timing_not_set);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", App.getUserBean().getAccountName());
        jsonObject.put("devId", mDevId);
        jsonObject.put("devType", deviceType);
        jsonObject.put("loopType", loopType);
        jsonObject.put("loopValue", loopValue);
        jsonObject.put("status", status);
        jsonObject.put("cKey", cKey);
        jsonObject.put("timeValue", timeValue);
        jsonObject.put("cmd", "addSmartTask");
        jsonObject.put("lan", String.valueOf(CommentUtils.getLanguage()));
        JSONArray jsonArray = new JSONArray();
        if (setConf.size() > 0) {
            for (SwitchTimingBean bean : setConf) {
                String json = new Gson().toJson(bean);
                JSONObject deviceJson = new JSONObject(json);
                jsonArray.put(deviceJson);
            }

            jsonObject.put("conf", jsonArray);
        }

        String json = jsonObject.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("data");
                    MyToastUtils.toast(msg);
                    if (code == 0) {
                        EventBus.getDefault().post(new DeviceTimingMsg());
                        ((Activity) context).finish();
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


    public void showWarnDialog() {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m206_delete), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteTiming();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 设备添加定时
     */
    private void deleteTiming() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", App.getUserBean().getAccountName());
        jsonObject.put("devId", mDevId);
        jsonObject.put("taskId", cid);
        jsonObject.put("cmd", "deleteSmartTask");
        jsonObject.put("lan", String.valueOf(CommentUtils.getLanguage()));
        String json = jsonObject.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("data");
                    MyToastUtils.toast(msg);
                    if (code == 0) {
                        EventBus.getDefault().post(new DeviceTimingMsg());
                        ((Activity) context).finish();
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GlobalConstant.REQUEST_CODE_SELECT_TIME) {
                road = data.getStringExtra(GlobalConstant.DEVICE_ROAD);
                timeValue = data.getStringExtra(GlobalConstant.TIME_VALUE);
                loopType = data.getStringExtra(GlobalConstant.TIME_LOOPTYPE);
                loopValue = data.getStringExtra(GlobalConstant.TIME_LOOPVALUE);
                cKey = data.getStringExtra(GlobalConstant.TIMING_CKEY);
                name = data.getStringExtra(GlobalConstant.DEVICE_NAME);
                List<SwitchTimingBean> dataList = baseView.getData();
                if ("0".equals(road)) {//全部
                    for (int i = 0; i < dataList.size(); i++) {
                        SwitchTimingBean switchTimingBean = dataList.get(i);
                        switchTimingBean.setcKey(cKey);
                        switchTimingBean.setStatus("0");
                        switchTimingBean.setLoopType(loopType);
                        switchTimingBean.setLoopValue(loopValue);
                        switchTimingBean.setTimeValue(timeValue);
                    }
                    baseView.setAllOpen();
                } else {
                    int index = Integer.parseInt(road) - 1;
                    SwitchTimingBean switchTimingBean = dataList.get(index);
                    switchTimingBean.setcKey(cKey);
                    switchTimingBean.setStatus("0");
                    switchTimingBean.setLoopType(loopType);
                    switchTimingBean.setLoopValue(loopValue);
                    switchTimingBean.setTimeValue(timeValue);
                }
                baseView.updataAdapter();
            }
        }
    }


}
