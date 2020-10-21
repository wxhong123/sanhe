package com.response;

import com.acc.common.util.annotation.DictField;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by androider on 2018/7/31.
 * 内容：
 */
public class CheckListResonse implements Serializable {


    /**
     * code : 000
     * data : {"checkCarNumber":0,"data":[{"id":"2c43ede00faf45848b04cdde305f08bc","isNewRecord":false,"sfzh":"420621198602240618","checkObject":"person","zbxxNumber":6,"checkTime":"2018-07-30 15:47:52","policeIdcard":"410725196009254269","policeName":"韩怀国","deptId":"110101000000"},{"id":"6348ffc6366b4258ac92dd3f700fd623","isNewRecord":false,"sfzh":"420621198602240618","checkObject":"person","zbxxNumber":6,"checkTime":"2018-07-30 15:29:58","policeIdcard":"410725196009254269","policeName":"韩怀国","deptId":"110101000000"},{"id":"9bb811d8b16a4154b474c6a8d442cb37","isNewRecord":false,"sfzh":"420621198602240618","checkObject":"person","zbxxNumber":3,"checkTime":"2018-07-26 10:31:10","policeIdcard":"410725196009254269","policeName":"韩怀国","deptId":"131082106000"}],"checkPersonNumber":3}
     * success : true
     */

    private String code;
    private DataBeanX data;
    private boolean success;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBeanX {
        /**
         * checkCarNumber : 0
         * data : [{"id":"2c43ede00faf45848b04cdde305f08bc","isNewRecord":false,"sfzh":"420621198602240618","checkObject":"person","zbxxNumber":6,"checkTime":"2018-07-30 15:47:52","policeIdcard":"410725196009254269","policeName":"韩怀国","deptId":"110101000000"},{"id":"6348ffc6366b4258ac92dd3f700fd623","isNewRecord":false,"sfzh":"420621198602240618","checkObject":"person","zbxxNumber":6,"checkTime":"2018-07-30 15:29:58","policeIdcard":"410725196009254269","policeName":"韩怀国","deptId":"110101000000"},{"id":"9bb811d8b16a4154b474c6a8d442cb37","isNewRecord":false,"sfzh":"420621198602240618","checkObject":"person","zbxxNumber":3,"checkTime":"2018-07-26 10:31:10","policeIdcard":"410725196009254269","policeName":"韩怀国","deptId":"131082106000"}]
         * checkPersonNumber : 3
         */

        private int checkCarNumber;
        private int checkPersonNumber;
        private List<DataBean> data;

        public int getCheckCarNumber() {
            return checkCarNumber;
        }

        public void setCheckCarNumber(int checkCarNumber) {
            this.checkCarNumber = checkCarNumber;
        }

        public int getCheckPersonNumber() {
            return checkPersonNumber;
        }

        public void setCheckPersonNumber(int checkPersonNumber) {
            this.checkPersonNumber = checkPersonNumber;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 2c43ede00faf45848b04cdde305f08bc
             * isNewRecord : false
             * sfzh : 420621198602240618
             * checkObject : person
             * zbxxNumber : 6
             * checkTime : 2018-07-30 15:47:52
             * policeIdcard : 410725196009254269
             * policeName : 韩怀国
             * deptId : 110101000000
             */

            //        -------人员信息------------
            private String xm;
            private String sfzh;
            @DictField(dictType = "sex")
            private String xb;
            @DictField(dictType = "mz")
            private String mz;
            private String csrq;
            private String hjdz;
            private String fzpcs;
            private String yxq;
            private String rylb;

            public String getFdjh() {
                return fdjh;
            }

            public void setFdjh(String fdjh) {
                this.fdjh = fdjh;
            }

            private String fdjh;
            private String zp;
            @DictField(dictType = "check_person_sfzh")
            private String sjly;

            //                 -------车辆信息------------
            @DictField(dictType = "car_type")
            private String hpzl;
            private String cphm;
            private String clys;
            private String clpp;
            private String cldjdz;
            private String czsfzh;
            private String czxm;
            private String czlxfs;
            private String czxxdz;

            //                  -----------------主表信息-----------------------

            private String checkObject;
            private String zbxxNumber;
            private String checkTime;
            private String id;
            private boolean isNewRecord;
            private String policeIdcard;
            private String policeName;
            private String deptId;
            @DatabaseField(columnName = "checkIdcardMode")
            private String checkIdcardMode;
            @DatabaseField(columnName = "checkNetworkStatus")
            private String checkNetworkStatus;


            public String getCheckNetworkStatus() {
                return checkNetworkStatus;
            }

            public void setCheckNetworkStatus(String checkNetworkStatus) {
                this.checkNetworkStatus = checkNetworkStatus;
            }

            public String getCheckIdcardMode() {
                return checkIdcardMode;
            }

            public void setCheckIdcardMode(String checkIdcardMode) {
                this.checkIdcardMode = checkIdcardMode;
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

            public boolean isNewRecord() {
                return isNewRecord;
            }

            public void setNewRecord(boolean newRecord) {
                isNewRecord = newRecord;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public String getSfzh() {
                return sfzh;
            }

            public void setSfzh(String sfzh) {
                this.sfzh = sfzh;
            }

            public String getCheckObject() {
                return checkObject;
            }

            public void setCheckObject(String checkObject) {
                this.checkObject = checkObject;
            }

            public String getZbxxNumber() {
                return zbxxNumber;
            }

            public void setZbxxNumber(String zbxxNumber) {
                this.zbxxNumber = zbxxNumber;
            }

            public String getCheckTime() {
                return checkTime;
            }

            public void setCheckTime(String checkTime) {
                this.checkTime = checkTime;
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

            public String getDeptId() {
                return deptId;
            }

            public void setDeptId(String deptId) {
                this.deptId = deptId;
            }
        }
    }
}
