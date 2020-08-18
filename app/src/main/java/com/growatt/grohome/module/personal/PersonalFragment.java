package com.growatt.grohome.module.personal;

import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.personal.presenter.PersonalPresenter;
import com.growatt.grohome.module.personal.view.IPersonalFragmentView;
import com.growatt.grohome.utils.GlideUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalFragment extends BaseFragment<PersonalPresenter> implements IPersonalFragmentView {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.v_background)
    View vBackground;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_username)
    AppCompatTextView tvUsername;
    @BindView(R.id.guideline_begin)
    Guideline guidelineBegin;
    @BindView(R.id.guideline_end)
    Guideline guidelineEnd;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.tv_message_center)
    AppCompatTextView tvMessageCenter;
    @BindView(R.id.tv_message_count)
    AppCompatTextView tvMessageCount;
    @BindView(R.id.card_view_message)
    CardView cardViewMessage;
    @BindView(R.id.iv_share_device)
    ImageView ivShareDevice;
    @BindView(R.id.tv_share_device)
    AppCompatTextView tvShareDevice;
    @BindView(R.id.cl_share_device)
    ConstraintLayout clShareDevice;
    @BindView(R.id.line_share_cache)
    View lineShareCache;
    @BindView(R.id.iv_cache)
    ImageView ivCache;
    @BindView(R.id.tv_cache)
    AppCompatTextView tvCache;
    @BindView(R.id.cl_cache)
    ConstraintLayout clCache;
    @BindView(R.id.line_cache_about)
    View lineCacheAbout;
    @BindView(R.id.iv_about)
    ImageView ivAbout;
    @BindView(R.id.tv_about)
    AppCompatTextView tvAbout;
    @BindView(R.id.cl_about)
    ConstraintLayout clAbout;
    @BindView(R.id.line_about_agreement)
    View lineAboutAgreement;
    @BindView(R.id.iv_agreement)
    ImageView ivAgreement;
    @BindView(R.id.tv_agreement)
    AppCompatTextView tvAgreement;
    @BindView(R.id.cl_agreement)
    ConstraintLayout clAgreement;
    @BindView(R.id.card_view_device)
    CardView cardViewDevice;
    @BindView(R.id.tv_third_party)
    AppCompatTextView tvThirdParty;
    @BindView(R.id.icon_alexa)
    ImageView iconAlexa;
    @BindView(R.id.tv_alexa)
    AppCompatTextView tvAlexa;
    @BindView(R.id.cl_alexa)
    ConstraintLayout clAlexa;
    @BindView(R.id.icon_google)
    ImageView iconGoogle;
    @BindView(R.id.tv_google)
    AppCompatTextView tvGoogle;
    @BindView(R.id.cl_google)
    ConstraintLayout clGoogle;
    @BindView(R.id.card_view_third)
    CardView cardViewThird;
    @BindView(R.id.v_empty)
    View vEmpty;
    @BindView(R.id.scrollview)
    ScrollView scrollview;


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
        //头像和昵称
        try {
            String path = Environment.getExternalStorageDirectory() + "/" + GlobalConstant.IMAGE_FILE_LOCATION;
            GlideUtils.showImageActNoCache(getActivity(), R.drawable.avatar, R.drawable.avatar, path, ivAvatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_avatar, R.id.tv_username})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
            case R.id.tv_username:
                presenter.toUsercenter();
                break;
        }
    }
}
