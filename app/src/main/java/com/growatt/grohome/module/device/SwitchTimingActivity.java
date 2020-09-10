package com.growatt.grohome.module.device;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.PanelTimingAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.SwitchTimingBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.customview.LinearDivider;
import com.growatt.grohome.module.device.presenter.SwitchTimingPresenter;
import com.growatt.grohome.module.device.view.ISwitchTimingView;
import com.growatt.grohome.utils.CommentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SwitchTimingActivity extends BaseActivity<SwitchTimingPresenter> implements ISwitchTimingView, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_top_line)
    View viewTopLine;
    @BindView(R.id.tvName)
    AppCompatTextView tvName;
    @BindView(R.id.tvTiming)
    AppCompatTextView tvTiming;
    @BindView(R.id.llTextGroup)
    LinearLayout llTextGroup;
    @BindView(R.id.ivHeadSwitch)
    ImageView ivHeadSwitch;
    @BindView(R.id.rlHeadSwitch)
    RelativeLayout rlHeadSwitch;
    @BindView(R.id.clPanelHead)
    ConstraintLayout clPanelHead;
    @BindView(R.id.tvOneTiming)
    TextView tvOneTiming;
    @BindView(R.id.rvSwitch)
    RecyclerView rvSwitch;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_delete)
    Button btnDelete;

    private PanelTimingAdapter mPtaAdapter;
    //头部
    private TextView tvMenuRightText;
    private MenuItem switchItem;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView) .statusBarColor(R.color.white).init();
    }

    @Override
    protected SwitchTimingPresenter createPresenter() {
        return new SwitchTimingPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_switch_timing;
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
        tvMenuRightText.setTextColor(ContextCompat.getColor(this, R.color.white));
        tvMenuRightText.setText("");

        tvName.setText(R.string.m37_panel_switch);
        ivHeadSwitch.setImageResource(R.drawable.scenes_on);


        //定时列表
        rvSwitch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mPtaAdapter = new PanelTimingAdapter(this, R.layout.item_panel_switch_timing_info, new ArrayList<>());
        int div = CommentUtils.dip2px(this, 24);
        rvSwitch.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL, div, ContextCompat.getColor(this, R.color.nocolor)));
        rvSwitch.setAdapter(mPtaAdapter);
        mPtaAdapter.setOnItemClickListener(this);
        mPtaAdapter.setOnItemChildClickListener(this);
    }

    @Override
    protected void initData() {
        presenter.getIntentData();
        try {
            presenter.getDetailData();
        } catch (Exception e) {
            e.printStackTrace();
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
    }

    @OnClick({R.id.clPanelHead, R.id.rlHeadSwitch, R.id.btn_save, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clPanelHead:
                presenter.jumpToSwitchTimgSet(0);
                break;
            case R.id.rlHeadSwitch:
                presenter.setAllSwitch();
                break;
            case R.id.btn_save:
                presenter.savaTiming();
                break;
            case R.id.btn_delete:
                presenter.showWarnDialog();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        List<SwitchTimingBean> data = mPtaAdapter.getData();
        SwitchTimingBean dataBean = data.get(position);
        if (view.getId() == R.id.ivSwitch) {
            String status = dataBean.getStatus();
            if ("0".equals(status)){
                dataBean.setStatus("1");
            }else {
                dataBean.setStatus("0");
            }
            mPtaAdapter.notifyDataSetChanged();
            int count=0;
            for (int i = 0; i < data.size(); i++) {
                SwitchTimingBean switchTimingBean = data.get(i);
                if ("0".equals(switchTimingBean.getStatus()))count++;
            }
            ivHeadSwitch.setImageResource(count == 0 ? R.drawable.scenes_off : R.drawable.scenes_on);
            if (count == 0) {
                presenter.allStatus = 1;
            } else {
                presenter.allStatus = 0;
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        SwitchTimingBean switchTimingBean = mPtaAdapter.getData().get(position);
        if (switchTimingBean != null){
            presenter.jumpToSwitchTimgSet(switchTimingBean);
        }
    }

    @Override
    public void initViews(String type) {

    }

    @Override
    public void showViews(String action) {
        if (GlobalConstant.ADD.equals(action)) {
            btnDelete.setVisibility(View.GONE);
        } else {
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setDeviceName(String deviceName) {
        if (!TextUtils.isEmpty(deviceName)) {
            tvName.setText(deviceName);
        }
    }

    @Override
    public void upData(List<SwitchTimingBean> newList) {
        mPtaAdapter.replaceData(newList);
    }

    @Override
    public void setAllSelect(boolean allSelect) {
        ivHeadSwitch.setImageResource(allSelect ? R.drawable.scenes_on : R.drawable.scenes_off);
    }

    @Override
    public void allSwitchOpen() {
        List<SwitchTimingBean> data = mPtaAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            SwitchTimingBean switchTimingBean = data.get(i);
            switchTimingBean.setStatus(String.valueOf(0));
        }
        mPtaAdapter.notifyDataSetChanged();
        presenter.allStatus = 0;
        ivHeadSwitch.setImageResource(R.drawable.scenes_on);
    }

    @Override
    public void allSwitchClose() {
        List<SwitchTimingBean> data = mPtaAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            SwitchTimingBean switchTimingBean = data.get(i);
            switchTimingBean.setStatus(String.valueOf(1));
        }
        mPtaAdapter.notifyDataSetChanged();
        presenter.allStatus = 1;
        ivHeadSwitch.setImageResource(R.drawable.scenes_off);
    }

    @Override
    public List<SwitchTimingBean> getData() {
        return mPtaAdapter.getData();
    }

    @Override
    public void setAllOpen() {
        ivHeadSwitch.setImageResource(R.drawable.scenes_on);
    }

    @Override
    public void updataAdapter() {
        mPtaAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String onError) {
        requestError(onError);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}
