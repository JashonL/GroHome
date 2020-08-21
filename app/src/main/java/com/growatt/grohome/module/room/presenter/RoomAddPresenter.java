package com.growatt.grohome.module.room.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.AllPermissionRequestCode;
import com.growatt.grohome.eventbus.HomeRoomStatusBean;
import com.growatt.grohome.module.room.view.IRoomAddView;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.FileUtils;
import com.growatt.grohome.utils.ImagePathUtil;
import com.growatt.grohome.utils.MyToastUtils;
import com.growatt.grohome.utils.PhotoUtil;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class RoomAddPresenter extends BasePresenter<IRoomAddView> {
    //原始数据uri:需要FileProvide
    private Uri imageUri;
    //裁剪后uri：不需要FileProvide
    private Uri cropImageUri;
    //添加房间时的图片路径:第一张图"1->现有资源/drawable" 2->drawable资源
//    private Bitmap bitmap;
    private String imagePath;

    /**
     * 相册图片相关
     */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;


    public RoomAddPresenter(IRoomAddView baseView) {
        super(baseView);
    }

    public RoomAddPresenter(Context context, IRoomAddView baseView) {
        super(context, baseView);
        checkPermission();
    }


    public void checkPermission(){
        //请求拍照权限
        if (!EasyPermissions.hasPermissions(context,AllPermissionRequestCode.PERMISSION_CAMERA)) {
            EasyPermissions.requestPermissions((Activity) context, String.format("%s:%s",context.getString(R.string.m93_request_permission), context.getString(R.string.m197_camera_or_storage)), AllPermissionRequestCode.PERMISSION_CAMERA_CODE, AllPermissionRequestCode.PERMISSION_CAMERA);
        }
    }


    /**
     * 切换图片调用相册或相机弹框
     */
    public void changeImgDialog() {
        List<String>photos=new ArrayList<>();
        photos.add(context.getString(R.string.m194_take_photo));
        photos.add(context.getString(R.string.m195_choose_photos));
         CircleDialogUtils.showTakePictureDialog((FragmentActivity) context, photos, new OnLvItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        try {
                            takePicture();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        selectPicture();
                        break;
                }
                return true;
            }
        });

    }


    public void takePicture() throws IOException {
        imageUri = PhotoUtil.getImageUri((FragmentActivity) context, PhotoUtil.getFile());
        PhotoUtil.takePicture((FragmentActivity) context, imageUri, CODE_CAMERA_REQUEST);
    }


    public void selectPicture() {
        PhotoUtil.openPic((FragmentActivity) context, CODE_GALLERY_REQUEST);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) throws IOException {
        if (resultCode == RESULT_CANCELED) {
            MyToastUtils.toast(context.getString(R.string.m89_cancel));
            return;
        }

        if (resultCode == RESULT_OK && requestCode == CODE_GALLERY_REQUEST) {//从相册获取
            if (data != null) {
                cropImageUri = PhotoUtil.startCropImageAct((FragmentActivity) context, data.getData());
            }
        } else if (resultCode == RESULT_OK && requestCode == CODE_CAMERA_REQUEST) {//拍照
            cropImageUri = Uri.fromFile(PhotoUtil.getFile());
            try {
                UCrop.of(imageUri, cropImageUri)
                        .withAspectRatio(710, 351)
                        .start((Activity) context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //裁剪
        if (resultCode == RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }


    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            //获取图片路径
            imagePath = ImagePathUtil.getRealPathFromUri(context, resultUri);
            baseView.getPhotoSuccess(imagePath);
        } else {
            baseView.getPhotoFail("");
        }
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            baseView.getPhotoFail(cropError.getMessage());
        } else {
            baseView.getPhotoFail("");
        }
    }

    public void savePic(int res){
        //获取资源图片
        @SuppressLint("ResourceType") InputStream is = context.getResources().openRawResource(res);
        File path= new File(App.getInstance().getFilesDir().getPath());
        File appDir = new File(path, "/images");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File pictureFile = new File(appDir, "AddRoomDefult.jpg");
        if (pictureFile.exists()) {
            pictureFile.delete();
        }
        imagePath= FileUtils.fileOut(is,pictureFile);
    }

    /**
     * 添加房间到服务器
     */

    public void addRoom(String roomName) {
        if (TextUtils.isEmpty(imagePath)){
            MyToastUtils.toast(R.string.m202_add_picture);
            return;
        }
        if (TextUtils.isEmpty(roomName)) {
            MyToastUtils.toast(R.string.m203_enter_room_name);
            return;
        }
        File imageFile=new File(imagePath);
        RequestBody requestBody =new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("roomName",roomName).
                addFormDataPart("userId",App.getUserBean().accountName).addFormDataPart("lan", String.valueOf(CommentUtils.getLanguage())).
                addFormDataPart("imagefile",imageFile.getName(),RequestBody.create(MediaType.parse("image/*"),imageFile)).build();
        addDisposable(apiServer.createRoom(requestBody), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String data =obj.getString("data");
                    if (code == 0) {
                     baseView.createRoomSuccess();
                        File file = new File(imagePath);
                        if (file.exists()) file.delete();
                        HomeRoomStatusBean bean = new HomeRoomStatusBean();
                        EventBus.getDefault().post(bean);
                    }else {
                        baseView.createRoomFail(data);
                    }
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
