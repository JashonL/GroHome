package com.growatt.grohome.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.growatt.grohome.utils.GlideUtils;
import com.ortiz.touchview.TouchImageView;

import java.util.List;

public class ManualAdapter extends PagerAdapter {
    private Context context;
    private List<String> urls;

    public ManualAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String url = urls.get(position);
        TouchImageView imageView = new TouchImageView(context);
        GlideUtils.showImageAct((Activity) context,url,imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    public void freshData(List<String> urls){
        this.urls = urls;
        notifyDataSetChanged();
    }
}
