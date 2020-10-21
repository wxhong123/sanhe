package com.response.AutheInfoBeans;

/**
 * String messageId = bundle.getString("messageId");
 * String version = bundle.getString("version");
 * int resultCode = bundle.getInt("resultCode");
 * String message = bundle.getString("message");
 * String userCredential = bundle.getString("userCredential");
 * String appCredential = bundle.getString("appCredential");
 *
 * Created by xh.w on 2020.10.20
 */
public class AutheInfo {

    private String messageId;
    private String version;
    private int resultCode;
    private String message;
    private UserCredential userCredential;
    private AppCredential appCredential;


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserCredential getUserCredential() {
        return userCredential;
    }

    public void setUserCredential(UserCredential userCredential) {
        this.userCredential = userCredential;
    }

    public AppCredential getAppCredential() {
        return appCredential;
    }

    public void setAppCredential(AppCredential appCredential) {
        this.appCredential = appCredential;
    }
}
