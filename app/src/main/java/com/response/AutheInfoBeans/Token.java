package com.response.AutheInfoBeans;

/**
 * Token
 *
 *     "tokenId":"a000000120123123123123123ec892ba",
 *     "orgId":"120000000000",
 *     "exten":""
 *
 *     Created by xh.w on 2020.10.20
 */
public class Token {

    private String tokenId;
    private String orgId;
    private String exten;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getExten() {
        return exten;
    }

    public void setExten(String exten) {
        this.exten = exten;
    }
}
