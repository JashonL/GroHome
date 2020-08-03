package com.growatt.grohome.module.scenes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.LaunchTapAdapter;
import com.growatt.grohome.adapter.LinkageSceneAdapter;
import com.growatt.grohome.adapter.LogsSceneAdapter;
import com.growatt.grohome.adapter.SceneViewPagerAdapter;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.device.DeviceTypeActivity;
import com.growatt.grohome.module.scenes.presenter.ScenesPresenter;
import com.growatt.grohome.module.scenes.view.IScenesView;
import com.growatt.grohome.utils.MyToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ScenesFragment extends BaseFragment<ScenesPresenter> implements IScenesView, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_title)
    TabLayout tabTitle;
    @BindView(R.id.view_pager)
    ViewPager viewPager;



    private RecyclerView mRlvLaunch;
    private RecyclerView mRlvLinkage;
    private RecyclerView mRlvLogs;

    private SceneViewPagerAdapter mSceneViewPagerAdapter;
    private LaunchTapAdapter mLaunchTapAdapter;
    private LinkageSceneAdapter mLinkageSceneAdapter;
    private LogsSceneAdapter mLogsSceneAdapter;

    private View launchEmpty;
    private View linkageEmpty;
    private View LogsEmpty;

    private LinearLayout llAddLaunchView;
    private LinearLayout llAddLinkageView;

    @Override
    protected ScenesPresenter createPresenter() {
        return new ScenesPresenter(getActivity(),this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scenes;
    }

    @Override
    protected void initView() {
        //头部toolBar
        tvTitle.setVisibility(View.GONE);
        toolbar.setTitle(R.string.m87_scenes);
        toolbar.inflateMenu(R.menu.menu_grohome);
        toolbar.setOnMenuItemClickListener(this);

        //viewpager
        List<View> pagers = new ArrayList<>();
        View launchView = LayoutInflater.from(getContext()).inflate(R.layout.view_launch_tap_to_run, null, false);
        View linkageView = LayoutInflater.from(getContext()).inflate(R.layout.view_linkage_detail, null, false);
        View logsView = LayoutInflater.from(getContext()).inflate(R.layout.view_logs_detail, null, false);

        //一键执行
        mRlvLaunch = launchView.findViewById(R.id.rlv_launch_tap);
        mRlvLaunch.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRlvLaunch.addItemDecoration(new LinearDivider(getActivity(),LinearLayoutManager.VERTICAL,5, ContextCompat.getColor(getActivity(),R.color.nocolor)));
        mLaunchTapAdapter=new LaunchTapAdapter(R.layout.item_launch_tap_to_run,new ArrayList<>());
        launchEmpty=LayoutInflater.from(getContext()).inflate(R.layout.scene_launch_empty_view,mRlvLaunch,false);
        llAddLaunchView = launchEmpty.findViewById(R.id.ll_add_launch_background);
        mLaunchTapAdapter.setEmptyView(launchEmpty);
        mRlvLaunch.setAdapter(mLaunchTapAdapter);
        //条件执行
        mRlvLinkage = linkageView.findViewById(R.id.rlv_linkage_detail);
        mRlvLinkage.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRlvLinkage.addItemDecoration(new LinearDivider(getActivity(),LinearLayoutManager.VERTICAL,ContextCompat.getColor(getActivity(),R.color.nocolor),32));
        mLinkageSceneAdapter=new LinkageSceneAdapter(R.layout.item_linkage_detail,new ArrayList<>());
        linkageEmpty=LayoutInflater.from(getContext()).inflate(R.layout.scene_linkage_empty_view,mRlvLinkage,false);
        llAddLinkageView = linkageEmpty.findViewById(R.id.ll_add_linkage_background);
        mLinkageSceneAdapter.setEmptyView(linkageEmpty);
        mRlvLinkage.setAdapter(mLinkageSceneAdapter);
        //日志
        mRlvLogs = logsView.findViewById(R.id.rlv_logs);
        mRlvLogs.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRlvLogs.addItemDecoration(new LinearDivider(getActivity(),LinearLayoutManager.VERTICAL,ContextCompat.getColor(getActivity(),R.color.nocolor),32));
        mLogsSceneAdapter=new LogsSceneAdapter(R.layout.item_logs_detail,new ArrayList<>());
        LogsEmpty = LayoutInflater.from(getContext()).inflate(R.layout.scene_logs_empty_view, mRlvLogs, false);
        mLogsSceneAdapter.setEmptyView(LogsEmpty);
        mRlvLogs.setAdapter(mLogsSceneAdapter);



        pagers.add(launchView);
        pagers.add(linkageView);
        pagers.add(logsView);
        mSceneViewPagerAdapter = new SceneViewPagerAdapter(pagers);
        viewPager.setAdapter(mSceneViewPagerAdapter);
        //将tablayout和Viewpager绑定
        tabTitle.setupWithViewPager(viewPager);
        //tablayout设置标题
        String[] titles = new String[]{getString(R.string.m81_launch_tap_to_run), getString(R.string.m82_linkage_details), getString(R.string.m83_logs)};
        tabTitle.removeAllTabs();
        for (String title : titles) {
            TabLayout.Tab tab = tabTitle.newTab();
            tab.setText(title);
            tabTitle.addTab(tab);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initListener() {
        super.initListener();
        llAddLaunchView.setOnClickListener(this);
        llAddLinkageView.setOnClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getContext(), DeviceTypeActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_add_launch_background:
               presenter.addSceneLaunchTap();
                break;
            case R.id.ll_add_linkage_background:
                MyToastUtils.toast("点击一liandong");
                break;
        }
    }
}
