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

    public static final String USER_URL = "http://server-cn.growatt.com/";


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
        @POST(USER_URL + "newTwoLoginAPI.do")
        Observable<String> login(@Field("userName") String username, @Field("password") String password);


        //注册
        @FormUrlEncoded
        @POST("user/register")
        Observable<String> register(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);


        //-----------------------【  收藏  】----------------------

        //收藏站内文章
        @POST("lg/collect/{id}/json")
        Observable<String> collectIn(@Path("id") Integer id);

        //取消收藏---文章列表
        @POST("lg/uncollect_originId/{id}/json")
        Observable<String> uncollect(@Path("id") Integer id);


        //-------------------------【  首页  】----------------------------------
        @POST("room/")
        Observable<String> getAllDevice(@Body RequestBody body);

        //---------------------------【   设备   】-----------------------------------
        @POST("/tuya/addDevice")
        Observable<String> addDevice(@Body RequestBody body);

    }

}
