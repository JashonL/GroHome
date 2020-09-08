package com.growatt.grohome.module.device;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
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
import butterknife.OnClick;

public class BulbSceneEditActivity extends BaseActivity<BulbScenePresenter> implements IBulbSceneView, BaseQuickAdapter.OnItemClickListener,
        SeekBar.OnSeekBarChangeListener, ColorBarView.OnColorChangeListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_scene_icon)
    ImageView ivSceneIcon;
    @BindView(R.id.tv_scene_name)
    TextView tvSceneName;
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
    @BindView(R.id.tv_flash_mode_value)
    TextView tvFlashModeValue;
    @BindView(R.id.gp_speed)
    Group gpSpeed;
    @BindView(R.id.seek_speed)
    AppCompatSeekBar seekSpeed;
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


    private List<BulbSceneColourBean> removeBeans = new ArrayList<>();


    //颜色适配器
    private BulbSceneColourAdapter mBulbSceneColourAdapter;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarView(statusBarView).init();
    }

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
        toolbar.setNavigationIcon(R.drawable.icon_return_w);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.nocolor));
        toolbar.inflateMenu(R.menu.menu_right_text);
        switchItem = toolbar.getMenu().findItem(R.id.item_save);
        switchItem.setActionView(R.layout.menu_right_text);
        tvMenuRightText = switchItem.getActionView().findViewById(R.id.tv_right_text);
        tvMenuRightText.setTextColor(ContextCompat.getColor(this, R.color.white));
        tvMenuRightText.setText(R.string.m162_reset);
        tvTitle.setText(R.string.m148_edit);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setOnMenuItemClickListener(this);
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
        seekSpeed.setOnSeekBarChangeListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.resetScene();
            }
        });

        tvMenuRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.resetScene();
            }
        });
    }


    @OnClick({R.id.ll_colour, R.id.ll_white, R.id.iv_delete, R.id.v_colour_flash_mode, R.id.btn_next, R.id.iv_scene_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_colour:
                setColourOrWhite(true);
                break;
            case R.id.ll_white:
                setColourOrWhite(false);
                break;
            case R.id.iv_delete:
                deleteColour();
                break;
            case R.id.v_colour_flash_mode:
                presenter.setSceneMode();
                break;
            case R.id.btn_next:
                presenter.setScene();
                break;
            case R.id.iv_scene_edit:
                presenter.editName();
                break;
        }
    }


    @Override
    public void setSceneName(String name) {
        tvSceneName.setText(name);
    }

    @Override
    public String getSceneName() {
        return tvSceneName.getText().toString();
    }

    @Override
    public void isWhite(String isWhite) {
        if (!GlobalConstant.STRING_STATUS_ON.equals(isWhite)){
            llWhite.setVisibility(View.GONE);
        }
    }

    @Override
    public void setMode(int mode) {
        switch (mode) {
            case 0:
                gpSpeed.setVisibility(View.GONE);
                tvFlashModeValue.setText(R.string.m163_static);
                List<BulbSceneColourBean> data = mBulbSceneColourAdapter.getData();
                List<BulbSceneColourBean> newList = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    if (i == 0) {
                        newList.add(data.get(i));
                    } else {
                        removeBeans.add(data.get(i));
                    }
                }
                mBulbSceneColourAdapter.replaceData(newList);
                presenter.setCurrentColourBean(null);
                hideColourViews();
                hideEditViews();
                hideWhiteViews();
                break;
            case 1:
                gpSpeed.setVisibility(View.VISIBLE);
                tvFlashModeValue.setText(R.string.m164_flash);
                if (mBulbSceneColourAdapter.getData().size() > 0) {
                    if (removeBeans != null && removeBeans.size() > 0) {
                        mBulbSceneColourAdapter.addData(removeBeans);
                        removeBeans.clear();
                    } else {
                        presenter.modeChange(mBulbSceneColourAdapter.getData());
                        mBulbSceneColourAdapter.notifyDataSetChanged();
                    }
                }

                break;
            case 2:
                gpSpeed.setVisibility(View.VISIBLE);
                tvFlashModeValue.setText(R.string.m165_breath);
                if (mBulbSceneColourAdapter.getData().size() > 0) {
                    if (removeBeans != null && removeBeans.size() > 0) {
                        mBulbSceneColourAdapter.addData(removeBeans);
                        removeBeans.clear();
                    } else {
                        presenter.modeChange(mBulbSceneColourAdapter.getData());
                        mBulbSceneColourAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @Override
    public void setSpeed(int speed) {
        int max = DeviceBulb.getSpeedMax();
        int min = DeviceBulb.getSpeedMin();
        float progress = (float) (speed - min) / (float) (max - min) * 100;
        seekSpeed.setProgress((int) progress);
    }

    @Override
    public void setColous(List<BulbSceneColourBean> colourBeans) {
        mBulbSceneColourAdapter.replaceData(colourBeans);
    }

    @Override
    public List<BulbSceneColourBean> getData() {
        return mBulbSceneColourAdapter.getData();
    }

    @Override
    public void updataSelected() {
        mBulbSceneColourAdapter.notifyDataSetChanged();
    }

    @Override
    public void addDataDeal() {
        mBulbSceneColourAdapter.notifyDataSetChanged();
        List<BulbSceneColourBean> data = mBulbSceneColourAdapter.getData();
        int select;
        if (data.size() > 6) {
            data.remove(data.size() - 1);
            mBulbSceneColourAdapter.notifyDataSetChanged();
            select = data.size() - 1;
        } else {
            select = data.size() - 2;
        }
        mBulbSceneColourAdapter.setNowSelectPosition(select);
        BulbSceneColourBean bulbSceneColourBean = data.get(select);
        presenter.setCurrentColourBean(bulbSceneColourBean);
        showEditViews();
        parserSceneColourBean(bulbSceneColourBean);
    }

    @Override
    public void setViewsById(int id) {
        switch (id) {
            case 0:
                ivSceneIcon.setImageResource(R.drawable.sence_night);
                break;
            case 1:
                ivSceneIcon.setImageResource(R.drawable.sence_read);
                break;
            case 2:
                ivSceneIcon.setImageResource(R.drawable.sence_meeting);
                break;
            case 3:
                ivSceneIcon.setImageResource(R.drawable.sence_leisure);
                break;
            case 4:
                ivSceneIcon.setImageResource(R.drawable.sence_soft);
                break;
            case 5:
                ivSceneIcon.setImageResource(R.drawable.sence_rainbow);
                break;
            case 6:
                ivSceneIcon.setImageResource(R.drawable.sence_shine);
                break;
            case 7:
                ivSceneIcon.setImageResource(R.drawable.sence_gorgeous);
                break;
        }
    }

    @Override
    public void onError(String msg) {
        requestError(msg);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BulbSceneColourBean bulbSceneColourBean = mBulbSceneColourAdapter.getData().get(position);
        if (bulbSceneColourBean != null) {
            presenter.setCurrentColourBean(bulbSceneColourBean);
            int itemType = bulbSceneColourBean.getItemType();
            if (itemType == GlobalConstant.STATUS_ITEM_DATA) {//正常的选项
                if (position != mBulbSceneColourAdapter.getNowSelectPosition()) {
                    showEditViews();
                    parserSceneColourBean(bulbSceneColourBean);
                } else {
                    hideColourViews();
                    hideEditViews();
                    hideWhiteViews();
                }
                mBulbSceneColourAdapter.setNowSelectPosition(position);
            } else {//添加
                presenter.addData(mBulbSceneColourAdapter.getData());
            }
        }

    }


    private void setColourOrWhite(boolean isColour) {
        BulbSceneColourBean currentColourBean = presenter.getCurrentColourBean();
        if (currentColourBean != null) {
            currentColourBean.setIsColour(isColour);
            parserSceneColourBean(currentColourBean);
        }
    }


    private void parserSceneColourBean(BulbSceneColourBean bulbSceneColourBean) {
        boolean isColour = bulbSceneColourBean.isColour();
        //设置彩光或者白光
        setColourTypeViews(isColour);
        if (isColour) {
            float[] hsv = bulbSceneColourBean.getHsv();
            int colour = bulbSceneColourBean.getColour();
            seekColour.setCurrentColor(colour);
            int colourTempter = (int) (hsv[1] * 100);
            int colourBrightNess = (int) (hsv[2] * 100);

            seekTemper.setProgress(colourTempter);
            seekBrightness.setProgress(colourBrightNess);

            presenter.bulbColourLightness(colourBrightNess);
            presenter.bulbColourTemper(colourTempter);

        } else {
            float[] hsv = bulbSceneColourBean.getWhiteHsv();
            int whiteTempter = (int) (hsv[1] * 100);
            int whiteBrightNess = (int) (hsv[2] * 100);


            seekWhite.setProgress(100 - whiteTempter);
            seekWhiteBrightness.setProgress(whiteBrightNess);

            presenter.bulbTemper(whiteTempter);
            presenter.bulbLightness(whiteBrightNess);

        }

    }


    private void setColourTypeViews(boolean isColour) {
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
                String isWhite = presenter.getIsWhite();
                if (!GlobalConstant.STRING_STATUS_ON.equals(isWhite)){
                    llColour.setBackgroundResource(R.drawable.shape_radio_circle);
                }else {
                    llColour.setBackgroundResource(R.drawable.shape_radio_left_selected);
                }
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
                seletPos = 3;
            }
            if (data.size() <= seletPos) {
                CircleDialogUtils.showCommentDialog(this, getString(R.string.m95_tips), getString(R.string.m257_cannot_be_deleted), v -> {
                });
            } else {

                mBulbSceneColourAdapter.remove(nowSelectPosition);


                int itemType = data.get(data.size() - 1).getItemType();
                if (currentFlashMode != 0 && itemType == GlobalConstant.STATUS_ITEM_DATA) {
                    presenter.dataDeal(data);
                }

                //收起编辑框
                mBulbSceneColourAdapter.setNowSelectPosition(-1);
                presenter.setCurrentColourBean(null);
                hideColourViews();
                hideEditViews();
                hideWhiteViews();

                //选中其他项
               /* if (nowSelectPosition == data.size() - 1) {
                    nowSelectPosition -= 1;
                } else if (nowSelectPosition > data.size() - 1) {
                    nowSelectPosition=data.size()-1;
                }else if (nowSelectPosition<=data.size()-2){
                    nowSelectPosition+=1;
                }
                mBulbSceneColourAdapter.setNowSelectPosition(nowSelectPosition);
                BulbSceneColourBean bulbSceneColourBean = data.get(nowSelectPosition);
                presenter.setCurrentColourBean(bulbSceneColourBean);
                parserSceneColourBean(bulbSceneColourBean);*/
            }
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //手动触发才调用方法
        if (fromUser) {
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
                presenter.setFlashSpeed(progress);
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
    public void onColorChange(int color) {
        presenter.bulbColour(color);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.resetScene();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
