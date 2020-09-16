package com.growatt.grohome.module.service;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.ManulListAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.ManualBean;
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
    @BindView(R.id.rv_manual)
    RecyclerView rvManual;
    @BindView(R.id.srl_pull)
    SwipeRefreshLayout swipeRefresh;

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
        TextView tvTips = emptyView.findViewById(R.id.tv_empty_tips);
        tvTips.setText(R.string.m260_no_data);
        mManualAdapter.setEmptyView(emptyView);
        rvManual.setAdapter(mManualAdapter);

        //下拉刷新
        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.color_theme_green));
    }

    @Override
    protected void initData() {
       presenter.getManulList();
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

        swipeRefresh.setOnRefreshListener(() -> {
            try {
                presenter.getManulList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ManualBean bean = mManualAdapter.getData().get(position);
        if (bean!=null){
            String content = bean.getContent();
            String title = bean.getTitle();
            presenter.toManualDetail(content,title);
        }
    }

    @Override
    public void updata(List<ManualBean> list) {
        mManualAdapter.replaceData(list);
    }

    @Override
    public void onError(String onError) {
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
        requestError(onError);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

}
