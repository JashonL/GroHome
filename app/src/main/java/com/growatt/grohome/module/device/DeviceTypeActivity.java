package com.growatt.grohome.module.device;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.DeviceTypeAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.DeviceTypeBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DevicePanel;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.presenter.DeviceTypePresenter;
import com.growatt.grohome.module.device.view.IDeviceTypeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DeviceTypeActivity extends BaseActivity<DeviceTypePresenter> implements IDeviceTypeView , BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rlv_device)
    RecyclerView mRlvdevice;

    /*设备部分*/
    private DeviceTypeAdapter mDeviceTypeAdapter;

    @Override
    protected DeviceTypePresenter createPresenter() {
        return new DeviceTypePresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_type;
    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected void initViews() {
        //头部toolBar
//        toolbar.setTitle(R.string.m35_添加设备);
        tvTitle.setText(R.string.m35_add_device);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        //列表
        mDeviceTypeAdapter = new DeviceTypeAdapter(R.layout.item_device_type, new ArrayList<>());
        mRlvdevice.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRlvdevice.setAdapter(mDeviceTypeAdapter);
        mRlvdevice.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL, 30, ContextCompat.getColor(this, R.color.nocolor)));
    }

    @Override
    protected void initData() {
        List<DeviceTypeBean> newList = new ArrayList<>();
//        String[] typeArray = new String[]{DeviceTypeConstant.TYPE_PADDLE, DeviceTypeConstant.TYPE_PANELSWITCH, DeviceTypeConstant.TYPE_THERMOSTAT, DeviceTypeConstant.TYPE_BULB, DeviceTypeConstant.TYPE_STRIP_LIGHTS};
        String[] typeArray = new String[]{DeviceTypeConstant.TYPE_PANELSWITCH,DeviceTypeConstant.TYPE_BULB};
//        String[] nameArray = new String[]{getString(DevicePlug.getNameRes()), getString(DevicePanel.getNameRes()), getString(DeviceThermostat.getNameRes()), getString(DeviceBulb.getNameRes()), getString(DeviceStripLights.getNameRes())};
        String[] nameArray = new String[]{getString(DevicePanel.getNameRes()),  getString(DeviceBulb.getNameRes())};
        for (int i = 0; i < typeArray.length; i++) {
            DeviceTypeBean bean = new DeviceTypeBean();
            bean.setBluethooth(false);
            bean.setWiFi(true);
            bean.setType(typeArray[i]);
            bean.setName(nameArray[i]);
            newList.add(bean);
        }
        mDeviceTypeAdapter.replaceData(newList);
    }


    @Override
    protected void initListener() {
        super.initListener();
        mDeviceTypeAdapter.setOnItemChildClickListener(this);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        DeviceTypeBean bean = mDeviceTypeAdapter.getData().get(position);
        String type = bean.getType();
        presenter.toConfigDeviceByType(type);
    }
}
