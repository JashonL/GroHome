package com.growatt.grohome.module.personal;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.bean.MessageBean;
import com.growatt.grohome.bean.PhotoEditBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.personal.presenter.PersonalPresenter;
import com.growatt.grohome.module.personal.view.IPersonalFragmentView;
import com.growatt.grohome.utils.GlideUtils;
import com.growatt.grohome.utils.MyToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.List;

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
 /*   @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;*/
    @BindView(R.id.tv_message_count)
    TextView tvMessageCount;

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

//        srlPull.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.color_theme_green));
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
        try {
            presenter.getLoginRecord();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
    /*    srlPull.setOnRefreshListener(() -> {
            try {
                presenter.getLoginRecord();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/
    }

    @OnClick({R.id.iv_avatar, R.id.tv_username,R.id.cl_share_device,R.id.card_view_message,R.id.cl_cache,R.id.cl_about,R.id.cl_google,R.id.cl_alexa})
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
            case R.id.cl_google:
                presenter.startGoogleHome();
                break;
            case R.id.cl_alexa:
                presenter.startAmazonAlexa();
                break;
            case R.id.cl_share_device:
                MyToastUtils.toast(R.string.m275_function_is_not_ready);
                break;
            case R.id.card_view_message:
                presenter.toMessageList();
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


    @Override
    public void hideLoading() {
        super.hideLoading();
     /*   if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }*/
    }


    @Override
    public void onError(String msg) {
      /*  if (srlPull != null && srlPull.isRefreshing()) {
            srlPull.setRefreshing(false);
        }*/
        requestError(msg);
    }

    @Override
    public void setMessageCount(List<MessageBean> list) {
        if (list.size()==0){
            tvMessageCount.setVisibility(View.GONE);
        }else {
            tvMessageCount.setVisibility(View.VISIBLE);
            tvMessageCount.setText(String.valueOf(list.size()));
        }
    }

}
