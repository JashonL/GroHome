package com.growatt.grohome.module.config;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.SelectRoomAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.config.Presenter.ConfigSuccePresenter;
import com.growatt.grohome.module.config.view.IConfigSuccessView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        mSelectRoomAdapter=new SelectRoomAdapter(R.layout.item_select_room,new ArrayList<>());
    }

    @Override
    protected void initData() {

    }


}
