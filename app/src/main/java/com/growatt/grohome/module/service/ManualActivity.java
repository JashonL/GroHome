package com.growatt.grohome.module.service;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.ManualAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.customview.MySwipeRefreshLayout;
import com.growatt.grohome.module.service.presenter.ManualPresenter;
import com.growatt.grohome.module.service.view.IManualView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ManualActivity extends BaseActivity<ManualPresenter> implements IManualView {


    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;

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
        mSceneViewPagerAdapter = new ManualAdapter(this,new ArrayList<>());
        viewPager.setAdapter(mSceneViewPagerAdapter);

        srlPull.setColorSchemeColors(ContextCompat.getColor(this, R.color.color_theme_green));
    }

    @Override
    protected void initData() {
        presenter.getImageList();
    }

    @Override
    protected void initListener() {
        super.initListener();
        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.getImageList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void showManual(List<String> imageUrls) {
        mSceneViewPagerAdapter.freshData(imageUrls);
    }

    @Override
    public void error(String msg) {
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
        requestError(msg);
    }





}
