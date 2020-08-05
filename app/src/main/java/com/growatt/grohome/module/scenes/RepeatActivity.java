package com.growatt.grohome.module.scenes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.customview.WeekSelectView;
import com.growatt.grohome.module.scenes.presenter.RepeatPresenter;
import com.growatt.grohome.module.scenes.view.IRepeatActivityView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepeatActivity extends BaseActivity<RepeatPresenter> implements IRepeatActivityView, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.week_onetime)
    WeekSelectView weekOnetime;
    @BindView(R.id.week_everyday)
    WeekSelectView weekEveryday;
    @BindView(R.id.week_monday)
    WeekSelectView weekMonday;
    @BindView(R.id.week_tuesday)
    WeekSelectView weekTuesday;
    @BindView(R.id.week_wednesday)
    WeekSelectView weekWednesday;
    @BindView(R.id.week_thursday)
    WeekSelectView weekThursday;
    @BindView(R.id.week_friday)
    WeekSelectView weekFriday;
    @BindView(R.id.week_saturday)
    WeekSelectView weekSaturday;
    @BindView(R.id.week_sunday)
    WeekSelectView weekSunday;


    private List<WeekSelectView> weekViews=new ArrayList<>();
    private List<WeekSelectView> timeViews=new ArrayList<>();

    @Override
    protected RepeatPresenter createPresenter() {
        return new RepeatPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_repeat;
    }

    @Override
    protected void initViews() {
        //头部初始化
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.inflateMenu(R.menu.menu_right_text);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m216_effective_period);

        //初始化选中
        timeViews.add(weekOnetime);
        timeViews.add(weekEveryday);
        weekViews.add(weekMonday);
        weekViews.add(weekTuesday);
        weekViews.add(weekWednesday);
        weekViews.add(weekThursday);
        weekViews.add(weekFriday);
        weekViews.add(weekSaturday);
        weekViews.add(weekSunday);


    }

    @Override
    protected void initData() {
        presenter.getAlreadySet();
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

        for (WeekSelectView view : timeViews) {
            view.setIselectChangeListener(isChecked -> {
                if (isChecked) setWeekViews();
                if (view == weekOnetime) {
                    weekOnetime.setIschecked(isChecked);
                    weekEveryday.setIschecked(false);
                } else if (view == weekEveryday) {
                    weekEveryday.setIschecked(isChecked);
                    weekOnetime.setIschecked(false);
                }
            });
        }
        for (WeekSelectView view : weekViews) {
            view.setIselectChangeListener(isChecked -> {
                if (isChecked) setTimeViews();
                if (view == weekMonday) {
                    weekMonday.setIschecked(isChecked);
                } else if (view == weekTuesday) {
                    weekTuesday.setIschecked(isChecked);
                } else if (view == weekWednesday) {
                    weekWednesday.setIschecked(isChecked);
                } else if (view == weekThursday) {
                    weekThursday.setIschecked(isChecked);
                } else if (view == weekFriday) {
                    weekFriday.setIschecked(isChecked);
                } else if (view == weekSaturday) {
                    weekSaturday.setIschecked(isChecked);
                } else if (view == weekSunday) {
                    weekSunday.setIschecked(isChecked);
                }
            });
        }
    }


    private void setTimeViews() {
        for (WeekSelectView view : timeViews) {
            view.setIschecked(false);
        }
    }

    private void setWeekViews() {
        for (WeekSelectView view : weekViews) {
            view.setIschecked(false);
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.item_save) {
            presenter.selectResult();
        }
        return true;
    }

    @Override
    public void upLoop(String loopType, String loopValue) {
        if (TextUtils.isEmpty(loopType)) return;
        switch (loopType) {
            case "-1":
                weekOnetime.setIschecked(true);
                break;
            case "0":
                weekEveryday.setIschecked(true);
                break;
            case "1":
                if (TextUtils.isEmpty(loopType)) return;
                if ("1111111".equals(loopValue)) {
                    weekEveryday.setIschecked(true);
                } else {
                    char[] chars = loopValue.toCharArray();
                    for (int i = 0; i < chars.length; i++) {
                        if ("1".equals(String.valueOf(chars[i]))) {
                            weekViews.get(i).setIschecked(true);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public Map<String,String> getLoop() {
        String loopType = presenter.getLoopType();
        String loopValue = presenter.getLoopValue();
        int selectType = 0;//选择循环方式
        for (WeekSelectView view : timeViews) {
            boolean ischecked = view.isIschecked();
            if (ischecked) {
                selectType = 1;
                break;
            }
        }
        if (selectType != 1) {
            for (WeekSelectView view : weekViews) {
                boolean ischecked = view.isIschecked();
                if (ischecked) {
                    selectType = 2;
                    loopType = "1";
                    break;
                }
            }
        }

        if (selectType == 1) {
            for (WeekSelectView view : timeViews) {
                if (view == weekOnetime) {
                    boolean ischecked = weekOnetime.isIschecked();
                    if (ischecked) {
                        loopType = "-1";
                        loopValue = "0000000";
                    }
                } else if (view == weekEveryday) {
                    boolean ischecked = weekEveryday.isIschecked();
                    if (ischecked) {
                        loopType = "0";
                        loopValue = "1111111";
                    }
                }
            }

        } else if (selectType == 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < weekViews.size(); i++) {
                boolean ischecked = weekViews.get(i).isIschecked();
                if (ischecked) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
            }
            loopValue = sb.toString();
        }
        Map<String,String>loopMap=new HashMap<>();
        loopMap.put(GlobalConstant.TIME_LOOPTYPE,loopType);
        loopMap.put(GlobalConstant.TIME_LOOPVALUE,loopValue);
        return loopMap;
    }


}
