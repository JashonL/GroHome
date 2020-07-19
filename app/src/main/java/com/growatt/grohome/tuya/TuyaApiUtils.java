package com.growatt.grohome.tuya;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

import java.util.List;

public class TuyaApiUtils {

    public static final String TUYA_TAG="tuya";

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
     * 涂鸦注册登录一体，如果已经注册会自动登录
     * @param context
     * @param countryCode
     * @param userName
     * @param password
     * @param callback
     */

    public static void autoLogin(Context context,String countryCode, String userName, String password,ILoginCallback callback){
        TuyaHomeSdk.getUserInstance().loginOrRegisterWithUid(countryCode,  userName,  password,  callback);
    }


    /**
     * 获取家庭列表
     */
    public static void getHomeList(@NonNull final ITuyaGetHomeListCallback callback) {
        FamilyManager.getInstance().getHomeList(callback);
    }

    /**
     * 初始化一个家庭
     */

    public static void getHomeDetail(long homeId, ITuyaHomeResultCallback callback) {
        TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(callback);
    }


    /**
     * 创建一个家庭
     */
    public static void createHome(String homeName, List<String> roomList, ITuyaHomeResultCallback callback) {
        FamilyManager.getInstance().createHome(homeName, roomList, callback);
    }

    public static long getHomeId(){
        return FamilyManager.getInstance().getCurrentHomeId();
    }


    //是否已经成功登录到涂鸦
    private static boolean isTuyaLogin = false;

    public static boolean isIsTuyaLogin() {
        return isTuyaLogin;
    }

    public static void setIsTuyaLogin(boolean isTuyaLogin) {
        TuyaApiUtils.isTuyaLogin = isTuyaLogin;
    }


    private static boolean isHomeInit = false;

    public static boolean isIsHomeInit() {
        return isHomeInit;
    }

    public static void setIsHomeInit(boolean isHomeInit) {
        TuyaApiUtils.isHomeInit = isHomeInit;
    }

}
