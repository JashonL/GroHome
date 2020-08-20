package com.growatt.grohome.module.config;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.config.Presenter.DeviceEZLightPresenter;
import com.growatt.grohome.module.config.view.IDeviceEZLightView;
import com.growatt.grohome.utils.GlideUtils;
import com.growatt.grohome.utils.SpanableStringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceEZLightActivity extends BaseActivity<DeviceEZLightPresenter> implements IDeviceEZLightView {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_step_one)
    TextView tvStepOne;
    @BindView(R.id.iv_config_one)
    ImageView ivConfigOne;
    @BindView(R.id.tv_step_two)
    TextView tvStepTwo;
    @BindView(R.id.iv_config_two)
    ImageView ivConfigTwo;
    @BindView(R.id.tv_step_three)
    TextView tvStepThree;
    @BindView(R.id.iv_config_three)
    ImageView ivConfigThree;

    @Override
    protected DeviceEZLightPresenter createPresenter() {
        return new DeviceEZLightPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_ez_light;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected void initViews() {
        //初始化头部
        tvTitle.setText(R.string.m105_ez_mode);
        toolbar.setNavigationIcon(R.drawable.icon_return);

        tvContent.setText(R.string.m122_ez_config_detail);

        //步骤一
        String step = getString(R.string.m64_step) + "1";
        SpannableStringBuilder stepOne = SpanableStringUtils.getSpanableBuilder(step).setBold(true).append("/3").setProportion(0.8f).create();
        tvStepOne.setText(stepOne);
        GlideUtils.showImageAct(this, R.drawable.net_image_course_01, ivConfigOne);

        //步骤二
        step = getString(R.string.m64_step) + "2";
        SpannableStringBuilder stepTwo = SpanableStringUtils.getSpanableBuilder(step).setBold(true).append("/3").setProportion(0.8f).create();
        tvStepTwo.setText(stepTwo);
        GlideUtils.showImageAct(this, R.drawable.net_image_course_02, ivConfigTwo);
        //步骤三
        step = getString(R.string.m64_step) + "3";
        SpannableStringBuilder stepThree = SpanableStringUtils.getSpanableBuilder(step).setBold(true).append("/3").setProportion(0.8f).create();
        tvStepThree.setText(stepThree);
        GlideUtils.showImageAct(this, R.drawable.net_image_course_03_en, ivConfigThree);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                presenter.toEcbindConfig();
                break;
        }
    }


}
