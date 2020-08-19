package com.growatt.grohome.handler;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.tuya.smart.android.common.utils.SafeHandler;

import java.lang.ref.WeakReference;
import java.util.Random;


/**
 * 防止内存泄漏
 */
public class NoleakHandler extends SafeHandler {
    static boolean isDebugMode = false;
    static Random random = new Random(System.currentTimeMillis());
    private boolean isAlive;
    private WeakReference<Context> mActivity;

    public NoleakHandler() {
        this.isAlive = true;
    }

    public NoleakHandler(Callback callback) {
        super(callback);
        this.isAlive = true;
    }

    public NoleakHandler(Context activity, Callback callback) {
        this(callback);
        this.mActivity = new WeakReference(activity);
    }

    public NoleakHandler(Looper looper, Callback callback) {
        super(looper, callback);
        this.isAlive = true;
    }

    public NoleakHandler(Activity activity, Looper looper, Callback callback) {
        this(looper, callback);
        this.mActivity = new WeakReference(activity);
    }

    public NoleakHandler(Looper looper) {
        super(looper);
        this.isAlive = true;
    }

    public NoleakHandler(Activity activity, Looper looper) {
        this(looper);
        this.mActivity = new WeakReference(activity);
    }

    public static void setDebugMode(boolean mode) {
        isDebugMode = mode;
    }

    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        return !this.isAlive ? false : super.sendMessageAtTime(msg, uptimeMillis);
    }

    public void dispatchMessage(Message msg) {
        if (this.isAlive) {
            if (isDebugMode) {
                if (this.mActivity != null) {
                    if (this.mActivity.get() != null) {
                        super.dispatchMessage(msg);
                    }
                } else {
                    super.dispatchMessage(msg);
                }
            } else {
                try {
                    if (this.mActivity != null) {
                        if (this.mActivity.get() != null) {
                            super.dispatchMessage(msg);
                        }
                    } else {
                        super.dispatchMessage(msg);
                    }
                } catch (Exception var3) {
                }
            }

            this.clearMsg(msg);
        }
    }

    public void clearMsg(Message msg) {
        msg.what = 0;
        msg.arg1 = 0;
        msg.arg2 = 0;
        msg.obj = null;
        msg.replyTo = null;
        msg.setTarget((Handler)null);
    }

    public void destroy() {
        this.isAlive = false;
    }
}
