package com.acc.common.util;


import java.io.Serializable;


/**
 * 网络请求对象
 * 作者：徐宏明 on 2018/5/15.
 * 邮箱：294985925@qq.com
 */
public class RequestInfo<T> implements Serializable {
    //设备信息
    private DeviceInfo deviceInfo;
    //应用信息
    private AppInfo appInfo;
    //用户信息
    private UserInfo userInfo;
    //bean对象
    private T data;

    public RequestInfo(T data) {
        this.deviceInfo = DeviceInfo.getInstance();
        this.appInfo = AppInfo.getInstance();
        this.userInfo = UserInfo.getInstance();
        this.data = data;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
