package com.growatt.grohome.module.personal;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.bean.PhotoEditBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.personal.presenter.PersonalPresenter;
import com.growatt.grohome.module.personal.view.IPersonalFragmentView;
import com.growatt.grohome.utils.GlideUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalFragment extends BaseFragment<PersonalPresenter> implements IPersonalFragmentView {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_username)
    AppCompatTextView tvUsername;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.reset().statusBarView(statusBarView).statusBarColor(R.color.color_button).init();
    }

    @Override
    protected PersonalPresenter createPresenter() {
        return new PersonalPresenter(getContext(),this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initView() {
        toolbar.setVisibility(View.GONE);

        //用户名
        String name = App.getUserBean().getAccountName();
        if (!TextUtils.isEmpty(name)) {
            tvUsername.setText(name);
        }
        String path = App.getInstance().getFilesDir().getPath() + "/" + GlobalConstant.IMAGE_FILE_LOCATION;
        showPicture(path);
    }

    private void showPicture(String path) {
        //头像和昵称
        try {
            GlideUtils.showImageActNoCache(getActivity(), R.drawable.avatar, R.drawable.avatar, path, ivAvatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }



    @OnClick({R.id.iv_avatar, R.id.tv_username,R.id.cl_cache,R.id.cl_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
            case R.id.tv_username:
                presenter.toUsercenter();
                break;
            case R.id.cl_cache:
                presenter.clearCache();
                break;
            case R.id.cl_about:
                presenter.about();
                break;
        }
    }


    /**
     * 刷新场景
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSceneseBean(PhotoEditBean bean) {
        if (bean != null) {
            try {
                String path = bean.getPath();
                showPicture(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
