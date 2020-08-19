package com.growatt.grohome.module.personal.presenter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.AllPermissionRequestCode;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.login.RegisterLoginActivity;
import com.growatt.grohome.module.personal.NewEmailActivity;
import com.growatt.grohome.module.personal.SetttingActivity;
import com.growatt.grohome.module.personal.UpdatepwdActivity;
import com.growatt.grohome.module.personal.view.ISettingView;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.PhotoUtil;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class SettingPresenter extends BasePresenter<ISettingView> {


    private Uri imageUri;

    //请求码
    public static final int CODE_GALLERY_REQUEST = 101;
    public static final int CODE_CAMERA_REQUEST = 102;

    public SettingPresenter(ISettingView baseView) {
        super(baseView);
    }

    public SettingPresenter(Context context, ISettingView baseView) {
        super(context, baseView);
    }


    public void changePassword() {
        Intent intent = new Intent(context, UpdatepwdActivity.class);
        ActivityUtils.startActivity((Activity) context, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void changeEmail() {
        Intent intent = new Intent(context, NewEmailActivity.class);
        ActivityUtils.startActivityForResult((Activity) context, intent, GlobalConstant.REQUEST_CODE_EDIT_EMAIL, ActivityUtils.ANIMATE_FORWARD, false);
    }


    public void changeImgDialog() {
        List<String> photos = new ArrayList<>();
        photos.add(context.getString(R.string.m194_take_photo));
        photos.add(context.getString(R.string.m195_choose_photos));
        CircleDialogUtils.showTakePictureDialog((FragmentActivity) context, photos, new OnLvItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //请求拍照权限
                        if (EasyPermissions.hasPermissions(context, AllPermissionRequestCode.PERMISSION_CAMERA)) {
                            try {
                                takePicture();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            EasyPermissions.requestPermissions((Activity) context, String.format("%s:%s", context.getString(R.string.m93_request_permission), context.getString(R.string.m197_camera_or_storage)), AllPermissionRequestCode.PERMISSION_CAMERA_CODE, AllPermissionRequestCode.PERMISSION_CAMERA);
                        }
                        break;
                    case 1:
                        if (EasyPermissions.hasPermissions(context, AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE)) {
                            selectPicture();
                        } else {
                            EasyPermissions.requestPermissions((Activity) context, String.format("%s:%s", context.getString(R.string.m93_request_permission), context.getString(R.string.m197_camera_or_storage)), AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE_CODE, AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE);
                        }
                        break;
                }
                return true;
            }
        });

    }


    public void takePicture() throws IOException {
        imageUri = PhotoUtil.getImageUri((FragmentActivity) context, PhotoUtil.getFile());
        PhotoUtil.takePicture((Activity) context, imageUri, CODE_CAMERA_REQUEST);
    }


    public void selectPicture() {
        PhotoUtil.openPic((Activity) context, CODE_GALLERY_REQUEST);
    }


    public void startCrop() throws IOException {
        Uri cropImageUri = Uri.fromFile(PhotoUtil.getFile());
        PhotoUtil.startCrop((FragmentActivity) context, imageUri, cropImageUri);
    }


    /**
     * 退出登录
     */
    public void showLogoutDialog() {
        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m288_sign_out), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applogout();
            }
        });
    }


    public void applogout() {
        TuyaApiUtils.setIsHomeInit(false);
        TuyaApiUtils.logoutTuya();
        List<WeakReference<Activity>> activityStack = App.getInstance().getActivityList();
        for (WeakReference<Activity> activity : activityStack) {
            if (activity != null && activity.get() != null) {
                Activity activity1 = activity.get();
                if (activity1 instanceof SetttingActivity) continue;//这里要忽略掉，要不然会闪屏
                activity1.finish();
            }
        }
        activityStack.clear();
        Intent intent=new Intent(context,RegisterLoginActivity.class);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_BACK,true);
    }

}
