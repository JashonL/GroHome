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

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.constants.AllPermissionRequestCode;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.room.view.IRoomImageView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.growatt.grohome.utils.CommentUtils.getLanguage;

public class RoomImagePresenter extends BasePresenter<IRoomImageView> {

    /**
     * 相册图片相关
     */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    //原始数据uri:需要FileProvide
    private Uri imageUri;
    //裁剪后uri：不需要FileProvide
    private Uri cropImageUri;
    private String imagePath;

    private HomeRoomBean mRoomBean;


    public RoomImagePresenter(IRoomImageView baseView) {
        super(baseView);
    }

    public RoomImagePresenter(Context context, IRoomImageView baseView) {
        super(context, baseView);
        checkPermission();
        String roomJson = ((Activity) context).getIntent().getStringExtra(GlobalConstant.ROOM_BEAN);
        if (!TextUtils.isEmpty(roomJson)) {
            this.mRoomBean = new Gson().fromJson(roomJson,HomeRoomBean.class);
             baseView.setImage(mRoomBean.getCdn());
        }

    }


    public void checkPermission(){
        //请求拍照权限
        if (!EasyPermissions.hasPermissions(context, AllPermissionRequestCode.PERMISSION_CAMERA)) {
            EasyPermissions.requestPermissions((Activity) context, String.format("%s:%s",context.getString(R.string.m93_request_permission), context.getString(R.string.m197_camera_or_storage)), AllPermissionRequestCode.PERMISSION_CAMERA_CODE, AllPermissionRequestCode.PERMISSION_CAMERA);
        }
    }




    /**
     * 切换图片调用相册或相机弹框
     */
    public void changeImgDialog() {
        List<String> photos=new ArrayList<>();
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


    /**
     * 联网更新图片
     *
     */
    public void updateImg() {
        if (TextUtils.isEmpty(imagePath)) {
            MyToastUtils.toast(R.string.m202_add_picture);
            return;
        }
        if (mRoomBean == null || mRoomBean.getCid() == -1) return;
        File imageFile=new File(imagePath);
        RequestBody requestBody =new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("roomId", String.valueOf(mRoomBean.getCid())).
                addFormDataPart("lan", String.valueOf(CommentUtils.getLanguage())).
                addFormDataPart("imagefile",imageFile.getName(),RequestBody.create(MediaType.parse("image/*"),imageFile)).build();
        addDisposable(apiServer.updateImage(requestBody), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    int code = obj.getInt("code");
                    String data =obj.getString("data");
                    if (code == 0) {
                        baseView.updateImageSuccess();
                        File file = new File(imagePath);
                        if (file.exists()) file.delete();
                    }else {
                        baseView.updateImageFail(data);
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

}
