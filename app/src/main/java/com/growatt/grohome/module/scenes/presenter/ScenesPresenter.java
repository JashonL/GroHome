package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.LogsSceneBean;
import com.growatt.grohome.bean.ScenesBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.scenes.SceneAddActivity;
import com.growatt.grohome.module.scenes.SceneDetailActivity;
import com.growatt.grohome.module.scenes.view.IScenesView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ScenesPresenter extends BasePresenter<IScenesView> {

    public ScenesPresenter(Context context, IScenesView baseView) {
        super(context, baseView);
    }

    public void addScene(String type) {
        Intent intent = new Intent(context, SceneAddActivity.class);
        intent.putExtra(GlobalConstant.SCENE_TYPE, type);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void toSceneDetail(ScenesBean.DataBean dataBean) {
        String beanJson = new Gson().toJson(dataBean);
        Intent intent = new Intent(context, SceneDetailActivity.class);
        intent.putExtra(GlobalConstant.SCENE_BEAN, beanJson);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    /**
     * 获取场景列表
     *
     * @throws Exception
     */
    public void getSceneList() throws Exception {
        JSONObject requestJson = new JSONObject();
        requestJson.put("userId", App.getUserBean().getAccountName());
        requestJson.put("cmd", "selectScene");
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
                        ScenesBean scenesBean = new Gson().fromJson(jsonBean, ScenesBean.class);
                        if (scenesBean != null) {
                            List<ScenesBean.DataBean> data = scenesBean.getData();
                            if (data != null) {
                                List<ScenesBean.DataBean> onClicklist = new ArrayList<>();
                                List<ScenesBean.DataBean> conditionList = new ArrayList<>();
                                for (ScenesBean.DataBean bean : data) {
                                    String status = bean.getStatus();
                                    if ("0".equals(status)) {
                                        bean.setItemType(1);
                                    } else {
                                        bean.setItemType(0);
                                    }
                                    if (TextUtils.isEmpty(bean.getIsCondition()))
                                        bean.setIsCondition("1");
                                    if ("0".equals(bean.getIsCondition())) {
                                        onClicklist.add(bean);
                                    } else {
                                        conditionList.add(bean);
                                    }
                                }
                                baseView.upDataLaunch(onClicklist);
                                baseView.upDataSmart(conditionList);
                            }
                        }
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
     * 获取场景执行日志
     *
     * @throws Exception
     */
    public void getSceneLogList() throws Exception {
        JSONObject requestJson = new JSONObject();
        requestJson.put("userId", App.getUserBean().getAccountName());
        requestJson.put("cmd", "sceneLogList");
        requestJson.put("lan", CommentUtils.getLanguage());
        String s = requestJson.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
                addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView,
                        true) {
                    @Override
                    public void onSuccess(String jsonBean) {
                        try {
                            JSONObject object = new JSONObject(jsonBean);
                            int code = object.getInt("code");
                            if (code == 0) {
                                JSONArray array = object.optJSONArray("data");
                                Map<String,List<LogsSceneBean>> listMap=new LinkedHashMap<>();
                                List<String>timeList=new ArrayList<>();
                                List<LogsSceneBean> logs = new ArrayList<>();
                                if (array != null) {
                                    String time = "";
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject = array.optJSONObject(i);
                                        String runTime = jsonObject.optString("runTime", "0");
                                        String[] s1 = runTime.split(" ");
                                        if (!time.equals(s1[0])) {
                                            time=s1[0];
                                            List<LogsSceneBean> list=new ArrayList<>();
                                            listMap.put(time,list);
                                            timeList.add(time);
                                        }
                                        List<LogsSceneBean> logsSceneBeans = listMap.get(s1[0]);
                                        LogsSceneBean bean = new LogsSceneBean();
                                        bean.setCname(jsonObject.optString("cname", ""));
                                        bean.setRunStatus(jsonObject.optString("runStatus", "0"));
                                        bean.setRunTime(s1[1]);
                                        bean.setDataType(GlobalConstant.SCENE_LOG_TYPE_BODY);
                                        bean.setItemType(GlobalConstant.STATUS_ITEM_DATA);
                                        if (logsSceneBeans!=null){
                                            logsSceneBeans.add(bean);
                                        }
                                    }
                                }
                                for (String time:timeList) {
                                    List<LogsSceneBean> logsSceneBeans = listMap.get(time);
                                    if (logsSceneBeans!=null){
                                        int size = logsSceneBeans.size();
                                        if (size==1){
                                            logsSceneBeans.get(0).setDataType(GlobalConstant.SCENE_LOG_TYPE_SINGLE);
                                        }else {
                                            logsSceneBeans.get(0).setDataType(GlobalConstant.SCENE_LOG_TYPE_HEADER);
                                            logsSceneBeans.get(size-1).setDataType(GlobalConstant.SCENE_LOG_TYPE_FOOT);
                                        }
                                        LogsSceneBean titlebean = new LogsSceneBean();
                                        titlebean.setRunTime(time);
                                        titlebean.setItemType(GlobalConstant.STATUS_ITEM_OTHER);
                                        logsSceneBeans.add(0,titlebean);
                                        logs.addAll(logsSceneBeans);
                                    }

                                }
                                baseView.updataLogs(logs);
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
     * 一键执行
     *
     * @param dataBean
     */

    public void launchTapToRun(ScenesBean.DataBean dataBean) {
        //封装json
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "fireScene");
            jsonObject.put("sceneId", dataBean.getCid());
            jsonObject.put("userId", dataBean.getUserId());
            jsonObject.put("onoff", 1);
            jsonObject.put("lan", CommentUtils.getLanguage());
            String jsonRequest = jsonObject.toString().replace("\\/", "/");
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonRequest);
            addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
                @Override
                public void onSuccess(String bean) {
                    try {
                        JSONObject object = new JSONObject(bean);
                        int code = object.getInt("code");
                        if (code == 0) {
                            baseView.launchTapToRunSuccess(dataBean);

                        } else {
                            String data = object.getString("data");
                            MyToastUtils.toast(data);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改场景模式
     */
    public void upScenesData(int position, ScenesBean.DataBean dataBean) throws JSONException {
        String requestJson = new Gson().toJson(dataBean);
        //封装json
        JSONObject jsonObject = new JSONObject(requestJson);
        jsonObject.put("cmd", "updateSceneNew");
        jsonObject.put("userId", App.getUserBean().getAccountName());
        jsonObject.put("onoff", 1);
        jsonObject.put("lan", CommentUtils.getLanguage());
        String jsonRequest = jsonObject.toString().replace("\\/", "/");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonRequest);
        addDisposable(apiServer.smartHomeRequest(body), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject object = new JSONObject(bean);
                    int code = object.getInt("code");
                    if (code == 0) {
                        baseView.updataSuccess(position, dataBean);
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


}
