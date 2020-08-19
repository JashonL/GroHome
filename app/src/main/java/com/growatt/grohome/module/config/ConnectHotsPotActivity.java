package com.growatt.grohome.module.config;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.eventbus.DeviceAddOrDelMsg;
import com.growatt.grohome.module.config.Presenter.ConnectHotsPotPresenter;
import com.growatt.grohome.module.config.view.IConnectHotsPotView;
import com.growatt.grohome.utils.GlideUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class ConnectHotsPotActivity extends BaseActivity<ConnectHotsPotPresenter> implements IConnectHotsPotView {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_page_title)
    TextView tvPageTitle;
    @BindView(R.id.tv_step_one)
    TextView tvStepOne;
    @BindView(R.id.v_background)
    ImageView vBackground;
    @BindView(R.id.tv_step_two)
    TextView tvStepTwo;
    @BindView(R.id.btn_note_next)
    Button btnNoteNext;
    @BindView(R.id.btn_next)
    Button btnNext;


    private boolean isfirst = true;
    private boolean mIsPaused = true;

    @Override
    protected ConnectHotsPotPresenter createPresenter() {
        return new ConnectHotsPotPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_hotspot;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        //初始化头部
        tvTitle.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.icon_return);

        //步骤一
        String s = "1." + getString(R.string.m59_connect_hotspot_picture);
        tvStepOne.setText(s);
        //图片
        GlideUtils.showImageAct(this, R.drawable.net_image02, vBackground);
        s = "2." + getString(R.string.m60_return_continue_add);
        tvStepTwo.setText(s);
        isfirst = true;
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



    @OnClick({R.id.btn_next, R.id.btn_note_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                isfirst = false;
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
                break;
            case R.id.btn_note_next:
                presenter.toEcbindConfig();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDevList(DeviceAddOrDelMsg msg) {
        if (msg.getType() == DeviceAddOrDelMsg.ADD_DEV) finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            presenter.registerBroadcastReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mIsPaused = false;
        if (isfirst) {
            btnNoteNext.setVisibility(View.GONE);
            btnNext.setText(R.string.m62_to_connect);
        } else {
            btnNoteNext.setVisibility(View.VISIBLE);
            btnNext.setText(R.string.m138_reconnect);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsPaused = true;
    }


    @Override
    public void onStop() {
        super.onStop();
        try {
            presenter.unRegisterWifiReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean getVisible() {
        return mIsPaused;
    }
}
