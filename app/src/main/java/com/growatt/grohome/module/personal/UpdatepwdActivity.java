package com.growatt.grohome.module.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.personal.presenter.UpdatePwdPresenter;
import com.growatt.grohome.module.personal.view.IUpdatePwdView;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.tv_new_password_title)
    TextView tvNewPasswordTitle;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.tv_repeat_password)
    TextView tvRepeatPassword;
    @BindView(R.id.et_repeat_password)
    EditText etRepeatPassword;
    @BindView(R.id.btn_ok)
    Button btnOk;

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
}
