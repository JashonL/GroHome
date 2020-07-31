package com.growatt.grohome.module.room;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.RoomLineListAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.room.presenter.RoomLineListPresenter;
import com.growatt.grohome.module.room.view.IRoomLineListView;
import com.growatt.grohome.utils.MyToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RoomLineListActivity extends BaseActivity<RoomLineListPresenter> implements IRoomLineListView,  BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNote)
    TextView tvNote;
    @BindView(R.id.ivAddRoom)
    ImageView ivAddRoom;
    @BindView(R.id.rvRoom)
    RecyclerView rvRoom;

    private RoomLineListAdapter mRoomLineListAdapter;

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
        //设备列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRoom.setLayoutManager(layoutManager);
        mRoomLineListAdapter = new RoomLineListAdapter(R.layout.item_room_line_list, new ArrayList<>());
        rvRoom.setAdapter(mRoomLineListAdapter);
        rvRoom.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(this, R.color.nocolor)));

        mRoomLineListAdapter.setOnItemChildClickListener(this);
        mRoomLineListAdapter.setOnItemClickListener(this);

    }

    @Override
    protected void initData() {
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
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        HomeRoomBean roomBean = mRoomLineListAdapter.getData().get(position);
        try {
            presenter.transferDevice(String.valueOf(roomBean.getCid()));
        } catch (Exception e) {
            e.printStackTrace();
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
    public void transferFail(String msg) {
        MyToastUtils.toast(msg);
    }
}
