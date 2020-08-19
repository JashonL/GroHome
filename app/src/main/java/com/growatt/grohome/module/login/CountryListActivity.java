package com.growatt.grohome.module.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.growatt.grohome.R;
import com.growatt.grohome.adapter.CountryAdapter;
import com.growatt.grohome.base.BaseActivity;
import com.growatt.grohome.bean.CountryBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.customview.MyLetterView;
import com.growatt.grohome.module.login.presenter.CountryListPresenter;
import com.growatt.grohome.module.login.view.ICountryListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CountryListActivity extends BaseActivity<CountryListPresenter> implements ICountryListView , MyLetterView.OnTouchLetterListener, TextWatcher,BaseQuickAdapter.OnItemClickListener{
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.editText1)
    EditText editText1;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mlv_city_letters)
    MyLetterView mlvCityLetters;

    private CountryAdapter mCountryAdapter;

    @Override
    protected CountryListPresenter createPresenter() {
        return new CountryListPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_country_list;
    }

    @Override
    protected void initViews() {
        //设置头部
        toolbar.setNavigationIcon(R.drawable.icon_return);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.nocolor));
        tvTitle.setText(R.string.m173_country);

        //国家列表
        mCountryAdapter=new CountryAdapter(R.layout.item_country,new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(mCountryAdapter);
    }

    @Override
    protected void initData() {
        presenter.getCountry();
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(v -> finish());
        mlvCityLetters.setOnTouchLetterListener(this);
        mCountryAdapter.setOnItemClickListener(this);
        editText1.addTextChangedListener(this);
    }

    @Override
    public void updataList(List<CountryBean> newList) {
        mCountryAdapter.replaceData(newList);
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
       presenter.upSearchChange(s);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CountryBean countryBean = mCountryAdapter.getData().get(position);
        String cityName = countryBean.getCityName();
        editText1.setText(cityName);
        editText1.setSelection(cityName.length());
        //直接跳转到上级界面
        String str=editText1.getText().toString();
        if(!TextUtils.isEmpty(str)){
            Intent intent=new Intent();
            intent.putExtra(GlobalConstant.COUNTRY, str);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onTouchLetter(String letter) {
        char section = letter.charAt(0);
        if(mCountryAdapter!=null){
            int positionForSection = mCountryAdapter.getPositionForSection(section);
            if (positionForSection!=-1){
                recyclerView.scrollToPosition(positionForSection);
                LinearLayoutManager mLayoutManager =  (LinearLayoutManager) recyclerView.getLayoutManager();
                assert mLayoutManager != null;
                mLayoutManager.scrollToPositionWithOffset(positionForSection, 0);
            }
        }
    }

    @Override
    public void onRelaseLetter() {

    }
}
