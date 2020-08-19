package com.growatt.grohome.module.personal;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.PhotoEditBean;
import com.growatt.grohome.constants.AllPermissionRequestCode;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.personal.presenter.SettingPresenter;
import com.growatt.grohome.module.personal.view.ISettingView;
import com.growatt.grohome.utils.GlideUtils;
import com.growatt.grohome.utils.ImagePathUtil;
import com.growatt.grohome.utils.MyToastUtils;
import com.growatt.grohome.utils.PhotoUtil;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

public class SetttingActivity extends BaseActivity<SettingPresenter> implements ISettingView {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivPhoto)
    CircleImageView ivPhoto;
    @BindView(R.id.ll_personal_photo)
    LinearLayout llPersonalPhoto;
    @BindView(R.id.iv_user_more)
    ImageView ivUserMore;
    @BindView(R.id.ll_username)
    LinearLayout llUsername;
    @BindView(R.id.iv_passwor_more)
    ImageView ivPassworMore;
    @BindView(R.id.ll_edit_password)
    LinearLayout llEditPassword;
    @BindView(R.id.iv_phone_more)
    ImageView ivPhoneMore;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.iv_email_more)
    ImageView ivEmailMore;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.iv_code_more)
    ImageView ivCodeMore;
    @BindView(R.id.ll_code)
    LinearLayout llCode;
    @BindView(R.id.btn_logout)
    Button login;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_email)
    TextView tvEmail;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settting;
    }

    @Override
    protected void initViews() {
        //头部
        tvTitle.setText(R.string.m277_my_information);
        toolbar.setNavigationIcon(R.drawable.icon_return);

        //用户名
        String accountName = App.getUserBean().getAccountName();
        if (!TextUtils.isEmpty(accountName)) {
            tvUsername.setText(accountName);
        }
        //用户名
        String email = App.getUserBean().getEmail();
        if (!TextUtils.isEmpty(email)) {
            tvEmail.setText(email);
        }
        //头像
        try {
            String path = Environment.getExternalStorageDirectory() + "/" + GlobalConstant.IMAGE_FILE_LOCATION;
            GlideUtils.showImageActNoCache(this, R.drawable.avatar, R.drawable.avatar, path, ivPhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.ll_personal_photo, R.id.ll_edit_password, R.id.ll_email,R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_personal_photo:
                presenter.changeImgDialog();
                break;
            case R.id.ll_edit_password:
                presenter.changePassword();
                break;
            case R.id.ll_email:
                presenter.changeEmail();
                break;
            case R.id.btn_logout:
                presenter.showLogoutDialog();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GlobalConstant.REQUEST_CODE_EDIT_EMAIL) {
                String email = data.getStringExtra("email");
                if (!TextUtils.isEmpty(email)) {
                    tvEmail.setText(email);
                }
            } else if (requestCode == SettingPresenter.CODE_GALLERY_REQUEST) {
                if (data != null) {
                    try {
                        PhotoUtil.startCropImageAct(this, data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == SettingPresenter.CODE_CAMERA_REQUEST) {
                try {
                    presenter.startCrop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
    }


    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            setImageToHeadView(resultUri);
        } else {
            MyToastUtils.toast(R.string.m201_fail);
        }
    }

    private void setImageToHeadView(Uri uri) {
        try {
            String plantPath = ImagePathUtil.getRealPathFromUri(this, uri);
            GlideUtils.showImageAct(this, R.drawable.avatar, R.drawable.avatar, plantPath, ivPhoto);
            saveBitmap(plantPath);
            EventBus.getDefault().post(new PhotoEditBean(plantPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveBitmap(String path) {
        Bitmap bitmap = ImagePathUtil.decodeBitmapFromResource(path, ivPhoto.getWidth(), ivPhoto.getHeight());
        File f = new File(
                Environment.getExternalStorageDirectory(),
                GlobalConstant.IMAGE_FILE_LOCATION);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case AllPermissionRequestCode.PERMISSION_CAMERA_CODE:
                if (EasyPermissions.hasPermissions(this, AllPermissionRequestCode.PERMISSION_CAMERA)) {
                    try {
                        presenter.takePicture();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE_CODE:
                if (EasyPermissions.hasPermissions(this, AllPermissionRequestCode.PERMISSION_EXTERNAL_STORAGE)) {
                    presenter.selectPicture();
                }
                break;
        }
    }
}
