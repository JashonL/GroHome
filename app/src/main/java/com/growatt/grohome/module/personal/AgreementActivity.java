package com.growatt.grohome.module.personal;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.personal.presenter.AgreementPresenter;
import com.growatt.grohome.module.personal.view.IAgreementView;

import butterknife.BindView;


public class AgreementActivity extends BaseActivity<AgreementPresenter> implements IAgreementView {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;



    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }


    @Override
    protected AgreementPresenter createPresenter() {
        return new AgreementPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initViews() {
        if (GlobalConstant.AGREEMENT.equals(presenter.contentType)){
            tvTitle.setText(R.string.m25_user_agreement);
        }else {
            tvTitle.setText(R.string.m110_privacy_policy);
        }
        toolbar.setNavigationIcon(R.drawable.icon_return);
    }

    @Override
    protected void initData() {
        presenter.getContent();
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
    public void showContent(String content) {
        tvContent.setText(content);
    }
}
