package com.acc.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 应用信息。
 * 作者：徐宏明 on 2018/5/15.
 * 邮箱：294985925@qq.com
 */
public class AppInfo implements Serializable {
    private static AppInfo INSTANCE = null;

    private AppInfo(Context context) {
        checkNotNull(context);
        PackageInfo pkg = null;
        try {
            pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            this.applicationId = pkg.packageName;
            this.versionCode = pkg.versionCode + "";
            this.versionName = pkg.versionName + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized AppInfo getInstance() {
        checkNotNull(INSTANCE, "AppInfo is not init.");
        return INSTANCE;
    }

    /**
     * 初始化AppInfo对象
     *
     * @param context 上下文
     */
    public static synchronized void init(Context context) {
        INSTANCE = new AppInfo(context);
    }

    private String applicationId;//应用名字
    private String versionName;//版本名字
    private String versionCode;//版本号

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}
