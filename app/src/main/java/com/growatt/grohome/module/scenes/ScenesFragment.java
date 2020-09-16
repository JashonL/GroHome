package com.growatt.grohome.module.scenes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.LaunchTapAdapter;
import com.growatt.grohome.adapter.LinkageSceneAdapter;
import com.growatt.grohome.adapter.LogsSceneAdapter;
import com.growatt.grohome.adapter.SceneViewPagerAdapter;
import com.growatt.grohome.adapter.ScenesDivceListAdapter;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.bean.ScenesBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.customview.GridDivider;
import com.growatt.grohome.customview.MySwipeRefreshLayout;
import com.growatt.grohome.eventbus.FreshScenesMsg;
import com.growatt.grohome.module.scenes.presenter.ScenesPresenter;
import com.growatt.grohome.module.scenes.view.IScenesView;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.mylhyl.circledialog.res.drawable.CircleDrawable;
import com.mylhyl.circledialog.res.values.CircleColor;
import com.mylhyl.circledialog.res.values.CircleDimen;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ScenesFragment extends BaseFragment<ScenesPresenter> implements IScenesView, Toolbar.OnMenuItemClickListener, View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_title)
    TabLayout tabTitle;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;


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
        return new ScenesPresenter(getActivity(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scenes;
    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarView(statusBarView).statusBarColor(R.color.gray_f7).statusBarDarkFont(true,0.2f).init();
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        //头部toolBar
        tvTitle.setVisibility(View.GONE);
        toolbar.setTitle(R.string.m87_scenes);
        toolbar.inflateMenu(R.menu.menu_grohome);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.gray_f7));

        srlPull.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.color_theme_green));

        //viewpager
        List<View> pagers = new ArrayList<>();
        View launchView = LayoutInflater.from(getContext()).inflate(R.layout.view_launch_tap_to_run, null, false);
        View linkageView = LayoutInflater.from(getContext()).inflate(R.layout.view_linkage_detail, null, false);
        View logsView = LayoutInflater.from(getContext()).inflate(R.layout.view_logs_detail, null, false);

        //一键执行
        mRlvLaunch = launchView.findViewById(R.id.rlv_launch_tap);
        mRlvLaunch.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int div = getResources().getDimensionPixelSize(R.dimen.dp_10);
        mRlvLaunch.addItemDecoration( new GridDivider(ContextCompat.getColor(getContext(), R.color.nocolor), div, div));
        mLaunchTapAdapter = new LaunchTapAdapter(new ArrayList<>());
        launchEmpty = LayoutInflater.from(getContext()).inflate(R.layout.scene_launch_empty_view, mRlvLaunch, false);
        llAddLaunchView = launchEmpty.findViewById(R.id.ll_add_launch_background);
        mLaunchTapAdapter.setEmptyView(launchEmpty);
        mRlvLaunch.setAdapter(mLaunchTapAdapter);
        //条件执行
        mRlvLinkage = linkageView.findViewById(R.id.rlv_linkage_detail);
        mRlvLinkage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        mRlvLinkage.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.VERTICAL, div, ContextCompat.getColor(getActivity(), R.color.nocolor)));
        mLinkageSceneAdapter = new LinkageSceneAdapter(new ArrayList<>());
        linkageEmpty = LayoutInflater.from(getContext()).inflate(R.layout.scene_linkage_empty_view, mRlvLinkage, false);
        llAddLinkageView = linkageEmpty.findViewById(R.id.ll_add_linkage_background);
        mLinkageSceneAdapter.setEmptyView(linkageEmpty);
        mRlvLinkage.setAdapter(mLinkageSceneAdapter);

        //日志
        mRlvLogs = logsView.findViewById(R.id.rlv_logs);
        mRlvLogs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        mRlvLogs.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.VERTICAL, ContextCompat.getColor(getActivity(), R.color.nocolor), 32));
        mLogsSceneAdapter = new LogsSceneAdapter(R.layout.item_logs_detail, new ArrayList<>());
        LogsEmpty = LayoutInflater.from(getContext()).inflate(R.layout.scene_logs_empty_view, mRlvLogs, false);
        mLogsSceneAdapter.setEmptyView(LogsEmpty);
        mRlvLogs.setAdapter(mLogsSceneAdapter);


        pagers.add(launchView);
        pagers.add(linkageView);
