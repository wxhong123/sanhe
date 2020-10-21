package com.acc.common.util;


import com.acc.sanheapp.BuildConfig;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * 网络请求服务
 * 作者：徐宏明 on 2017/12/6.
 * 邮箱：294985925@qq.com
 */

public final class ServiceGenerator {
    /**
     * 缓存builder对象
     */
    private static HashMap<String, Retrofit.Builder> builderCache = new HashMap<>();
    /**
     * 网络访问客户端
     */
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    /**
     * 打印网络请求和返回体内容。添加拦截之后，会将下载更新的进度拦截掉，
     * 导致下载APK无法观看进度，所以在发布的时候，需要关闭拦截
     */

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    /**
     * 创建网络请求服务
     *
     * @param serviceClass 服务请求类
     * @param <S>          服务请求实现代理类
     * @return 服务请求实现代理类对象
     */
    public synchronized static <S> S createService(Class<S> serviceClass, String host) {
        if (AccTool.nullOrEmpty(host))
            host = "http://192.168.0.1:9999";
        Retrofit.Builder builder;
        if (builderCache.containsKey(serviceClass.toString())) {
            builder = builderCache.get(host);
        } else {
            if (BuildConfig.DEBUG && !httpClient.interceptors().contains(logging))
                httpClient.addInterceptor(logging);
            builder = new Retrofit.Builder()
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(host)
                    .client(httpClient.build());
            builderCache.put(host, builder);
        }
        return builder.build().create(serviceClass);
    }
}
