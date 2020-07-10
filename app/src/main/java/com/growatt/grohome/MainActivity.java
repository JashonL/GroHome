package com.growatt.grohome;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.atess.enegy.constants.GlobalConstant;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.adapter.ArticleAdapter;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.bean.Article;
import com.growatt.grohome.module.home.GrohomeFragment;
import com.growatt.grohome.module.personal.PersonalFragment;
import com.growatt.grohome.module.scenes.ScenesFragment;
import com.growatt.grohome.module.service.ServiceFragment;
import com.growatt.grohome.utils.MyToastUtils;
import com.yechaoa.yutils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<HomePresenter> implements IMainActivityView, BaseQuickAdapter.OnItemChildClickListener, BottomNavigationBar.OnTabSelectedListener {
    private long keydownTime;



    private ArticleAdapter mArticleAdapter;
    private List<Article.DataDetailBean> mArticles = new ArrayList<>();
    private int mPosition;


    @BindView(R.id.bootom_navigation_bar)
    BottomNavigationBar bottomNavigationView;
    @BindView(R.id.fl_content)
    FrameLayout flContent;


    /**
     * 包括四个fragment
     */
    private GrohomeFragment mGrohomeFragment;
    private ScenesFragment  mScenesFragment;
    private PersonalFragment mPersonalFragment;
    private ServiceFragment mServiceFragment;

    private FragmentManager mManager;
    private FragmentTransaction mTransaction;


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
//        mHomeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mManager = getSupportFragmentManager();


        //底部控件
        // TODO 设置模式
        bottomNavigationView.setMode(BottomNavigationBar.MODE_FIXED);
        // TODO 设置背景色样式
        bottomNavigationView.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationView.setBarBackgroundColor(R.color.white);
        /**
         *  new BottomNavigationItem(R.mipmap.tab_home_pressed,"首页") ,选中的图标，文字
         *  setActiveColorResource：选中的颜色
         *  setInactiveIconResource：未选中的图标
         *  setInActiveColorResource：未选中的颜色
         */
        bottomNavigationView.clearAll();
        bottomNavigationView
                .addItem(new BottomNavigationItem(R.drawable.tab_home_selected, getString(R.string.m13_grohome)).setActiveColorResource(R.color.color_theme_green).setInactiveIconResource(R.drawable.tab_home_normal).setInActiveColorResource(R.color.color_text_00))
                .addItem(new BottomNavigationItem(R.drawable.tab_scenes_selected, getString(R.string.m10_场景)).setActiveColorResource(R.color.color_theme_green).setInactiveIconResource(R.drawable.tab_scenes_normal).setInActiveColorResource(R.color.color_text_00))
                .addItem(new BottomNavigationItem(R.drawable.tab_service_selected, getString(R.string.m11_服务)).setActiveColorResource(R.color.color_theme_green).setInactiveIconResource(R.drawable.tab_service_normal).setInActiveColorResource(R.color.color_text_00))
                .addItem(new BottomNavigationItem(R.drawable.tab_me_selected, getString(R.string.m12_我的)).setActiveColorResource(R.color.color_theme_green).setInactiveIconResource(R.drawable.tab_me_normal).setInActiveColorResource(R.color.color_text_00))
                .initialise();
        bottomNavigationView.setTabSelectedListener(this);
        bottomNavigationView.selectTab(0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.getArticleList();
    }

    @Override
    protected void initData() {
    }


    @Override
    public void setArticleData(BaseBean<Article> list) {
        mArticles = list.data.datas;
        mArticleAdapter = new ArticleAdapter(R.layout.item_article_list, list.data.datas);
//        mHomeRecyclerView.setAdapter(mArticleAdapter);
        mArticleAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void showArticleError(String errorMessage) {
        ToastUtil.showToast(errorMessage);
    }

    @Override
    public void showCollectSuccess(String successMessage) {
        ToastUtil.showToast(successMessage);
        mArticles.get(mPosition).collect = true;
        //因为收藏成功，所以要刷新界面，以显示小红心
        mArticleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCollectError(String errorMessage) {
        ToastUtil.showToast(errorMessage);

    }

    @Override
    public void showUncollectSuccess(String successMessage) {
        ToastUtil.showToast(successMessage);
        mArticles.get(mPosition).collect = false;
        //因为取消收藏成功，所以要刷新界面，以取消显示小红心
        mArticleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showUncollectError(String errorMessage) {
        ToastUtil.showToast(errorMessage);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - keydownTime) > GlobalConstant.APP_EXIT_LONG_TIME) {
                MyToastUtils.toast(getString(R.string.m1_exit));
                keydownTime = System.currentTimeMillis();
            } else {
                App.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onTabSelected(int position) {
        //开启事务
        mTransaction = mManager.beginTransaction();
        hideFragment(mTransaction);
        switch (position) {
            case 0:
                if (mGrohomeFragment == null) {
                    mGrohomeFragment = new GrohomeFragment();
                    mTransaction.add(R.id.fl_content, mGrohomeFragment);
                }else {
                    mTransaction.show(mGrohomeFragment);
                }
                break;
            case 1:
                if (mScenesFragment == null) {
                    mScenesFragment = new ScenesFragment();
                    mTransaction.add(R.id.fl_content, mScenesFragment);
                }else {
                    mTransaction.show(mScenesFragment);
                }
                break;
            case 2:
                if (mServiceFragment == null) {
                    mServiceFragment = new ServiceFragment();
                    mTransaction.add(R.id.fl_content, mServiceFragment);
                }else {
                    mTransaction.show(mServiceFragment);
                }
                break;
            case 3:
                if (mPersonalFragment == null) {
                    mPersonalFragment = new PersonalFragment();
                    mTransaction.add(R.id.fl_content, mPersonalFragment);
                }else {
                    mTransaction.show(mPersonalFragment);
                }
                break;
        }
        mTransaction.commit();

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    /**
     * 隐藏fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (mGrohomeFragment != null) {
            transaction.hide(mGrohomeFragment);
        }
        if (mScenesFragment != null) {
            transaction.hide(mScenesFragment);
        }
        if (mServiceFragment != null) {
            transaction.hide(mServiceFragment);
        }
        if (mPersonalFragment != null) {
            transaction.hide(mPersonalFragment);
        }
    }

}
