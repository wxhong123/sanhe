package com.qhd;

import com.data.SourceJsPackage;

import java.io.Serializable;

/**
 * Created by androider on 2018/8/29.
 * 内容：
 */
public class UpdateDataBean {


    /**
     * code : 000
     * data : {"id":"181b0cf2062948e895098b09c44e2c16","isNewRecord":false,"createDate":"2018-08-28 14:01:33","updateDate":"2018-08-28 14:01:33","versionNo":"2018081209","filePath":"/donwload/big/2018081209.zip","fileName":"2018081209.zip","fileExt":"zip","fileSize":73756671,"fileDescribe":"2018年8月28每一次数据上传","fileType":"1","downloadType":"0"}
     * success : true
     */

    private String code;
    private SourceJsPackage data;
    private boolean success;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SourceJsPackage getData() {
        return data;
    }

    public void setData(SourceJsPackage data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 181b0cf2062948e895098b09c44e2c16
         * isNewRecord : false
         * createDate : 2018-08-28 14:01:33
         * updateDate : 2018-08-28 14:01:33
         * versionNo : 2018081209
         * filePath : /donwload/big/2018081209.zip
         * fileName : 2018081209.zip
         * fileExt : zip
         * fileSize : 73756671
         * fileDescribe : 2018年8月28每一次数据上传
         * fileType : 1
         * downloadType : 0
         */

        private String id;
        private boolean isNewRecord;
        private String createDate;
        private String updateDate;
        private String versionNo;
        private String filePath;
        private String fileName;
        private String fileExt;
        private int fileSize;
        private String fileDescribe;
        private String fileType;
        private String downloadType;

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

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getVersionNo() {
            return versionNo;
        }

        public void setVersionNo(String versionNo) {
            this.versionNo = versionNo;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileExt() {
            return fileExt;
        }

        public void setFileExt(String fileExt) {
            this.fileExt = fileExt;
        }

        public int getFileSize() {
            return fileSize;
        }

        public void setFileSize(int fileSize) {
            this.fileSize = fileSize;
        }

        public String getFileDescribe() {
            return fileDescribe;
        }

        public void setFileDescribe(String fileDescribe) {
            this.fileDescribe = fileDescribe;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getDownloadType() {
            return downloadType;
        }

        public void setDownloadType(String downloadType) {
            this.downloadType = downloadType;
        }
    }
}
