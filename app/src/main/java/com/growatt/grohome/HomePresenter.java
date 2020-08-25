package com.growatt.grohome;


import android.content.Context;

import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.eventbus.DeviceStatusMessage;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MD5andKL;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class HomePresenter  extends BasePresenter<IMainActivityView> {

    public HomePresenter(IMainActivityView baseView) {
        super(baseView);
    }


    public HomePresenter(Context context,IMainActivityView baseView) {
        super(context, baseView);
    }

    /**
     * 第一次加载文章列表
     */
    public void getArticleList() {
        addDisposable(apiServer.getArticleList(0), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String bean) {
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
     * 登录涂鸦
     */
    public void loginTuya(Context context){
        String accountName = App.getUserBean().getAccountName();
        String code = App.getUserBean().getUserTuyaCode();
        TuyaApiUtils.autoLogin(context,code, App.getUserBean().getAccountName(), MD5andKL.encryptPassword(accountName),loginCallback);
    }

    private ILoginCallback loginCallback=new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
            //登录成功处理
            isNeedCreateHome();
        }

        @Override
        public void onError(String code, String error) {
            //登录失败处理

        }
    };



    /**
     * 判断是否需要创建一个家庭
     */
    private void isNeedCreateHome() {
        //登录涂鸦成功,获取家庭，如果没有创建家庭那么就创建一个
        TuyaApiUtils.getHomeList(homelistCallBack);
    }


    private ITuyaGetHomeListCallback homelistCallBack=new ITuyaGetHomeListCallback() {

        @Override
        public void onSuccess(List<HomeBean> homeBeans) {
            if (CommentUtils.isEmpty(homeBeans)) {
                createHome();
            } else {
                initHome();
            }
        }

        @Override
        public void onError(String errorCode, String error) {

        }
    };


    /**
     * 创建一个默认的涂鸦家庭
     */
    private void createHome() {
        ArrayList<String> rooms = new ArrayList<>();
        rooms.add("bedroom");
        TuyaApiUtils.createHome("MyHome", rooms, new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean homeBean) {
                initHome();
            }

            @Override
            public void onError(String s, String s1) {
            }
        });
    }



    private void initHome() {
        TuyaApiUtils.getHomeDetail(TuyaApiUtils.getHomeId(), new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean homeBean) {
                TuyaApiUtils.setIsHomeInit(true);
                EventBus.getDefault().post(new DeviceStatusMessage());
            }

            @Override
            public void onError(String s, String s1) {
            }
        });
    }


}
