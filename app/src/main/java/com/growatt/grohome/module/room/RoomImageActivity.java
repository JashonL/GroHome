package com.growatt.grohome.module.room;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Guideline;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.room.presenter.RoomImagePresenter;
import com.growatt.grohome.module.room.view.IRoomImageView;
import com.growatt.grohome.utils.GlideUtils;
import com.growatt.grohome.utils.MyToastUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomImageActivity extends BaseActivity<RoomImagePresenter> implements IRoomImageView, Toolbar.OnMenuItemClickListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.guideLeft15)
    Guideline guideLeft15;
    @BindView(R.id.guideRight15)
    Guideline guideRight15;
    @BindView(R.id.tvNoteSelect)
    TextView tvNoteSelect;
    @BindView(R.id.ivRoomImgSelect)
    ImageView ivRoomImgSelect;
    @BindView(R.id.tv_defauted)
    TextView tvDefauted;
    @BindView(R.id.viewImg2)
    View viewImg2;
    @BindView(R.id.tvNote1)
    TextView tvNote1;
    @BindView(R.id.ivRoomImg1)
    ImageView ivRoomImg1;
    @BindView(R.id.viewImg3)
    View viewImg3;
    @BindView(R.id.tvNote2)
    TextView tvNote2;
    @BindView(R.id.ivRoomImg2)
    ImageView ivRoomImg2;
    @BindView(R.id.viewImg4)
    View viewImg4;
    @BindView(R.id.tvNote3)
    TextView tvNote3;
    @BindView(R.id.ivRoomImg3)
    ImageView ivRoomImg3;
    @BindView(R.id.viewImg5)
    View viewImg5;
    @BindView(R.id.tvNote4)
    TextView tvNote4;
    @BindView(R.id.ivRoomImg4)
    ImageView ivRoomImg4;
    @BindView(R.id.viewImg6)
    View viewImg6;

    //头部
    private TextView tvMenuRightText;
    private MenuItem switchItem;

    @Override
    protected RoomImagePresenter createPresenter() {
        return new RoomImagePresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_image;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected void initViews() {
        //头部
        tvTitle.setText(R.string.m190_select_cover_image);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.inflateMenu(R.menu.menu_grohome);
        toolbar.inflateMenu(R.menu.menu_right_text);
        switchItem = toolbar.getMenu().findItem(R.id.item_save);
        switchItem.setActionView(R.layout.menu_right_text);
        tvMenuRightText = switchItem.getActionView().findViewById(R.id.tv_right_text);
        tvMenuRightText.setText(R.string.m248_save);

        String default1 = getString(R.string.m192_default_picture) + "1";
        tvNote1.setText(default1);
        String default2 = getString(R.string.m192_default_picture) + "2";
        tvNote2.setText(default2);
        String default3 = getString(R.string.m192_default_picture) + "3";
        tvNote3.setText(default3);
        String default4 = getString(R.string.m192_default_picture) + "4";
        tvNote4.setText(default4);

        GlideUtils.showImageAct(this, R.drawable.bathroom_m, R.drawable.bathroom_m, R.drawable.bathroom_m, ivRoomImg1);
        GlideUtils.showImageAct(this, R.drawable.bedroom_m, R.drawable.bedroom_m, R.drawable.bedroom_m, ivRoomImg2);
        GlideUtils.showImageAct(this, R.drawable.kitchen_m, R.drawable.kitchen_m, R.drawable.kitchen_m, ivRoomImg3);
        GlideUtils.showImageAct(this, R.drawable.liveroom_m, R.drawable.liveroom_m, R.drawable.liveroom_m, ivRoomImg4);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
        tvMenuRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    presenter.updateImg();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save:
           /*     View bodyView=LayoutInflater.from(getActivity()).inflate(R.layout.bulb_dialog_white_mode,null,false);
                CircleDialogUtils.showBulbWhiteMode(bodyView, GrohomeFragment.this.getFragmentManager(), view -> {

                });*/
                presenter.updateImg();
                break;
        }
        return true;
    }

    @Override
    public void getPhotoSuccess(String path) {
        GlideUtils.showImageAct(this, path, ivRoomImgSelect);
        tvDefauted.setVisibility(View.GONE);
    }

    @Override
    public void getPhotoFail(String message) {
        if (TextUtils.isEmpty(message)) {
            MyToastUtils.toast(getString(R.string.m201_fail));
        } else {
            MyToastUtils.toast(message);

        }
    }

    @Override
    public void setImage(String path) {
        tvDefauted.setVisibility(View.GONE);
        GlideUtils.showImageAct(this, R.drawable.liveroom_m, R.drawable.liveroom_m, path, ivRoomImgSelect);
    }

    @Override
    public void updateImageSuccess() {
        finish();
    }

    @Override
    public void updateImageFail(String msg) {
        MyToastUtils.toast(msg);
    }

    @Override
    public void onError(String msg) {
        requestError(msg);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
        finish();
    }


    @OnClick({R.id.ivRoomImgSelect, R.id.ivRoomImg1, R.id.ivRoomImg2, R.id.ivRoomImg3,R.id.ivRoomImg4, R.id.tv_defauted})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivRoomImgSelect:
            case R.id.tv_defauted:
                presenter.changeImgDialog();
                break;
            case R.id.ivRoomImg1:
                setImageView(R.drawable.bathroom_m);
                break;
            case R.id.ivRoomImg2:
                setImageView(R.drawable.bedroom_m);
                break;
            case R.id.ivRoomImg3:
                setImageView(R.drawable.kitchen_m);
                break;
            case R.id.ivRoomImg4:
                setImageView(R.drawable.liveroom_m);
                break;
        }
    }


    private void setImageView(int res) {
        tvDefauted.setVisibility(View.GONE);
        GlideUtils.showImageAct(this, res, res, res, ivRoomImgSelect);
        presenter.savePic(res);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            presenter.onActivityResult(requestCode, resultCode, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
