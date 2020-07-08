package com.growatt.grohome.app;

import android.app.Activity;
import android.app.Application;

import com.hjq.toast.ToastUtils;
import com.yechaoa.yutils.ActivityUtil;
import com.yechaoa.yutils.LogUtil;
import com.yechaoa.yutils.YUtils;

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

        //初始化
        YUtils.initialize(this);
        //设置打印开关
        LogUtil.setIsLog(true);
        //注册Activity生命周期
        registerActivityLifecycleCallbacks(ActivityUtil.getActivityLifecycleCallbacks());
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
            System.exit(0);
        }
    }

}
