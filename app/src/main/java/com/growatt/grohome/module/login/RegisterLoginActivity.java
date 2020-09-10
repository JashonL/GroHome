package com.growatt.grohome.module.login;

import android.content.Intent;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.tabs.TabLayout;
import com.growatt.grohome.MainActivity;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.login.presenter.RegisterLoginPresenter;
import com.growatt.grohome.module.login.view.IRegisterLoginView;
import com.growatt.grohome.utils.SpanableStringUtils;
import com.hjq.toast.ToastUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterLoginActivity extends BaseActivity<RegisterLoginPresenter> implements IRegisterLoginView, TabLayout.OnTabSelectedListener {


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
    @BindView(R.id.et_register_password)
    TextView etRegisterPassword;
    @BindView(R.id.et_repeat_register_password)
    TextView etRepeatPassword;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    @BindView(R.id.ctl_group_register)
    ConstraintLayout ctlGroupRegister;
    @BindView(R.id.iv_passwor_view)
    ImageView ivPasswordView;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.tv_policy)
    TextView tvPolicy;

    private boolean passwordOn = false;


    @Override
    protected RegisterLoginPresenter createPresenter() {
        return new RegisterLoginPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_login;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true).init();
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
        String logHing = getString(R.string.m15_email);
        etUsername.setHint(logHing);
        etPassword.setHint(R.string.m8_password);

        //初始化注册
        tvCountry.setHint(R.string.m16_select_country);
        tvZone.setHint(R.string.m17_select_time_zone);
        etRegisterPassword.setHint(R.string.m8_password);
        etRepeatPassword.setHint(R.string.m8_password);
        tvEmail.setHint(R.string.m15_email);
        tvVerificationCode.setHint(R.string.m30_verification_code);

        SpannableStringBuilder agreement = SpanableStringUtils.getSpanableBuilder(getString(R.string.m25_user_agreement)).setUnderLine(true).create();
        tvAgreement.setText(agreement);
        SpannableStringBuilder policy = SpanableStringUtils.getSpanableBuilder(getString(R.string.m110_privacy_policy)).setUnderLine(true).create();
        tvPolicy.setText(policy);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Map<String, String> userInfo = presenter.getUserInfo();
        if (userInfo != null) {
            String username = userInfo.get(GlobalConstant.SP_USER_NAME);
            String password = userInfo.get(GlobalConstant.SP_USER_PASSWORD);
            if (!TextUtils.isEmpty(username)) {
                etUsername.setText(username);
                etUsername.setSelection(username.length());
            }
            if (!TextUtils.isEmpty(password)) {
                etPassword.setText(password);
            }
        }
    }

    @Override
    protected void initData() {
        presenter.getCurrentZone();
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

    @OnClick({R.id.btn_login, R.id.btn_register, R.id.iv_passwor_view, R.id.ll_country, R.id.tv_get_code, R.id.ll_zone, R.id.tv_forgot_pwd,R.id.tv_agreement,R.id.tv_policy})
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
                String country = tvCountry.getText().toString();
                if (GlobalConstant.STRING_CHINA_CHINESE.equals(country) || country.toLowerCase().equals(GlobalConstant.STRING_CHINA_ENLISH)) {
                    country = "China";
                }
                String zone = tvZone.getText().toString();
                zone = zone.replace("GMT", "");
                String rePassword = etRegisterPassword.getText().toString();
                String rePasswordRepeat = etRepeatPassword.getText().toString();
                String email = tvEmail.getText().toString();
                String verification = tvVerificationCode.getText().toString();
                presenter.register(email, rePassword, rePasswordRepeat, zone, email, country, verification);
                break;
            case R.id.iv_passwor_view:
                clickPasswordSwitch();
                break;
            case R.id.ll_country:
                presenter.getCountry();
                break;
            case R.id.tv_get_code:
                presenter.getVerificationCode(tvGetCode);
                break;
            case R.id.ll_zone:
                presenter.setZone();
                break;
            case R.id.tv_forgot_pwd:
                presenter.resetPassword();
                break;
            case R.id.tv_agreement:
                presenter.startAgreement();
                break;
            case R.id.tv_policy:
                presenter.startPolicy();
                break;
        }
    }


    public void clickPasswordSwitch() {
        passwordOn = !passwordOn;
        if (passwordOn) {
            ivPasswordView.setImageResource(R.drawable.icon_signin_see);
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            ivPasswordView.setImageResource(R.drawable.icon_signin_see);
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        if (etPassword.getText().length() > 0) {
            etPassword.setSelection(etPassword.getText().length());
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
    public void onError(String error) {
        requestError(error);
    }

    @Override
    public void loginSuccess(String user) {
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    public String getEmail() {
        return tvEmail.getText().toString();
    }

    @Override
    public void getCodeStart() {
        if (tvGetCode.isEnabled()) {
            tvGetCode.setEnabled(false);
        }
    }

    @Override
    public void timing(int second) {
        tvGetCode.setText(second + "s");
    }

    @Override
    public void getCodeEnd() {
        if (!tvGetCode.isEnabled()) {
            tvGetCode.setEnabled(true);
        }
        tvGetCode.setText(R.string.m175_get_code);
    }

    @Override
    public void setZone(String zone) {
        tvZone.setText(zone);
    }

    @Override
    public boolean isAgreement() {
        return cbAgreement.isChecked();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegisterLoginPresenter.START_FOR_RESULT_COUNTRY) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra(GlobalConstant.COUNTRY);
                if (TextUtils.isEmpty(result)) return;
                presenter.getServerByCountry(result);
                tvCountry.setText(result);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
