/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.response.db_beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 人员警示信息Entity
 * @author hanhg
 * @version 2018-07-17
 */
@DatabaseTable(tableName = "CHECK_PERSON_JS")
public class CheckPersonJs {

	@DatabaseField(id = true, columnName = "ID")
	private String id;
	@DatabaseField(columnName = "CHECK_ID")
	private String checkId;		// 核查单号
	@DatabaseField(columnName = "CHECK_TIME")
	private String checkTime;		// 核查时间
	@DatabaseField(columnName = "SFZH")
	private String sfzh;		// 身份证号
	@DatabaseField(columnName = "RYLB")
	private String rylb;		// 中标人员类别
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

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
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

	public String getSjly() {
		return sjly;
	}

	public void setSjly(String sjly) {
		this.sjly = sjly;
	}
	
}