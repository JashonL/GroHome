package com.growatt.grohome.base;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.constants.AllPermissionRequestCode;
import com.gyf.immersionbar.ImmersionBar;
import com.yechaoa.yutils.LogUtil;
import com.yechaoa.yutils.YUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView, EasyPermissions.PermissionCallbacks {

    protected P presenter;

    protected abstract P createPresenter();

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initData();

    protected Toolbar mToolBar;

    protected ImmersionBar mImmersionBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutInflater.from(this).inflate(getLayoutId(), null));
        ButterKnife.bind(this);
        App.getInstance().addActivityList(new WeakReference<>(this));
        presenter = createPresenter();
        initViews();
        //初始化沉浸式
        initImmersionBar();
        initData();
    }
    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        mImmersionBar=  ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.nocolor)//这里的颜色，你可以自定义。
                .init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListener();
    }

    protected void initToolbar() {
        if (mToolBar == null) {
            mToolBar = findViewById(R.id.toolbar);
            if (mToolBar == null) {
            } else {
                mToolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.color_text_00));
            }
        }
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }


    protected void setTitle(String title) {
        if (mToolBar != null) {
            mToolBar.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (mToolBar != null) {
            mToolBar.setTitle(titleId);
        }
    }

    protected void setSubTitle(String title) {
        if (mToolBar != null) {
            mToolBar.setSubtitle(title);
        }
    }

    protected void setLogo(Drawable logo) {
        if (mToolBar != null) {
            mToolBar.setLogo(logo);
        }
    }


    protected void setNavigationIcon(Drawable logo) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(logo);
        }
    }

    protected void setMenu(int resId, Toolbar.OnMenuItemClickListener listener) {
        if (mToolBar != null) {
            mToolBar.inflateMenu(resId);
            mToolBar.setOnMenuItemClickListener(listener);
        }
    }

    protected void setDisplayHomeAsUpEnabled(int iconResId, final View.OnClickListener listener) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(iconResId);
            if (listener != null) {
                mToolBar.setNavigationOnClickListener(listener);
            } else {
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    protected void setDisplayHomeAsUpEnabled() {
        setDisplayHomeAsUpEnabled(R.drawable.icon_return, null);
    }

    protected void setDisplayHomeAsUpEnabled(final View.OnClickListener listener) {
        setDisplayHomeAsUpEnabled(R.drawable.icon_return, listener);
    }

    protected void hideToolBarView() {
        if (mToolBar != null) {
            mToolBar.setVisibility(View.GONE);
        }
    }

    protected void showToolBarView() {
        if (mToolBar != null) {
            mToolBar.setVisibility(View.VISIBLE);
        }
    }

    protected void initListener() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁时，解除绑定
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Override
    public void showLoading() {
        YUtils.showLoading("");
    }

    @Override
    public void hideLoading() {
        YUtils.dismissLoading();
    }

    @Override
    public void onErrorCode(BaseBean bean) {
        YUtils.dismissLoading();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        //将权限交给EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 用户同意权限
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    /**
     * 用户拒绝了权限
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //如果是点击了不再提醒并且拒绝了权限，需要手动引导开启权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).setRequestCode(requestCode).build().show();
        }

    }

    //手动设置开启返回回调处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode >= AllPermissionRequestCode.ALL_PERMISSION_CODE && requestCode <= AllPermissionRequestCode.PERMISSION_LOCATION_CODE) {
            this.onPermissionsGranted(requestCode, null);
        }
    }

}
