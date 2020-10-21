package com.acc.common;

/**
 * Created by androider on 2018/7/18.
 * 内容：
 */
public class URL {
    /**
     * 三河公安网地址
     */
//    public static final String DefUrl = "http://192.168.11.10:10010";
    /**
     * 护城河地址
     */
    public static final String DefUrl = "http://192.168.0.10:7070";
    /**
     * 公司外网
     */
    //public static final String DefUrl = "http://vr.accellence.cn:9100";
    /**
     * 互联网地址
     */
    //public static final String DefUrl = "http://39.104.166.235:8080";
    /**
     * 北京卡连公安网
     */
    //public static final String DefUrl = "http://172.16.10.10:14104";
    /**
     * 外网
     */
    //public static final String DefUrl = "http://192.168.43.171:8081";
    public static String host;
    public static String RootUrl;
    //用户在线登录接口
    public static final String USER_LOGIN = "interface/login";
    //核查人/车
    public static final String query = "checkinterface/startCheck";
    //核查记录列表
    public static final String collectlist = "checkinterface/queryRecord";
    //核查记录详情
    public static final String collectdetail = "checkinterface/queryRecordDetail";
    //数据更新接口
    public static final String SYNC_DATA_URL = "data/sync/queryChange";
    //获得布控数据最近版本
    public static final String SYNC_KUKONG_DATA_URL = "data/sync/queryLastChange";
    //异常上报接口
    public static final String UPLOAD_EXCEPTION_URL = "sys/syslog/uploadException";
    //离线数据包下载接口
    public static final String SYNC_OFFLINE_DATA_URL = "source/package/queryNewPackage";
    //离线数据上传
    public static final String UPLOADOFFLINE = "checkinterface/upload";
    //app版本查看
    public static final String UPDATEAPP = "app/info/queryNewApp";
    //确定查询
    public static final String Confirm = "checkinterface/confirmCheck";
    //使用身份证登录
    public static final String LoginBySFZH = "interface/loginByCardNumber";
}
