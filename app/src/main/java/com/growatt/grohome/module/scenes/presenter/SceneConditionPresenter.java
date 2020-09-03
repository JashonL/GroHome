package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.PanelSwitchBean;
import com.growatt.grohome.bean.SceneBulbSetInfo;
import com.growatt.grohome.bean.SceneConditionBean;
import com.growatt.grohome.bean.ScenesRoadBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.scenes.view.ISceneConditionView;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
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

    //模式选择
    private String mode;
    private String temp;
    private String bright;
    private String countdown;

    private DialogFragment dialogFragment;

    private String[] modes;

    public SceneConditionPresenter(ISceneConditionView baseView) {
        super(baseView);
    }

    public SceneConditionPresenter(Context context, ISceneConditionView baseView) {
        super(context, baseView);
        modes = new String[]{context.getString(R.string.m298_white), context.getString(R.string.m298_colour), context.getString(R.string.m10_scenes)};
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
            baseView.setSocketUi(linkType);
            SceneBulbSetInfo setInfo = mSceneCondition.getSetInfo();
            if (setInfo != null) {
                mode = setInfo.getMode();
                int index;
                if (!TextUtils.isEmpty(mode)) {
                    String[] s = mode.split("_");
                    if (s.length >= 3) {
                        try {
                            int isEnable = Integer.parseInt(s[0]);
                            if (isEnable != 0){
                                index = Integer.parseInt(s[2]);
                                baseView.selectedMode(modes[index - 1]);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
                temp = setInfo.getTemp();
                if (!TextUtils.isEmpty(temp)) {
                    String[] s = temp.split("_");
                    if (s.length >= 3) {
                        try {
                            int isEnable = Integer.parseInt(s[0]);
                            if (isEnable != 0){
                                String symbol = s[1];
                                int value = Integer.parseInt(s[2]);
                                String currenTemp = symbol +"  "+ value;
                                baseView.setTemp(currenTemp);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
                bright = setInfo.getBright();
                if (!TextUtils.isEmpty(bright)) {
                    String[] s = bright.split("_");
                    if (s.length >= 3) {
                        try {
                            int isEnable = Integer.parseInt(s[0]);
                            if (isEnable != 0){
                                String symbol = s[1];
                                int value = Integer.parseInt(s[2]);
                                String currenBright = symbol +"  "+ value;
                                baseView.setBright(currenBright);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
                countdown = setInfo.getCountdown();
                if (!TextUtils.isEmpty(countdown)) {
                    String[] s = countdown.split("_");
                    if (s.length >= 3) {
                        try {
                            int isEnable = Integer.parseInt(s[0]);
                            if (isEnable != 0) {
                                String symbol = s[1];
                                int value = Integer.parseInt(s[2]);
                                String leftTime = symbol + "  " + value + "  " + context.getString(R.string.m303_second);
                                baseView.setCountDown(leftTime);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
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
                baseView.onError(msg);
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
            case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
            case DeviceTypeConstant.TYPE_BULB:
                if (!baseView.getSwitchChecked() && !baseView.getBrightChecked() && !baseView.getTimeChecked() && !baseView.getTempChecked()) {
                    MyToastUtils.toast(R.string.m299_least_one_condition);
                    return;
                }
                if (!baseView.getSwitchChecked()){
                    bean.setLinkType(linkType);
                }

                SceneBulbSetInfo setInfo = new SceneBulbSetInfo();
                if (!baseView.getModeChecked()||TextUtils.isEmpty(mode)) {
                    mode="0_equal_1";
                }
                if (!baseView.getBrightChecked()||TextUtils.isEmpty(bright)) {
                    bright="0_equal_10";
                }

                if (!baseView.getTimeChecked()||TextUtils.isEmpty(countdown)) {
                    countdown="0_equal_0";
                }

                if (!baseView.getTempChecked()||TextUtils.isEmpty(temp)) {
                    temp="0_equal_0";
                }
                setInfo.setMode(mode);
                setInfo.setBright(bright);
                setInfo.setCountdown(countdown);
                setInfo.setTemp(temp);
                bean.setSetInfo(setInfo);
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


    /**
     * 设置模式
     */

    public void setDeviceMode() {
        View bodyView = LayoutInflater.from(context).inflate(R.layout.item_dialog_select, null, false);
        dialogFragment = CircleDialogUtils.showCommentBodyDialog(bodyView, ((FragmentActivity) context).getSupportFragmentManager(), new OnCreateBodyViewListener() {
            @Override
            public void onCreateBodyView(View view) {
                RadioButton radioWhite = view.findViewById(R.id.radio_white);
                RadioButton radioColour = view.findViewById(R.id.radio_colour);
                RadioButton radioScenes = view.findViewById(R.id.radio_scenes);
                int index;
                if (!TextUtils.isEmpty(mode)) {
                    String[] s = mode.split("_");
                    if (s.length >= 3) {
                        try {
                            int isEnable = Integer.parseInt(s[0]);
                            if (isEnable != 0) {
                                index = Integer.parseInt(s[2]);
                                switch (index) {
                                    case 1:
                                        radioWhite.setChecked(true);
                                        break;
                                    case 2:
                                        radioColour.setChecked(true);
                                        break;
                                    case 3:
                                        radioScenes.setChecked(true);
                                        break;
                                }
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                }

                view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogFragment.dismiss();
                    }
                });

                view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mode = null;
                        baseView.selectedMode("");
                        dialogFragment.dismiss();
                    }
                });

                view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (radioWhite.isChecked()) {
                            mode = "1_equal_" + 1;
                            baseView.selectedMode(modes[0]);
                        }
                        if (radioColour.isChecked()) {
                            mode = "1_equal_" + 2;
                            baseView.selectedMode(modes[1]);
                        }
                        if (radioScenes.isChecked()) {
                            baseView.selectedMode(modes[2]);
                            mode = "1_equal_" + 3;
                        }
                        dialogFragment.dismiss();
                    }
                });
            }
        });
    }


    public void setBrightValue() {
        View bodyView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_progress, null, false);
        dialogFragment = CircleDialogUtils.showCommentBodyDialog(bodyView, ((FragmentActivity) context).getSupportFragmentManager(), new OnCreateBodyViewListener() {
            @Override
            public void onCreateBodyView(View view) {
                TextView tvTitle = view.findViewById(R.id.tv_title);
                RadioGroup radioGroup = view.findViewById(R.id.rg_compared);
                TextView tvValue = view.findViewById(R.id.tv_value);
                AppCompatSeekBar seekPercent = view.findViewById(R.id.seek_percent);
                TextView btnCancel = view.findViewById(R.id.btn_cancel);
                TextView btnDelete = view.findViewById(R.id.btn_delete);
                TextView btnOk = view.findViewById(R.id.btn_ok);
                TextView tvMinus = view.findViewById(R.id.tv_minus);
                TextView tvPlus = view.findViewById(R.id.tv_plus);
                tvTitle.setText(R.string.m91_bright_ness);
                tvValue.setText(0 + "");
                seekPercent.setMax(990);
                radioGroup.check(R.id.rb_equal);
                if (!TextUtils.isEmpty(bright)) {
                    String[] s = bright.split("_");
                    if (s.length >= 3) {
                        try {
                            int enable=Integer.parseInt(s[0]);
                            if (enable==0)return;
                            int value = Integer.parseInt(s[2]);
                            seekPercent.setProgress(value - 10);
                            tvValue.setText(value + "");
                            String symbol=s[1];
                            switch (symbol){
                                case "less":
                                    radioGroup.check(R.id.rb_less);
                                    break;
                                case "equal":
                                    radioGroup.check(R.id.rb_equal);
                                    break;
                                case "greater":
                                    radioGroup.check(R.id.rb_greater);
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }

                tvMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int progress = seekPercent.getProgress();
                        seekPercent.setProgress(--progress);
                    }
                });

                tvPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int progress = seekPercent.getProgress();
                        seekPercent.setProgress(++progress);
                    }
                });


                seekPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        String value = String.valueOf(progress + 10);
                        tvValue.setText(value);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogFragment.dismiss();
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bright = null;
                        baseView.setBright("");
                        dialogFragment.dismiss();
                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        String symbol = "less";
                        switch (checkedRadioButtonId) {
                            case R.id.rb_less:
                                symbol = "less";
                                break;
                            case R.id.rb_equal:
                                symbol = "equal";
                                break;
                            case R.id.rb_greater:
                                symbol = "greater";
                                break;
                        }
                        int progress = seekPercent.getProgress() + 10;
                        String value = symbol + " " + progress;
                        baseView.setBright(value);
                        bright = "1_" + symbol + "_" + progress;
                        dialogFragment.dismiss();
                    }
                });
            }
        });
    }


    /**
     * 进度框
     */

    public void setTempValue() {
        View bodyView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_progress, null, false);
        dialogFragment = CircleDialogUtils.showCommentBodyDialog(bodyView, ((FragmentActivity) context).getSupportFragmentManager(), new OnCreateBodyViewListener() {
            @Override
            public void onCreateBodyView(View view) {
                TextView tvTitle = view.findViewById(R.id.tv_title);
                RadioGroup radioGroup = view.findViewById(R.id.rg_compared);
                TextView tvValue = view.findViewById(R.id.tv_value);
                AppCompatSeekBar seekPercent = view.findViewById(R.id.seek_percent);
                TextView btnCancel = view.findViewById(R.id.btn_cancel);
                TextView btnDelete = view.findViewById(R.id.btn_delete);
                TextView btnOk = view.findViewById(R.id.btn_ok);
                TextView tvMinus = view.findViewById(R.id.tv_minus);
                TextView tvPlus = view.findViewById(R.id.tv_plus);
                tvTitle.setText(R.string.m92_colour_temp);
                tvValue.setText(0 + "");
                seekPercent.setMax(1000);
                radioGroup.check(R.id.rb_equal);
                if (!TextUtils.isEmpty(temp)) {
                    String[] s = temp.split("_");
                    if (s.length >= 3) {
                        try {
                            int enable=Integer.parseInt(s[0]);
                            if (enable==0)return;
                            int value = Integer.parseInt(s[2]);
                            seekPercent.setProgress(value);
                            tvValue.setText(value + "");
                            String symbol=s[1];
                            switch (symbol){
                                case "less":
                                    radioGroup.check(R.id.rb_less);
                                    break;
                                case "equal":
                                    radioGroup.check(R.id.rb_equal);
                                    break;
                                case "greater":
                                    radioGroup.check(R.id.rb_greater);
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }

                tvMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int progress = seekPercent.getProgress();
                        seekPercent.setProgress(--progress);
                    }
                });

                tvPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int progress = seekPercent.getProgress();
                        seekPercent.setProgress(++progress);
                    }
                });

                seekPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        String value = progress + "";
                        tvValue.setText(value);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogFragment.dismiss();
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp = null;
                        baseView.setTemp("");
                        dialogFragment.dismiss();
                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        String symbol = "less";
                        switch (checkedRadioButtonId) {
                            case R.id.rb_less:
                                symbol = "less";
                                break;
                            case R.id.rb_equal:
                                symbol = "equal";
                                break;
                            case R.id.rb_greater:
                                symbol = "greater";
                                break;
                        }

                        int progress = seekPercent.getProgress();
                        String value = symbol + " " + progress;
                        baseView.setTemp(value);
                        temp = "1_" + symbol + "_" + progress;
                        dialogFragment.dismiss();
                    }
                });
            }
        });
    }


    public void setTime() {
        View bodyView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_progress, null, false);
        dialogFragment = CircleDialogUtils.showCommentBodyDialog(bodyView, ((FragmentActivity) context).getSupportFragmentManager(), new OnCreateBodyViewListener() {
            @Override
            public void onCreateBodyView(View view) {
                TextView tvTitle = view.findViewById(R.id.tv_title);
                RadioGroup radioGroup = view.findViewById(R.id.rg_compared);
                TextView tvValue = view.findViewById(R.id.tv_value);
                AppCompatSeekBar seekPercent = view.findViewById(R.id.seek_percent);
                TextView btnCancel = view.findViewById(R.id.btn_cancel);
                TextView btnDelete = view.findViewById(R.id.btn_delete);
                TextView btnOk = view.findViewById(R.id.btn_ok);
                TextView tvMinus = view.findViewById(R.id.tv_minus);
                TextView tvPlus = view.findViewById(R.id.tv_plus);
                tvTitle.setText(R.string.m145_left_time);
                radioGroup.check(R.id.rb_equal);
                tvValue.setText(0 + "");
                seekPercent.setMax(86400);
                if (!TextUtils.isEmpty(countdown)) {
                    String[] s = countdown.split("_");
                    if (s.length >= 3) {
                        try {
                            int enable=Integer.parseInt(s[0]);
                            if (enable==0)return;
                            int value = Integer.parseInt(s[2]);
                            seekPercent.setProgress(value);
                            String time = value + "  " + context.getString(R.string.m303_second);
                            tvValue.setText(time);
                            String symbol=s[1];
                            switch (symbol){
                                case "less":
                                    radioGroup.check(R.id.rb_less);
                                    break;
                                case "equal":
                                    radioGroup.check(R.id.rb_equal);
                                    break;
                                case "greater":
                                    radioGroup.check(R.id.rb_greater);
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }

                tvMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int progress = seekPercent.getProgress();
                        seekPercent.setProgress(--progress);
                    }
                });

                tvPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int progress = seekPercent.getProgress();
                        seekPercent.setProgress(++progress);
                    }
                });

                seekPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        String value = progress + "  " + context.getString(R.string.m303_second);
                        tvValue.setText(value);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogFragment.dismiss();
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countdown = null;
                        baseView.setCountDown("");
                        dialogFragment.dismiss();
                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        String symbol = "less";
                        switch (checkedRadioButtonId) {
                            case R.id.rb_less:
                                symbol = "less";
                                break;
                            case R.id.rb_equal:
                                symbol = "equal";
                                break;
                            case R.id.rb_greater:
                                symbol = "greater";
                                break;
                        }

                        int progress = seekPercent.getProgress();
                        String value = symbol + "  " + progress + "  " + context.getString(R.string.m303_second);
                        baseView.setCountDown(value);
                        countdown = "1_" + symbol + "_" + progress;
                        dialogFragment.dismiss();
                    }
                });
            }
        });
    }


    public void showTimeSelect() {
        int hour = 0;
        int min = 0;
        if (!TextUtils.isEmpty(countdown) && !"0".equals(countdown)) {
            String[] s = countdown.split("_");
            if (s.length >= 3) {
                int time = Integer.parseInt(s[2]);
                hour = time / (60 * 60);
                min = (time % (60 * 60)) / (60);
            }
        }
        FragmentManager supportFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        dialogFragment = CircleDialogUtils.showWhiteTimeSelect(context, hour, min, supportFragmentManager, true, new CircleDialogUtils.timeSelectedListener() {
            @Override
            public void cancle() {
                dialogFragment.dismiss();
            }

            @Override
            public void ok(boolean status, int hour, int min) {
                if (status) {
                    int value = hour * 3600 + min * 60;
                    countdown = "1_equal_" + value;
                    baseView.setCountDown(hour + " h " + min + " min ");
                }
                dialogFragment.dismiss();
            }
        });
    }

}
