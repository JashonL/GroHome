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
import com.growatt.grohome.utils.MyToastUtils;
import com.growatt.grohome.utils.Mydialog;
import com.gyf.immersionbar.ImmersionBar;

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

    protected ImmersionBar mImmersionBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        //得到context,在后面的子类Fragment中都可以直接调用
        mContext = getActivity();
        presenter = createPresenter();
        initView();
        initImmersionBar();
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

    public void initListener() {
    }


    protected void initToolbar(View contentView) {
        if (mToolBar == null) {
            mToolBar = (Toolbar) contentView.findViewById(R.id.toolbar);
            if (mToolBar == null) {
            } else {
                mToolBar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.color_text_00));
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

    public void requestError(String msg){
        MyToastUtils.toast(msg);
    }

    @Override
    public void onErrorCode(BaseBean bean) {
        Mydialog.dissmiss();
    }

    /**
     * 显示加载中
     */
    @Override
    public void showLoading() {
        Mydialog.show(mContext,"");
    }

    /**
     * 隐藏加载中
     */
    @Override
    public void hideLoading() {
        Mydialog.dissmiss();
    }


    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .statusBarColor(R.color.white)//这里的颜色，你可以自定义。
                .init();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }

}
