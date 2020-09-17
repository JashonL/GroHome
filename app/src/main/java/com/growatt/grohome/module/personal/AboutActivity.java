package com.growatt.grohome.module.personal;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.personal.presenter.AboutPresenter;
import com.growatt.grohome.module.personal.view.IAboutView;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity<AboutPresenter> implements IAboutView {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_app_logo)
    ImageView ivAppLogo;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_header)
    RelativeLayout llHeader;
    @BindView(R.id.tv_about_agreement)
    TextView tvAboutAgreement;
    @BindView(R.id.iv_protocal_more)
    ImageView ivProtocalMore;
    @BindView(R.id.ll_about_agreement)
    LinearLayout llAboutAgreement;
    @BindView(R.id.v_phone_divider)
    View vPhoneDivider;
    @BindView(R.id.tv_about_phone)
    TextView tvAboutPhone;
    @BindView(R.id.iv_hotline_more)
    ImageView ivHotlineMore;
    @BindView(R.id.ll_about_phone)
    LinearLayout llAboutPhone;
    @BindView(R.id.v_email_divider)
    View vEmailDivider;
    @BindView(R.id.tv_about_email)
    TextView tvAboutEmail;
    @BindView(R.id.iv_official_more)
    ImageView ivOfficialMore;
    @BindView(R.id.ll_about_email)
    LinearLayout llAboutEmail;
    @BindView(R.id.v_website_divider)
    View vWebsiteDivider;
    @BindView(R.id.tv_about_website)
    TextView tvAboutWebsite;
    @BindView(R.id.iv_website_more)
    ImageView ivWebsiteMore;
    @BindView(R.id.ll_about_website)
    LinearLayout llAboutWebsite;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected AboutPresenter createPresenter() {
        return new AboutPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initViews() {
        //头部
        tvTitle.setText(R.string.m72_about);
        toolbar.setNavigationIcon(R.drawable.icon_return);

    }

    @Override
    protected void initData() {
        presenter.getVersionName();
        presenter.getCustomerPhone();
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

    @Override
    public void setVersionName(String name) {
        if (!TextUtils.isEmpty(name)) {
            tvVersion.setText(name);
        }
    }

    @Override
    public void setAppicon() {
        ivAppLogo.setImageResource(R.mipmap.ic_launcher_round);
    }

    @Override
    public void setPhone(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            tvAboutPhone.setText(phone);
        }
    }

    @Override
    public void setEmail(String email) {
        if (!TextUtils.isEmpty(email)) {
            tvAboutEmail.setText(email);
        }
    }

    @Override
    public void onError(String onError) {
        requestError(onError);
    }


    @OnClick({R.id.ll_about_website,R.id.ll_about_agreement,R.id.ll_about_policy})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.ll_about_agreement:
                presenter.getUserAgreement();
                break;
            case R.id.ll_about_website:
                presenter.toWebSite();
                break;

            case R.id.ll_about_policy:
                presenter.getPrivacyPolicy();
                break;
        }
    }
}
