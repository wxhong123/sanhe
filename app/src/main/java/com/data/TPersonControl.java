package com.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;


/**
 * 廊坊本地监控人员数据
 */
@DatabaseTable(tableName = "T_PERSON_CONTROL")
public class TPersonControl implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true,columnName = "ID")
    private int id;        //编号
    @DatabaseField(columnName = "SFZ")
    private String sfz;		// 身份证
    @DatabaseField(columnName = "XM")
    private String xm;		// 姓名
    @DatabaseField(columnName = "XB")
    private String xb;		// 性别
    @DatabaseField(columnName = "BKMS")
    private String bkms;		// 布控描述
    @DatabaseField(columnName = "CZFS")
    private String czfs;		// 处置方式
    @DatabaseField(columnName = "LXDH")
    private String lxdh;		// 联系电话
    @DatabaseField(columnName = "KSSJ")
    private String kssj;		// 开始时间
    @DatabaseField(columnName = "JSSJ")
    private String jssj;		// 结束时间
    @DatabaseField(columnName = "USERID")
    private String userid;		// 申请人ID
    @DatabaseField(columnName = "USERNAME")
    private String username;		// 申请人
    @DatabaseField(columnName = "SPR")
    private String spr;		// 审批人
    @DatabaseField(columnName = "SPSJ")
    private String spsj;		// 审批时间
    @DatabaseField(columnName = "SPYJ")
    private String spyj;		// 审批意见
    @DatabaseField(columnName = "SPZT")
    private int spzt;		// 审批状态(0.未审批 1.审批通过 2.审批不通过)
    @DatabaseField(columnName = "BKRLX")
    private String bkrlx;		// 被控人类型
    @DatabaseField(columnName = "BKLXR")
    private String bklxr;		// 布控联系人
    @DatabaseField(columnName = "BKLXDH")
    private String bklxdh;		// 布控联系电话

    @DatabaseField(columnName = "BKQY")
    private String bkqy;		// 布控区域

    @DatabaseField(columnName = "TJSJ")
    private String tjsj;		// 提交时间

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public String getSpsj() {
        return spsj;
    }

    public void setSpsj(String spsj) {
        this.spsj = spsj;
    }


    public String getTjsj() {
        return tjsj;
    }

    public void setTjsj(String tjsj) {
        this.tjsj = tjsj;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz;
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


    public String getBkms() {
        return bkms;
    }

    public void setBkms(String bkms) {
        this.bkms = bkms;
    }


    public String getCzfs() {
        return czfs;
    }

    public void setCzfs(String czfs) {
        this.czfs = czfs;
    }


    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSpr() {
        return spr;
    }

    public void setSpr(String spr) {
        this.spr = spr;
    }





    public String getSpyj() {
        return spyj;
    }

    public void setSpyj(String spyj) {
        this.spyj = spyj;
    }

    public int getSpzt() {
        return spzt;
    }

    public void setSpzt(int spzt) {
        this.spzt = spzt;
    }


    public String getBkrlx() {
        return bkrlx;
    }

    public void setBkrlx(String bkrlx) {
        this.bkrlx = bkrlx;
    }


    public String getBklxr() {
        return bklxr;
    }

    public void setBklxr(String bklxr) {
        this.bklxr = bklxr;
    }


    public String getBklxdh() {
        return bklxdh;
    }

    public void setBklxdh(String bklxdh) {
        this.bklxdh = bklxdh;
    }



    public String getBkqy() {
        return bkqy;
    }

    public void setBkqy(String bkqy) {
        this.bkqy = bkqy;
    }

}
