package com.acc.common.util;

import com.response.CheckCarBean;
import com.response.CheckDetailResponse;
import com.response.CheckListResonse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 网络请求工具
 * 作者：徐宏明 on 2018/6/5.
 * 邮箱：294985925@qq.com
 */
public interface BimoClient {

    /**
     * 下载文件
     *
     * @param fileUrl 文件下载地址
     * @return 下载对象，包含一个流文件
     */
    @Streaming
    @GET
    Observable<ResponseBody> downFile(@Url String fileUrl);

    /**
     * 核查人员/车辆
     *
     * @param obj 查询条件
     * @return 核查结果
     */
    @GET("checkinterface/startCheck")
    Observable<CheckDetailResponse> checkPerson(@Query("jsonObj") String obj);

    /**
     * 核查车辆
     *
     * @param obj 核查条件
     * @return 核查结果
     */
    @GET("checkinterface/startCheck")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<CheckCarBean> checkCar(@Query("jsonObj") String obj);

    /**
     * 核查记录
     *
     * @param obj 核查条件
     * @return 核查结果
     */
    @GET("checkinterface/queryRecord")
    Observable<CheckListResonse> checkList(@Query("jsonObj") String obj);

    /**
     * 核查详情
     *
     * @param obj 核查条件
     * @return 核查结果
     */
    @GET("checkinterface/queryRecordDetail")
    Observable<CheckDetailResponse> checkDetail(@Query("jsonObj") String obj);
}
