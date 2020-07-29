package com.growatt.grohome.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.growatt.grohome.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class GlideUtils {

    public static void showImageContext(Context context, String path, ImageView iv) {
        showImageContext(context, R.drawable.loading, R.drawable.loading, path, iv);
    }

    public static void showImageContext(Context context, int placeholderRes, int errorRes, String path, ImageView iv) {
        Glide.with(context).load(path).placeholder(placeholderRes).error(errorRes).dontAnimate().into(iv);
    }

    public static void showImageAct(Activity act, String path, ImageView iv) {
        showImageAct(act, R.drawable.loading, R.drawable.loading, path, iv);
    }

    public static void showImageAct(Activity act, int resId, ImageView iv) {
        showImageAct(act, R.drawable.loading, R.drawable.loading, resId, iv);
    }

    public static void showImageAct(Activity act, int placeholderRes, int errorRes, String path, ImageView iv) {
        Glide.with(act).load(path).placeholder(placeholderRes).error(errorRes).dontAnimate().into(iv);
    }

    public static void showImageActNoCache(Activity act, int placeholderRes, int errorRes, String path, ImageView iv) {
        Glide.with(act).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(placeholderRes).error(errorRes).dontAnimate().into(iv);
    }

    public static void showImageAct(Activity act, int placeholderRes, int errorRes, int resId, ImageView iv) {
        Glide.with(act).load(resId).placeholder(placeholderRes).error(errorRes).dontAnimate().into(iv);
    }

    public static void showImageContext(Context con, int placeholderRes, int errorRes, int resId, ImageView iv) {
        Glide.with(con).load(resId).placeholder(placeholderRes).error(errorRes).dontAnimate().into(iv);
    }

    public void setImageBackground(Context context, int resId, View view) {
        Glide.with(context)
                .asBitmap()
                .load(resId)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(resource);
                        view.setBackground(drawable);
                    }

                });
    }

    /**
     * @param act
     * @param resId
     * @param iv
     * @param type:0:表示没有占位图;1:默认占位图；其他为咱位图资源
     */
    public static void showImageAct(Activity act, int resId, ImageView iv, int type) {
        switch (type) {
            case 0:
                Glide.with(act).load(resId).error(R.drawable.loading).dontAnimate().into(iv);
                break;
            case 1:
                Glide.with(act).load(resId).placeholder(R.drawable.loading).error(R.drawable.loading).dontAnimate().into(iv);
                break;
            default:
                Glide.with(act).load(resId).placeholder(type).error(R.drawable.loading).dontAnimate().into(iv);
                break;
        }
    }


    //加载圆角图片
    public static void showImageContext(Context context,int placeholderRes,int errorRes,String path,ImageView iv,int dp){
        Glide.with(context).load(path).placeholder(placeholderRes).error(errorRes).dontAnimate().transform(new RoundedCornersTransformation(dp,0)).into(iv);
    }

    //加载圆角图片
    public  void showImageContext(Context context,int placeholderRes,int errorRes,int path,ImageView iv,int dp){
        Glide.with(context).load(path).placeholder(placeholderRes).error(errorRes).dontAnimate().transform(new RoundedCornersTransformation(dp,0)).into(iv);
    }
}
