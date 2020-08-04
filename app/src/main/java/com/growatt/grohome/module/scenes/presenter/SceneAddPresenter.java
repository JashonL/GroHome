package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.FreshScenesMsg;
import com.growatt.grohome.module.device.AllDeviceActivity;
import com.growatt.grohome.module.scenes.SceneTaskSettingActivity;
import com.growatt.grohome.module.scenes.view.ISceneAddView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.mylhyl.circledialog.CircleDialog;
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

public class SceneAddPresenter extends BasePresenter<ISceneAddView> {
    private String sceneType;

    public SceneAddPresenter(ISceneAddView baseView) {
        super(baseView);
    }

    public SceneAddPresenter(Context context, ISceneAddView baseView) {
        super(context, baseView);
    }

    public void getSceneType() {
        sceneType = ((Activity) context).getIntent().getStringExtra(GlobalConstant.SCENE_TYPE);
        baseView.setViewBySceneType(sceneType);
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
        String [] conditions=new String[]{context.getString(R.string.m146_timer),context.getString(R.string.m243_device_status_changes)};
        CircleDialogUtils.showCommentItemDialog((FragmentActivity) context, "", Arrays.asList(conditions), Gravity.BOTTOM, new OnLvItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:

                        break;
                    case 1:
                        selectDevice(deviceSelect);
                        break;
                }
                return true;
            }
        });

    }


    public void selectDevice(String deviceSelect) {
        Intent intent = new Intent(context, AllDeviceActivity.class);
        intent.putExtra(GlobalConstant.SCENE_DEVICE_SELECT, deviceSelect);
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
        int isCondition = 0;
        if (GlobalConstant.SCENE_SMART.equals(sceneType)) {
            isCondition = 1;
        }
        JSONObject requestJson = new JSONObject();
        requestJson.put("cmd", "updateSceneNew");
        requestJson.put("userId", App.getUserBean().getAccountName());
        requestJson.put("status", 0);
        requestJson.put("name", name);
        requestJson.put("onoff", 1);
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
        addDisposable(apiServer.createScene(body), new BaseObserver<String>(baseView, true) {
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

            }
        });
    }

}
