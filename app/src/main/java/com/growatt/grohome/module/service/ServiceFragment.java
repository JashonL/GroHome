package com.growatt.grohome.module.service;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.ServiceCommondityAdapter;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.module.service.presenter.ServicePresenter;
import com.growatt.grohome.module.service.view.IServiceFragmentView;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;

public class ServiceFragment extends BaseFragment<ServicePresenter> implements IServiceFragmentView {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.icon_manual)
    ImageView iconManual;
    @BindView(R.id.tv_manual)
    AppCompatTextView tvManual;
    @BindView(R.id.cl_manual)
    ConstraintLayout clManual;
    @BindView(R.id.icon_faq)
    ImageView iconFaq;
    @BindView(R.id.tv_faq)
    AppCompatTextView tvFaq;
    @BindView(R.id.cl_faq)
    ConstraintLayout clFaq;
    @BindView(R.id.tv_selection)
    AppCompatTextView tvSelection;

    private ServiceCommondityAdapter mServiceCommondityAdapter;

    @Override
    protected ServicePresenter createPresenter() {
        return new ServicePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_service;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarView(statusBarView).statusBarColor(R.color.white).statusBarDarkFont(true,0.2f).init();
    }

    @Override
    protected void initView() {
        mServiceCommondityAdapter = new ServiceCommondityAdapter(R.layout.item_commodity, new ArrayList<>());
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {

    }
}
