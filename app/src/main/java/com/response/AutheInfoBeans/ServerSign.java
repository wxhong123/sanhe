package com.response.AutheInfoBeans;

/**
 * ServerSign
 *
 *     "alg":"SM3+SM2",
 *     "signature":"b000000120123123123123123ec892ba",
 *     "sn":"1000000000100211",
 *     "url":"http://20.3.1.166/UAS/Credential.do"
 *
 * Created by xh.w on 2020.10.20
 */
public class ServerSign {
    private String alg;
    private String signature;
    private String sn;

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;


}
