package com.growatt.grohome.module.scenes;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.scenes.presenter.EffectivePeriodPresenter;
import com.growatt.grohome.module.scenes.view.IEffectivePeriodView;
import com.growatt.grohome.utils.CommentUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EffectivePeriodActivity extends BaseActivity<EffectivePeriodPresenter> implements IEffectivePeriodView, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;

    @BindView(R.id.status_bar_view)
    View statusBarView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_time_period_value)
    AppCompatTextView tvTimePeriodValue;
    @BindView(R.id.card_time_period)
    CardView cardTimePeriod;
    @BindView(R.id.tv_loop_value)
    AppCompatTextView tvLoopValue;
    @BindView(R.id.tv_time_value)
    AppCompatTextView tvTimeValue;
    @BindView(R.id.card_timing)
    CardView cardTiming;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected EffectivePeriodPresenter createPresenter() {
        return new EffectivePeriodPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_effective_period;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.inflateMenu(R.menu.menu_right_text);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m216_effective_period);

        tvTimePeriodValue.setText(R.string.m235_not_set);
        tvLoopValue.setText(R.string.m235_not_set);
        tvTimeValue.setText(R.string.m235_not_set);
    }

    @Override
    protected void initData() {
        presenter.getAlreadySet();
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.item_save) {
            presenter.selectResult();
        }
        return true;
    }


    @OnClick({R.id.card_time_period, R.id.card_repeat,R.id.card_timing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.card_time_period:
                presenter.selectTime();
                break;
            case R.id.card_repeat:
                presenter.selectRepeat();
                break;
            case R.id.card_timing:
                presenter.showTimePickView();
                break;
        }
    }

    @Override
    public void upTimePeriod(String startTime, String endTime) {
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            String time = startTime + "~" + endTime;
            tvTimePeriodValue.setText(time);
        }
    }

    @Override
    public void upTimeValue(String timeValue) {
        if (!TextUtils.isEmpty(timeValue)){
            tvTimeValue.setText(timeValue);
        }
    }

    @Override
    public void upLoop(String looptype, String loopValue) {
        if (!TextUtils.isEmpty(looptype)) {
            StringBuilder loopStyle = new StringBuilder();
            switch (looptype) {
                case "-1":
                    loopStyle = new StringBuilder(getString(R.string.m222_single));
                    break;
                case "0":
                    loopStyle = new StringBuilder(getString(R.string.m224_everyday));
                    break;
                case "1":
                    if ("1111111".equals(loopValue)) {
                        loopStyle = new StringBuilder(getString(R.string.m224_everyday));
                    } else {
                        List<String> weeks = CommentUtils.getWeeks(this);
                        char[] chars = loopValue.toCharArray();
                        for (int i = 0; i < chars.length; i++) {
                            if (String.valueOf(chars[i]).equals("1")) {
                                loopStyle.append(weeks.get(i)).append(",");
                            }
                        }
                    }
                    break;
                default:
                    loopStyle = new StringBuilder("");
                    break;
            }
            String cycleDay = loopStyle.toString();
            if (cycleDay.endsWith(",")) {
                cycleDay = cycleDay.substring(0, cycleDay.length() - 1);
            }
            tvLoopValue.setText(cycleDay);
        }
    }

    @Override
    public void hideTime() {
        cardTiming.setVisibility(View.GONE);
    }

    @Override
    public void hideTimePeriod() {
        cardTimePeriod.setVisibility(View.GONE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

}
