package com.growatt.grohome.module.room;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.RoomEditDevAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.GroDeviceBean;
import com.growatt.grohome.bean.HomeRoomBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.room.presenter.RoomEditPresenter;
import com.growatt.grohome.module.room.view.IRoomEditView;
import com.growatt.grohome.utils.MyToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomEditActivity extends BaseActivity<RoomEditPresenter> implements IRoomEditView, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.guideLeft15)
    Guideline guideLeft15;
    @BindView(R.id.guideRight15)
    Guideline guideRight15;
    @BindView(R.id.viewName)
    View viewName;
    @BindView(R.id.tvNameTitle)
    TextView tvNameTitle;
    @BindView(R.id.tvNameValue)
    TextView tvNameValue;
    @BindView(R.id.ivName)
    ImageView ivName;
    @BindView(R.id.viewImg)
    View viewImg;
    @BindView(R.id.tvImgTitle)
    TextView tvImgTitle;
    @BindView(R.id.tvImgValue)
    TextView tvImgValue;
    @BindView(R.id.tvDeviceTitle)
    TextView tvDeviceTitle;
    @BindView(R.id.ivAddDevice)
    ImageView ivAddDevice;
    @BindView(R.id.rvDevice)
    RecyclerView rvDevice;
    @BindView(R.id.btnDelete)
    Button btnDelete;

    private RoomEditDevAdapter mRoomEditDevAdapter;

    @Override
    protected RoomEditPresenter createPresenter() {
        return new RoomEditPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_edit;
    }

    @Override
    protected void initViews() {
        //头部toolBar
        tvTitle.setText(R.string.m148_edit);
        toolbar.inflateMenu(R.menu.menu_grohome);
        toolbar.setNavigationIcon(R.drawable.icon_return);


        //设备列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDevice.setLayoutManager(layoutManager);
        mRoomEditDevAdapter = new RoomEditDevAdapter(R.layout.item_edit_device, new ArrayList<>());
        rvDevice.setAdapter(mRoomEditDevAdapter);
        rvDevice.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL, 32, ContextCompat.getColor(this, R.color.nocolor)));

        mRoomEditDevAdapter.setOnItemChildClickListener(this);
        mRoomEditDevAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        presenter.getData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        GroDeviceBean groDeviceBean = mRoomEditDevAdapter.getData().get(position);
        if (adapter == mRoomEditDevAdapter) {
            switch (view.getId()) {
                case R.id.tvDelete:
                    presenter.deleteDevice(groDeviceBean.getDevId(), groDeviceBean.getDevType());
                    break;
                case R.id.tvTransfer:
                    presenter.transferDevice(groDeviceBean.getDevId(), groDeviceBean.getDevType());
                    break;
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void upRoomData(HomeRoomBean bean) {
        if (bean != null) {
            tvNameValue.setText(bean.getName());
            List<GroDeviceBean> devList = bean.getDevList();
            if (devList != null) {
                mRoomEditDevAdapter.replaceData(devList);
            }
        }
    }

    @Override
    public void editNameSuccess(String name) {
        tvNameValue.setText(name);
    }

    @Override
    public void editNameFail(String msg) {
        MyToastUtils.toast(msg);
    }

    @Override
    public void deleteRoomSuccess() {
        finish();
    }

    @Override
    public void deleteDeviceSuccess(String deviceId) {

    }


    @OnClick({R.id.viewName, R.id.ivAddDevice, R.id.btnDelete, R.id.viewImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivAddDevice:
                presenter.toAddDevice();
                break;
            case R.id.btnDelete:
                presenter.deleteRoom();
                break;
            case R.id.viewName://修改房间名称
                presenter.editRoomName();
                break;
            case R.id.viewImg:
                presenter.updateImage();
                break;
        }
    }
}
