package com.growatt.grohome.module.home;

import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.home.presenter.RoomListPresenter;
import com.growatt.grohome.module.home.view.IRoomListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomListActivity extends BaseActivity<RoomListPresenter> implements IRoomListView {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_title)
    TabLayout tabTitle;
    @BindView(R.id.iv_room_pic)
    ImageView ivRoomPic;
    @BindView(R.id.iv_pic_edit)
    ImageView ivPicEdit;
    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;

    @Override
    protected RoomListPresenter createPresenter() {
        return new RoomListPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_list;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }

}
