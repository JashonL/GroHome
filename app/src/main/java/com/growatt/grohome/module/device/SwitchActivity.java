package com.growatt.grohome.module.device;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.SwitchAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.PanelSwitchBean;
import com.growatt.grohome.customview.GridDivider;
import com.growatt.grohome.eventbus.DevEditNameBean;
import com.growatt.grohome.eventbus.DeviceAddOrDelMsg;
import com.growatt.grohome.eventbus.TransferDevMsg;
import com.growatt.grohome.module.device.presenter.SwitchPresenter;
import com.growatt.grohome.module.device.view.ISwitchView;
import com.growatt.grohome.utils.CommentUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SwitchActivity extends BaseActivity<SwitchPresenter> implements ISwitchView, Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_panel_grid)
    RecyclerView rvPanelGrid;
    @BindView(R.id.ivAllOpen)
    ImageView ivAllOpen;
    @BindView(R.id.ivAllClose)
    ImageView ivAllClose;
    private SwitchAdapter mSwitchAdapter;

    @Override
    protected SwitchPresenter createPresenter() {
        return new SwitchPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_switch;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarView(statusBarView).statusBarColor(R.color.color_panel_background).init();
    }


    @Override
    protected void initViews() {
        //设置头部
        toolbar.setNavigationIcon(R.drawable.icon_return_w);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_panel_background));
        toolbar.inflateMenu(R.menu.menu_device_setting);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        //初始化开关列表
        mSwitchAdapter = new SwitchAdapter(R.layout.item_switch);
        int div = CommentUtils.dip2px(this, 20);
        GridDivider defaultItemDecoration = new GridDivider(ContextCompat.getColor(this, R.color.nocolor), div, div);
        if (rvPanelGrid.getItemDecorationCount() == 0) {
            rvPanelGrid.addItemDecoration(defaultItemDecoration);
        }
        changeLayout(true, new ArrayList<>());
    }


    private void changeLayout(boolean isChange, List<PanelSwitchBean.SwichBean> newList) {
        //布局切换方法
        if (isChange) {
            //瀑布流设置
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            rvPanelGrid.setLayoutManager(layoutManager);
            mSwitchAdapter = new SwitchAdapter(R.layout.item_switch, newList);
            rvPanelGrid.setAdapter(mSwitchAdapter);
        } else {
            //列表模式
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvPanelGrid.setLayoutManager(layoutManager);
            mSwitchAdapter = new SwitchAdapter(R.layout.item_switch, newList);
            rvPanelGrid.setAdapter(mSwitchAdapter);
        }
        mSwitchAdapter.setOnItemChildClickListener(this);
        mSwitchAdapter.setOnItemClickListener(this);
    }


    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
        mSwitchAdapter.setOnItemClickListener(this);
        mSwitchAdapter.setOnItemChildClickListener(this);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        try {
            presenter.initDevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String onError) {
        requestError(onError);
    }

    @Override
    public void setTitle(String name) {
        if (!TextUtils.isEmpty(name)) {
            tvTitle.setText(name);
        }
    }

    @Override
    public void freshData(List<PanelSwitchBean.SwichBean> beanList) {
        if (beanList != null) {
            if (beanList.size() > 1) {
                changeLayout(true, beanList);
            } else {
                changeLayout(false, beanList);
            }
            setAllOnOff();
        }
    }

    @Override
    public void freshStatus(int switchId, boolean onOff) {
        PanelSwitchBean.SwichBean swichBean = mSwitchAdapter.getData().get(switchId);
        swichBean.setOnOff(onOff ? 1 : 0);
        mSwitchAdapter.notifyDataSetChanged();
        setAllOnOff();
    }

    /**
     * 设置全开或者全关按钮
     */
    private void setAllOnOff() {
        int openCount = 0;
        for (int i = 0; i < mSwitchAdapter.getData().size(); i++) {
            PanelSwitchBean.SwichBean swichBean = mSwitchAdapter.getData().get(i);
            int onOff = swichBean.getOnOff();
            if (onOff == 1) {
                openCount++;
            }
        }
    }

    private void setOnoffButton(int openRes, int closeRes) {
        ivAllOpen.setImageResource(openRes);
        ivAllClose.setImageResource(closeRes);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_set) {
            presenter.jumpSetting();
        }
        return true;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        PanelSwitchBean.SwichBean swichBean = mSwitchAdapter.getData().get(position);
        if (view.getId() == R.id.cl_setting) {
            presenter.jumpEditName(swichBean.getId(),swichBean.getCustomName());
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mSwitchAdapter) {
            PanelSwitchBean.SwichBean swichBean = mSwitchAdapter.getData().get(position);
            presenter.singleOnOff(swichBean.getSwitchId());
        }
    }


    @OnClick({R.id.llAllOpen, R.id.llAllClose, R.id.llTiming})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llAllOpen:
                presenter.allOnoff(true);
                break;
            case R.id.llAllClose:
                presenter.allOnoff(false);
                break;
            case R.id.llTiming:
                presenter.jumpTiming();
                break;
        }
    }



    /*修改设备名称*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showEditSuccess(DevEditNameBean msg) {
        if (msg != null) {
            String name = msg.getName();
            presenter.setDevName(name);
            setTitle(name);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDevTransferBean(TransferDevMsg bean) {
        if (bean != null) {
            //获取列表设备列表
            try {
                presenter.setRoomInfo(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(DeviceAddOrDelMsg bean) {
        if (bean != null) {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyTuya();
        EventBus.getDefault().unregister(this);
    }
}
