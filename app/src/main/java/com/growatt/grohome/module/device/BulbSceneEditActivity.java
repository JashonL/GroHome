package com.growatt.grohome.module.device;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growatt.grohome.R;
import com.growatt.grohome.adapter.BulbSceneAdapter;
import com.growatt.grohome.adapter.BulbSceneColourAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.BulbSceneColourBean;
import com.growatt.grohome.customview.CircleView;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.device.presenter.BulbScenePresenter;
import com.growatt.grohome.module.device.view.IBulbSceneView;
import com.growatt.grohome.utils.CommentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.tv_colour_title)
    TextView tvColourTitle;
    @BindView(R.id.rlv_scene)
    RecyclerView rlvScene;
    @BindView(R.id.v_line_colour)
    View vLineColour;
    @BindView(R.id.grop_colour)
    Group gropColour;
    @BindView(R.id.iv_colour_button)
    RadioButton ivColourButton;
    @BindView(R.id.iv_white_button)
    RadioButton ivWhiteButton;
    @BindView(R.id.rg_color_select)
    RadioGroup rgColorSelect;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.gp_colour)
    Group gpColour;
    @BindView(R.id.seek_colour)
    AppCompatSeekBar seekColour;
    @BindView(R.id.iv_brightness_dark)
    ImageView ivBrightnessDark;
    @BindView(R.id.seek_brightness)
    AppCompatSeekBar seekBrightness;
    @BindView(R.id.iv_brightness_bright)
    ImageView ivBrightnessBright;
    @BindView(R.id.iv_temper_cold)
    ImageView ivTemperCold;
    @BindView(R.id.seek_temper)
    AppCompatSeekBar seekTemper;
    @BindView(R.id.iv_temper_hot)
    ImageView ivTemperHot;
    @BindView(R.id.gp_white)
    Group gpWhite;
    @BindView(R.id.seek_white)
    AppCompatSeekBar seekWhite;
    @BindView(R.id.iv_white_brightness_left)
    ImageView ivWhiteBrightnessLeft;
    @BindView(R.id.seek_white_brightness)
    AppCompatSeekBar seekWhiteBrightness;
    @BindView(R.id.iv_white_brightness_right)
    ImageView ivWhiteBrightnessRight;
    @BindView(R.id.v_bottom)
    View vBottom;
    @BindView(R.id.ctl_scence_color)
    ConstraintLayout ctlScenceColor;
    @BindView(R.id.v_colour_flash_mode)
    View vColourFlashMode;
    @BindView(R.id.tv_flash_mode_title)
    TextView tvFlashModeTitle;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.tv_flash_mode_value)
    TextView tvFlashModeValue;
    @BindView(R.id.gp_speed)
    Group gpSpeed;
    @BindView(R.id.v_colour_flash_speed)
    View vColourFlashSpeed;
    @BindView(R.id.tv_flash_speed_title)
    TextView tvFlashSpeedTitle;
    @BindView(R.id.iv_speed_slowly)
    ImageView ivSpeedSlowly;
    @BindView(R.id.seek_speed)
    AppCompatSeekBar seekSpeed;
    @BindView(R.id.iv_speed_fast)
    ImageView ivSpeedFast;
    @BindView(R.id.btn_next)
    Button btnNext;


    //头部
    private TextView tvMenuRightText;
    private MenuItem switchItem;




    //颜色
    private BulbSceneColourAdapter mBulbSceneColourAdapter;

    private List<BulbSceneColourBean> colours = new ArrayList<>();

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
        toolbar.inflateMenu(R.menu.menu_right_text);
        switchItem = toolbar.getMenu().findItem(R.id.item_save);
        switchItem.setActionView(R.layout.menu_right_text);
        tvMenuRightText = switchItem.getActionView().findViewById(R.id.tv_right_text);
        tvMenuRightText.setTextColor(ContextCompat.getColor(this, R.color.white));
        tvMenuRightText.setText(R.string.m162_reset);
        tvTitle.setText(R.string.m148_edit);


        //场景颜色列表
        rlvScene.setLayoutManager(new GridLayoutManager(this, 6));
        mBulbSceneColourAdapter = new BulbSceneColourAdapter(R.layout.item_bulb_scene, new ArrayList<>());
        rlvScene.setAdapter(mBulbSceneColourAdapter);
        int div = CommentUtils.dip2px(this, 10);
        rlvScene.addItemDecoration(new LinearDivider(this, LinearLayoutManager.HORIZONTAL, div, ContextCompat.getColor(this, R.color.nocolor)));

        gropColour.setVisibility(View.GONE);

    }

    @Override
    protected void initData() {
        try {
            presenter.getSceneBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();


    }


    @Override
    public void setViewById(int id) {
        switch (id) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
        }
    }

    @Override
    public void setSceneName(String name) {
        tvSceneName.setText(name);
    }


    private void setColorPick(CircleView view) {
        int color = view.getColor();

    }


}
