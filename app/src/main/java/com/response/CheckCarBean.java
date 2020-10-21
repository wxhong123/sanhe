package com.response;

import com.arong.swing.db.entity.KeyCar;

import java.io.Serializable;
import java.util.List;

/**
 * Created by androider on 2018/7/30.
 * 内容：
 */
public class CheckCarBean implements Serializable {


    /**
     * code : 000
     * data : {"id":"e4be1b1d2bb64ee3b1ec205dad4e9280","clxx":{"hpzl":"01","cphm":"京B14701"},"zbclList":[{"cphm":"京B14701","hpzl":"01","czsfzh":"271331199606500708","czxm":"张德孔","clsbdm":"100202","clys":"白色","fdjh":"er8808","cllb":"盗抢","clfs":"抓获","aqms":"暑期期间环秦三道防线安检查控，及时发现过滤不安全因素，确保暑期安全稳定","bklxr":"王东","bklxfs":"13312547854","rwmc":"暑期安保","rwid":"9bed552bdc2047bb810680d0818235ca","pfirstid":"498ff4aab1de4ae6880a31da7ad7227f","pverid":"d7bf9aca67af4013bb264eacd30e00f4","sjly":"anrong"},{"cphm":"京B14701","hpzl":"01","czsfzh":"271331199606500708","czxm":"张德孔","clsbdm":"100202","clys":"白色","fdjh":"er8808","cllb":"盗抢","clfs":"抓获2","aqms":"暑期期间环秦三道防线安检查控，及时发现过滤不安全因素，确保暑期安全稳定","bklxr":"王东","bklxfs":"13312547854","rwmc":"暑期安保","rwid":"9bed552bdc2047bb810680d0818235ca","pfirstid":"498ff4aab1de4ae6880a31da7ad7227f","pverid":"d7bf9aca67af4013bb264eacd30e00f4","sjly":"anrong"}]}
     * success : true
     */

    private String code;
    private DataBean data;
    private String msg;
    private boolean success;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        /**
         * id : e4be1b1d2bb64ee3b1ec205dad4e9280
         * clxx : {"hpzl":"01","cphm":"京B14701"}
         * zbclList : [{"cphm":"京B14701","hpzl":"01","czsfzh":"271331199606500708","czxm":"张德孔","clsbdm":"100202","clys":"白色","fdjh":"er8808","cllb":"盗抢","clfs":"抓获","aqms":"暑期期间环秦三道防线安检查控，及时发现过滤不安全因素，确保暑期安全稳定","bklxr":"王东","bklxfs":"13312547854","rwmc":"暑期安保","rwid":"9bed552bdc2047bb810680d0818235ca","pfirstid":"498ff4aab1de4ae6880a31da7ad7227f","pverid":"d7bf9aca67af4013bb264eacd30e00f4","sjly":"anrong"},{"cphm":"京B14701","hpzl":"01","czsfzh":"271331199606500708","czxm":"张德孔","clsbdm":"100202","clys":"白色","fdjh":"er8808","cllb":"盗抢","clfs":"抓获2","aqms":"暑期期间环秦三道防线安检查控，及时发现过滤不安全因素，确保暑期安全稳定","bklxr":"王东","bklxfs":"13312547854","rwmc":"暑期安保","rwid":"9bed552bdc2047bb810680d0818235ca","pfirstid":"498ff4aab1de4ae6880a31da7ad7227f","pverid":"d7bf9aca67af4013bb264eacd30e00f4","sjly":"anrong"}]
         */

        private String id;
        private BaseCar clxx;
        private List<com.response.KeyCar> zbclList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public BaseCar getClxx() {
            return clxx;
        }

        public void setClxx(BaseCar clxx) {
            this.clxx = clxx;
        }

        public List<com.response.KeyCar> getZbclList() {
            return zbclList;
        }

        public void setZbclList(List<com.response.KeyCar> zbclList) {
            this.zbclList = zbclList;
        }
    }
}
