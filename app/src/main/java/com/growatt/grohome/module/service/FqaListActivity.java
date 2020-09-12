package com.growatt.grohome.module.service;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.FqaAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.service.presenter.FQAPresenter;
import com.growatt.grohome.module.service.view.IFQAView;

import java.util.ArrayList;

import butterknife.BindView;

public class FqaListActivity extends BaseActivity<FQAPresenter> implements IFQAView {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_faq)
    RecyclerView rvFaq;


    private FqaAdapter mFqaAdapter;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected FQAPresenter createPresenter() {
        return new FQAPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fqa_list;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);
        tvTitle.setText(R.string.m317_common_problem);

        rvFaq.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mFqaAdapter = new FqaAdapter(R.layout.item_faq, new ArrayList<>());
        View emptyView = LayoutInflater.from(this).inflate(R.layout.comment_empty_view, rvFaq, false);
        mFqaAdapter.setEmptyView(emptyView);
        rvFaq.setAdapter(mFqaAdapter);
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
