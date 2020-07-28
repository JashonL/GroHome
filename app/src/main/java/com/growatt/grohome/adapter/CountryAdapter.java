package com.growatt.grohome.adapter;

import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.CountryBean;

import java.util.List;

public class CountryAdapter extends BaseQuickAdapter<CountryBean, BaseViewHolder> implements SectionIndexer {

    private List<CountryBean> countrys;


    private int getPosition(CountryBean item) {
        return item != null && countrys != null && !countrys.isEmpty() ? countrys.indexOf(item) : -1;
    }

    public CountryAdapter(int layoutResId, @Nullable List<CountryBean> data) {
        super(layoutResId, data);
        countrys=data;
    }



    public CountryAdapter(@Nullable List<CountryBean> data) {
        super(data);
    }

    public CountryAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CountryBean item) {
        TextView tvSortletter = helper.getView(R.id.tv_item_sortletter);
        tvSortletter.setText(String.valueOf(item.getSortLetter()));
        helper.setText(R.id.tv_country, String.valueOf(item.getCityName()));
        if (getPositionForSection(getSectionForPosition(getPosition(item))) == getPosition(item)) {
            tvSortletter.setVisibility(View.VISIBLE);
        } else {
            tvSortletter.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return countrys == null ? 0 : countrys.size();
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            CountryBean bean = getItem(i);
            if (bean.getSortLetter() == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int i) {
        CountryBean bean = getItem(i);
        return bean.getSortLetter();
    }
}
