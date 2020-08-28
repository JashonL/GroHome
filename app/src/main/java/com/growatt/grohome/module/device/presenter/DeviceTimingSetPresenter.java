package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.DeviceTimingBean;
import com.growatt.grohome.bean.SwitchTimingBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.DeviceTimingMsg;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.view.IDeviceTimingSetView;
import com.growatt.grohome.module.scenes.RepeatActivity;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.growatt.grohome.utils.PickViewUtils;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class DeviceTimingSetPresenter extends BasePresenter<IDeviceTimingSetView> {

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

    public DeviceTimingSetPresenter(IDeviceTimingSetView baseView) {
        super(baseView);
    }

    public DeviceTimingSetPresenter(Context context, IDeviceTimingSetView baseView) {
        super(context, baseView);
    }

    public void getIntentData() {
        Intent intent = ((Activity) context).getIntent();
        mAction = intent.getStringExtra(GlobalConstant.ACTION);
        baseView.showViews(mAction);
        deviceType = intent.getStringExtra(GlobalConstant.DEVICE_TYPE);
        if (GlobalConstant.ADD.equals(mAction)) {
            name = intent.getStringExtra(GlobalConstant.DEVICE_NAME);
            mDevId = intent.getStringExtra(GlobalConstant.DEVICE_ID);
            tempUnit = intent.getStringExtra(GlobalConstant.TEMP_UNIT);
            mode = intent.getStringExtra(GlobalConstant.TEMP_MODE);
            road = intent.getStringExtra(GlobalConstant.DEVICE_ROAD);
            initResource();
            baseView.initViews(deviceType);
        } else {
            if (DeviceTypeConstant.TYPE_PANELSWITCH.equals(deviceType)){
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
                    initResource();
                    setOnoffUI();
                    baseView.setTimeValue(timeValue);
                    baseView.initViews(deviceType);
                    setRepeatUI();
                }
            }else {
                String switchTimingJson = intent.getStringExtra(GlobalConstant.SWTICH_TIMING_BEAN);
                if (!TextUtils.isEmpty(switchTimingJson)) {
                    SwitchTimingBean switchTimingBean = new Gson().fromJson(switchTimingJson, SwitchTimingBean.class);
                    cKey = switchTimingBean.getcKey();
                    name = switchTimingBean.getName();
                    road = switchTimingBean.getRoad();
                    timeValue = switchTimingBean.getTimeValue();
                    loopType = switchTimingBean.getLoopType();
                    loopValue = switchTimingBean.getLoopValue();
                    cValue = switchTimingBean.getcValue();
                    tempUnit = intent.getStringExtra(GlobalConstant.TEMP_UNIT);
                    mode = intent.getStringExtra(GlobalConstant.TEMP_MODE);
                    setOnoffUI();
                    baseView.setTimeValue(timeValue);
                    baseView.initViews(deviceType);
                    setRepeatUI();
                }
            }

        }

    }


    private void setOnoffUI() {
        String onOff;
        if (TextUtils.isEmpty(cKey)) return;
        switch (cKey) {
            case GlobalConstant.SCENE_DEVICE_SET:
                onOff = onOffArray[0];
                if (!TextUtils.isEmpty(cValue)) {
                    String value = String.format("%1$s" + GlobalConstant.TEMP_UNIT_CELSIUS, cValue);
                    baseView.setValue(value);
                }
                break;
            case GlobalConstant.SCENE_DEVICE_OPEN:
                onOff = onOffArray[0];
                break;
            default:
                onOff = onOffArray[1];
                break;
        }
        baseView.setOnoffViews(onOff);
    }


    private void setRepeatUI() {
        StringBuilder loopStyle = new StringBuilder();
        if ("-1".equals(loopType)) {
            loopStyle = new StringBuilder(context.getString(R.string.m222_single));
        } else if ("0".equals(loopType)) {
            loopStyle = new StringBuilder(context.getString(R.string.m224_everyday));
        } else if ("1".equals(loopType)) {
            if (loopValue.equals("1111111")) {
                loopStyle = new StringBuilder(context.getString(R.string.m224_everyday));
            } else {
                char[] chars = loopValue.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (String.valueOf(chars[i]).equals("1")) {
                        String week = CommentUtils.getWeeks(context).get(i);
                        loopStyle.append(week).append(",");
                    }
                }
            }
        } else {
            loopStyle = new StringBuilder("");
        }
        String cycleDay = loopStyle.toString();
        if (cycleDay.endsWith(",")) {
            cycleDay = cycleDay.substring(0, cycleDay.length() - 1);
        }
        baseView.setRepeate(cycleDay);
    }


    private void initResource() {
        onOffArray = new String[]{context.getString(R.string.m167_on), context.getString(R.string.m168_off)};
        double temp = 16;
        double maxTemp = 30;
        if (deviceType.equals(DeviceTypeConstant.TYPE_THERMOSTAT)) {
            setTempRange(tempUnit, mode);
        } else {
            double step = 1;
            while (temp <= maxTemp) {
                temps.add(String.valueOf(temp));
                temp += step;
            }

        }

    }


    /**
     * 根据不同模式和温标配置不同的温度范围
     *
     * @param unit
     * @param mode
     */
    private void setTempRange(String unit, String mode) {
        temps.clear();
        double temp;
        double maxTemp;
        double step;
        if (unit.equals(GlobalConstant.TEMP_UNIT_CELSIUS)) {//摄氏度
            temp = 5.0;
            step = 0.5;
            if (GlobalConstant.MODE_HOLIDAY.equals(mode)) {
                maxTemp = 15;
            } else {
                maxTemp = 40;
            }
        } else {//华氏度
            temp = 41.0;
            step = 1;
            if (GlobalConstant.MODE_HOLIDAY.equals(mode)) {
                maxTemp = 59;
            } else {
                maxTemp = 104;
            }
        }
        while (temp <= maxTemp) {
            temps.add(String.valueOf(temp));
            temp += step;
        }
    }


    public void showTimeSelectDialog() {
        FragmentManager supportFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        dialogFragment = CircleDialogUtils.showWhiteTimeSelect(context, 0, 0, supportFragmentManager,false, new CircleDialogUtils.timeSelectedListener() {
            @Override
            public void cancle() {
                dialogFragment.dismiss();
            }

            @Override
            public void ok(boolean status,int hour, int min) {
                String hourString=hour <10?("0"+hour):hour+"";
                String minString=min <10?("0"+min):min+"";
                timeValue = hourString + ":" + minString;
                baseView.setTimeValue(timeValue);
                dialogFragment.dismiss();
            }
        });

    }


    /**
     * 设置重复
     */
    /**
     * 跳转重复设置
     */
    public void selectRepeat() {
        Intent intent = new Intent(context, RepeatActivity.class);
        intent.putExtra(GlobalConstant.TIME_LOOPTYPE, loopType);
        intent.putExtra(GlobalConstant.TIME_LOOPVALUE, loopValue);
        ActivityUtils.startActivityForResult((Activity) context, intent, GlobalConstant.REQUEST_CODE_SELECT_REPEAT, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GlobalConstant.REQUEST_CODE_SELECT_REPEAT) {
                loopType = data.getStringExtra(GlobalConstant.TIME_LOOPTYPE);
                loopValue = data.getStringExtra(GlobalConstant.TIME_LOOPVALUE);
                setRepeatUI();
            }

        }
    }


    public void setOnOff() {
        CircleDialogUtils.showCommentItemDialog((FragmentActivity) context, Arrays.asList(onOffArray), Gravity.BOTTOM, new OnLvItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        cKey = GlobalConstant.SCENE_DEVICE_OPEN;
                        if (!TextUtils.isEmpty(cValue)) {
                            String value = String.format("%1$s" + GlobalConstant.TEMP_UNIT_CELSIUS, cValue);
                            baseView.setValue(value);
                        }
                        break;
                    case 1:
                        cKey = GlobalConstant.SCENE_DEVICE_SHUT;
                        cValue = null;
                        break;
                }
                baseView.setOnoffViews(onOffArray[position]);
                return true;
            }
        });

    }


    public void selectTemp() {
        PickViewUtils.showPickView((Activity) context, temps, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                cValue = temps.get(options1);
                String value = String.format("%1$s" + GlobalConstant.TEMP_UNIT_CELSIUS, cValue);
                baseView.setValue(value);
                cKey = "set";
            }
        }, App.getInstance().getString(R.string.m215_setting_temperature));
    }



    public void showWarnDialog(){
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m206_delete), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteTiming();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void submitServer(){
        if (TextUtils.isEmpty(baseView.getViewsTimeValue())) {
            MyToastUtils.toast(R.string.m261_timing_not_set);
            return;
        }
        if (TextUtils.isEmpty(baseView.getViewsRepeat())) {
            MyToastUtils.toast(R.string.m262_repeat_not_set);
            return;
        }
        if (TextUtils.isEmpty(baseView.getViewsonffValue())) {
            MyToastUtils.toast(R.string.m264_state_not_set);
            return;
        }
        if (DeviceTypeConstant.TYPE_PANELSWITCH.equals(deviceType)){
            Intent intent=new Intent();
            intent.putExtra(GlobalConstant.DEVICE_ROAD,road);
            intent.putExtra(GlobalConstant.TIME_VALUE,timeValue);
            intent.putExtra(GlobalConstant.TIME_LOOPTYPE,loopType);
            intent.putExtra(GlobalConstant.TIME_LOOPVALUE,loopValue);
            intent.putExtra(GlobalConstant.TIMING_CKEY,cKey);
            intent.putExtra(GlobalConstant.DEVICE_NAME,name);
            ((Activity)context).setResult(Activity.RESULT_OK,intent);
            ((Activity)context).finish();
        }else {
            if (GlobalConstant.EDIT.equals(mAction)) {
                try {
                    editTiming();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    addTiming();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }




    public void deleteTiming() throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("userId", App.getUserBean().getAccountName());
        requestJson.put("cmd", "deleteSmartTask");
        requestJson.put("devId", mDevId);
        requestJson.put("taskId", cid);
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String jsonBean) {
                try {
                    JSONObject object = new JSONObject(jsonBean);
                    int code = object.getInt("code");
                    if (code == 0) {
                        EventBus.getDefault().post(new DeviceTimingMsg());
                        ((Activity)context).finish();
                    }
                    String data = object.getString("data");
                    MyToastUtils.toast(data);
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


    public void addTiming() throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("userId",App.getUserBean().getAccountName());
        requestJson.put("devId", mDevId);
        requestJson.put("devType", deviceType);
        requestJson.put("loopType", loopType);
        requestJson.put("loopValue", loopValue);
        requestJson.put("cKey", cKey);
        requestJson.put("status", "0");
        requestJson.put("timeValue", timeValue);
        requestJson.put("cmd", "addSmartTask");
        requestJson.put("lan", CommentUtils.getLanguage());
        requestJson.put("road", road);
        if (!TextUtils.isEmpty(cValue)){
            requestJson.put("cValue", cValue);
        }

        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String jsonBean) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonBean);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("data");
                    MyToastUtils.toast(msg);
                    if (code == 0) {
                        EventBus.getDefault().post(new DeviceTimingMsg());
                        ((Activity)context).finish();
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


    public void editTiming() throws JSONException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("cid", cid);
        requestJson.put("userId",App.getUserBean().getAccountName());
        requestJson.put("devId", mDevId);
        requestJson.put("devType", deviceType);
        requestJson.put("loopType", loopType);
        requestJson.put("loopValue", loopValue);
        requestJson.put("cKey", cKey);
        requestJson.put("status", "0");
        requestJson.put("timeValue", timeValue);
        requestJson.put("cmd", "updateSmartTask");
        requestJson.put("lan", CommentUtils.getLanguage());
        requestJson.put("road", road);
        if (!TextUtils.isEmpty(cValue)){
            requestJson.put("cValue", cValue);
        }

        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String jsonBean) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonBean);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("data");
                    MyToastUtils.toast(msg);
                    if (code == 0) {
                        EventBus.getDefault().post(new DeviceTimingMsg());
                        ((Activity)context).finish();
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



}
