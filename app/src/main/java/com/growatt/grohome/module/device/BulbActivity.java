package com.growatt.grohome.module.device;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.BulbSceneAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.BulbSceneBean;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.customview.colorpicker.BlurMaskCircularView;
import com.growatt.grohome.customview.colorpicker.CircleBackgroundView;
import com.growatt.grohome.customview.colorpicker.ColorPicker;
import com.growatt.grohome.eventbus.DevEditNameBean;
import com.growatt.grohome.eventbus.DeviceAddOrDelMsg;
import com.growatt.grohome.eventbus.TransferDevMsg;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.presenter.BulbPresenter;
import com.growatt.grohome.module.device.view.IBulbView;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BulbActivity extends BaseActivity<BulbPresenter> implements IBulbView, SeekBar.OnSeekBarChangeListener, ColorPicker.OnColorChangedListener, ColorPicker.OnColorSelectedListener, BaseQuickAdapter.OnItemClickListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_white_light)
    ImageView ivWhiteLight;
    @BindView(R.id.iv_colour_light)
    ImageView ivColourLight;
    @BindView(R.id.iv_scenec_light)
    ImageView ivScenecLight;
    @BindView(R.id.v_bulb_backgroud_white)
    CircleBackgroundView vBulbBackgroudWhite;
    @BindView(R.id.seek_brightness_white)
    AppCompatSeekBar seekBrightnessWhite;
    @BindView(R.id.seek_temp_white)
    AppCompatSeekBar seekTempWhite;
    @BindView(R.id.seek_brightness_colour)
    AppCompatSeekBar seekBrightnessColour;
    @BindView(R.id.seek_temp_colour)
    AppCompatSeekBar seekTempColour;
    @BindView(R.id.iv_switch)
    ImageView ivSwitch;
    @BindView(R.id.layout_white)
    View whiteClude;
    @BindView(R.id.layout_colour)
    View colourClude;
    @BindView(R.id.layout_scene)
    View sceneClude;
    @BindView(R.id.layout_offline)
    View layoutOffline;
    @BindView(R.id.color_picker)
    ColorPicker colorPicker;
    @BindView(R.id.white_mask_view)
    BlurMaskCircularView whiteMaskView;
    @BindView(R.id.rlv_scene)
    RecyclerView rlvScene;
    @BindView(R.id.v_bulb_background_scene)
    View sceneBackGround;
    @BindView(R.id.v_bulb_backgroud_offline)
    CircleBackgroundView vBulbOffline;
    @BindView(R.id.tv_left_time_value)
    TextView tvLeftTimeValue;
    @BindView(R.id.tv_left_time_title)
    TextView tvLeftTimeTitle;
    @BindView(R.id.tv_leftdown)
    TextView tvLeftDown;
    @BindView(R.id.v_bulb_backgroud_colour)
    CircleBackgroundView vBulbCirBackgroud;

    private BulbSceneAdapter mBulbSceneAdapter;

    @Override
    protected BulbPresenter createPresenter() {
        return new BulbPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bulb;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarView(statusBarView).init();
    }

    @Override
    protected void initViews() {
        //设置头部
        toolbar.setNavigationIcon(R.drawable.icon_return_w);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.nocolor));
        toolbar.inflateMenu(R.menu.menu_device_setting);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        showViewsByTab(DeviceBulb.BULB_MODE_WHITE);
        ivSwitch.setImageResource(R.drawable.icon_on);


        //场景列表
        rlvScene.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBulbSceneAdapter = new BulbSceneAdapter(R.layout.item_bulb_scene, new ArrayList<>());
        rlvScene.setAdapter(mBulbSceneAdapter);
        int div = CommentUtils.dip2px(this, 24);
        rlvScene.addItemDecoration(new LinearDivider(this, LinearLayoutManager.HORIZONTAL, div, ContextCompat.getColor(this, R.color.nocolor)));
        whiteMaskView.setVisibility(View.GONE);

        //底部控件
        tvLeftTimeValue.setVisibility(View.GONE);
        tvLeftTimeTitle.setVisibility(View.GONE);
        tvLeftDown.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        List<BulbSceneBean> bulbSceneBeans = presenter.initScene();
        mBulbSceneAdapter.replaceData(bulbSceneBeans);
        sceneBackGround.setBackgroundResource(R.drawable.sence_night);//默认选中第一个
        try {
            presenter.initDevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDeviceTitle(String devName) {
        tvTitle.setText(devName);
    }

    @Override
    public void setViewsByDeviceType(String deviceType) {

    }

    @Override
    public void setOnoff(String onoff) {
        if ("true".equals(onoff)) {//状态开启
            ivSwitch.setImageResource(R.drawable.icon_on);
            setMode(presenter.getMode());
        } else {//关闭
            whiteClude.setVisibility(View.GONE);
            colourClude.setVisibility(View.GONE);
            sceneClude.setVisibility(View.GONE);
            layoutOffline.setVisibility(View.VISIBLE);
            vBulbOffline.setColor(ContextCompat.getColor(this, R.color.white_1a));
            ivSwitch.setImageResource(R.drawable.icon_off);
        }
    }


    @Override
    public void setBright(String bright) {
        if (!CommentUtils.isStringEmpty(bright)) {
            try {
                //亮度的范围是10-1000，所以计算百分比需要减去10
                int brightValue = (Integer.parseInt(bright) - 10) * 100 / (1000 - 10);
                seekBrightnessWhite.setProgress(brightValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setColour(int color) {
        colorPicker.setColor(color);
    }

    @Override
    public void set(String controdata) {

    }

    @Override
    public void setCuntDown(String countdown) {
        if (!CommentUtils.isStringEmpty(countdown) && !"0".equals(countdown)) {
            int time = Integer.parseInt(countdown);
            int hour = time / (60 * 60);
            int min = (time % (60 * 60)) / (60);
            countdown = hour + " h " + min + " min ";
            tvLeftTimeValue.setText(countdown);

            tvLeftTimeTitle.setVisibility(View.VISIBLE);
            tvLeftTimeValue.setVisibility(View.VISIBLE);
            tvLeftDown.setVisibility(View.GONE);
        } else {
            tvLeftTimeTitle.setVisibility(View.GONE);
            tvLeftTimeValue.setVisibility(View.GONE);
            tvLeftDown.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setScene(String scene) {
        //解析返回的场景设置ui
        if (!CommentUtils.isStringEmpty(scene) && scene.length() > 2) {
            String number = scene.substring(0, 2);
            int id = CommentUtils.hexStringToInter(number);
            mBulbSceneAdapter.setNowSelectPosition(id);
            rlvScene.scrollToPosition(id);
            Integer integer = DeviceBulb.getSceneDefultPicRes().get(id);
            sceneBackGround.setBackgroundResource(integer);
        }
    }

    @Override
    public void isWhiteMode(String mode) {
        if (!TextUtils.isEmpty(mode)) {
            if (!"1".equals(mode)) {
                whiteClude.setVisibility(View.GONE);
                ivWhiteLight.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setMode(String mode) {
        showViewsByTab(mode);
    }

    @Override
    public void setTemp(String temp) {
        if (!CommentUtils.isStringEmpty(temp)) {
            try {
                int brightValue = Integer.parseInt(temp);
                int mProgree = (1000 - brightValue) * seekTempWhite.getMax() / 1000;
                seekTempWhite.setProgress(mProgree);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setControData(String controdata) {

    }

    @Override
    public void setSatProgress(int progress) {
        seekTempColour.setProgress(progress / 10);

    }

    @Override
    public void setVatProgress(int progress) {
        try {
            //亮度的范围是10-1000，所以计算百分比需要减去10
            int brightValue = (progress - 10) * 100 / (1000 - 10);
            seekBrightnessColour.setProgress(brightValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendCommandSucces() {

    }

    @Override
    public void sendCommandError(String code, String error) {
        String msg = getString(R.string.m151_send_dps_failed) + ":" + code + "--->" + error;
        MyToastUtils.toast(msg);
    }

    @Override
    public void setCenterColor(int color) {
        vBulbCirBackgroud.setColor(color);
    }

    @Override
    public void setWhiteBgColor(int color) {
        vBulbBackgroudWhite.setColor(color);
    }

    @Override
    public void setWhiteMaskView(int color) {
        whiteMaskView.setColor(color);

    }

    @Override
    public void setColourMaskView(int color) {
    }

    @Override
    public BulbSceneBean getSceneBean() {
        return mBulbSceneAdapter.getItem(mBulbSceneAdapter.getNowSelectPosition());
    }

    @Override
    public void deviceOnline(boolean status) {
        if (!status) {//不在线
            ivWhiteLight.setSelected(false);
            ivColourLight.setSelected(false);
            ivScenecLight.setSelected(false);
            whiteClude.setVisibility(View.GONE);
            colourClude.setVisibility(View.GONE);
            sceneClude.setVisibility(View.GONE);
            layoutOffline.setVisibility(View.VISIBLE);
            vBulbOffline.setColor(ContextCompat.getColor(this, R.color.white_1a));
            ivSwitch.setImageResource(R.drawable.icon_off);
        }
    }

    @Override
    public void upDataSceneList(List<BulbSceneBean> sceneList) {
        mBulbSceneAdapter.replaceData(sceneList);
    }

    @Override
    public List<BulbSceneBean> getSceneList() {
        return mBulbSceneAdapter.getData();
    }

    @Override
    public void onError(String onError) {
        requestError(onError);
    }

    public void showViewsByTab(String mode) {
        if (DeviceBulb.BULB_MODE_WHITE.equals(mode)) {//白光模式
            ivWhiteLight.setSelected(true);
            ivColourLight.setSelected(false);
            ivScenecLight.setSelected(false);
            whiteClude.setVisibility(View.VISIBLE);
            colourClude.setVisibility(View.GONE);
            sceneClude.setVisibility(View.GONE);
            layoutOffline.setVisibility(View.GONE);
        } else if (DeviceBulb.BULB_MODE_COLOUR.equals(mode)) {//彩光模式
            ivWhiteLight.setSelected(false);
            ivColourLight.setSelected(true);
            ivScenecLight.setSelected(false);
            whiteClude.setVisibility(View.GONE);
            colourClude.setVisibility(View.VISIBLE);
            sceneClude.setVisibility(View.GONE);
            layoutOffline.setVisibility(View.GONE);
        } else {
            ivWhiteLight.setSelected(false);
            ivColourLight.setSelected(false);
            ivScenecLight.setSelected(true);
            whiteClude.setVisibility(View.GONE);
            colourClude.setVisibility(View.GONE);
            sceneClude.setVisibility(View.VISIBLE);
            layoutOffline.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.iv_white_light, R.id.iv_colour_light, R.id.iv_scenec_light, R.id.iv_switch, R.id.iv_edit, R.id.tv_edit,
            R.id.tv_leftdown, R.id.tv_left_time_value, R.id.tv_left_time_title, R.id.tv_timer,
            R.id.v_bulb_backgroud_white, R.id.v_bulb_background_scene,R.id.v_bulb_backgroud_offline, R.id.v_bulb_backgroud_colour})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_white_light:
                presenter.bulbMode(DeviceBulb.BULB_MODE_WHITE);
                break;
            case R.id.iv_colour_light:
                presenter.bulbMode(DeviceBulb.BULB_MODE_COLOUR);
                break;
            case R.id.iv_scenec_light:
                presenter.bulbMode(DeviceBulb.BULB_MODE_SCENE);
                break;
            case R.id.v_bulb_backgroud_offline:
            case R.id.v_bulb_backgroud_colour:
            case R.id.v_bulb_background_scene:
            case R.id.v_bulb_backgroud_white:
            case R.id.iv_switch:
                presenter.bulbSwitch();
                break;
            case R.id.iv_edit:
            case R.id.tv_edit:
                if (presenter.getMusicOnoff() && mBulbSceneAdapter.getNowSelectPosition() == 0) {
                    return;
                }
                presenter.toEditScene();
                break;
            case R.id.tv_leftdown:
            case R.id.tv_left_time_title:
            case R.id.tv_left_time_value:
                presenter.bulbCountdown();
                break;
            case R.id.tv_timer:
                presenter.jumpTiming();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(this);
        seekTempWhite.setOnSeekBarChangeListener(this);
        seekBrightnessWhite.setOnSeekBarChangeListener(this);
        seekBrightnessColour.setOnSeekBarChangeListener(this);
        seekTempColour.setOnSeekBarChangeListener(this);
        colorPicker.setOnColorChangedListener(this);//不松手
        colorPicker.setOnColorSelectedListener(this);//松手
        mBulbSceneAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            //亮度调节
            if (seekBar == seekBrightnessWhite) {
                int light = (progress * (1000 - 10)) / 100 + 10;
                presenter.bulbBrightness(light);
            }
            //温度调节
            if (seekBar == seekTempWhite) {
                int mProgree = (seekTempWhite.getMax() - progress) * 10;
                presenter.bulbTemper(mProgree);
            }
            //亮度调节
            if (seekBar == seekBrightnessColour) {
                int light = (progress * (1000 - 10)) / 100 + 10;
                presenter.bulbColourVal(light);
            }

            if (seekBar == seekTempColour) {
                presenter.bulbColourSat(progress * 10);
            }
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onColorChanged(int color) {
        presenter.bulbColour(color);
    }

    @Override
    public void onColorSelected(int color) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BulbSceneBean bulbSceneBean = mBulbSceneAdapter.getData().get(position);
        presenter.bulbScene(bulbSceneBean.getSceneValue());
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_set) {
            presenter.jumpSetting();
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdataScenes(@NonNull List<BulbSceneBean> sceneList) {
        upDataSceneList(sceneList);
    }


    /*修改设备名称*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showEditSuccess(DevEditNameBean msg) {
        if (msg != null) {
            String name = msg.getName();
            presenter.setDevName(name);
           if (!TextUtils.isEmpty(name)){
               tvTitle.setText(name);
           }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDevTransferBean(TransferDevMsg bean) {
        if (bean != null) {
            //获取列表设备列表
            try {
                presenter.setRoomInfo(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(DeviceAddOrDelMsg bean) {
        if (bean != null) {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyTuya();
        EventBus.getDefault().unregister(this);
    }
}
