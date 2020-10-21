package com.acc.common.Provider;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.acc.common.AppConfig;

/**
 * 获取凭证票据,
 * call方式调用，返回值为Bundle对象
 * Created by xh.w on 2020.10.20
 */
public class MyContentResolver {

    public static Bundle call(Context context, String methodName) {

        Uri uri = Uri.parse(AppConfig.URI);
        ContentResolver resolver = context.getContentResolver();
        Bundle params = new Bundle();//构建参数
        params.putString("messageId", AppConfig.MESSAGE_ID);//消息ID
        params.putString("version", AppConfig.VERSION);//接口版本号，当前为1
        params.putString("appId", AppConfig.APP_ID);//应用标识【应用注册时，由发布系统提供。（应用创建后，登录开发者中心可以查看）】，非空
        params.putString("orgId", AppConfig.ORG_ID);//归属机构标识【河北省标识代码固定值130000000000】，非空
        params.putString("networkAreaCode", AppConfig.NETWORK_AREA_CODE);//网络区域类型【对接的二类应用传2，三类应用传3】，非空
        params.putString("packageName ", AppConfig.PACKAGE_NAME);//应用包名，可空

        return resolver.call(uri, methodName, null, params);//call方式调用，返回值为Bundle对象
    }
}
