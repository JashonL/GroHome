package com.growatt.grohome.module.config;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.RoomAdapter;
import com.growatt.grohome.adapter.SelectRoomAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.customview.GridDivider;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.config.Presenter.ConfigSuccePresenter;
import com.growatt.grohome.module.config.view.IConfigSuccessView;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.utils.CommentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigSuccessActivity extends BaseActivity<ConfigSuccePresenter> implements IConfigSuccessView {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_success_flag)
    ImageView ivSuccessFlag;
    @BindView(R.id.tv_config_success)
    TextView tvConfigSuccess;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.v_edit_name_background)
    View vEditNameBackground;
    @BindView(R.id.iv_edit_name)
    ImageView ivEditName;
    @BindView(R.id.tv_edit_name)
    AppCompatTextView tvEditName;
    @BindView(R.id.tv_select_room)
    TextView tvSelectRoom;
    @BindView(R.id.rlv_room)
    RecyclerView rlvRoom;
    @BindView(R.id.btn_done)
    Button btnDone;


    private SelectRoomAdapter mSelectRoomAdapter;

    @Override
    protected ConfigSuccePresenter createPresenter() {
        return new ConfigSuccePresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_config_success;
    }

    @Override
    protected void initViews() {
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        tvTitle.setText(R.string.m200_success);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        //房间列表初始化
        mSelectRoomAdapter=new SelectRoomAdapter(new ArrayList<>());
        rlvRoom.setLayoutManager(new GridLayoutManager(this, 6));
        rlvRoom.setAdapter(mSelectRoomAdapter);
        int div = CommentUtils.dip2px(this, 10);
        rlvRoom.addItemDecoration( new GridDivider(ContextCompat.getColor(this, R.color.nocolor), div, div));
    }

    @Override
    protected void initData() {
        presenter.getRoomList();
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



    @OnClick({R.id.btn_done,R.id.v_edit_name_background})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                break;
            case R.id.v_edit_name_background:
                presenter.editName();
                break;
        }
    }

    @Override
    public void upRoomList(List<HomeRoomBean> homeRoomList) {
        mSelectRoomAdapter.replaceData(homeRoomList);
    }

    @Override
    public void setDeviceName(String deviceName) {
        if (!TextUtils.isEmpty(deviceName)){
            tvDeviceName.setText(deviceName);
        }
    }
}
