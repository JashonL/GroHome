package com.growatt.grohome.module.personal;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.personal.presenter.SettingPresenter;
import com.growatt.grohome.module.personal.view.ISettingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SetttingActivity extends BaseActivity<SettingPresenter> implements ISettingView {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivPhoto)
    CircleImageView ivPhoto;
    @BindView(R.id.ll_personal_photo)
    LinearLayout llPersonalPhoto;
    @BindView(R.id.iv_user_more)
    ImageView ivUserMore;
    @BindView(R.id.ll_username)
    LinearLayout llUsername;
    @BindView(R.id.iv_passwor_more)
    ImageView ivPassworMore;
    @BindView(R.id.ll_edit_password)
    LinearLayout llEditPassword;
    @BindView(R.id.iv_phone_more)
    ImageView ivPhoneMore;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.iv_email_more)
    ImageView ivEmailMore;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.iv_code_more)
    ImageView ivCodeMore;
    @BindView(R.id.ll_code)
    LinearLayout llCode;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.tv_username)
    TextView tvUsername;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settting;
    }

    @Override
    protected void initViews() {
        //头部
        tvTitle.setText(R.string.m277_my_information);
        toolbar.setNavigationIcon(R.drawable.icon_return);

        //用户名
        String accountName = App.getUserBean().getAccountName();
        if (!TextUtils.isEmpty(accountName)) {
            tvUsername.setText(accountName);
        }
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


    @OnClick({ R.id.ll_personal_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_personal_photo:
                break;

        }
    }
}
