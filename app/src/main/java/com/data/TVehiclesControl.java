package com.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * 廊坊本地监控车辆数据
 */
@DatabaseTable(tableName = "T_VEHICLES_CONTROL")
public class TVehiclesControl implements Serializable {

    private static final long serialVersionUID = 1L;
    @DatabaseField(columnName = "CPH")
    private String cph;        // 车牌号
    @DatabaseField(columnName = "FDJH")
    private String fdjh;        // 发动机号
    @DatabaseField(columnName = "CJH")
    private String cjh;        // 车架号
    @DatabaseField(columnName = "HPZL")
    private String hpzl;        // 号牌种类
    @DatabaseField(columnName = "CX")
    private String cx;        // 车型
    @DatabaseField(columnName = "CSYS")
    private String csys;        // 车身颜色
    @DatabaseField(columnName = "CJ")
    private String cj;        // 厂家
    @DatabaseField(columnName = "XH")
    private String xh;        // 型号
    @DatabaseField(columnName = "CZXM")
    private String czxm;        // 车主姓名
    @DatabaseField(columnName = "CZSFZ")
    private String czsfz;        // 车主身份证
    @DatabaseField(columnName = "CZLXDH")
    private String czlxdh;        // 车主联系电话
    @DatabaseField(columnName = "CZDZ")
    private String czdz;        // 车主地址
    @DatabaseField(columnName = "HPYS")
    private String hpys;        // 牌号颜色
    @DatabaseField(columnName = "CGCCDLSJ")
    private String cgccdlsj;        // 车管初次登录时间
    @DatabaseField(columnName = "SFBX")
    private String sfbx;        // 是否保险   下拉   否0 是1
    @DatabaseField(columnName = "BXQK")
    private String bxqk;        // 保险情况
    @DatabaseField(columnName = "BKDW")
    private String bkdw;        // 布控单位
    @DatabaseField(columnName = "LRMJ")
    private String lrmj;        // 录入民警
    @DatabaseField(columnName = "LRMJLXDH")
    private String lrmjlxdh;        // 录入民警 联系电话
    @DatabaseField(columnName = "SQRQ")
    private String sqrq;        // 申请开始日期
    @DatabaseField(columnName = "BKYXJB")
    private String bkyxjb;        // 布控优先级别   分为三级
    @DatabaseField(columnName = "BJMJ")
    private String bjmj;        // 报警民警
    @DatabaseField(columnName = "BKSY")
    private String bksy;        // 布控事由
    @DatabaseField(id = true,columnName = "BKID")
    private Integer bkid;        // 车辆布控id
    @DatabaseField(columnName = "SPNR")
    private String spnr;        // 审批信息
    @DatabaseField(columnName = "SPZT")
    private String spzt;        // 审批状态
    @DatabaseField(columnName = "SPMJ")
    private String spmj;        // 审批民警
    @DatabaseField(columnName = "SPRQ")
    private String sprq;        // 审批日期
    @DatabaseField(columnName = "SQJSFZH")
    private String sqjsfzh;        // 申请民警身份证号
    @DatabaseField(columnName = "BKDQ")
    private String bkdq;        // 布控结束日期
    @DatabaseField(columnName = "SPRSFZH")
    private String sprsfzh;        // 审批民警身份证号
    @DatabaseField(columnName = "STATUS")
    private Long status;        // 删除状态
    @DatabaseField(columnName = "SPR")
    private String spr;        // 审批人
    @DatabaseField(columnName = "SPYJ")
    private String spyj;        // 审批意见
    @DatabaseField(columnName = "CZFS")
    private String czfs;        // 处置方式
    @DatabaseField(columnName = "SPSJ")
    private String spsj;        // 审批时间
    @DatabaseField(columnName = "BKQY")
    private String bkqy;        // 布控区域


    public String getSpsj() {
        return spsj;
    }

    public void setSpsj(String spsj) {
        this.spsj = spsj;
    }

    public String getCph() {
        return cph;
    }

    public void setCph(String cph) {
        this.cph = cph;
    }


    public String getFdjh() {
        return fdjh;
    }

    public void setFdjh(String fdjh) {
        this.fdjh = fdjh;
    }


