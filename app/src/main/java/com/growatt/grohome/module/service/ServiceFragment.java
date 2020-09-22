package com.growatt.grohome.module.service;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.BannerCustomAdapter;
import com.growatt.grohome.adapter.ServiceCommondityAdapter;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.bean.CommondityBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.service.presenter.ServicePresenter;
import com.growatt.grohome.module.service.view.IServiceFragmentView;
import com.growatt.grohome.utils.CommentUtils;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ServiceFragment extends BaseFragment<ServicePresenter> implements IServiceFragmentView, BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;

    private ServiceCommondityAdapter mServiceCommondityAdapter;
    private boolean isAdalreadyShow = false;

    @Override
    protected ServicePresenter createPresenter() {
        return new ServicePresenter(getActivity(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_service;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarView(statusBarView).statusBarColor(R.color.gray_f7).statusBarDarkFont(true, 0.2f).init();
    }

    @Override
    protected void initView() {
        toolbar.setVisibility(View.GONE);

        //设备列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvDevice.setLayoutManager(layoutManager);
        mServiceCommondityAdapter = new ServiceCommondityAdapter(R.layout.item_service_commondity, new ArrayList<>());
        rlvDevice.setAdapter(mServiceCommondityAdapter);
        int div = CommentUtils.dip2px(getActivity(), 20);
        rlvDevice.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.VERTICAL, div, ContextCompat.getColor(getActivity(), R.color.nocolor)));


        /**
         * 动态设置轮播图的宽高
         */
        float ratio=686/320f;
        float dimension = getResources().getDimension(R.dimen.dp_10);
        int px = CommentUtils.dip2px(getContext(), dimension);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) banner.getLayoutParams();
        int width=getResources().getDisplayMetrics().widthPixels-px*2;
        layoutParams.height= (int) (width/ratio);
        banner.setLayoutParams(layoutParams);

    }

    @Override
    protected void initData() {
        presenter.getAdvertisingList();
        List<CommondityBean> newList = presenter.getNewList();
        mServiceCommondityAdapter.replaceData(newList);
    }


    @Override
    public void initListener() {
        super.initListener();
        mServiceCommondityAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isAdalreadyShow) {
            presenter.getAdvertisingList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setBannerList(List<String> bannerList) {
        isAdalreadyShow = true;
        banner.setAdapter(new BannerCustomAdapter(bannerList)).addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(getActivity()));
     /*   banner.setAdapter(new BannerImageAdapter<String>(bannerList) {
            @Override
            public void onBindView(BannerImageHolder holder, String path, int position, int size) {
                //图片加载自己实现
                Glide.with(holder.itemView)
                        .load(path)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imageView);
            }
        })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(getActivity()));*/
    }

    @Override
    public void onError(String onError) {
        requestError(onError);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CommondityBean commondityBean = mServiceCommondityAdapter.getData().get(position);
        String url = commondityBean.getUrl();
        presenter.openWebView(url);
    }

    @OnClick({R.id.cl_manual, R.id.cl_faq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_manual:
                presenter.toManual();
                break;
            case R.id.cl_faq:
                presenter.toFqa();
                break;
        }
    }
}
