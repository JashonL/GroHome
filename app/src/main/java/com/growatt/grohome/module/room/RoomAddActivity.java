package com.growatt.grohome.module.room;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.room.presenter.RoomAddPresenter;
import com.growatt.grohome.module.room.view.IRoomAddView;
import com.growatt.grohome.utils.GlideUtils;
import com.growatt.grohome.utils.MyToastUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomAddActivity extends BaseActivity<RoomAddPresenter> implements IRoomAddView,Toolbar.OnMenuItemClickListener {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivRoomImgSelect)
    ImageView ivRoomImgSelect;
    @BindView(R.id.tv_defauted)
    TextView tvDefauted;
    @BindView(R.id.tvNote1)
    TextView tvNote1;
    @BindView(R.id.ivRoomImg1)
    ImageView ivRoomImg1;
    @BindView(R.id.tvNote2)
    TextView tvNote2;
    @BindView(R.id.ivRoomImg2)
    ImageView ivRoomImg2;
    @BindView(R.id.tvNote3)
    TextView tvNote3;
    @BindView(R.id.ivRoomImg3)
    ImageView ivRoomImg3;
    @BindView(R.id.etNameValue)
    EditText etNameValue;
    @BindView(R.id.tvNote4)
    TextView tvNote4;
    @BindView(R.id.ivRoomImg4)
    ImageView ivRoomImg4;
    @BindView(R.id.viewImg6)
    View viewImg6;

    @Override
    protected RoomAddPresenter createPresenter() {
        return new RoomAddPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_add;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView) .statusBarColor(R.color.white).init();
    }


    @Override
    protected void initViews() {
        tvTitle.setText(R.string.m193_add_room);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.inflateMenu(R.menu.menu_grohome);
        toolbar.setOnMenuItemClickListener(this);
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
    }


    @OnClick({R.id.ivRoomImgSelect, R.id.ivRoomImg1, R.id.ivRoomImg2, R.id.ivRoomImg3,R.id.ivRoomImg4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivRoomImgSelect:
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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
           /*     View bodyView=LayoutInflater.from(getActivity()).inflate(R.layout.bulb_dialog_white_mode,null,false);
                CircleDialogUtils.showBulbWhiteMode(bodyView, GrohomeFragment.this.getFragmentManager(), view -> {

                });*/

                presenter.addRoom(etNameValue.getText().toString());
                break;
        }
        return true;
    }


    private void setImageView(int res){
        tvDefauted.setVisibility(View.GONE);
        GlideUtils.showImageAct(this, R.drawable.liveroom_m, R.drawable.liveroom_m, res, ivRoomImgSelect);
        presenter.savePic(res);
    }

 /*   @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        finish();
    }
*/

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


    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
        finish();
    }

    @Override
    public void getPhotoSuccess(String path) {
        GlideUtils.showImageAct(this, path, ivRoomImgSelect);
        tvDefauted.setVisibility(View.GONE);
    }

    @Override
    public void getPhotoFail(String meesage) {
        if (TextUtils.isEmpty(meesage)){
            MyToastUtils.toast(getString(R.string.m201_fail));
        }else {
            MyToastUtils.toast(meesage);

        }
    }

    @Override
    public void createRoomSuccess() {
        MyToastUtils.toast(R.string.m200_success);
        finish();
    }

    @Override
    public void createRoomFail(String msg) {
        MyToastUtils.toast(msg);
    }

    @Override
    public void onError(String onError) {
        requestError(onError);
    }
}
