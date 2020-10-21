/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.response.db_beans;

import com.acc.common.util.annotation.DictField;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 人员核查信息Entity
 *
 * @author hanhgq
 * @version 2018-07-17
 */
@DatabaseTable(tableName = "CHECK_PERSON")
public class CheckPerson {
    @DatabaseField(id = true, columnName = "ID")
    private String id;
    @DatabaseField(columnName = "CHECK_ID")
    private String checkId;        // 核查单号
    @DatabaseField(columnName = "CHECK_TIME")
    private String checkTime;        // 核查时间
    @DatabaseField(columnName = "LXDH")
    private String lxdh;        // 联系电话
    @DatabaseField(columnName = "SFFS")
    private String sffs;        // 是否发热
    @DatabaseField(columnName = "XM")
    private String xm;        // 姓名
    @DatabaseField(columnName = "XB")
    @DictField(dictType = "sex")
    private String xb;        // 性别
    @DatabaseField(columnName = "MZ")
    @DictField(dictType = "mz")
    private String mz;        // 民族
    @DatabaseField(columnName = "CSRQ")
    private String csrq;        // 出生日期
    @DatabaseField(columnName = "SFZH")
    private String sfzh;        // 身份证号
    @DatabaseField(columnName = "HJDZ")
    private String hjdz;        // 户籍地址
    @DatabaseField(columnName = "FZPCS")
    private String fzpcs;        // 发证派出所
    @DatabaseField(columnName = "YXQ")
    private String yxq;        // 身份证有效期
    @DatabaseField(columnName = "RYLB")
    private String rylb;        // 人员类别
    @DatabaseField(columnName = "ZP")
    private String zp;        // 人员照片
    @DatabaseField(columnName = "SJLY")
    @DictField(dictType = "check_person_sfzh")
    private String sjly;        // 数据来源

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getSffs() {
        return sffs;
    }

    public void setSffs(String sffs) {
        this.sffs = sffs;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
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

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getHjdz() {
        return hjdz;
    }

    public void setHjdz(String hjdz) {
        this.hjdz = hjdz;
    }

    public String getFzpcs() {
        return fzpcs;
    }

    public void setFzpcs(String fzpcs) {
        this.fzpcs = fzpcs;
    }

    public String getYxq() {
        return yxq;
    }

    public void setYxq(String yxq) {
        this.yxq = yxq;
    }

    public String getRylb() {
        return rylb;
    }

    public void setRylb(String rylb) {
        this.rylb = rylb;
    }

    public String getZp() {
        return zp;
    }

    public void setZp(String zp) {
        this.zp = zp;
    }

    public String getSjly() {
        return sjly;
    }

    public void setSjly(String sjly) {
        this.sjly = sjly;
    }

}