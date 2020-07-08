package com.growatt.grohome.base;


import com.growatt.grohome.R;
import com.growatt.grohome.app.App;

import java.io.IOException;

/**
 * Description : BaseException
 *
 * @author XuCanyou666
 * @date 2020/2/7
 */


public class BaseException extends IOException {

    /**
     * 解析数据失败
     */
    public static final String PARSE_ERROR_MSG = App.getInstance().getString(R.string.m2_parse_error_msg);
    /**
     * 网络问题
     */
    public static final String BAD_NETWORK_MSG = App.getInstance().getString(R.string.m3_bad_network_msg);;
    /**
     * 连接错误
     */
    public static final String CONNECT_ERROR_MSG = App.getInstance().getString(R.string.m4_connect_error_msg);;
    /**
     * 连接超时
     */
    public static final String CONNECT_TIMEOUT_MSG = App.getInstance().getString(R.string.m5_connect_timeout_msg);;
    /**
     * 未知错误
     */
    public static final String OTHER_MSG = App.getInstance().getString(R.string.m6_other_msg);;

    private String errorMsg;
    private int errorCode;

    public String getErrorMsg() {
        return errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public BaseException(String message) {
        this.errorMsg = message;
    }

    public BaseException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorMsg = errorMsg;
    }

    public BaseException(int errorCode, String message) {
        this.errorMsg = message;
        this.errorCode = errorCode;
    }

}