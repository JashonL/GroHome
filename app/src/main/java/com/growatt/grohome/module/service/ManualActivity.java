package com.growatt.grohome.module.service;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.ManualAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.service.presenter.ManualPresenter;
import com.growatt.grohome.module.service.view.IManualView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ManualActivity extends BaseActivity<ManualPresenter> implements IManualView {


    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ManualAdapter mSceneViewPagerAdapter;


    @Override
    protected ManualPresenter createPresenter() {
        return new ManualPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manual;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return_w);
        tvTitle.setText("");

        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.nocolor));

        mSceneViewPagerAdapter = new ManualAdapter(this, new ArrayList<>());
        viewPager.setAdapter(mSceneViewPagerAdapter);

    }

    @Override
    protected void initData() {
        presenter.getImageList();
     /*   if (!TextUtils.isEmpty(presenter.title)){
            tvTitle.setText(presenter.title);
        }*/
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
    public void showManual(List<String> imageUrls) {
        mSceneViewPagerAdapter.freshData(imageUrls);
    }

    @Override
    public void error(String msg) {
        requestError(msg);
    }



}
