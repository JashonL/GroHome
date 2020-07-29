package com.growatt.grohome.module.home;

import android.view.MenuItem;
import android.view.View;
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

public class RoomListActivity extends BaseActivity<RoomListPresenter> implements IRoomListView , Toolbar.OnMenuItemClickListener {
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
        //头部toolBar
        tvTitle.setVisibility(View.GONE);
        toolbar.setTitle(R.string.m34_welcome_groHome);
        toolbar.inflateMenu(R.menu.menu_grohome);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
           /*     View bodyView=LayoutInflater.from(getActivity()).inflate(R.layout.bulb_dialog_white_mode,null,false);
                CircleDialogUtils.showBulbWhiteMode(bodyView, GrohomeFragment.this.getFragmentManager(), view -> {

                });*/
                break;
        }
        return true;
    }

}
