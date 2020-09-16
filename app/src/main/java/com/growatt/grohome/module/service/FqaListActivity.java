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
import com.growatt.grohome.adapter.FqaAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.FqaBean;
import com.growatt.grohome.module.service.presenter.FQAPresenter;
import com.growatt.grohome.module.service.view.IFQAView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FqaListActivity extends BaseActivity<FQAPresenter> implements IFQAView, BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_faq)
    RecyclerView rvFaq;
    @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;


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
        tvTitle.setText(R.string.m78_faq);

        rvFaq.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mFqaAdapter = new FqaAdapter(R.layout.item_faq, new ArrayList<>());
        View emptyView = LayoutInflater.from(this).inflate(R.layout.comment_empty_view, rvFaq, false);
        TextView tvTips = emptyView.findViewById(R.id.tv_empty_tips);
        tvTips.setText(R.string.m260_no_data);
        mFqaAdapter.setEmptyView(emptyView);
        rvFaq.setAdapter(mFqaAdapter);

        //下拉刷新
        srlPull.setColorSchemeColors(ContextCompat.getColor(this, R.color.color_theme_green));
    }

    @Override
    protected void initData() {
        presenter.getFqaList();
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
        mFqaAdapter.setOnItemClickListener(this);

        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.getFqaList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        FqaBean fqaBean = mFqaAdapter.getData().get(position);
        if (fqaBean!=null){
            presenter.toFqaDetail(fqaBean.getContent());
        }
    }


    @Override
    public void onError(String onError) {
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
        requestError(onError);
    }

    @Override
    public void updata(List<FqaBean> list) {
        mFqaAdapter.replaceData(list);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
    }


}
