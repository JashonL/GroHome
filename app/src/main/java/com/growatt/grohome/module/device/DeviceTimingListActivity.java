package com.growatt.grohome.module.device;

import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.growatt.grohome.adapter.DeviceTimingAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.bean.DeviceTimingBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.eventbus.DeviceTimingMsg;
import com.growatt.grohome.module.device.presenter.DeviceTimingPresenter;
import com.growatt.grohome.module.device.view.IDeviceTimingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DeviceTimingListActivity extends BaseActivity<DeviceTimingPresenter> implements IDeviceTimingView , BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener{
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_timing)
    RecyclerView rvTiming;
    @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;


    private DeviceTimingAdapter mAdapter;

    //头部
    private TextView tvMenuRightText;
    private MenuItem switchItem;

    @Override
    protected DeviceTimingPresenter createPresenter() {
        return new DeviceTimingPresenter(this, this);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView) .statusBarColor(R.color.white).init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_timing;
    }

    @Override
    protected void initViews() {
        //头部
        tvTitle.setText(R.string.m146_timer);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.inflateMenu(R.menu.menu_right_text);
        switchItem = toolbar.getMenu().findItem(R.id.item_save);
        switchItem.setActionView(R.layout.menu_right_text);
        tvMenuRightText = switchItem.getActionView().findViewById(R.id.tv_right_text);
        tvMenuRightText.setTextColor(ContextCompat.getColor(this, R.color.color_text_33));
        tvMenuRightText.setText(R.string.m259_add);


        //定时列表
        rvTiming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DeviceTimingAdapter(R.layout.item_device_timing_list, new ArrayList<>());
        int div = getResources().getDimensionPixelSize(R.dimen.dp_10);
        LinearDivider linearDivider = new LinearDivider(this, LinearLayoutManager.VERTICAL, div, ContextCompat.getColor(this, R.color.nocolor));
        rvTiming.setAdapter(mAdapter);
        rvTiming.addItemDecoration(linearDivider);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.comment_empty_view, rvTiming, false);
        TextView emptyTextView = emptyView.findViewById(R.id.tv_empty_tips);
        emptyTextView.setText(R.string.m260_no_data);
        mAdapter.setEmptyView(emptyView);

        srlPull.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        try {
            presenter.refresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        srlPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    presenter.refresh();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        tvMenuRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.jumpToSwitchTimgSet();
            }
        });
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        DeviceTimingBean dataBean = mAdapter.getData().get(position);
        if (view.getId() == R.id.iv_switch) {
            int status = dataBean.getStatus();
            try {
                if (status == 0) {
                    dataBean.setStatus(1);
                    presenter.editTiming(dataBean);
                } else {
                    dataBean.setStatus(0);
                    presenter.editTiming(dataBean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DeviceTimingBean dataBean = mAdapter.getData().get(position);
        if (dataBean != null) {
            presenter.jumpToSwitchTimgSet(dataBean);
        }
    }

    @Override
    public void updata(List<DeviceTimingBean> beanList) {
        mAdapter.replaceData(beanList);
    }

    @Override
    public void upList() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String onError) {
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
        requestError(onError);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
    }

    @Override
    public void onErrorCode(BaseBean bean) {
        super.onErrorCode(bean);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(DeviceTimingMsg bean) {
        if (bean != null) {
            //获取列表设备列表
            try {
                presenter.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
