package com.growatt.grohome.module.config;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.customview.NodeProgressView;
import com.growatt.grohome.module.config.Presenter.DeviceConfigPresenter;
import com.growatt.grohome.module.config.view.IDeviceConfigView;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.MyToastUtils;

import butterknife.BindView;

public class DeviceConfigActivity extends BaseActivity<DeviceConfigPresenter> implements IDeviceConfigView {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_page_title)
    TextView tvPageTitle;
    @BindView(R.id.tv_sub_title)
    TextView tvSubTitle;
    @BindView(R.id.v_scan_background)
    View vScanBackground;
    @BindView(R.id.tv_current_progress)
    TextView tvCurrentProgress;
    @BindView(R.id.node_progress)
    NodeProgressView nodeProgress;

    //旋转动画
    private Animation animation;

    @Override
    protected DeviceConfigPresenter createPresenter() {
        return new DeviceConfigPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_config;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected void initViews() {
        //初始化头部
        tvTitle.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        //开始动画
        vScanBackground.setBackgroundResource(R.drawable.net_image_scan);
        animation = AnimationUtils.loadAnimation(this, R.anim.scaning_rote);
        vScanBackground.startAnimation(animation);
    }



    private void showConfigFailDialog(){
        View bodyView= LayoutInflater.from(this).inflate(R.layout.dialog_config_fail,null,false);
        CircleDialogUtils.showFailConfig(bodyView, getSupportFragmentManager(), view -> {
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.dialogFail();
                }
            });

            view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//回到选择wifi界面
                    presenter.reTryConfig();
                }
            });
        });
    }



    @Override
    protected void initData() {
        presenter.startConfig();
    }


    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> presenter.cancel());
    }

    @Override
    public void getTokenFail(String s, String s1) {
        MyToastUtils.toast("Errorcode:" + s + ":" + "ErrorMessage" + s1);
    }

    @Override
    public void showConfigFail(String errorCode, String error, int mode) {
        showConfigFailDialog();
    }

    @Override
    public void onError(String msg) {
        requestError(msg);
    }

    @Override
    public void showConnectPage() {

    }

    @Override
    public void showSuccessPage(String devId, String pId, String devName) {
        try {
            presenter.addDevice(pId,devId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showFailurePage(int mode) {
        presenter.dialogFail();
    }

    @Override
    public void setConnectProgress(float progress, int animationDuration) {
        String currentProgress = (int) progress + "%";
        tvCurrentProgress.setText(currentProgress);
    }

    @Override
    public void showNetWorkFailurePage(int mode) {
        presenter.dialogFail();
    }

    @Override
    public void showBindDeviceSuccessTip() {
        nodeProgress.setCurentNode(2);
    }

    @Override
    public void showDeviceFindTip(String gwId) {
        nodeProgress.setCurentNode(1);
    }

    @Override
    public void showConfigSuccessTip() {
        nodeProgress.setCurentNode(3);
    }

    @Override
    public void showBindDeviceSuccessFinalTip() {

    }

    @Override
    public void setAddDeviceName(String name) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.cancel();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
        super.onDestroy();
    }
}
