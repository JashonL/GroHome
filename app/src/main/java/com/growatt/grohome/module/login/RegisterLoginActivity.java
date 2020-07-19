package com.growatt.grohome.module.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.tabs.TabLayout;
import com.growatt.grohome.MainActivity;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.bean.User;
import com.growatt.grohome.module.login.presenter.RegisterLoginPresenter;
import com.growatt.grohome.module.login.view.IRegisterLoginView;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
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
        String[] titles = new String[]{getString(R.string.m14_login), getString(R.string.m9_registered)};
        tabTitle.removeAllTabs();
        for (String title : titles) {
            TabLayout.Tab tab = tabTitle.newTab();
            tab.setText(title);
            tabTitle.addTab(tab);
        }
        tabTitle.addOnTabSelectedListener(this);

        //初始化登录
        String logHing = getString(R.string.m7_username) + "/" + getString(R.string.m15_email);
        etUsername.setHint(logHing);
        etPassword.setHint(R.string.m8_password);

        //初始化注册
        tvCountry.setHint(R.string.m16_select_country);
        tvZone.setHint(R.string.m17_select_time_zone);
        tvPwd.setHint(R.string.m8_password);
        tvRepeatPwd.setHint(R.string.m8_password);
        tvEmail.setHint(R.string.m15_email);
        tvVerificationCode.setHint(R.string.m30_verification_code);
    }

    @Override
    protected void initData() {

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
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    ToastUtils.show(R.string.m143_username_empty);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.show(R.string.m144_password_empty);
                    return;
                }
//                startActivity(new Intent(this, MainActivity.class));
//                presenter.userLogin(username,password);
                presenter.getUserType();
                break;
            case R.id.btn_register:

                break;
        }
    }

    @Override
    public String getUserName() {
        return etUsername.getText().toString();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public void loginSuccess(String user) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void loginError(String errorMessage) {
        ToastUtils.show(errorMessage);
    }
}
