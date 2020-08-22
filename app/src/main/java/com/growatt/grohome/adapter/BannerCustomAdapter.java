package com.growatt.grohome.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.growatt.grohome.R;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

public class BannerCustomAdapter extends BannerAdapter<String, BannerCustomAdapter.ImageHolder> {

    public BannerCustomAdapter(List<String> datas) {
        super(datas);
    }

    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView view = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_image, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        //这里判断高度和宽带是否都是match_parent
        if (params.height != -1 || params.width != -1) {
            params.height = -1;
            params.width = -1;
            view.setLayoutParams(params);
        }
        BannerUtils.setBannerRound(view,20);
        return new ImageHolder(view);
    }

    @Override
    public void onBindView(ImageHolder holder, String data, int position, int size) {
        //通过图片加载器实现圆角，你也可以自己使用圆角的imageview，实现圆角的方法很多，自己尝试哈
        Glide.with(holder.itemView)
                .load(data)
                .thumbnail(Glide.with(holder.itemView).load(R.drawable.loading))
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(holder.imageView);

    }

    class ImageHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageHolder(@NonNull View view) {
            super(view);
            this.imageView = (ImageView) view;
        }
    }
}
