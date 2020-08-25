package com.growatt.grohome.module.scenes.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.ScenesBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.scenes.SceneAddActivity;
import com.growatt.grohome.module.scenes.SceneDetailActivity;
import com.growatt.grohome.module.scenes.view.IScenesView;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                                    if ("0".equals(status)){
                                        bean.setItemType(1);
                                    }else {
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
    public void upScenesData(int position,ScenesBean.DataBean dataBean) throws JSONException {
        String requestJson = new Gson().toJson(dataBean);
        //封装json
        JSONObject jsonObject = new JSONObject(requestJson);
        jsonObject.put("cmd","updateSceneNew");
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
                       baseView.updataSuccess(position,dataBean);
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
