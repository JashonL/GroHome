package com.growatt.grohome.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;


/**
 * 防止内存泄漏
 */
public class NoleakHandler<T extends IcallbackHandler> extends Handler {
    private WeakReference<T> wr;
    public NoleakHandler(T t) {
        wr=new WeakReference<>(t);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        T t = wr.get();
        if (t!=null){
            t.callback();
        }
    }
}
