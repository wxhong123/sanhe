package com.response.AutheInfoBeans;

/**
 *     "appInfo":{
 *         "appId":"3532b4567eef468f9238a81db2cff0ea",
 *         "orgId":"120000000000",
 *         "packageName":"com.xdja.drs",
 *         "networkAreaCode":"1",
 *         "name":"安全客户端",
 *         "csType":"1",
 *         "exten":""
 *     }
 *
 *     Created by xh.w on 2020.10.20
 */
public class AppInfo {

    private String appId;
    private String orgId;
    private String packageName;
    private String networkAreaCode;
    private String name;
    private String csType;
    private String exten;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getNetworkAreaCode() {
        return networkAreaCode;
    }

    public void setNetworkAreaCode(String networkAreaCode) {
        this.networkAreaCode = networkAreaCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCsType() {
        return csType;
    }

    public void setCsType(String csType) {
        this.csType = csType;
    }

    public String getExten() {
        return exten;
    }

    public void setExten(String exten) {
        this.exten = exten;
    }
}
