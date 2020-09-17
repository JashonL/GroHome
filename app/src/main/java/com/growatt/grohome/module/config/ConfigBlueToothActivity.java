package com.growatt.grohome.module.config;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.config.presenter.BluetoothPresenter;
import com.growatt.grohome.module.config.view.IBluetoothConfigView;
import com.growatt.grohome.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ConfigBlueToothActivity extends BaseActivity<BluetoothPresenter> implements IBluetoothConfigView {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_page_title)
    TextView tvPageTitle;
    @BindView(R.id.tv_sub_title)
    TextView tvSubTitle;
    @BindView(R.id.tv_sub_title_two)
    TextView tvSubTitleTwo;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_tips)
    TextView tvTips;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected BluetoothPresenter createPresenter() {
        return new BluetoothPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth_config;
    }

    @Override
    protected void initViews() {
        GlideUtils.showGifContext(this,R.drawable.bluetooth,ivSearch);
    }

    @Override
    protected void initData() {
        presenter.isBleEnable();
    }



    @OnClick(R.id.tv_cancel)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                finish();
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        presenter.registerBroadcastReceiver();
    }


    @Override
    public void onStop() {
        super.onStop();
        try {
            presenter.unregisterBluetoothReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        presenter.checkLocationPermissions();
    }


    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
