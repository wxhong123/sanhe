package com.response.AutheInfoBeans;

/**
 * UserInfo
 *
 *     "userId":"b000000120123123123123123ec892ba",
 *     "orgId":"120000000000",
 *     "sfzh":"410184197910211212",
 *     "jh":"999999",
 *     "xm":"韩秀德",
 *     "exten":""
 *
 *  Created by xh.w on 2020.10.20
 */
public class UserInfo {

    private String userId;
    private String orgId;
    private String sfzh;
    private String jh;
    private String xm;
    private String exten;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getJh() {
        return jh;
    }

    public void setJh(String jh) {
        this.jh = jh;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getExten() {
        return exten;
    }

    public void setExten(String exten) {
        this.exten = exten;
    }
}
