package com.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 核查人员信息
 * 作者：徐宏明 on 2018/5/22.
 * 邮箱：294985925@qq.com
 */
@DatabaseTable(tableName = "SOURCE_JS_PERSON")
public class Person implements Serializable {
    @DatabaseField(id = true,columnName = "ID")
    private String id;//ID唯一标识
    @DatabaseField(columnName = "SFHM")
    private String sfhm;//身份证
    @DatabaseField(columnName = "XM")
    private String xm;//姓名
    @DatabaseField(columnName = "XB")
    private String xb;//性别
    @DatabaseField(columnName = "MZ")
    private String mz;//民族
    @DatabaseField(columnName = "CSRQ")
    private String csrq;//出生日期
    @DatabaseField(columnName = "SFZZZ")
    private String sfzzz;//身份证住址
    @DatabaseField(columnName = "QFJG")
    private String qfjg;//签发机关
    @DatabaseField(columnName = "RYLB")
    private String rylb;//人员类别
    @DatabaseField(columnName = "CLFS")
    private String clfs;//处置方式
    @DatabaseField(columnName = "AQMS")
    private String aqms;//案情描述
    @DatabaseField(columnName = "BKLXR")
    private String bklxr;//布控联系人
    @DatabaseField(columnName = "RWMC")
    private String rwmc;//任务名称
    @DatabaseField(columnName = "BKKSSJ")
    private String bkkssj;//布控开始时间
    @DatabaseField(columnName = "BKJSSJ")
    private String bkjssj;//布控结束时间
    @DatabaseField(columnName = "BKPC")
    private String bkpc;//布控批次
    @DatabaseField(columnName = "CREATE_BY")
    private String createBy;
    @DatabaseField(columnName ="UPDATE_BY")
    private String updateBy;
    @DatabaseField(columnName ="DEL_FLAG")
    private String defFlag;
    @DatabaseField(columnName ="REMARKS")
    private String remarks;
    @DatabaseField(columnName ="CREATE_DATE")
    private String createDate;//创建时间
    @DatabaseField(columnName = "UPDATE_DATE")
    private String updateDate;
    @DatabaseField(columnName = "BKLXFS")
    private String bklxfs;//布控联系方式

    public String getBklxfs() {
        return bklxfs;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }


    public void setBklxfs(String bklxfs) {
        this.bklxfs = bklxfs;
    }


    public String getBkkssj() {
        return bkkssj;
    }

    public void setBkkssj(String bkkssj) {
        this.bkkssj = bkkssj;
    }

    public String getBkjssj() {
        return bkjssj;
    }

    public void setBkjssj(String bkjssj) {
        this.bkjssj = bkjssj;
    }

    public String getBkpc() {
        return bkpc;
    }

    public void setBkpc(String bkpc) {
        this.bkpc = bkpc;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getDefFlag() {
        return defFlag;
    }

    public void setDefFlag(String defFlag) {
        this.defFlag = defFlag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getSfhm() {
        return sfhm;
    }

    public void setSfhm(String sfhm) {
        this.sfhm = sfhm;
    }

    public String getQfjg() {
        return qfjg;
    }

    public void setQfjg(String qfjg) {
        this.qfjg = qfjg;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getSfzzz() {
        return sfzzz;
    }

    public void setSfzzz(String sfzzz) {
        this.sfzzz = sfzzz;
    }

    public String getRylb() {
        return rylb;
    }

    public void setRylb(String rylb) {
        this.rylb = rylb;
    }

    public String getClfs() {
        return clfs;
    }

    public void setClfs(String clfs) {
        this.clfs = clfs;
    }

    public String getAqms() {
        return aqms;
    }

    public void setAqms(String aqms) {
        this.aqms = aqms;
    }

    public String getBklxr() {
        return bklxr;
    }

    public void setBklxr(String bklxr) {
        this.bklxr = bklxr;
    }

    public String getRwmc() {
        return rwmc;
    }

    public void setRwmc(String rwmc) {
        this.rwmc = rwmc;
    }
}
