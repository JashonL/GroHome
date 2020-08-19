package com.growatt.grohome.adapter;

import android.os.Build;
import android.text.Html;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.Article;

import java.util.List;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

/**
 * Description : ArticleAdapter
 *
 * @author XuCanyou666
 * @date 2020/2/8
 */


public class ArticleAdapter extends BaseQuickAdapter<Article.DataDetailBean, BaseViewHolder> {

    public ArticleAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    private boolean mTypeIsCollect = false;

    public void setType(boolean typeIsCollect) {
        mTypeIsCollect = typeIsCollect;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void convert(BaseViewHolder helper, Article.DataDetailBean item) {
        //fromHtml，因为搜索结果中的title中含有html标签
        helper.setText(R.id.article_title, Html.fromHtml(item.title,FROM_HTML_MODE_LEGACY));
        helper.setText(R.id.article_chapter, item.chapterName);
        helper.setText(R.id.article_author, item.author);
        //设置收藏的点击事件
        helper.addOnClickListener(R.id.article_favorite);
        //先判断类型是不是收藏列表，因为收藏列表不返回item.collect字段，所以没法判断


        if (item.collect) {
            Glide.with(mContext).load(R.drawable.ic_like_checked).into((ImageView) helper.getView(R.id.article_favorite));

        } else {
            Glide.with(mContext).load(R.drawable.ic_like_checked).into((ImageView) helper.getView(R.id.article_favorite));
        }
    }
}
