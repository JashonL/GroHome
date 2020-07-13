package com.growatt.grohome;


import android.content.Context;
import android.util.Log;

import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.bean.Article;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.bean.User;
import com.yechaoa.yutils.YUtils;


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


    /**
     * 注册涂鸦
     *
     */
    public void registerTuya(Context context){
        TuyaApiUtils.registerTuya(context,"APP开发者1","app12345678",registerCallback);
    }


    private IRegisterCallback registerCallback=new IRegisterCallback() {
        @Override
        public void onSuccess(User user) {
            //注册成功处理
           Log.i("liaojinsha","注册成功");
        }

        @Override
        public void onError(String code, String error) {
            //注册失败处理
            //注册成功处理
            Log.i("liaojinsha","注册失败");
        }
    };



    /**
     * 登录涂鸦
     */
    public void loginTuya(Context context){
        TuyaApiUtils.loginTuya(context,"ceshi007","",loginCallback);
    }

    private ILoginCallback loginCallback=new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
            //登录成功处理
        }

        @Override
        public void onError(String code, String error) {
            //登录失败处理
        }
    };

}