    public String getCjh() {
        return cjh;
    }

    public void setCjh(String cjh) {
        this.cjh = cjh;
    }


    public String getHpzl() {
        return hpzl;
    }

    public void setHpzl(String hpzl) {
        this.hpzl = hpzl;
    }


    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }


    public String getCsys() {
        return csys;
    }

    public void setCsys(String csys) {
        this.csys = csys;
    }


    public String getCj() {
        return cj;
    }

    public void setCj(String cj) {
        this.cj = cj;
    }


    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }


    public String getCzxm() {
        return czxm;
    }

    public void setCzxm(String czxm) {
        this.czxm = czxm;
    }


    public String getCzsfz() {
        return czsfz;
    }

    public void setCzsfz(String czsfz) {
        this.czsfz = czsfz;
    }


    public String getCzlxdh() {
        return czlxdh;
    }

    public void setCzlxdh(String czlxdh) {
        this.czlxdh = czlxdh;
    }


    public String getCzdz() {
        return czdz;
    }

    public void setCzdz(String czdz) {
        this.czdz = czdz;
    }


    public String getHpys() {
        return hpys;
    }

    public void setHpys(String hpys) {
        this.hpys = hpys;
    }


    public String getCgccdlsj() {
        return cgccdlsj;
    }

    public void setCgccdlsj(String cgccdlsj) {
        this.cgccdlsj = cgccdlsj;
    }


    public String getSfbx() {
        return sfbx;
    }

    public void setSfbx(String sfbx) {
        this.sfbx = sfbx;
    }


    public String getBxqk() {
        return bxqk;
    }

    public void setBxqk(String bxqk) {
        this.bxqk = bxqk;
    }


    public String getBkdw() {
        return bkdw;
    }

    public void setBkdw(String bkdw) {
        this.bkdw = bkdw;
    }


    public String getLrmj() {
        return lrmj;
    }

    public void setLrmj(String lrmj) {
        this.lrmj = lrmj;
    }


    public String getLrmjlxdh() {
        return lrmjlxdh;
    }

    public void setLrmjlxdh(String lrmjlxdh) {
        this.lrmjlxdh = lrmjlxdh;
    }


    public String getSqrq() {
        return sqrq;
    }

    public void setSqrq(String sqrq) {
        this.sqrq = sqrq;
    }

    public String getBkyxjb() {
        return bkyxjb;
    }

    public void setBkyxjb(String bkyxjb) {
        this.bkyxjb = bkyxjb;
    }


    public String getBjmj() {
        return bjmj;
    }

    public void setBjmj(String bjmj) {
        this.bjmj = bjmj;
    }


    public String getBksy() {
        return bksy;
    }

    public void setBksy(String bksy) {
        this.bksy = bksy;
    }

    public Integer getBkid() {
        return bkid;
    }

    public void setBkid(Integer bkid) {
        this.bkid = bkid;
    }


    public String getSpnr() {
        return spnr;
    }

    public void setSpnr(String spnr) {
        this.spnr = spnr;
    }


    public String getSpzt() {
        return spzt;
    }

    public void setSpzt(String spzt) {
        this.spzt = spzt;
    }


    public String getSpmj() {
        return spmj;
    }

    public void setSpmj(String spmj) {
        this.spmj = spmj;
    }


    public String getSprq() {
        return sprq;
    }

    public void setSprq(String sprq) {
        this.sprq = sprq;
    }


    public String getSqjsfzh() {
        return sqjsfzh;
    }

    public void setSqjsfzh(String sqjsfzh) {
        this.sqjsfzh = sqjsfzh;
    }


    public String getBkdq() {
        return bkdq;
    }

    public void setBkdq(String bkdq) {
        this.bkdq = bkdq;
    }


    public String getSprsfzh() {
        return sprsfzh;
    }

    public void setSprsfzh(String sprsfzh) {
        this.sprsfzh = sprsfzh;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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


    public String getCzfs() {
        return czfs;
    }

    public void setCzfs(String czfs) {
        this.czfs = czfs;
    }



    public String getBkqy() {
        return bkqy;
    }

    public void setBkqy(String bkqy) {
        this.bkqy = bkqy;
    }
}
