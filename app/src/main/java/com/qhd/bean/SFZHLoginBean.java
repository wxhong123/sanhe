package com.qhd.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by androider on 2018/9/4.
 * 内容：
 */
public class SFZHLoginBean implements Serializable {


    /**
     * status : 200
     * msg : 登录成功！
     * data : [{"org_name":"李旗庄派出所","policeorg":"131082101000","policesfzh":"132801196010181617","zzjgpcs":"131082101000","roleIdList":[],"policename":"郝毅男","policemanid":"000234","id":"132801196010181617","role_level":"","loginInfo":"loginSuccess","zzjgsj":"110101000000","user_is_effective":"1","roleSQL":" AND (a.id = '132801196010181617')","user_password":"493d116c5977e785aef2a0f70fae483eb70ac8740d0f17e0d9d13c2a","role_name":""}]
     * totalnumber : 1
     */

    private String status;
    private String msg;
    private int totalnumber;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotalnumber() {
        return totalnumber;
    }

    public void setTotalnumber(int totalnumber) {
        this.totalnumber = totalnumber;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * org_name : 李旗庄派出所
         * policeorg : 131082101000
         * policesfzh : 132801196010181617
         * zzjgpcs : 131082101000
         * roleIdList : []
         * policename : 郝毅男
         * policemanid : 000234
         * id : 132801196010181617
         * role_level :
         * loginInfo : loginSuccess
         * zzjgsj : 110101000000
         * user_is_effective : 1
         * roleSQL :  AND (a.id = '132801196010181617')
         * user_password : 493d116c5977e785aef2a0f70fae483eb70ac8740d0f17e0d9d13c2a
         * role_name :
         */

        private String org_name;
        private String policeorg;
        private String policesfzh;
        private String zzjgpcs;
        private String policename;
        private String policemanid;
        private String id;
        private String role_level;
        private String loginInfo;
        private String zzjgsj;
        private String user_is_effective;
        private String roleSQL;
        private String user_password;
        private String role_name;
        private List<?> roleIdList;

        public String getOrg_name() {
            return org_name;
        }

        public void setOrg_name(String org_name) {
            this.org_name = org_name;
        }

        public String getPoliceorg() {
            return policeorg;
        }

        public void setPoliceorg(String policeorg) {
            this.policeorg = policeorg;
        }

        public String getPolicesfzh() {
            return policesfzh;
        }

        public void setPolicesfzh(String policesfzh) {
            this.policesfzh = policesfzh;
        }

        public String getZzjgpcs() {
            return zzjgpcs;
        }

        public void setZzjgpcs(String zzjgpcs) {
            this.zzjgpcs = zzjgpcs;
        }

        public String getPolicename() {
            return policename;
        }

        public void setPolicename(String policename) {
            this.policename = policename;
        }

        public String getPolicemanid() {
            return policemanid;
        }

        public void setPolicemanid(String policemanid) {
            this.policemanid = policemanid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRole_level() {
            return role_level;
        }

        public void setRole_level(String role_level) {
            this.role_level = role_level;
        }

        public String getLoginInfo() {
            return loginInfo;
        }

        public void setLoginInfo(String loginInfo) {
            this.loginInfo = loginInfo;
        }

        public String getZzjgsj() {
            return zzjgsj;
        }

        public void setZzjgsj(String zzjgsj) {
            this.zzjgsj = zzjgsj;
        }

        public String getUser_is_effective() {
            return user_is_effective;
        }

        public void setUser_is_effective(String user_is_effective) {
            this.user_is_effective = user_is_effective;
        }

        public String getRoleSQL() {
            return roleSQL;
        }

        public void setRoleSQL(String roleSQL) {
            this.roleSQL = roleSQL;
        }

        public String getUser_password() {
            return user_password;
        }

        public void setUser_password(String user_password) {
            this.user_password = user_password;
        }

        public String getRole_name() {
            return role_name;
        }

        public void setRole_name(String role_name) {
            this.role_name = role_name;
        }

        public List<?> getRoleIdList() {
            return roleIdList;
        }

        public void setRoleIdList(List<?> roleIdList) {
            this.roleIdList = roleIdList;
        }
    }
}
