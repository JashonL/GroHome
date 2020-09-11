package com.growatt.grohome.module.personal;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.personal.presenter.NewEmailPresenter;
import com.growatt.grohome.module.personal.view.INewEmailView;

import butterknife.BindView;
import butterknife.OnClick;

public class NewEmailActivity extends BaseActivity<NewEmailPresenter> implements INewEmailView {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.btn_send_code)
    Button btnSendCode;
    @BindView(R.id.et_code)
    EditText etCode;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected NewEmailPresenter createPresenter() {
        return new NewEmailPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_email;
    }

    @Override
    protected void initViews() {
        //头部
        tvTitle.setText(R.string.m15_email);
        toolbar.setNavigationIcon(R.drawable.icon_return);

        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.sp_10), false);
        //邮箱
        SpannableString ss1 = new SpannableString(getString(R.string.m176_enter_email));
        //验证码
        SpannableString ss2 = new SpannableString(getString(R.string.m30_verification_code));
        ss1.setSpan(ass, 0, ss1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(ass, 0, ss2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etEmail.setHint(new SpannedString(ss1)); // 一定要进行转换,否则属性会消失
        etCode.setHint(new SpannedString(ss2)); // 一定要进行转换,否则属性会消失

        //邮箱
        String email = App.getUserBean().getEmail();
        if (!TextUtils.isEmpty(email)) {
            etEmail.setText(email);
            etEmail.setSelection(email.length());
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



    @OnClick({R.id.btn_send_code, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_code:
                presenter.sendSms(view);
                break;
            case R.id.btn_ok:
                presenter.updateUserEmail();
                break;
        }
    }

    @Override
    public String getEmail() {
        return etEmail.getText().toString();
    }

    @Override
    public void getVerificationCode() {
        if (btnSendCode.isEnabled()) {
            btnSendCode.setEnabled(false);
        }
    }


    @Override
    public void getVerificationCodeEnd() {
        btnSendCode.setEnabled(true);
        //显示文本
        btnSendCode.setText(R.string.m30_verification_code);
    }

    @Override
    public void setCountDown(String countDown) {
        //倒计时
        btnSendCode.setText(countDown);
    }

    @Override
    public void onError(String onError) {
        requestError(onError);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
