package com.growatt.grohome.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.growatt.grohome.R;
import com.yechaoa.yutils.ActivityUtil;
import com.yechaoa.yutils.YUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description : BaseFragment
 *
 * @author XuCanyou666
 * @date 2020/2/7
 */


public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {

    private Unbinder unbinder;
    protected Context mContext;

    protected P presenter;

    protected abstract P createPresenter();

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected Toolbar mToolBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        //得到context,在后面的子类Fragment中都可以直接调用
        mContext = ActivityUtil.getCurrentActivity();
        presenter = createPresenter();
        initView();
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //do something
        unbinder.unbind();
        //销毁时，解除绑定
        if (presenter != null) {
            presenter.detachView();
        }

        mToolBar = null;
    }

    private void initListener() {
    }


    protected void initToolbar(View contentView) {
        if (mToolBar == null) {
            mToolBar = (Toolbar) contentView.findViewById(R.id.toolbar);
            if (mToolBar == null) {
            } else {
                mToolBar.setTitleTextColor(ContextCompat.getColor(getContext(),R.color.color_title_00));
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

    protected void setDisplayHomeAsUpEnabled() {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(R.drawable.icon_return);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
    }

    protected void setDisplayHomeAsUpEnabled(final View.OnClickListener listener) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(R.drawable.icon_return);
            mToolBar.setNavigationOnClickListener(listener);
        }
    }

    protected void hideToolBarView() {
        if (mToolBar != null && mToolBar.isShown()) {
            mToolBar.setVisibility(View.GONE);
        }
    }

    protected void showToolBarView() {
        if (mToolBar != null && !mToolBar.isShown()) {
            mToolBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorCode(BaseBean bean) {
    }

    /**
     * 显示加载中
     */
    @Override
    public void showLoading() {
        YUtils.showLoading(ActivityUtil.getCurrentActivity(), "加载中");
    }

    /**
     * 隐藏加载中
     */
    @Override
    public void hideLoading() {
        YUtils.dismissLoading();
    }


}
