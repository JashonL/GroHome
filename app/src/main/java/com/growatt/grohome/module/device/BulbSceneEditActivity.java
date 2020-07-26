package com.growatt.grohome.module.device;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.customview.CircleView;
import com.growatt.grohome.module.device.presenter.BulbScenePresenter;
import com.growatt.grohome.module.device.view.IBulbSceneView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BulbSceneEditActivity extends BaseActivity<BulbScenePresenter> implements IBulbSceneView {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_scene_icon)
    ImageView ivSceneIcon;
    @BindView(R.id.tv_scene_name)
    TextView tvSceneName;
    @BindView(R.id.iv_scene_edit)
    ImageView ivSceneEdit;
    @BindView(R.id.ctl_scence_color)
    View vColourBackground;
    @BindView(R.id.tv_colour_title)
    TextView tvColourTitle;
    @BindView(R.id.iv_color_1)
    CircleView ivColor1;
    @BindView(R.id.iv_color_2)
    CircleView ivColor2;
    @BindView(R.id.iv_color_3)
    CircleView ivColor3;
    @BindView(R.id.iv_color_4)
    CircleView ivColor4;
    @BindView(R.id.iv_color_5)
    CircleView ivColor5;
    @BindView(R.id.iv_color_6)
    CircleView ivColor6;
    @BindView(R.id.ll_scene)
    LinearLayout llScene;
    @BindView(R.id.v_colour_flash_mode)
    View vColourFlashMode;
    @BindView(R.id.tv_flash_mode_title)
    TextView tvFlashModeTitle;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.tv_flash_mode_value)
    TextView tvFlashModeValue;
    @BindView(R.id.v_colour_flash_speed)
    View vColourFlashSpeed;
    @BindView(R.id.tv_flash_speed_title)
    TextView tvFlashSpeedTitle;
    @BindView(R.id.iv_brightness_white_left)
    ImageView ivBrightnessWhiteLeft;
    @BindView(R.id.seek_brightness_white)
    AppCompatSeekBar seekBrightnessWhite;
    @BindView(R.id.iv_brightness_right_white)
    ImageView ivBrightnessRightWhite;


    private TextView tvMenuRightText;
    private MenuItem switchItem;

    @Override
    protected BulbScenePresenter createPresenter() {
        return new BulbScenePresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bulb_edit;
    }

    @Override
    protected void initViews() {
        //初始化toolbar
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.nocolor));
        switchItem = toolbar.getMenu().findItem(R.id.action_switch_text);
        switchItem.setActionView(R.layout.menu_config_switch);
        tvMenuRightText = switchItem.getActionView().findViewById(R.id.tv_config_mode);
        tvMenuRightText.setTextColor(ContextCompat.getColor(this, R.color.white));
        tvMenuRightText.setText(R.string.m162_reset);
        tvTitle.setText(R.string.m148_edit);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    @OnClick({R.id.ctl_scence_color, R.id.v_colour_flash_mode, R.id.v_colour_flash_speed, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ctl_scence_color:
                break;
            case R.id.v_colour_flash_mode:
                presenter.setSceneMode();
                break;
            case R.id.v_colour_flash_speed:
                break;
            case R.id.btn_next:
                break;
        }
    }
}
