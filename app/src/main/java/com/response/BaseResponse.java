package com.response;

/**
 * Created by androider on 2018/8/28.
 * 内容：
 */
public class BaseResponse {

    /**
     * code : 000
     * data : {"id":"28288bf7adad404098ca87121b84fdba","isNewRecord":false,"packageName":"acc.com.gongan","label":"河北省核查信息","icon":"res/drawable-mdpi-v4/ic_launcher.png","versionName":"v1.0.2","versionCode":2,"minSdkVersion":"16","targetSdkVersion":"21","downloadType":"0","apkPath":"/donwload/apk/sanhe1.0.2.apk","apkSize":14653110,"apkReleaseState":"1","versionDesc":"最新数据包","releaseTime":"2018-08-28 14:02:30"}
     * success : true
     */

    private String code;
    private DataBean data;
    private boolean success;

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
         * id : 28288bf7adad404098ca87121b84fdba
         * isNewRecord : false
         * packageName : acc.com.gongan
         * label : 河北省核查信息
         * icon : res/drawable-mdpi-v4/ic_launcher.png
         * versionName : v1.0.2
         * versionCode : 2
         * minSdkVersion : 16
         * targetSdkVersion : 21
         * downloadType : 0
         * apkPath : /donwload/apk/sanhe1.0.2.apk
         * apkSize : 14653110
         * apkReleaseState : 1
         * versionDesc : 最新数据包
         * releaseTime : 2018-08-28 14:02:30
         */

        private String id;
        private boolean isNewRecord;
        private String packageName;
        private String label;
        private String icon;
        private String versionName;
        private int versionCode;
        private String minSdkVersion;
        private String targetSdkVersion;
        private String downloadType;
        private String apkPath;
        private int apkSize;
        private String apkReleaseState;
        private String versionDesc;
        private String releaseTime;

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

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getMinSdkVersion() {
            return minSdkVersion;
        }

        public void setMinSdkVersion(String minSdkVersion) {
            this.minSdkVersion = minSdkVersion;
        }

        public String getTargetSdkVersion() {
            return targetSdkVersion;
        }

        public void setTargetSdkVersion(String targetSdkVersion) {
            this.targetSdkVersion = targetSdkVersion;
        }

        public String getDownloadType() {
            return downloadType;
        }

        public void setDownloadType(String downloadType) {
            this.downloadType = downloadType;
        }

        public String getApkPath() {
            return apkPath;
        }

        public void setApkPath(String apkPath) {
            this.apkPath = apkPath;
        }

        public int getApkSize() {
            return apkSize;
        }

        public void setApkSize(int apkSize) {
            this.apkSize = apkSize;
        }

        public String getApkReleaseState() {
            return apkReleaseState;
        }

        public void setApkReleaseState(String apkReleaseState) {
            this.apkReleaseState = apkReleaseState;
        }

        public String getVersionDesc() {
            return versionDesc;
        }

        public void setVersionDesc(String versionDesc) {
            this.versionDesc = versionDesc;
        }

        public String getReleaseTime() {
            return releaseTime;
        }

        public void setReleaseTime(String releaseTime) {
            this.releaseTime = releaseTime;
        }
    }
}
