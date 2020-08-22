package com.growatt.grohome.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Outline;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class CommentUtils {
    //判断WiFi是否打开
    public static boolean isWiFi(Context context) throws Exception {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo == null) return false;
        return networkInfo.isConnected();
    }

    /**
     * 获取SSID
     *
     * @param activity 上下文
     * @return WIFI 的SSID
     */
    public static String getWiFiSsid(Activity activity) throws Exception {
        String ssid = null;
        ConnectivityManager manager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo.State wifi = Objects.requireNonNull(manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState();
        if (wifi == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            assert mWifiManager != null;
            WifiInfo info = mWifiManager.getConnectionInfo();
            return info.getSSID().replace("\"", "");
        } else {
            WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (mWifiManager != null) {
                WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
                int networkId = connectionInfo.getNetworkId();
                ssid = connectionInfo.getSSID();
                List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
                for (WifiConfiguration wificonf : configuredNetworks) {
                    if (wificonf.networkId == networkId) {
                        ssid = wificonf.SSID;
                        break;
                    }
                }
            }
            if (TextUtils.isEmpty(ssid)) return ssid;
            if (ssid.contains("\"")) {
                ssid = ssid.replace("\"", "");
            }
        }
        return ssid;
    }


    /**
     * 判断wifi是否是5G
     *
     * @param ssid    wifi名称
     * @param context 上下文
     * @return
     */

    public static boolean is5GHz(String ssid, Context context) {
        try {
            WifiManager wifiManger = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            assert wifiManger != null;
            WifiInfo wifiInfo = wifiManger.getConnectionInfo();
            if (wifiInfo != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int freq = wifiInfo.getFrequency();
                return freq > 4900 && freq < 5900;
            } else return ssid.toUpperCase().endsWith("5G");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 判断集合是否为空
     */
    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //dp转像素
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //将int数值转成4位的16进制的字符串，不足的补0
    public static String integerToHexstring(int value, int scale) {
        String hex = Integer.toHexString(value);
        if (hex.length() < scale) {
            StringBuilder suff = new StringBuilder();
            for (int i = 0; i < scale - hex.length(); i++) {
                suff.append("0");
            }
            hex = suff.toString() + hex;
        }
        return hex;
    }


    //将16进制的字符串转成int数值，不足的补0
    public static int hexStringToInter(String value) {
        if (TextUtils.isEmpty(value)) return 0;
        return Integer.parseInt(value, 16);
    }


    /**
     * 获取新语言
     *
     * @return
     */
    public static int getLanguage() {
        int lan = 1;
        Locale locale = App.getInstance().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.toLowerCase().contains("zh")) {
            language = "zh_cn";
            lan = 0;
            if (!locale.getCountry().toLowerCase().equals("cn")) {
                lan = 14;
            }
        }
        if (language.toLowerCase().contains("en")) {
            language = "en";
            lan = 1;
        }
        if (language.toLowerCase().contains("fr")) {
            language = "fr";
            lan = 2;
        }
        if (language.toLowerCase().contains("ja")) {
            language = "ja";
            lan = 3;
        }
        if (language.toLowerCase().contains("it")) {
            language = "it";
            lan = 4;
        }
        if (language.toLowerCase().contains("ho")) {
            language = "ho";
            lan = 5;
        }
        if (language.toLowerCase().contains("tk")) {
            language = "tk";
            lan = 6;
        }
        if (language.toLowerCase().contains("pl")) {
            language = "pl";
            lan = 7;
        }
        if (language.toLowerCase().contains("gk")) {
            language = "gk";
            lan = 8;
        }
        if (language.toLowerCase().contains("gm")) {
            language = "gm";
            lan = 9;
        }
        if (language.toLowerCase().contains("pt")) {
            language = "pt";
            lan = 10;
        }
        if (language.toLowerCase().contains("sp")) {
            language = "sp";
            lan = 11;
        }
        if (language.toLowerCase().contains("vn")) {
            language = "vn";
            lan = 12;
        }
        if (language.toLowerCase().contains("hu")) {
            language = "hu";
            lan = 13;
        }
        return lan;
    }



    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return createGmtOffsetString(true, true, tz.getRawOffset());
    }

    public static String createGmtOffsetString(boolean includeGmt, boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, 2, offsetMinutes % 60);
        return builder.toString();
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
    }



    public static int getLocale() {
        Locale locale = App.getInstance().getResources().getConfiguration().locale;
        String language = locale.getLanguage().toLowerCase();
        int a;
        if (language.contains("cn") || language.contains("zh")) {
            a = 0;
            if (!locale.getCountry().toLowerCase().equals("cn")){
                a = 2;
            }
        } else if (language.contains("en")) {
            a = 1;

        } else {
            a = 2;
        }
        return a;
    }


    /**
     * 获取新语言
     *
     * @return
     */
    public static List<String> getZones() {
        List<String> zones = new ArrayList<>();
        zones.add("-12");
        zones.add("-11");
        zones.add("-10");
        zones.add("-9");
        zones.add("-8");
        zones.add("-7");
        zones.add("-6");
        zones.add("-5");
        zones.add("-4");
        zones.add("-3");
        zones.add("-2");
        zones.add("-1");
        zones.add("0");
        zones.add("+1");
        zones.add("+2");
        zones.add("+3");
        zones.add("+4");
        zones.add("+5");
        zones.add("+6");
        zones.add("+7");
        zones.add("+8");
        zones.add("+9");
        zones.add("+10");
        zones.add("+11");
        zones.add("+12");
        return zones;
    }


    public static List<String> getWeeks(Context context) {
        List<String> weeks = new ArrayList<>();
        weeks.add(context.getString(R.string.m225_monday));
        weeks.add(context.getString(R.string.m226_tuesday));
        weeks.add(context.getString(R.string.m227_wednesday));
        weeks.add(context.getString(R.string.m228_thursday));
        weeks.add(context.getString(R.string.m229_friday));
        weeks.add(context.getString(R.string.m230_saturday));
        weeks.add(context.getString(R.string.m231_sunday));
        return weeks;
    }


    //隐藏虚拟键盘
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    //显示虚拟键盘
    public static void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.showSoftInput(v, 0);
    }


    /**
     * [获取应用程序包名称信息]
     *
     * @param context
     * @return 当前应用的包名
     */
    public static String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断字符串是否是"null"
     */


    public static boolean isStringEmpty(String s) {
        return "null".equals(s);
    }


    /**
     * 获取24小时
     */
    public static List<String> getHours() {
        List<String> hours = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            if (hour < 10) hours.add("0" + hour);
            else hours.add(String.valueOf(hour));
        }
        return hours;
    }

    /**
     * 获取60分钟
     */
    public static List<String> getMins() {
        List<String> getMins = new ArrayList<>();
        for (int min = 0; min < 24; min++) {
            if (min < 10) getMins.add("0" + min);
            else getMins.add(String.valueOf(min));
        }
        return getMins;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 设置view圆角
     *
     * @param radius
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setBannerRound(View view,float radius) {
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        view.setClipToOutline(true);
    }
}
