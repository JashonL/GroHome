package com.growatt.grohome.module.personal;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseFragment;
import com.growatt.grohome.module.personal.presenter.PersonalPresenter;
import com.growatt.grohome.module.personal.view.IPersonalFragmentView;

public class PersonalFragment extends BaseFragment<PersonalPresenter> implements IPersonalFragmentView {

    @Override
    protected PersonalPresenter createPresenter() {
        return new PersonalPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
