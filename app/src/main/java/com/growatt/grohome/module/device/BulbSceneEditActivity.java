package com.growatt.grohome.module.device;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.BulbSceneColourAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.BulbSceneColourBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.customview.CircleView;
import com.growatt.grohome.customview.ColorBarView;
import com.growatt.grohome.customview.GridDivider;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.presenter.BulbScenePresenter;
import com.growatt.grohome.module.device.view.IBulbSceneView;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BulbSceneEditActivity extends BaseActivity<BulbScenePresenter> implements IBulbSceneView, BaseQuickAdapter.OnItemClickListener, SeekBar.OnSeekBarChangeListener, ColorBarView.OnColorChangeListener{


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


    @BindView(R.id.ll_color_select)
    LinearLayout rgColorSelect;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;

    @BindView(R.id.seek_colour)
    ColorBarView seekColour;
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
    @BindView(R.id.ll_colour)
    LinearLayout llColour;
    @BindView(R.id.ll_white)
    LinearLayout llWhite;


    //头部
    private TextView tvMenuRightText;
    private MenuItem switchItem;


    //颜色编辑控件
    private List<View> colorEditViews = new ArrayList<>();
    private List<View> colourViews = new ArrayList<>();
    private List<View> whiteViews = new ArrayList<>();


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
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));

        //场景颜色列表
        rlvScene.setLayoutManager(new GridLayoutManager(this, 6));
        mBulbSceneColourAdapter = new BulbSceneColourAdapter(new ArrayList<>());
        rlvScene.setAdapter(mBulbSceneColourAdapter);
        int div = CommentUtils.dip2px(this, 5);
        rlvScene.addItemDecoration(new GridDivider(ContextCompat.getColor(this, R.color.nocolor), div, div));

        //颜色编辑控件
        colorEditViews.add(vLineColour);
        colorEditViews.add(rgColorSelect);
        colorEditViews.add(ivDelete);
        colorEditViews.add(vBottom);
        colourViews.add(seekColour);
        colourViews.add(ivBrightnessDark);
        colourViews.add(seekBrightness);
        colourViews.add(ivBrightnessBright);
        colourViews.add(ivTemperCold);
        colourViews.add(seekTemper);
        colourViews.add(ivTemperHot);
        whiteViews.add(seekWhite);
        whiteViews.add(ivWhiteBrightnessLeft);
        whiteViews.add(ivWhiteBrightnessRight);
        whiteViews.add(seekWhiteBrightness);
        hideWhiteViews();
        hideColourViews();
        hideEditViews();

    }

    private void hideWhiteViews() {
        for (int i = 0; i < whiteViews.size(); i++) {
            whiteViews.get(i).setVisibility(View.GONE);
        }
    }

    private void hideColourViews() {
        for (int i = 0; i < colourViews.size(); i++) {
            colourViews.get(i).setVisibility(View.GONE);
        }
    }

    private void hideEditViews() {
        for (int i = 0; i < colorEditViews.size(); i++) {
            colorEditViews.get(i).setVisibility(View.GONE);
        }
    }


    private void showWhiteViews() {
        for (int i = 0; i < whiteViews.size(); i++) {
            whiteViews.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void showColourViews() {
        for (int i = 0; i < colourViews.size(); i++) {
            colourViews.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void showEditViews() {
        for (int i = 0; i < colorEditViews.size(); i++) {
            colorEditViews.get(i).setVisibility(View.VISIBLE);
        }
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
        mBulbSceneColourAdapter.setOnItemClickListener(this);
        seekTemper.setOnSeekBarChangeListener(this);
        seekBrightness.setOnSeekBarChangeListener(this);
        seekWhite.setOnSeekBarChangeListener(this);
        seekWhiteBrightness.setOnSeekBarChangeListener(this);
        seekColour.setOnColorChangerListener(this);
    }


    @OnClick({R.id.ll_colour, R.id.ll_white, R.id.iv_delete, R.id.v_colour_flash_mode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_colour:
                setSelectChange(true);
                break;
            case R.id.ll_white:
                setSelectChange(false);
                break;
            case R.id.iv_delete:
                deleteColour();
                break;
            case R.id.v_colour_flash_mode:
                presenter.setSceneMode();
                break;


        }
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

    @Override
    public void setMode(int mode) {
        switch (mode){
            case 0:
                gpSpeed.setVisibility(View.GONE);
                tvFlashModeValue.setText(R.string.m163_static);
                break;
            case 1:
                gpSpeed.setVisibility(View.VISIBLE);
                tvFlashModeValue.setText(R.string.m164_flash);
                break;
            case 2:
                gpSpeed.setVisibility(View.VISIBLE);
                tvFlashModeValue.setText(R.string.m165_breath);
                break;
        }
    }

    @Override
    public void setSpeed(int speed) {
        int max= DeviceBulb.getSpeedMax();
        int min=DeviceBulb.getSpeedMin();
        float progress = (float)(speed - min) / (float)(max - min)*100;
        seekSpeed.setProgress((int) progress);
    }

    @Override
    public void setColous(List<BulbSceneColourBean> colourBeans) {
        mBulbSceneColourAdapter.replaceData(colourBeans);
    }

    @Override
    public void updataSelected() {
        mBulbSceneColourAdapter.notifyDataSetChanged();
    }


    private void setColorPick(CircleView view) {
        int color = view.getColor();

    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BulbSceneColourBean bulbSceneColourBean = mBulbSceneColourAdapter.getData().get(position);
        if (bulbSceneColourBean!=null){
            presenter.setCurrentColourBean(bulbSceneColourBean);
            int itemType = bulbSceneColourBean.getItemType();
            if (itemType== GlobalConstant.STATUS_ITEM_DATA){//正常的选项
                if (position != mBulbSceneColourAdapter.getNowSelectPosition()) {
                    showEditViews();
                    parserSceneColourBean(bulbSceneColourBean);
                } else {
                    hideColourViews();
                    hideEditViews();
                    hideWhiteViews();
                }
                mBulbSceneColourAdapter.setNowSelectPosition(position);
            }else {//添加

            }
        }

    }



    private void parserSceneColourBean(BulbSceneColourBean bulbSceneColourBean){
        boolean isColour = bulbSceneColourBean.isColour();
        //设置彩光或者白光
        setSelectChange(isColour);
        float[] hsv = bulbSceneColourBean.getHsv();
        if (isColour){
            int colour = bulbSceneColourBean.getColour();
            seekColour.setCurrentColor(colour);
            int colourTempter= (int) (hsv[1]*100);
            int colourBrightNess= (int) (hsv[2]*100);
            seekTemper.setProgress(colourTempter);
            seekBrightness.setProgress(colourBrightNess);

        }else {
            int whiteTempter= (int) (hsv[1]*100);
            int whiteBrightNess= (int) (hsv[2]*100);
            seekWhite.setProgress(100-whiteTempter);
            seekWhiteBrightness.setProgress(whiteBrightNess);
        }

    }



    private void setSelectChange(boolean isColour) {
        if (isColour) {
            showColourViews();
            hideWhiteViews();
            onCheckedChanged(R.id.ll_colour);
        } else {
            onCheckedChanged(R.id.ll_white);
            showWhiteViews();
            hideColourViews();
        }
    }


    public void onCheckedChanged(int checkedId) {
        switch (checkedId) {
            case R.id.ll_colour:
                llColour.setBackgroundResource(R.drawable.shape_radio_left_selected);
                llWhite.setBackgroundResource(R.drawable.shape_radio_right_normal);
                break;
            case R.id.ll_white:
                llColour.setBackgroundResource(R.drawable.shape_radio_left_normal);
                llWhite.setBackgroundResource(R.drawable.shape_radio_right_selected);
                break;
        }
    }


    private void deleteColour() {
        List<BulbSceneColourBean> data = mBulbSceneColourAdapter.getData();
        int nowSelectPosition = mBulbSceneColourAdapter.getNowSelectPosition();
        if (data.size() > nowSelectPosition) {
            int currentFlashMode = presenter.getCurrentFlashMode();
            int seletPos;
            if (currentFlashMode == 0) {
                seletPos = 1;
            } else {
                seletPos = 2;
            }
            if (data.size() <= seletPos) {
                CircleDialogUtils.showCommentDialog(this, getString(R.string.m95_tips), getString(R.string.m257_cannot_be_deleted), v -> {
                });
            } else {
                mBulbSceneColourAdapter.setNowSelectPosition(nowSelectPosition - 1);
                presenter.setCurrentColourBean(data.get(nowSelectPosition - 1));
                mBulbSceneColourAdapter.remove(nowSelectPosition);
            }
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //温度调节
        if (seekBar == seekWhite) {
            int mProgree = seekWhite.getMax() - progress;
            presenter.bulbTemper(mProgree);
        }
        //亮度调节
        if (seekBar == seekWhiteBrightness) {
            presenter.bulbLightness(progress);
        }


        //饱和度调节
        if (seekBar == seekBrightness) {
            presenter.bulbColourLightness(progress);
        }
        //温度调节
        if (seekBar == seekTemper) {
            presenter.bulbColourTemper(progress);
        }

        //速率
        if (seekBar == seekSpeed) {

        }


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onColorChange(int color) {
        presenter.bulbColour(color);
    }
}
