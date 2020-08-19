package com.growatt.grohome.module.scenes;

import android.text.TextUtils;
import android.view.MenuItem;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.scenes.presenter.PeriodCommentSetPresenter;
import com.growatt.grohome.module.scenes.view.IPeriodCommentSetView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PeriodCommentSetActivity extends BaseActivity<PeriodCommentSetPresenter> implements IPeriodCommentSetView, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wheel_start_hour)
    WheelView wheelStartHour;
    @BindView(R.id.wheel_start_min)
    WheelView wheelStartMin;
    @BindView(R.id.wheel_end_hour)
    WheelView wheelEndHour;
    @BindView(R.id.wheel_end_min)
    WheelView wheelEndMin;


    private List<String> hours = new ArrayList<>();
    private List<String> mins = new ArrayList<>();
    private String hour_start="00";
    private String min_start="00";
    private String hour_end="00";
    private String min_end="00";

    @Override
    protected PeriodCommentSetPresenter createPresenter() {
        return new PeriodCommentSetPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_period_comment_set;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.inflateMenu(R.menu.menu_right_text);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m247_time_setting);


        for (int hour = 0; hour < 24; hour++) {
            if (hour < 10) hours.add("0" + hour);
            else hours.add(String.valueOf(hour));
        }
        for (int hour = 0; hour < 60; hour++) {
            if (hour < 10) mins.add("0" + hour);
            else mins.add(String.valueOf(hour));
        }
        //初始化时间选择器
        wheelStartHour.setCyclic(true);
        wheelStartHour.isCenterLabel(true);
        wheelStartHour.setAdapter(new ArrayWheelAdapter<>(hours));
        wheelStartHour.setCurrentItem(0);
        wheelStartHour.setLabel(getString(R.string.m249_hour));

        wheelEndHour.setCyclic(true);
        wheelEndHour.isCenterLabel(true);
        wheelEndHour.setAdapter(new ArrayWheelAdapter<>(hours));
        wheelEndHour.setCurrentItem(0);
        wheelEndHour.setLabel(getString(R.string.m249_hour));

        wheelStartMin.setCyclic(true);
        wheelStartMin.isCenterLabel(true);
        wheelStartMin.setAdapter(new ArrayWheelAdapter<>(mins));
        wheelStartMin.setCurrentItem(0);
        wheelStartMin.setLabel(getString(R.string.m250_min));


        wheelEndMin.setCyclic(true);
        wheelEndMin.isCenterLabel(true);
        wheelEndMin.setAdapter(new ArrayWheelAdapter<>(mins));
        wheelEndMin.setCurrentItem(0);
        wheelEndMin.setLabel(getString(R.string.m250_min));

    }

    @Override
    protected void initData() {
        presenter.getAlreadySet();
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());

        wheelStartHour.setOnItemSelectedListener(index ->  hour_start=hours.get(index));
        wheelEndHour.setOnItemSelectedListener(index -> hour_end=hours.get(index));
        wheelStartMin.setOnItemSelectedListener(index ->min_start=mins.get(index));
        wheelEndMin.setOnItemSelectedListener(index -> min_end=mins.get(index));

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.item_save) {
            presenter.selectResult();
        }
        return true;
    }

    @Override
    public void upTime(String startTime, String endTime) {
        if (!TextUtils.isEmpty(startTime)){
            String[] split = startTime.split("[\\D]");
            int currenStartH=Integer.parseInt(split[0]);
            int currenStartM =Integer.parseInt(split[1]);
            wheelStartHour.setCurrentItem(currenStartH);
            wheelStartMin.setCurrentItem(currenStartM);
        }
        if (!TextUtils.isEmpty(endTime)){
            String[] split = endTime.split("[\\D]");
            int currenEndH=Integer.parseInt(split[0]);
            int currenEndM =Integer.parseInt(split[1]);
            wheelEndHour.setCurrentItem(currenEndH);
            wheelEndMin.setCurrentItem(currenEndM);
        }
    }

    @Override
    public String getStartTime() {
        return hour_start + ":" + min_start;
    }

    @Override
    public String getEndTime() {
        return hour_end + ":" + min_end;
    }
}
