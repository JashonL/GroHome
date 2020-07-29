package com.growatt.grohome.http;


import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.bean.Article;
import com.growatt.grohome.bean.User;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Description : API
 * 接口的管理类
 *
 * @author XuCanyou666
 * @date 2020/2/7
 */


public class API {

    static final String BASE_URL = "http://energy.growatt.com/";
//    static final String BASE_URL = "https://www.wanandroid.com/";

    private static final String OSS_URL = "http://oss1.growatt.com/";

    public static final String USER_URL = "http://server-api.growatt.com/";

    public static final String VERIFICATION_CODE="/newTwoRegisterAPI.do?action=sendEmailVerification";

    public static final String TEST_URL="http://192.168.3.214:8082/eic_web/";



    public interface WAZApi {

        //-----------------------【首页相关】----------------------


        //首页文章列表 这里的{}是填入页数
        @GET("article/list/{page}/json")
        Observable<String> getArticleList(@Path("page") Integer page);


        //-----------------------【登录注册】----------------------

        //登录
        @FormUrlEncoded
        @POST(OSS_URL + "api/v2/login")
        Observable<String> getUserType(@Field("userName") String username, @Field("password") String password, @Field("language") String language);

        @FormUrlEncoded
        @POST
        Observable<String> login(@Url String url, @Field("userName") String username, @Field("password") String password);


        //注册
        @FormUrlEncoded
        @POST("user/register")
        Observable<String> register(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);


        //获取国家
        @GET(USER_URL+"newCountryCityAPI.do?op=getCountryList")
        Observable<String> getCountry();

        //根据国家获取服务器
        @GET(USER_URL+"newLoginAPI.do?op=getServerUrl")
        Observable<String> getServerCountry(@Query("country")String country);

        //获取邮箱验证码
        @FormUrlEncoded
        @POST
        Observable<String> getVerificationCode(@Url String url, @Field("accountName")String accountName,@Field("email")String email,@Field("language")String language);

        @FormUrlEncoded
        @POST
        Observable<String> groHomeRegister(@Url String url, @Field("regUserName")String regUserName,@Field("regPassword")String regPassword,@Field("regTimeZone")String regTimeZone,@Field("regEmail")String regEmail,@Field("regCountry")String regCountry);


        //-----------------------【  收藏  】----------------------

        //收藏站内文章
        @POST("lg/collect/{id}/json")
        Observable<String> collectIn(@Path("id") Integer id);

        //取消收藏---文章列表
        @POST("lg/uncollect_originId/{id}/json")
        Observable<String> uncollect(@Path("id") Integer id);


        //-------------------------【  首页  】----------------------------------
        //获取设备列表
        @POST("room/")
        Observable<String> getAllDevice(@Body RequestBody body);

        //获取房间列表
        @POST(TEST_URL+"room/")
        Observable<String> getRoomList(@Body RequestBody body);

        //---------------------------【   设备   】-----------------------------------
        @POST("/tuya/addDevice")
        Observable<String> addDevice(@Body RequestBody body);
        @POST("/tuya/switchInfo")
        Observable<String> getSwitchDetail(@Body RequestBody body);
    }

}
