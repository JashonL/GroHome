package com.growatt.grohome.module.room;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.RoomLineListAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.eventbus.HomeRoomStatusBean;
import com.growatt.grohome.module.room.presenter.RoomLineListPresenter;
import com.growatt.grohome.module.room.view.IRoomLineListView;
import com.growatt.grohome.utils.MyToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomLineListActivity extends BaseActivity<RoomLineListPresenter> implements IRoomLineListView, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvRoom)
    RecyclerView rvRoom;
    @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;


    private RoomLineListAdapter mRoomLineListAdapter;
    //头部
    private TextView tvMenuRightText;
    private MenuItem switchItem;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected RoomLineListPresenter createPresenter() {
        return new RoomLineListPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_linelist;
    }

    @Override
    protected void initViews() {
        //头部toolBar
        tvTitle.setText(R.string.m207_transfer);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.inflateMenu(R.menu.menu_right_text);
        switchItem = toolbar.getMenu().findItem(R.id.item_save);
        switchItem.setActionView(R.layout.menu_right_text);
        tvMenuRightText = switchItem.getActionView().findViewById(R.id.tv_right_text);
        tvMenuRightText.setText(R.string.m248_save);
        //设备列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRoom.setLayoutManager(layoutManager);
        mRoomLineListAdapter = new RoomLineListAdapter(R.layout.item_room_line_list, new ArrayList<>());
        rvRoom.setAdapter(mRoomLineListAdapter);
        rvRoom.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(this, R.color.nocolor)));

        mRoomLineListAdapter.setOnItemChildClickListener(this);
        mRoomLineListAdapter.setOnItemClickListener(this);

        srlPull.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        presenter.getRoomList();
    }

    /**
     * 刷新房间
     *
     * @param bean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRoomBean(@NonNull HomeRoomStatusBean bean) {
        try {
            presenter.getRoomList();
        } catch (Exception e) {
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
                    presenter.getRoomListByServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvMenuRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //当前选中的房间
                    HomeRoomBean nowItem = mRoomLineListAdapter.getNowItem();
                    if (nowItem == null) return;
                    presenter.transferDevice(String.valueOf(nowItem.getCid()),nowItem.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mRoomLineListAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mRoomLineListAdapter) {
            mRoomLineListAdapter.setSelectItem(position);
        }

    }

    @Override
    public void updata(List<HomeRoomBean> roomList) {
        mRoomLineListAdapter.replaceData(roomList);
    }

    @Override
    public void transferSuccess() {
        MyToastUtils.toast(R.string.m200_success);
        finish();
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

    @Override
    public void transferFail(String msg) {
        MyToastUtils.toast(msg);
    }

    @Override
    public void onError(String msg) {
        if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }
        requestError(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @OnClick(R.id.ivAddRoom)
    public void onViewClicked() {
        presenter.addRoom();
    }
}
