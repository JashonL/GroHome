package com.growatt.grohome.module.personal;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.personal.presenter.UpdatePwdPresenter;
import com.growatt.grohome.module.personal.view.IUpdatePwdView;

import butterknife.BindView;
import butterknife.OnClick;

public class UpdatepwdActivity extends BaseActivity<UpdatePwdPresenter> implements IUpdatePwdView {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_old_password_title)
    TextView tvOldPasswordTitle;
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.iv_password_view)
    ImageView ivPasswordView;
    @BindView(R.id.ll_old_visible)
    LinearLayout llOldVisible;
    @BindView(R.id.tv_new_password_title)
    TextView tvNewPasswordTitle;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.iv_old_view)
    ImageView ivOldView;
    @BindView(R.id.ll_new_visible)
    LinearLayout llNewVisible;
    @BindView(R.id.tv_repeat_password)
    TextView tvRepeatPassword;
    @BindView(R.id.et_repeat_password)
    EditText etRepeatPassword;
    @BindView(R.id.iv_repeat_view)
    ImageView ivRepeatView;
    @BindView(R.id.ll_repeat_visible)
    LinearLayout llRepeatVisible;
    @BindView(R.id.btn_ok)
    Button btnOk;


    private boolean passwordOn = false;

    private boolean registerPassordOn = false;

    private boolean repeatePassordOn = false;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected UpdatePwdPresenter createPresenter() {
        return new UpdatePwdPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_password;
    }

    @Override
    protected void initViews() {
        //头部
        tvTitle.setText(R.string.m277_my_information);
        toolbar.setNavigationIcon(R.drawable.icon_return);

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


    @OnClick({R.id.btn_ok,R.id.ll_old_visible,R.id.ll_new_visible,R.id.ll_repeat_visible})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                presenter.changePassword();
                break;
            case R.id.ll_old_visible:
                passwordOn = !passwordOn;
                clickPasswordSwitch(ivPasswordView,etOldPassword,passwordOn);
                break;
            case R.id.ll_new_visible:
                registerPassordOn = !registerPassordOn;
                clickPasswordSwitch(ivOldView,etNewPassword,registerPassordOn);
                break;
            case R.id.ll_repeat_visible:
                repeatePassordOn = !repeatePassordOn;
                clickPasswordSwitch(ivRepeatView,etRepeatPassword,repeatePassordOn);
                break;
        }

    }

    @Override
    public String getOldPassWord() {
        return etOldPassword.getText().toString();
    }

    @Override
    public String getNewPassWord() {
        return etNewPassword.getText().toString();
    }

    @Override
    public String getRepeatePassWord() {
        return etRepeatPassword.getText().toString();
    }

    @Override
    public void onError(String error) {
        requestError(error);
    }


    public void clickPasswordSwitch(ImageView imageView, EditText editText, boolean visible) {
        if (visible) {
            imageView.setImageResource(R.drawable.icon_signin_see);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            imageView.setImageResource(R.drawable.icon_signin_conceal);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        if (editText.getText().length() > 0) {
            editText.setSelection(editText.getText().length());
        }
    }

}
