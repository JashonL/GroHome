package com.growatt.grohome.module.login;

import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.login.presenter.ForgotPassWordPresenter;
import com.growatt.grohome.module.login.view.IForgotPasswordView;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotpasswordActivity extends BaseActivity<ForgotPassWordPresenter> implements IForgotPasswordView {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_email)
    EditText etEmail;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected ForgotPassWordPresenter createPresenter() {
        return new ForgotPassWordPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);
        tvTitle.setText(R.string.m292_retrievepwd_title);

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


    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        presenter.findPwdByUsername(1);
    }

    @Override
    public String userEmail() {
        return etEmail.getText().toString();
    }

    @Override
    public void onError(String onError) {
        requestError(onError);
    }


}
