package com.growatt.grohome.module.device;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.module.device.presenter.EditNamePresenter;
import com.growatt.grohome.module.device.view.IEditNameView;

import org.json.JSONException;

import butterknife.BindView;

public class EditNameActivity extends BaseActivity<EditNamePresenter> implements IEditNameView ,Toolbar.OnMenuItemClickListener{
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewName)
    View viewName;
    @BindView(R.id.tvNameTitle)
    TextView tvNameTitle;
    @BindView(R.id.etNameValue)
    EditText etNameValue;

    //头部
    private TextView tvMenuRightText;
    private MenuItem switchItem;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarDarkFont(true, 0.2f).statusBarView(statusBarView).statusBarColor(R.color.white).init();
    }

    @Override
    protected EditNamePresenter createPresenter() {
        return new EditNamePresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_name;
    }

    @Override
    protected void initViews() {
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.inflateMenu(R.menu.menu_right_text);
        switchItem = toolbar.getMenu().findItem(R.id.item_save);
        switchItem.setActionView(R.layout.menu_right_text);
        tvMenuRightText = switchItem.getActionView().findViewById(R.id.tv_right_text);
        tvMenuRightText.setTextColor(ContextCompat.getColor(this, R.color.color_text_33));
        tvMenuRightText.setText(R.string.m248_save);
        tvTitle.setText(R.string.m148_edit);
        toolbar.setOnMenuItemClickListener(this);

    }

    @Override
    protected void initData() {
        presenter.getData();
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
                try {
                    presenter.editNameByServer();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    public void setName(String name) {
        if (!TextUtils.isEmpty(name)){
            etNameValue.setText(name);
            etNameValue.setSelection(name.length());
        }
    }

    @Override
    public String getName() {
        return etNameValue.getText().toString();
    }

    @Override
    public void onError(String msg) {
        requestError(msg);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
