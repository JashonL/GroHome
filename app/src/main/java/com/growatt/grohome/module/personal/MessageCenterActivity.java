package com.growatt.grohome.module.personal;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.MessageAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.MessageBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.personal.presenter.MessagePresenter;
import com.growatt.grohome.module.personal.view.IMessageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MessageCenterActivity extends BaseActivity<MessagePresenter> implements IMessageView {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_meesage)
    RecyclerView rvMeesage;
    @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;


    private MessageAdapter mMessageAdapter;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }


    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void initViews() {
        //头部
        tvTitle.setText(R.string.m69_message_center);
        toolbar.setNavigationIcon(R.drawable.icon_return);

        //列表
        rvMeesage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMessageAdapter = new MessageAdapter(R.layout.item_message, new ArrayList<>());
        rvMeesage.setAdapter(mMessageAdapter);

        rvMeesage.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL,  32,ContextCompat.getColor(this, R.color.nocolor)));
        //设置空布局
        View view = LayoutInflater.from(this).inflate(R.layout.comment_empty_view, rvMeesage, false);
        mMessageAdapter.setEmptyView(view);


    }

    @Override
    protected void initData() {
        presenter.getDataByIntent();
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


        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.getLoginRecord();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setMessageData(List<MessageBean> list) {
        mMessageAdapter.replaceData(list);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
    }


    @Override
    public void onError(String msg) {
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
        requestError(msg);
    }

}
