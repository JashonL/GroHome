package com.growatt.grohome.tuya;

import android.content.Context;

import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

public class TuyaApiUtils {


    /**
     * 注册到涂鸦
     *
     * @param context
     * @param userName         用户名
     * @param password         密码
     * @param registerCallback 注册回调处理
     */

    public static void registerTuya(Context context, String userName, String password, IRegisterCallback registerCallback) {
        TuyaHomeSdk.getUserInstance().registerAccountWithUid("86", userName, password, registerCallback);
    }

    /**
     * 登录到涂鸦
     *
     * @param context
     * @param userName 用户名
     * @param password 密码
     */

    public static void loginTuya(Context context, String userName, String password, ILoginCallback iLoginCallback) {
        TuyaHomeSdk.getUserInstance().loginWithUid("86", userName, password, iLoginCallback);
    }


    /**
     * 初始化一个家庭
     */

    public static void getHomeDetail(long homeId, ITuyaHomeResultCallback callback) {
        TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(callback);
    }


}
