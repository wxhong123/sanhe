package com.response.AutheInfoBeans;

/**
 * "head":{
 *
 *     "version":"1.0",
 *     "credType":"1",
 *     "token":{
 *         "tokenId":"a000000120123123123123123ec892ba",
 *         "orgId":"120000000000",
 *         "exten":""
 *     },
 *     "duration":{
 *         "startTime":"2012312312231",
 *         "endTime":"2012312312290"
 *     }
 *
 * },
 *
 * Created by xh.w on 2020.10.20
 */
public class Head {
    private String version;
    private String credType;
    private Token token;
    private Duration duration;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCredType() {
        return credType;
    }

    public void setCredType(String credType) {
        this.credType = credType;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
