package com.growatt.grohome.module.login.presenter;

import android.content.Context;
import android.text.Editable;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.CountryBean;
import com.growatt.grohome.module.login.view.ICountryListView;
import com.growatt.grohome.utils.PinYinUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CountryListPresenter extends BasePresenter<ICountryListView> {

    private List<String> countrys=new ArrayList<>();
    private List<String> list1=new ArrayList<>();

    public CountryListPresenter(ICountryListView baseView) {
        super(baseView);
    }

    public CountryListPresenter(Context context, ICountryListView baseView) {
        super(context, baseView);
    }


    public void getCountry(){
        addDisposable(apiServer.getCountry(), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    int result = jsonObject.optInt("result");
                    if (result == 1) {
                        JSONObject obj = jsonObject.optJSONObject("obj");
                        if (obj==null)return;
                        JSONArray array = obj.optJSONArray("countrys");
                        if (array==null)return;
                        String[] strs = new String[array.length()];
                        for (int i = 0; i < array.length(); i++) {
                            strs[i]=array.optString(i);
                        }
                        Arrays.sort(strs,String.CASE_INSENSITIVE_ORDER);
                        List<CountryBean> newList = new ArrayList<>();
                        for (int i = 0; i < strs.length-1; i++) {
                            countrys.add(strs[i]);
                            String cityname = strs[i];
                            CountryBean countryBean = new CountryBean();
                            countryBean.setCityName(cityname);
                            countryBean.setPyName(PinYinUtil.getPinYin(cityname));
                            countryBean.setSortLetter(countryBean.getPyName().charAt(0));
                            newList.add(countryBean);
                        }
                        Collections.sort(newList, (lhs, rhs) -> lhs.getPyName().compareTo(rhs.getPyName()));
                        String nameZh=strs[strs.length-1];
                        CountryBean beanZh = new CountryBean();
                        beanZh.setCityName(nameZh);
                        beanZh.setPyName(PinYinUtil.getPinYin(nameZh));
                        beanZh.setSortLetter(beanZh.getPyName().charAt(0));
                        if(nameZh.contains(context.getString(R.string.m174_china))){
                            newList.add(0, beanZh);
                            countrys.add(0, nameZh);

                        }else{
                            newList.add(beanZh);
                            countrys.add(nameZh);
                        }
                        baseView.updataList(newList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String msg) {
            }
        });
    }


    public void upSearchChange(Editable s){
        String str=s.toString();
        str=str.toLowerCase();
        list1 = new ArrayList<>();
        if(countrys!=null){
            for (int i = 0; i <countrys.size(); i++) {
                String str1=countrys.get(i).toLowerCase();
                if(str1.contains(str)){
                    list1.add(countrys.get(i));
                }
            }
            List<CountryBean> newList = new ArrayList<>();
            for (int i = 0; i <list1.size(); i++) {
                String cityname = list1.get(i);
                CountryBean bean = new CountryBean();
                bean.setCityName(cityname);
                bean.setPyName(PinYinUtil.getPinYin(cityname));
                bean.setSortLetter(bean.getPyName().charAt(0));
                newList.add(bean);
            }
            baseView.updataList(newList);
        }
    }


}
