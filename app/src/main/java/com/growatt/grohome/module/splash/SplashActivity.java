package com.growatt.grohome.module.splash;

import android.content.Intent;
import android.os.Handler;

import com.growatt.grohome.MainActivity;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.SharedPreferencesUnit;
import com.gyf.immersionbar.ImmersionBar;

public class SplashActivity extends BaseActivity<SplashPresenter> implements ISplashView {

    protected ImmersionBar mImmersionBar;


    private Handler handler = new Handler();
    private Runnable runnableToLogin = new Runnable() {
        @Override
        public void run() {
            //自动登录
            boolean autoLogin = SharedPreferencesUnit.getInstance(SplashActivity.this).getBoolean(GlobalConstant.SP_AUTO_LOGIN);
            if (autoLogin) {
                presenter.autologin();
            } else {
                presenter.toLogin();
            }
        }
    };


    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {
        handler.postDelayed(runnableToLogin, 0);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableToLogin);
        handler = null;
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.nocolor)//这里的颜色，你可以自定义。
                .init();
    }

    @Override
    public void loginSuccess(String user) {
        Intent intent=new Intent(this, MainActivity.class);
        ActivityUtils.startActivity(this,intent,ActivityUtils.ANIMATE_FORWARD,true);
    }
}
