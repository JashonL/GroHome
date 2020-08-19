package com.growatt.grohome.module.config;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.eventbus.DeviceAddOrDelMsg;
import com.growatt.grohome.module.config.Presenter.DeviceAPLightPresenter;
import com.growatt.grohome.module.config.view.IDeviceAPLightView;
import com.growatt.grohome.utils.GlideUtils;
import com.growatt.grohome.utils.Mydialog;
import com.growatt.grohome.utils.SpanableStringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceAPLightActivity extends BaseActivity<DeviceAPLightPresenter> implements IDeviceAPLightView {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_step_one)
    TextView tvStepOne;
    @BindView(R.id.tv_content_step1)
    TextView tvContentStep1;
    @BindView(R.id.iv_config_one)
    ImageView ivConfigOne;
    @BindView(R.id.tv_step_two)
    TextView tvStepTwo;
    @BindView(R.id.tv_content_step2)
    TextView tvContentStep2;
    @BindView(R.id.iv_config_two)
    ImageView ivConfigTwo;
    @BindView(R.id.tv_step_three)
    TextView tvStepThree;
    @BindView(R.id.tv_content_step3)
    TextView tvContentStep3;
    @BindView(R.id.iv_config_three)
    ImageView ivConfigThree;
    @BindView(R.id.tv_step_four)
    TextView tvStepFour;
    @BindView(R.id.tv_content_step4)
    TextView tvContentStep4;
    @BindView(R.id.iv_config_four)
    ImageView ivConfigFour;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.v_bottom_margin)
    View vBottomMargin;

    @Override
    protected DeviceAPLightPresenter createPresenter() {
        return new DeviceAPLightPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_ap_light;
    }

    @Override
    protected void initViews() {
        //初始化头部
        tvTitle.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.icon_return);

        tvContent.setText(R.string.m122_ez_config_detail);

        //步骤一
        String step = getString(R.string.m64_step) + "1";
        SpannableStringBuilder stepOne = SpanableStringUtils.getSpanableBuilder(step).setBold(true).append("/4").setProportion(0.8f).create();
        tvStepOne.setText(stepOne);
        GlideUtils.showImageAct(this, R.drawable.net_image_course_01, ivConfigOne);


        //步骤二
        step = getString(R.string.m64_step) + "2";
        SpannableStringBuilder stepTwo = SpanableStringUtils.getSpanableBuilder(step).setBold(true).append("/4").setProportion(0.8f).create();
        tvStepTwo.setText(stepTwo);
        GlideUtils.showImageAct(this, R.drawable.net_image_course_02, ivConfigTwo);
        //步骤三
        step = getString(R.string.m64_step) + "3";
        SpannableStringBuilder stepThree = SpanableStringUtils.getSpanableBuilder(step).setBold(true).append("/4").setProportion(0.8f).create();
        tvStepThree.setText(stepThree);
        GlideUtils.showImageAct(this, R.drawable.net_image_course_03_en, ivConfigThree);
        //步骤四
        step = getString(R.string.m64_step) + "4";
        SpannableStringBuilder stepFour = SpanableStringUtils.getSpanableBuilder(step).setBold(true).append("/4").setProportion(0.8f).create();
        tvStepFour.setText(stepFour);
        GlideUtils.showImageAct(this, R.drawable.net_image_course_04_en, ivConfigThree);
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                presenter.getTokenForConfigDevice();
                break;
        }
    }

    @Override
    public void showDialog() {
        Mydialog.show(this,"");
    }

    @Override
    public void dissmissDialog() {
        Mydialog.dissmiss();
    }

    @Override
    public void getTuyaTokenSuccess() {

    }

    @Override
    public void getTuyaTokenFails(String errorCode, String errorMsg) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDevList(DeviceAddOrDelMsg msg) {
        if (msg.getType() == DeviceAddOrDelMsg.ADD_DEV) finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