//        pagers.add(logsView);
        mSceneViewPagerAdapter = new SceneViewPagerAdapter(pagers);
        viewPager.setAdapter(mSceneViewPagerAdapter);
        //将tablayout和Viewpager绑定
        tabTitle.setupWithViewPager(viewPager);
        //tablayout设置标题
//        String[] titles = new String[]{getString(R.string.m81_launch_tap_to_run), getString(R.string.m82_smart), getString(R.string.m83_logs)};
        String[] titles = new String[]{getString(R.string.m81_launch_tap_to_run), getString(R.string.m82_smart)};
        tabTitle.removeAllTabs();
        for (String title : titles) {
            TabLayout.Tab tab = tabTitle.newTab();
            tab.setText(title);
            tabTitle.addTab(tab);
        }
    }

    @Override
    protected void initData() {
        try {
            presenter.getSceneList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.getSceneList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
    }


    @Override
    public void initListener() {
        super.initListener();
        llAddLaunchView.setOnClickListener(this);
        llAddLinkageView.setOnClickListener(this);
        mLaunchTapAdapter.setOnItemClickListener(this);
        mLaunchTapAdapter.setOnItemChildClickListener(this);
        mLinkageSceneAdapter.setOnItemClickListener(this);
        mLinkageSceneAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(getContext(), SceneCustomizeActivity.class));
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_launch_background:
                presenter.addScene(GlobalConstant.SCENE_LUANCH_TAP_TO_RUN);
                break;
            case R.id.ll_add_linkage_background:
                presenter.addScene(GlobalConstant.SCENE_SMART);
                break;
        }
    }

    @Override
    public void upDataLaunch(List<ScenesBean.DataBean> data) {
        mLaunchTapAdapter.replaceData(data);
    }

    @Override
    public void upDataSmart(List<ScenesBean.DataBean> data) {
        mLinkageSceneAdapter.replaceData(data);

    }

    @Override
    public void launchTapToRunSuccess(ScenesBean.DataBean dataBean) {
        List<SceneTaskBean> conf = dataBean.getConf();
        View bodyView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_scenes_exeresult, null, false);
        CircleDialogUtils.showCommentBodyViewNoCancel(getContext(), bodyView, dataBean.getName(), getActivity().getSupportFragmentManager(), new OnCreateBodyViewListener() {
            @Override
            public void onCreateBodyView(View view) {
                CircleDrawable bgCircleDrawable = new CircleDrawable(CircleColor.DIALOG_BACKGROUND
                        , 0, 0, CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS);
                view.setBackgroundDrawable(bgCircleDrawable);
                RecyclerView recyclerView = view.findViewById(R.id.rv_device);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                ScenesDivceListAdapter adapter = new ScenesDivceListAdapter(R.layout.item_scenes_device_stutas, conf);
                recyclerView.setAdapter(adapter);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void updataSuccess(int position,ScenesBean.DataBean dataBean) {
        String status = dataBean.getStatus();
        if ("0".equals(status)){
            dataBean.setItemType(1);
        }else {
            dataBean.setItemType(0);
        }
        mLinkageSceneAdapter.setData(position,dataBean);
    }

    @Override
    public void onError(String onError) {
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
        requestError(onError);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

        if (adapter == mLaunchTapAdapter) {//执行
            ScenesBean.DataBean dataBean = mLaunchTapAdapter.getData().get(position);
            if (view.getId() == R.id.iv_right) {
                presenter.toSceneDetail(dataBean);
            }
        }
        if (adapter == mLinkageSceneAdapter) {
            ScenesBean.DataBean dataBean = mLinkageSceneAdapter.getData().get(position);
            if (view.getId() == R.id.iv_right) {
                String status = dataBean.getStatus();
                if ("0".equals(status)){
                    dataBean.setStatus("1");
                }else {
                    dataBean.setStatus("0");
                }
                try {
                    presenter.upScenesData(position,dataBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mLaunchTapAdapter) {//执行
            ScenesBean.DataBean dataBean = mLaunchTapAdapter.getData().get(position);
            presenter.launchTapToRun(dataBean);
        }
        if (adapter == mLinkageSceneAdapter) {
            ScenesBean.DataBean dataBean = mLinkageSceneAdapter.getData().get(position);
            presenter.toSceneDetail(dataBean);
        }
    }

    /**
     * 刷新场景
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSceneseBean(FreshScenesMsg bean) {
        if (bean != null) {
            try {
                presenter.getSceneList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
