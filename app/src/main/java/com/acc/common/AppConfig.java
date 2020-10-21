package com.acc.common;

/**
 * 配置文件
 * 下面的配置信息需要根据根据统一平台提供的信息重新设置
 * Created by xh.w on 2020.10.20
 */
public class AppConfig {

    // 应用客户端获取凭证票据时，需要的参数
    public static final String URI = "content://com.ydjw.ua.getCredential";
    public static final String MESSAGE_ID = "MessageId";  //消息ID
    public static final String VERSION = "1"; //接口版本号，当前为1
    public static final String APP_ID = "appId"; //应用标识【应用注册时，由发布系统提供。（应用创建后，登录开发者中心可以查看）】，非空
    public static final String ORG_ID = "130000000000"; //归属机构标识【河北省标识代码固定值130000000000】，非空
    public static final String NETWORK_AREA_CODE = "2"; //网络区域类型【对接的二类应用传2，三类应用传3】，非空
    public static final String PACKAGE_NAME = "com.acc.sanheapp"; //应用包名，可空

    // 用户/应用凭证
    public static final String USER_CREDENTIAL = "UserCredential";
    public static final String APP_CREDENTIAL = "AppCredential";
    // 用户/应用Token
    public static final String APP_TOKEN = "appToken";
    public static final String USER_TOKEN = "userToken";
    public static final String JING_HAO = "jh";

    // 获取appId需要的参数
    public static final String APP_KEY = "ZDodJGX2";
    public static final String APP_VERSION = "ZDodJGX2";
    public static final String APP_ZONE = "ZDodJGX2";
    public static final String APP_TYPE = "ZDodJGX2";
    public static final String APP_SECRET = "ZDodJGX2";

}
