package com.growatt.grohome.app;

import android.app.Activity;
import android.app.Application;

import com.growatt.grohome.bean.User;
import com.growatt.grohome.utils.LogUtil;
import com.hjq.toast.ToastUtils;
import com.mylhyl.circledialog.res.values.CircleColor;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    /*
     * 单例模式获取Application:饿汉式
     */
    public static App app;


    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;

        /*吐司提示*/
        ToastUtils.init(this);

        //初始化涂鸦
        TuyaHomeSdk.init(this);
        TuyaHomeSdk.setDebugMode(true);
        //全局初始化弹框
        initCirclerDialog();

        LogUtil.setIsLog(true);
    }



    /**
     * 防止内存泄漏使用弱引用来存activity
     */

    private List<WeakReference<Activity>>activityList=new ArrayList<>();

    public List<WeakReference<Activity>> getActivityList() {
        return activityList;
    }

    public void addActivityList(WeakReference<Activity> activity) {
        this.activityList .add(activity);
    }

    public void exit(){
        try {
            for (WeakReference weakReference : activityList) {
                Activity activity = (Activity) weakReference.get();
                if(activity != null){
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //注销涂鸦
            TuyaHomeSdk.onDestroy();
            System.exit(0);
        }
    }

    /**
     * 设置弹框按钮颜色等
     */
    private void initCirclerDialog() {
        CircleColor.ITEM_CONTENT_TEXT = 0xFF007AFF;
        CircleColor.FOOTER_BUTTON_TEXT_POSITIVE = 0xFF4B814B;
        CircleColor.FOOTER_BUTTON_TEXT_NEGATIVE = 0xFF4B814B;
    }


    /**
     * 保存全局用户
     */
    public static User userBean;

    public static User getUserBean() {
        return userBean;
    }

    public static void setUserBean(User userBean) {
        App.userBean = userBean;
    }
}
