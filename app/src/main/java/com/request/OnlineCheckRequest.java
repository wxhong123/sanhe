package com.request;

import java.io.Serializable;

/**
 * Created by androider on 2018/7/18.
 * 内容：
 */
public class OnlineCheckRequest implements Serializable {

    /**
     * username : anrong
     * password : anrong
     * deviceId : 00000100002
     * sfzh : 21060119840419541x,623433199303123301
     * cphm : 冀A15353
     * hpzl : 07
     * policeIdcard  : 调用者身份证号
     * policeName  : 调用者姓名
     * checkTime : 2018-07-17 14:01:33
     * latitude : 0
     * longitude : 0
     * “deptId” : ”4314331”
     * checkIdcardMode : ocr
     * checkNetworkStatus : online
     * checkObject : person
     */

    private String username;
    private String password;
    private String deviceId;
    private String sfzh;
    private String xm;
    private String cphm;
    private String hpzl;
    private String policeIdcard;
    private String policeName;
    private String checkTime;
    private double latitude;
    private double longitude;
    private String deptId;
    private String checkIdcardMode;
    private String checkNetworkStatus;
    private String checkObject;
    private String groupId;
    private String checkAddress;//核查地点
    private String checkTaskType;//核查任务类型

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getCheckAddress() {
        return checkAddress;
    }

    public void setCheckAddress(String checkAddress) {
        this.checkAddress = checkAddress;
    }

    public String getCheckTaskType() {
        return checkTaskType;
    }

    public void setCheckTaskType(String checkTaskType) {
        this.checkTaskType = checkTaskType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }


    public String getCphm() {
        return cphm;
    }

    public void setCphm(String cphm) {
        this.cphm = cphm;
    }

    public String getHpzl() {
        return hpzl;
    }

    public void setHpzl(String hpzl) {
        this.hpzl = hpzl;
    }

    public String getPoliceIdcard() {
        return policeIdcard;
    }

    public void setPoliceIdcard(String policeIdcard) {
        this.policeIdcard = policeIdcard;
    }

    public String getPoliceName() {
        return policeName;
    }

    public void setPoliceName(String policeName) {
        this.policeName = policeName;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCheckIdcardMode() {
        return checkIdcardMode;
    }

    public void setCheckIdcardMode(String checkIdcardMode) {
        this.checkIdcardMode = checkIdcardMode;
    }

    public String getCheckNetworkStatus() {
        return checkNetworkStatus;
    }

    public void setCheckNetworkStatus(String checkNetworkStatus) {
        this.checkNetworkStatus = checkNetworkStatus;
    }

    public String getCheckObject() {
        return checkObject;
    }

    public void setCheckObject(String checkObject) {
        this.checkObject = checkObject;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
