package com.growatt.grohome.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import com.growatt.grohome.R;
import com.growatt.grohome.app.App;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    protected P presenter;

    protected abstract P createPresenter();

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initData();

    protected Toolbar mToolBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutInflater.from(this).inflate(getLayoutId(), null));
        ButterKnife.bind(this);
        App.getInstance().addActivityList(new WeakReference<>(this));
        presenter=createPresenter();
        initViews();
        initData();
    }



    @Override
    protected void onResume() {
        super.onResume();
        initListener();
    }

    protected void initToolbar() {
        if (mToolBar == null) {
            mToolBar = findViewById(R.id.toolbar);
            if (mToolBar == null) {
            } else {
                mToolBar.setTitleTextColor(ContextCompat.getColor(this,R.color.color_text_00));
            }
        }
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }


    protected void setTitle(String title) {
        if (mToolBar != null) {
            mToolBar.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (mToolBar != null) {
            mToolBar.setTitle(titleId);
        }
    }

    protected void setSubTitle(String title) {
        if (mToolBar != null) {
            mToolBar.setSubtitle(title);
        }
    }

    protected void setLogo(Drawable logo) {
        if (mToolBar != null) {
            mToolBar.setLogo(logo);
        }
    }


    protected void setNavigationIcon(Drawable logo) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(logo);
        }
    }

    protected void setMenu(int resId, Toolbar.OnMenuItemClickListener listener) {
        if (mToolBar != null) {
            mToolBar.inflateMenu(resId);
            mToolBar.setOnMenuItemClickListener(listener);
        }
    }

    protected void setDisplayHomeAsUpEnabled(int iconResId, final View.OnClickListener listener) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(iconResId);
            if (listener != null) {
                mToolBar.setNavigationOnClickListener(listener);
            } else {
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    protected void setDisplayHomeAsUpEnabled() {
        setDisplayHomeAsUpEnabled(R.drawable.icon_return, null);
    }

    protected void setDisplayHomeAsUpEnabled(final View.OnClickListener listener) {
        setDisplayHomeAsUpEnabled(R.drawable.icon_return, listener);
    }

    protected void hideToolBarView() {
        if (mToolBar != null) {
            mToolBar.setVisibility(View.GONE);
        }
    }

    protected void showToolBarView() {
        if (mToolBar != null) {
            mToolBar.setVisibility(View.VISIBLE);
        }
    }

    protected void initListener() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁时，解除绑定
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onErrorCode(BaseBean bean) {

    }


}
