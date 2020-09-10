package com.growatt.grohome.module.config;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.config.Presenter.WiFiSetGuidePresenter;
import com.growatt.grohome.module.config.view.IWiFiSetGuideView;

import butterknife.BindView;

public class WiFiSetGuideActivity extends BaseActivity<WiFiSetGuidePresenter> implements IWiFiSetGuideView {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected WiFiSetGuidePresenter createPresenter() {
        return new WiFiSetGuidePresenter(this,this);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView) .statusBarColor(R.color.white).init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wifi_set_guide;
    }

    @Override
    protected void initViews() {
        //初始化头部
        tvTitle.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.icon_return);
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void initData() {
        try {
            presenter.readFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showConfigText(String content) {
        tvContent.setText(content);
    }
}
