package com.growatt.grohome;


import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.Article;

public class HomePresenter  extends BasePresenter<IMainActivityView> {

    public HomePresenter(IMainActivityView baseView) {
        super(baseView);
    }

    /**
     * 第一次加载文章列表
     */
    public void getArticleList() {
        addDisposable(apiServer.getArticleList(0), new BaseObserver<BaseBean<Article>>(baseView,true) {
            @Override
            public void onSuccess(BaseBean<Article> bean) {
                baseView.setArticleData(bean);
            }

            @Override
            public void onError(String msg) {
                baseView.showArticleError(msg + "(°∀°)ﾉ");
            }
        });
    }



    /**
     * 收藏
     *
     * @param id 文章id
     */
    public void collect(int id) {
        addDisposable(apiServer.collectIn(id), new BaseObserver<BaseBean>(baseView) {
            @Override
            public void onSuccess(BaseBean bean) {
                baseView.showCollectSuccess("收藏成功（￣▽￣）");
            }

            @Override
            public void onError(String msg) {
                baseView.showCollectError(msg + "(°∀°)ﾉ");
            }
        });
    }

    /**
     * 取消收藏
     *
     * @param id 文章id
     */
    public void uncollect(int id) {
        addDisposable(apiServer.uncollect(id), new BaseObserver<BaseBean>(baseView) {
            @Override
            public void onSuccess(BaseBean bean) {
                baseView.showUncollectSuccess("取消收藏成功（￣▽￣）");
            }

            @Override
            public void onError(String msg) {
                baseView.showUncollectError(msg + "(°∀°)ﾉ");
            }
        });
    }

}
