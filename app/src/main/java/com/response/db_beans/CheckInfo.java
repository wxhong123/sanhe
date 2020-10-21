/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.response.db_beans;

import com.acc.common.util.annotation.DictField;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * 核查信息采集Entity
 *
 * @author hanhg
 * @version 2018-07-17
 */
@DatabaseTable(tableName = "CHECK_INFO")
public class CheckInfo {


    @DatabaseField(columnName = "ID", id = true)
    private String id;
    @DatabaseField(columnName = "DEPT_ID")
    private String deptId;        // 归属机构
    @DatabaseField(columnName = "POLICE_IDCARD")
    private String policeIdcard;        // 警员身份证号
    @DatabaseField(columnName = "POLICE_NAME")
    private String policeName;        // 警员姓名
    @DatabaseField(columnName = "CHECK_TIME")
    private Date checkTime;        // 采集时间
    @DatabaseField(columnName = "LATITUDE")
    private Double latitude;        // 纬度
    @DatabaseField(columnName = "LONGITUDE")
    private Double longitude;        // 经度
    @DatabaseField(columnName = "GROUP_ID")
    private String groupId;        // 核查组ID
    @DatabaseField(columnName = "CHECK_IDCARD_MODE")
    private String checkIdcardMode;        // 身份证核查核查方式
    @DatabaseField(columnName = "CHECK_NETWORK_STATUS")
    private String checkNetworkStatus;        // 核查网络状态
    @DatabaseField(columnName = "CHECK_OBJECT")
    private String checkObject;        // 核查对象
    @DatabaseField(columnName = "DEVICE_ID")
    private String deviceId;        // 核查设备号
    @DatabaseField(columnName = "CREATE_BY")
    private String createBy;
    @DatabaseField(columnName = "CREATE_DATE")
    private Date createDate;
    @DatabaseField(columnName = "UPDATE_BY")
    private String updateBy;
    @DatabaseField(columnName = "UPDATE_DATE")
    private Date updateDate;
    @DatabaseField(columnName = "REMARKS")
    private Date remarks;
    @DatabaseField(columnName = "DEL_FLAG")
    private Date defFlag;
    @DatabaseField(columnName = "CHECK_ADDRESS")
    private String checkAddress;
    @DatabaseField(columnName = "CHECK_TASK_TYPE")
    @DictField(dictType = "check_task_type")
    private String checkTaskType;


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

    public Date getRemarks() {
        return remarks;
    }

    public void setRemarks(Date remarks) {
        this.remarks = remarks;
    }

    public Date getDefFlag() {
        return defFlag;
    }

    public void setDefFlag(Date defFlag) {
        this.defFlag = defFlag;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
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

    //@NotNull(message="采集时间不能为空")
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}