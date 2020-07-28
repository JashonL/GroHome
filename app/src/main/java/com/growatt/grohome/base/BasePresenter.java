package com.growatt.grohome.base;




import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.growatt.grohome.handler.NoleakHandler;
import com.growatt.grohome.http.API;
import com.growatt.grohome.http.RetrofitService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Description : BasePresenter
 *
 * @author XuCanyou666
 * @date 2020/2/7
 */


public class BasePresenter<V extends BaseView> implements Handler.Callback {

    private CompositeDisposable compositeDisposable;
    public V baseView;
    public NoleakHandler handler;
    public Context context;

    /**
     * 这个后面可以直接用   Example：apiServer.login(username, password)；
     */
    protected API.WAZApi apiServer = RetrofitService.getInstance().getApiService();

    public BasePresenter(V baseView) {
        this.baseView = baseView;
    }
    public BasePresenter(Context context,V baseView) {
        this.baseView = baseView;
        this.context=context;
    }

    public void initHandler(Context context){
        handler=new NoleakHandler(context,this);
    }

    /**
     * 解除绑定
     */
    public void detachView() {
        baseView = null;
        removeDisposable();
    }

    /**
     * 返回 view
     */
    public V getBaseView() {
        return baseView;
    }

    public void addDisposable(Observable<?> observable, BaseObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable
                .add(observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(observer));
    }

    private void removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }


    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }

    public void onDestroy() {
        if (handler!=null){
            this.handler.destroy();
        }
    }
}
