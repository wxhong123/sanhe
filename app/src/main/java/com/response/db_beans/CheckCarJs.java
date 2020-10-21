/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.response.db_beans;

import com.acc.common.util.annotation.DictField;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 车辆警示信息Entity
 * @author hanhg
 * @version 2018-07-17
 */
@DatabaseTable(tableName = "CHECK_CAR_JS")
public class CheckCarJs {
	@DatabaseField(id = true, columnName = "ID")
	private String id;
	@DatabaseField(columnName = "CHECK_ID")
	private String checkId;		// 核查单号
	@DatabaseField(columnName = "CHECK_TIME")
	private String checkTime;		// 核查时间
	@DatabaseField(columnName = "CPHM")
	private String cphm;		// 车牌号码
	@DatabaseField(columnName = "HPZL")
	@DictField(dictType = "car_type")
	private String hpzl;		// 车牌类别
	@DatabaseField(columnName = "CLLB")
	private String cllb;		// 中标车辆类别
	@DatabaseField(columnName = "CLFS")
	private String clfs;		// 中标处置方式
	@DatabaseField(columnName = "AQMS")
	private String aqms;		// 中标案情描述
	@DatabaseField(columnName = "BKLXR")
	private String bklxr;		// 中标联系人
	@DatabaseField(columnName = "BKLXFS")
	private String bklxfs;		// 中标联系人方式
	@DatabaseField(columnName = "RWMC")
	private String rwmc;		// 中标任务名称
	@DatabaseField(columnName = "RWID")
	private String rwid;		// 中标任务ID
	@DatabaseField(columnName = "PFIRSTID")
	private String pfirstid;		// 初始版本号
	@DatabaseField(columnName = "PVERID")
	private String pverid;		// 版本号
	@DatabaseField(columnName = "CZSFZH")
	private String czsfzh;		// 车主身份证号
	@DatabaseField(columnName = "CZXM")
	private String czxm;		// 车主姓名
	@DatabaseField(columnName = "CLSBDM")
	private String clsbdm;		// 车辆识别代码
	@DatabaseField(columnName = "CLYS")
	private String clys;		// 车辆颜色
	@DatabaseField(columnName = "FDJH")
	private String fdjh;		// 发动机号
	@DatabaseField(columnName = "SJLY")
	private String sjly;		// 数据来源

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

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
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

	public String getRwid() {
		return rwid;
	}

	public void setRwid(String rwid) {
		this.rwid = rwid;
	}

	public String getPfirstid() {
		return pfirstid;
	}

	public void setPfirstid(String pfirstid) {
		this.pfirstid = pfirstid;
	}

	public String getPverid() {
		return pverid;
	}

	public void setPverid(String pverid) {
		this.pverid = pverid;
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

	public String getClsbdm() {
		return clsbdm;
	}

	public void setClsbdm(String clsbdm) {
		this.clsbdm = clsbdm;
	}

	public String getClys() {
		return clys;
	}

	public void setClys(String clys) {
		this.clys = clys;
	}

	public String getFdjh() {
		return fdjh;
	}

	public void setFdjh(String fdjh) {
		this.fdjh = fdjh;
	}

	public String getSjly() {
		return sjly;
	}

	public void setSjly(String sjly) {
		this.sjly = sjly;
	}
	
}