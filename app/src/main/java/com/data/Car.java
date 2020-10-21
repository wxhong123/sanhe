package com.data;

import android.arch.persistence.room.ColumnInfo;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者：徐宏明 on 2018/5/22.
 * 邮箱：294985925@qq.com
 */
@DatabaseTable(tableName = "SOURCE_JS_CAR")
public class Car implements Serializable {
    @DatabaseField(id = true, columnName = "ID")
    private String id;//ID唯一标识
    @DatabaseField(columnName = "CPHM")
    private String cphm;//车牌号码
    @DatabaseField(columnName = "HPZL")
    private String hpzl;//车牌种类

    @DatabaseField(columnName = "FDJH")
    private String fdjh;//发动机号
    @DatabaseField(columnName = "CLLB")
    private String cllb;//车辆类别
    @DatabaseField(columnName = "CLFS")
    private String clfs;//处置方式
    @DatabaseField(columnName = "AQMS")
    private String aqms;//案情描述
    @DatabaseField(columnName = "BKLXR")
    private String bklxr;//布控联系人
    @DatabaseField(columnName = "BKLXFS")
    private String bklxfs;//布控人员联系方式
    @DatabaseField(columnName = "RWMC")
    private String rwmc;//任务名称
    @ColumnInfo(name = "CREATE_DATE")
    @DatabaseField(columnName = "CREATE_DATE")
    private String createDate;//创建时间
    @DatabaseField(columnName = "BKKSSJ")
    private String bkkssj;//布控开始时间
    @DatabaseField(columnName = "BKJSSJ")
    private String bkjssj;//布控结束时间
    @DatabaseField(columnName = "BKPC")
    private String bkpc;//布控批次
    @DatabaseField(columnName = "CREATE_BY")
    private String createBy;
    @DatabaseField(columnName = "UPDATE_BY")
    private String updateBy;
    @DatabaseField(columnName = "UPDATE_DATE")
    private String updateDate;
    @DatabaseField(columnName = "DEL_FLAG")
    private String defFlag;
    @DatabaseField(columnName = "REMARKS")
    private String remarks;
    @DatabaseField(columnName = "CLSDBM")
    private String clsdbm;


    public String getClsdbm() {
        return clsdbm;
    }

    public void setClsdbm(String clsdbm) {
        this.clsdbm = clsdbm;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public String getDefFlag() {
        return defFlag;
    }

    public void setDefFlag(String defFlag) {
        this.defFlag = defFlag;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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


    public String getFdjh() {
        return fdjh;
    }

    public void setFdjh(String fdjh) {
        this.fdjh = fdjh;
    }

    public String getCllb() {
        return cllb;
    }

    public void setCllb(String cllb) {
        this.cllb = cllb;
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

    public String getBklxfs() {
        return bklxfs;
    }

    public void setBklxfs(String bklxfs) {
        this.bklxfs = bklxfs;
    }

    public String getRwmc() {
        return rwmc;
    }

    public void setRwmc(String rwmc) {
        this.rwmc = rwmc;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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


}
