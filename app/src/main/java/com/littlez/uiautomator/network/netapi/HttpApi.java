package com.littlez.uiautomator.network.netapi;


import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by xiaozhi on 2018/12/27.
 * <p>
 * 存放所有的Api
 */

public interface HttpApi {


    /**
     * 跟新 jar
     *
     * @param map
     * @return
     */
    @GET("AutoRunner.jar")
    Call<ResponseBody> downloadJar(@QueryMap Map<String, Object> map);

    /**
     * 跟新apk
     *
     * @param map
     * @return
     */
    @GET("uiautomator_apk")
    Call<ResponseBody> updateApk(@QueryMap Map<String, Object> map);

}
