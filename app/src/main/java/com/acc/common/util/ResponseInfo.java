package com.acc.common.util;

import java.io.Serializable;
import java.util.List;

/**
 * 网络请求返回对象
 * 作者：徐宏明 on 2018/5/15.
 * 邮箱：294985925@qq.com
 */
public class ResponseInfo<T> implements Serializable {
    //请求返回状态码
    private String statusCode;
    //请求返回描述
    private String description;
    //返回的请求bean
    private List<T> data;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
