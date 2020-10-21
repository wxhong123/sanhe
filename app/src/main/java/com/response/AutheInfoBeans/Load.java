package com.response.AutheInfoBeans;

/**
 * "load":{
 *
 *     "userInfo":{}
 *
 * },
 *
 * Created by xh.w on 2020.10.20
 */
public class Load {

    private UserInfo userInfo;
    private AppInfo appInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }
}
