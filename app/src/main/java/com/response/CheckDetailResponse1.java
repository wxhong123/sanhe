package com.response;

import com.response.db_beans.CheckInfo;

import java.util.List;

public class CheckDetailResponse1 {

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private CheckInfo main;
        private BasePerson ryxx;
        private BaseCar clxx;
        private List<com.arong.swing.db.entity.KeyPerson> zbryList;
        private List<com.arong.swing.db.entity.KeyCar> zbclList;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public CheckInfo getMain() {
            return main;
        }

        public void setMain(CheckInfo main) {
            this.main = main;
        }

        public BasePerson getRyxx() {
            return ryxx;
        }

        public void setRyxx(BasePerson ryxx) {
            this.ryxx = ryxx;
        }

        public BaseCar getClxx() {
            return clxx;
        }

        public void setClxx(BaseCar clxx) {
            this.clxx = clxx;
        }

        public List<com.arong.swing.db.entity.KeyPerson> getZbryList() {
            return zbryList;
        }

        public void setZbryList(List<com.arong.swing.db.entity.KeyPerson> zbryList) {
            this.zbryList = zbryList;
        }

        public List<com.arong.swing.db.entity.KeyCar> getZbclList() {
            return zbclList;
        }

        public void setZbclList(List<com.arong.swing.db.entity.KeyCar> zbclList) {
            this.zbclList = zbclList;
        }

    }
}
