package com.growatt.grohome.module.service;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.ManulListAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.service.presenter.ManualListPresenter;
import com.growatt.grohome.module.service.view.IManualListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ManualListActivity extends BaseActivity<ManualListPresenter> implements IManualListView, BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.gl_start)
    Guideline glStart;
    @BindView(R.id.gl_end)
    Guideline glEnd;
    @BindView(R.id.rv_manual)
    RecyclerView rvManual;

    private ManulListAdapter mManualAdapter;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }


    @Override
    protected ManualListPresenter createPresenter() {
        return new ManualListPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manual_list;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);
        tvTitle.setText(R.string.m317_common_problem);

        rvManual.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mManualAdapter = new ManulListAdapter(R.layout.item_faq, new ArrayList<>());
        View emptyView = LayoutInflater.from(this).inflate(R.layout.comment_empty_view, rvManual, false);
        mManualAdapter.setEmptyView(emptyView);
        rvManual.setAdapter(mManualAdapter);
    }

    @Override
    protected void initData() {
        List<String> newlist = presenter.getManulList();
        mManualAdapter.replaceData(newlist);
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
        mManualAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        presenter.toManualDetail(position);
    }
}
