package com.growatt.grohome.constants;

import android.Manifest;
import android.annotation.SuppressLint;

/**
 * 所有申请回调的申请码
 */
public class AllPermissionRequestCode {

    /**
     * 相机和存储权限
     */
    public static final String[] PERMISSION_CAMERA = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 单独存储权限
     */
    public static final String[] PERMISSION_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 存储权限+安装权限
     */
    @SuppressLint("InlinedApi")
    public static final String[] PERMISSION_EXTERNAL_STORAGE_INSTALL = {Manifest.permission.REQUEST_INSTALL_PACKAGES};
    /**
     * 单独相机权限
     */
    public static final String PERMISSION_CAMERA_ONE = Manifest.permission.CAMERA;
    /**
     * 定位权限 + 存储权限 + 手机状态权限组
     */
    public static final String PERMISSION_LOCATION[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};



    //多个权限请求码
    public static final int ALL_PERMISSION_CODE=1000;

    //请求存储权限
    public static final int PERMISSION_EXTERNAL_STORAGE_CODE = 1001;

    //请求相机权限
    public static final int PERMISSION_CAMERA_CODE = 1002;

    //位置权限
    public static final int PERMISSION_LOCATION_CODE = 1003;

}
