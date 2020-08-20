package com.growatt.grohome.utils;

import com.growatt.grohome.http.API;

public class UrlUtil {

    public static String replaceUrl(String preUrl) {
        if ("server-cn.growatt.com".equals(preUrl)) {
            preUrl = API.URL_CN_HOST;
        } else if ("server.growatt.com".equals(preUrl)) {
            preUrl = API.URL_HOST;
        }
        return preUrl;
    }

}
