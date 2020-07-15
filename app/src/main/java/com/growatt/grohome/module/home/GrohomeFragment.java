package com.growatt.grohome.module.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.GroHomeDevAdapter;
import com.growatt.grohome.adapter.RoomAdapter;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.device.DeviceTypeActivity;
import com.growatt.grohome.module.home.presenter.GrohomePresenter;
import com.growatt.grohome.module.home.view.IGrohomeView;

import java.util.ArrayList;

import butterknife.BindView;

public class GrohomeFragment extends BaseFragment<GrohomePresenter> implements IGrohomeView,Toolbar.OnMenuItemClickListener {

    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.guild_left)
    Guideline guildLeft;
    @BindView(R.id.guild_right)
    Guideline guildRight;
    @BindView(R.id.room_title_background)
    View roomTitleBackground;
    @BindView(R.id.tv_room_title)
    TextView tvRoomTitle;
    @BindView(R.id.tv_all_sence)
    TextView tvAllSence;
    @BindView(R.id.iv_all_room)
    ImageView ivAllRoom;
    @BindView(R.id.cl_all_room)
    ConstraintLayout clAllRoom;
    @BindView(R.id.rlv_room)
    RecyclerView rlvRoom;
    @BindView(R.id.device_title_background)
    View deviceTitleBackground;
    @BindView(R.id.tv_device_title)
    TextView tvDeviceTitle;
    @BindView(R.id.iv_switch_devlist)
    ImageView ivSwitchDevlist;
    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;


    /*房间部分*/
    private RoomAdapter mRoomAdapter;
    /*设备部分*/
    private GroHomeDevAdapter mGrohomeAdapter;



    @Override
    protected GrohomePresenter createPresenter() {
        return new GrohomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_grohome;
    }

    @Override
    protected void initView() {
         //头部toolBar
        tvTitle.setVisibility(View.GONE);
        toolbar.setTitle(R.string.m34_welcome_groHome);
        toolbar.inflateMenu(R.menu.menu_grohome);
        toolbar.setOnMenuItemClickListener(this);
        //房间列表初始化
        mRoomAdapter=new RoomAdapter(R.layout.item_room_view,new ArrayList<>());
        rlvRoom.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        rlvRoom.setAdapter(mRoomAdapter);
        rlvRoom.addItemDecoration(new LinearDivider(getActivity(),LinearLayoutManager.HORIZONTAL,5, ContextCompat.getColor(getActivity(),R.color.nocolor)));
        //设备列表初始化
        mGrohomeAdapter=new GroHomeDevAdapter(R.layout.item_device_grid,new ArrayList<>());
        rlvDevice.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rlvDevice.setAdapter(mGrohomeAdapter);
        //设置空布局
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.list_empty_view,rlvDevice,false);
        mGrohomeAdapter.setEmptyView(view);
    }

    @Override
    protected void initData() {

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getContext(), DeviceTypeActivity.class));
           /*     View bodyView=LayoutInflater.from(getActivity()).inflate(R.layout.bulb_dialog_white_mode,null,false);
                CircleDialogUtils.showBulbWhiteMode(bodyView, GrohomeFragment.this.getFragmentManager(), view -> {

                });*/
                break;
        }
        return true;
    }
}
