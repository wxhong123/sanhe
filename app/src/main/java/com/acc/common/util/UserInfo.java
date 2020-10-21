package com.acc.common.util;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 用户信息。
 * 作者：徐宏明 on 2018/5/15.
 * 邮箱：294985925@qq.com
 */
public class UserInfo implements Serializable {
    private static UserInfo INSTANCE = null;//使用单例模式
    private String userId;//用户ID

    private UserInfo(String userId) {
        this.userId = userId;
    }

    public static synchronized UserInfo getInstance() {
        checkNotNull(INSTANCE, "UserInfo is not init.");
        return INSTANCE;
    }

    public static synchronized void init(String userId) {
        INSTANCE = new UserInfo(userId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
