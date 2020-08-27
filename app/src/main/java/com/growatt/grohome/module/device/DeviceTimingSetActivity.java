package com.growatt.grohome.module.device;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;
import com.growatt.grohome.module.device.presenter.DeviceTimingSetPresenter;
import com.growatt.grohome.module.device.view.IDeviceTimingSetView;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceTimingSetActivity extends BaseActivity<DeviceTimingSetPresenter> implements IDeviceTimingSetView {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_time_value)
    AppCompatTextView tvTimeValue;
    @BindView(R.id.tv_repeat_value)
    AppCompatTextView tvRepeatValue;
    @BindView(R.id.tv_onoff_value)
    AppCompatTextView tvOnoffValue;
    @BindView(R.id.tv_temp_value)
    AppCompatTextView tvTempValue;
    @BindView(R.id.cl_temp)
    ConstraintLayout clTemp;
    @BindView(R.id.tv_delete)
    TextView tvDelete;


    //头部
    private TextView tvMenuRightText;
    private MenuItem switchItem;

    @Override
    protected DeviceTimingSetPresenter createPresenter() {
        return new DeviceTimingSetPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_timing_set;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView) .statusBarColor(R.color.white).init();
    }

    @Override
    protected void initViews() {
        //头部
        tvTitle.setText(R.string.m146_timer);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.inflateMenu(R.menu.menu_right_text);
        switchItem = toolbar.getMenu().findItem(R.id.item_save);
        switchItem.setActionView(R.layout.menu_right_text);
        tvMenuRightText = switchItem.getActionView().findViewById(R.id.tv_right_text);
        tvMenuRightText.setTextColor(ContextCompat.getColor(this, R.color.color_text_33));
        tvMenuRightText.setText(R.string.m248_save);

    }

    @Override
    protected void initData() {
        presenter.getIntentData();
    }


    @Override
    public void showViews(String action) {
        if (GlobalConstant.ADD.equals(action)) {
            tvDelete.setVisibility(View.GONE);

        } else {
            tvDelete.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void initViews(String type) {
        if (DeviceTypeConstant.TYPE_THERMOSTAT.equals(type) || DeviceTypeConstant.TYPE_AIRCONDITION.equals(type)) {
            clTemp.setVisibility(View.VISIBLE);
        } else {
            clTemp.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnoffViews(String onoff) {
        tvOnoffValue.setText(onoff);
    }

    @Override
    public void setValue(String cValue) {
        tvTempValue.setText(cValue);
    }

    @Override
    public void setRepeate(String repeate) {
        tvRepeatValue.setText(repeate);
    }

    @Override
    public void setTimeValue(String timeValue) {
        tvTimeValue.setText(timeValue);
    }

    @Override
    public String getViewsTimeValue() {
        return tvTimeValue.getText().toString();
    }

    @Override
    public String getViewsRepeat() {
        return tvRepeatValue.getText().toString();
    }

    @Override
    public String getViewsonffValue() {
        return tvOnoffValue.getText().toString();
    }

    @Override
    public void onError(String msg) {
        requestError(msg);
    }


    @OnClick({R.id.cl_time, R.id.cl_repeat, R.id.cl_onoff, R.id.cl_temp, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_time:
                presenter.showTimeSelectDialog();
                break;
            case R.id.cl_repeat:
                presenter.selectRepeat();
                break;
            case R.id.cl_onoff:
                presenter.setOnOff();
                break;
            case R.id.cl_temp:
                presenter.selectTemp();
                break;
            case R.id.tv_delete:
                presenter.showWarnDialog();
                break;
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
        tvMenuRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.submitServer();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}
