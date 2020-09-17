package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.SceneConditionBean;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.bean.ScenesBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.FreshScenesMsg;
import com.growatt.grohome.module.device.AllDeviceActivity;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.scenes.EffectivePeriodActivity;
import com.growatt.grohome.module.scenes.SceneConditionActivity;
import com.growatt.grohome.module.scenes.SceneTaskSettingActivity;
import com.growatt.grohome.module.scenes.view.ISceneDetailView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SceneDetailPresenter extends BasePresenter<ISceneDetailView> {

    private String sceneType = GlobalConstant.SCENE_LUANCH_TAP_TO_RUN;
    private int satisfy;
    private ScenesBean.DataBean scenesBean;
    private String status;

    public SceneDetailPresenter(ISceneDetailView baseView) {
        super(baseView);
    }

    public SceneDetailPresenter(Context context, ISceneDetailView baseView) {
        super(context, baseView);
    }


    public void getSceneType() {
        String sceneJson = ((Activity) context).getIntent().getStringExtra(GlobalConstant.SCENE_BEAN);
        if (!TextUtils.isEmpty(sceneJson)) {
            scenesBean = new Gson().fromJson(sceneJson, ScenesBean.DataBean.class);
            if ("0".equals(scenesBean.getIsCondition())) {
                sceneType = GlobalConstant.SCENE_LUANCH_TAP_TO_RUN;
            } else {
                sceneType = GlobalConstant.SCENE_SMART;
            }
            status = scenesBean.getStatus();
            satisfy= Integer.parseInt(scenesBean.getSatisfy());
            baseView.setStatus(status);
            baseView.setViewBySceneType(sceneType);
            baseView.updataViews(scenesBean);
            baseView.setConditionMet(satisfy);
        }
    }


    /**
     * 设置是否启用
     */
    public void setStatus() {
        if ("0".equals(status)) {
            status = "1";
        } else {
            status = "0";
        }
        baseView.setStatus(status);
    }


    public void showInuptNameDialog() {
        String name = baseView.getName();
        if (TextUtils.isEmpty(name)) name = "";
        CircleDialogUtils.showCommentInputDialog((FragmentActivity) context, context.getString(R.string.m211_scene_name), name,
                context.getString(R.string.m233_please_entry_name), true, new OnInputClickListener() {
                    @Override
                    public boolean onClick(String text, View v) {
                        if (!TextUtils.isEmpty(text)) {
                            baseView.setSceneName(text);
                        }
                        return true;
                    }
                });
    }


    public void addCondition(String deviceSelect) {
        String[] conditions = new String[]{context.getString(R.string.m146_timer), context.getString(R.string.m243_device_status_changes)};
        CircleDialogUtils.showCommentItemDialog((FragmentActivity) context, context.getString(R.string.m244_add_new_condition), Arrays.asList(conditions), Gravity.CENTER, new OnLvItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectTime(deviceSelect);
                        break;
                    case 1:
                        selectDevice(deviceSelect);
                        break;
                }
                return true;
            }
        });

    }


    public void selectConditionMet() {
        String[] conditions = new String[]{context.getString(R.string.m217_all_conditions_are_met), context.getString(R.string.m218_any_conditions_is_met)};
        CircleDialogUtils.showCommentItemDialog((FragmentActivity) context, context.getString(R.string.m244_add_new_condition), Arrays.asList(conditions), Gravity.CENTER, new OnLvItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
                satisfy = position;
                baseView.setConditionMet(satisfy);
                return true;
            }
        });

    }


    public void selectTime(String deviceSelect) {
        Intent intent = new Intent(context, EffectivePeriodActivity.class);
        intent.putExtra(GlobalConstant.SCENE_DEVICE_SELECT, deviceSelect);
        intent.putExtra(GlobalConstant.SET_TIMEVALUE_OR_TIMEPERIOD, GlobalConstant.SET_TIMEVALUE);
        ActivityUtils.startActivityForResult((Activity) context, intent, GlobalConstant.REQUEST_CODE_EDIT_SCENE_TIME, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void selectDevice(String deviceSelect) {
        Intent intent = new Intent(context, AllDeviceActivity.class);
        intent.putExtra(GlobalConstant.SCENE_DEVICE_SELECT, deviceSelect);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    public void toEditCondition(SceneConditionBean sceneConditionBean) {
        if (sceneConditionBean == null) return;
        String devType = sceneConditionBean.getDevType();
        if (DeviceTypeConstant.TYPE_TIME.equals(devType)) {
            toEditConditionTime(sceneConditionBean);
        } else {
            toEditConditionDevice(sceneConditionBean);
        }
    }


    private void toEditConditionTime(SceneConditionBean sceneConditionBean) {
        Intent intent = new Intent(context, EffectivePeriodActivity.class);
        intent.putExtra(GlobalConstant.SCENE_DEVICE_SELECT, GlobalConstant.SCENE_EDIT);
        intent.putExtra(GlobalConstant.SET_TIMEVALUE_OR_TIMEPERIOD, GlobalConstant.SET_TIMEVALUE);
        intent.putExtra(GlobalConstant.TIME_VALUE, sceneConditionBean.getTimeValue());
        intent.putExtra(GlobalConstant.TIME_LOOPTYPE, sceneConditionBean.getLinkType());
        intent.putExtra(GlobalConstant.TIME_LOOPVALUE, sceneConditionBean.getLinkValue());
        ActivityUtils.startActivityForResult((Activity) context, intent, GlobalConstant.REQUEST_CODE_EDIT_SCENE_TIME, ActivityUtils.ANIMATE_FORWARD, false);
    }


    private void toEditConditionDevice(SceneConditionBean sceneConditionBean) {
        String bean = new Gson().toJson(sceneConditionBean);
        Intent intent = new Intent(context, SceneConditionActivity.class);
        intent.putExtra(GlobalConstant.SCENE_CONDITION_BEAN, bean);
        intent.putExtra(GlobalConstant.SCENE_CREATE_OR_EDIT, GlobalConstant.SCENE_EDIT);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void toEditConfig(SceneTaskBean sceneTaskBean) {
        if (sceneTaskBean == null) return;
        String bean = new Gson().toJson(sceneTaskBean);
        Intent intent = new Intent(context, SceneTaskSettingActivity.class);
        intent.putExtra(GlobalConstant.SCENE_TASK_BEAN, bean);
        intent.putExtra(GlobalConstant.SCENE_CREATE_OR_EDIT, GlobalConstant.SCENE_EDIT);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void deleteDevice(int pos) {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m95_tips), context.getString(R.string.m210_delete_device), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseView.deleteTaskDevice(pos);
            }
        });
    }


    public void deleteCondition(int pos) {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m95_tips), context.getString(R.string.m210_delete_device), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseView.deleteCondition(pos);
            }
        });
    }

    /**
     * 添加场景模式
     */
    public void upScenesData() throws JSONException {
        String name = baseView.getName();
        if (TextUtils.isEmpty(name)) {
            MyToastUtils.toast(R.string.m233_please_entry_name);
            return;
        }
        List<SceneTaskBean> conf = baseView.getData();
        if (conf.isEmpty()) {
            MyToastUtils.toast(R.string.m242_please_set_task);
            return;
        }
        JSONObject requestJson = new JSONObject();
        requestJson.put("cmd", "updateSceneNew");
        requestJson.put("userId", App.getUserBean().getAccountName());
        requestJson.put("cid", scenesBean.getCid());
        requestJson.put("status", status);
        requestJson.put("name", name);
        requestJson.put("onoff", 1);
        int isCondition;
        if (GlobalConstant.SCENE_SMART.equals(sceneType)) {
            isCondition = 1;
            List<SceneConditionBean> condition = baseView.getConditionBean();
            if (condition.isEmpty()) {
                MyToastUtils.toast(R.string.m242_please_set_task);
                return;
            }
            if (condition.size() == 1) satisfy = 0;
            requestJson.put("satisfy", satisfy);
            JSONArray jsonArray1 = new JSONArray();
            if (condition.size() > 0) {
                for (SceneConditionBean bean : condition) {
                    String json = new Gson().toJson(bean);
                    JSONObject deviceJson = new JSONObject(json);
                    jsonArray1.put(deviceJson);
                }

                requestJson.put("conditionconf", jsonArray1);
            }
        } else {
            isCondition = 0;
        }
        requestJson.put("isCondition", isCondition);
        requestJson.put("lan", CommentUtils.getLanguage());
        JSONArray jsonArray = new JSONArray();
        if (conf.size() > 0) {
            for (SceneTaskBean bean : conf) {
                String json = new Gson().toJson(bean);
                JSONObject deviceJson = new JSONObject(json);
                jsonArray.put(deviceJson);
            }

            requestJson.put("conf", jsonArray);
        }
        String jsonRequest = requestJson.toString().replace("\\/", "/");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonRequest);
        addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject object = new JSONObject(bean);
                    int code = object.getInt("code");
                    if (code == 0) {
                        EventBus.getDefault().post(new FreshScenesMsg());
                        ((Activity) context).finish();
                    }
                    String data = object.getString("data");
                    baseView.addSceneResult(data);
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
     * 删除场景
     */
    public void deleteScene() {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m206_delete), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    requestDelete();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 删除场景模式
     */
    private void requestDelete() throws JSONException {
        //封装json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd", "deleteScense");
        jsonObject.put("cid", scenesBean.getCid());
        jsonObject.put("lan", CommentUtils.getLanguage());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String jsonBean) {
                try {
                    JSONObject object = new JSONObject(jsonBean);
                    int code = object.getInt("code");
                    if (code == 0) {
                        FreshScenesMsg bean = new FreshScenesMsg();
                        EventBus.getDefault().post(bean);
                        ((Activity)context).finish();
                    }
                    String data = object.getString("data");
                    MyToastUtils.toast(data);
                } catch (JSONException e) {
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
