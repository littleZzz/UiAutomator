package com.littlez.uiautomator.network.netutils;


import android.text.TextUtils;


import com.littlez.uiautomator.base.MyApplication;
import com.littlez.uiautomator.network.netapi.HttpApi;
import com.littlez.uiautomator.network.netapi.URLConstant;
import com.littlez.uiautomator.util.SPUtils;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by littleZ on 2018/3/27.
 */

public class HttpMethods {
    public String TAG = "HttpMethods";
    public static final String CACHE_NAME = "littleZ";
    public static String BASE_URL = URLConstant.BASE_URL;
    private static final int DEFAULT_CONNECT_TIMEOUT = 20;
    private static final int DEFAULT_WRITE_TIMEOUT = 20;
    private static final int DEFAULT_READ_TIMEOUT = 20;
    private Retrofit retrofit;
    private HttpApi httpApi;

    /**
     * 请求失败重连次数
     */
    private int RETRY_COUNT = 0;
    private OkHttpClient.Builder okHttpBuilder;
    public final OkHttpClient okHttpClient;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        okHttpBuilder = new OkHttpClient.Builder();
        /**
         * 设置缓存
         */
        if (!URLConstant.isDebug) {
            File cacheFile = new File(MyApplication.appContext.getExternalCacheDir(), CACHE_NAME);
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            Interceptor cacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (!NetUtil.isNetworkConnected()) {
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();
                    }
                    Response response = chain.proceed(request);
                    if (!NetUtil.isNetworkConnected()) {
                        int maxAge = 0;
                        // 有网络时 设置缓存超时时间0个小时
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader(CACHE_NAME)// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                .build();
                    } else {
                        // 无网络时，设置超时为4周
                        int maxStale = 60 * 60 * 24 * 28;
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .removeHeader(CACHE_NAME)
                                .build();
                    }
                    return response;
                }
            };
            okHttpBuilder.cache(cache).addInterceptor(cacheInterceptor);
        }


        /**
         * 设置头信息
         */
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder();
//                        .addHeader("Accept-Encoding", "gzip")
//                        .addHeader("Accept", "application/json")
//                        .addHeader("Content-Type", "application/json; charset=utf-8")
//                        .method(originalRequest.method(), originalRequest.body());

                //添加请求头信息，服务器进行token有效性验证
//                String token = (String) SPUtils.get(MyApplication.appContext, Constant.TOKEN_KEY, "");
//                requestBuilder.addHeader("isApp", "true");
//                if (!TextUtils.isEmpty(token))
//                    requestBuilder.addHeader("token", token);


                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        okHttpBuilder.addInterceptor(headerInterceptor);


        /**
         * 设置超时和重新连接
         */
//        if (!URLConstant.isDebug) okHttpBuilder.proxy(Proxy.NO_PROXY);//防止抓包
        okHttpBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        //错误重连
        okHttpBuilder.retryOnConnectionFailure(true);

        //不加以下两行代码,https请求不到自签名的服务器 (添加https请求 信任所有证书)
        okHttpBuilder.sslSocketFactory(createSSLSocketFactory());//创建一个证书对象
        //校验名称,这个对象就是信任所有的主机,也就是信任所有https的请求
        okHttpBuilder.hostnameVerifier(new TrustAllHostnameVerifier());

        okHttpClient = okHttpBuilder.build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())//json转换成JavaBean
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        httpApi = retrofit.create(HttpApi.class);


    }


    private static HttpMethods INSTANCE = new HttpMethods();

    //获取单例
    public static HttpMethods getInstance() {
        return INSTANCE;
    }

    /**
     * 获取retrofit
     *
     * @return
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void changeBaseUrl(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        httpApi = retrofit.create(HttpApi.class);
    }

    /**
     * 获取httpService
     *
     * @return
     */
    public HttpApi getHttpApi() {
        return httpApi;
    }

    /**
     * 设置订阅 和 所在的线程环境
     */
    public <T> void toSubscribe(Observable<T> o, DisposableObserver<T> s) {

        try {
            o.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retry(RETRY_COUNT)//请求失败重连次数
                    .subscribe(s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }


    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    //信任所有的服务器,返回true
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}