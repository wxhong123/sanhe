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
 * 车辆信息Entity
 *
 * @author hanhg
 * @version 2018-07-17
 */

@DatabaseTable(tableName = "CHECK_CAR")
public class CheckCar implements Serializable {

    @DatabaseField(id = true, columnName = "ID")
    private String id;
    @DatabaseField(columnName = "CHECK_ID")
    private String checkId;        // 核查单号
    @DatabaseField(columnName = "CHECK_TIME")
    private Date checkTime;        // 核查时间
    @DatabaseField(columnName = "HPZL")
    @DictField(dictType = "car_type")
    private String hpzl;        // 号牌类型
    @DatabaseField(columnName = "CPHM")
    private String cphm;        // 号牌号码
    @DatabaseField(columnName = "CLYS")
    @DictField(dictType = "car_color")
    private String clys;        // 车辆颜色
    @DatabaseField(columnName = "CLPP")
    private String clpp;        // 车辆品牌
    @DatabaseField(columnName = "CLDJDZ")
    private String cldjdz;        // 车辆登录地址
    @DatabaseField(columnName = "CZSFZH")
    private String czsfzh;        // 车主身份证号
    @DatabaseField(columnName = "CZXM")
    private String czxm;        // 车主姓名
    @DatabaseField(columnName = "CZLXFS")
    private String czlxfs;        // 车主联系方式
    @DatabaseField(columnName = "CZXXDZ")
    private String czxxdz;        // 车主详细地址
    @DatabaseField(columnName = "FDJH")
    private String fdjh;        // 发动机号

    private boolean needTransform;

    public boolean isNeedTransform() {
        return needTransform;
    }

    public void setNeedTransform(boolean needTransform) {
        this.needTransform = needTransform;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getHpzl() {
        return hpzl;
    }

    public void setHpzl(String hpzl) {
        this.hpzl = hpzl;
    }

    public String getCphm() {
        return cphm;
    }

    public void setCphm(String cphm) {
        this.cphm = cphm;
    }

    public String getClys() {
        return clys;
    }

    public void setClys(String clys) {
        this.clys = clys;
    }

    public String getClpp() {
        return clpp;
    }

    public void setClpp(String clpp) {
        this.clpp = clpp;
    }

    public String getCldjdz() {
        return cldjdz;
    }

    public void setCldjdz(String cldjdz) {
        this.cldjdz = cldjdz;
    }

    public String getCzsfzh() {
        return czsfzh;
    }

    public void setCzsfzh(String czsfzh) {
        this.czsfzh = czsfzh;
    }

    public String getCzxm() {
        return czxm;
    }

    public void setCzxm(String czxm) {
        this.czxm = czxm;
    }

    public String getCzlxfs() {
        return czlxfs;
    }

    public void setCzlxfs(String czlxfs) {
        this.czlxfs = czlxfs;
    }

    public String getCzxxdz() {
        return czxxdz;
    }

    public void setCzxxdz(String czxxdz) {
        this.czxxdz = czxxdz;
    }

    public String getFdjh() {
        return fdjh;
    }

    public void setFdjh(String fdjh) {
        this.fdjh = fdjh;
    }

}