package com.growatt.grohome.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.tabs.TabLayout;
import com.growatt.grohome.MainActivity;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.login.presenter.RegisterLoginPresenter;
import com.growatt.grohome.module.login.view.IRegisterLoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterLoginActivity extends BaseActivity<RegisterLoginPresenter> implements IRegisterLoginView, TabLayout.OnTabSelectedListener {


    @BindView(R.id.iv_top_image)
    ImageView ivTopImage;
    @BindView(R.id.tab_title)
    TabLayout tabTitle;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.ctl_group_login)
    ConstraintLayout ctlGroupLogin;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.tv_zone)
    TextView tvZone;
    @BindView(R.id.tv_pwd)
    TextView tvPwd;
    @BindView(R.id.tv_repeat_pwd)
    TextView tvRepeatPwd;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    @BindView(R.id.ctl_group_register)
    ConstraintLayout ctlGroupRegister;

    @Override
    protected RegisterLoginPresenter createPresenter() {
        return new RegisterLoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_login;
    }

    @Override
    protected void initViews() {
        //初始化tablayout
        String[] titles = new String[]{getString(R.string.m14_登录), getString(R.string.m9_注册)};
        tabTitle.removeAllTabs();
        for (String title : titles) {
            TabLayout.Tab tab = tabTitle.newTab();
            tab.setText(title);
            tabTitle.addTab(tab);
        }
        tabTitle.addOnTabSelectedListener(this);

        //初始化登录
        String logHing = getString(R.string.m7_用户名) + "/" + getString(R.string.m15_邮箱);
        etUsername.setHint(logHing);
        etPassword.setHint(R.string.m8_密码);

        //初始化注册
        tvCountry.setHint(R.string.m16_请选择国家);
        tvZone.setHint(R.string.m17_选择时区);
        tvPwd.setHint(R.string.m8_密码);
        tvRepeatPwd.setHint(R.string.m8_密码);
        tvEmail.setHint(R.string.m15_邮箱);
        tvVerificationCode.setHint(R.string.m30_验证码);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        switch (position) {
            case 0:
                ctlGroupLogin.setVisibility(View.VISIBLE);
                ctlGroupRegister.setVisibility(View.GONE);
                break;
            case 1:
                ctlGroupLogin.setVisibility(View.GONE);
                ctlGroupRegister.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn_register:

                break;
        }
    }
}
