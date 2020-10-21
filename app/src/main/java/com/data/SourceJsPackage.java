/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * 离线数据库信息Entity
 *
 * @author hanhg
 * @version 2018-07-18
 */
@DatabaseTable(tableName = "SOURCE_JS_PACKAGE")
public class SourceJsPackage implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(columnName = "VERSION_NO")
    private String versionNo;        // 版本号
    @DatabaseField(columnName = "FILE_PATH")
    private String filePath;        // 文件路径
    @DatabaseField(columnName = "FILE_NAME")
    private String fileName;        // 文件名称
    @DatabaseField(columnName = "FILE_EXT")
    private String fileExt;        // 文件扩展名
    @DatabaseField(columnName = "FILE_SIZE")
    private Long fileSize;        // 文件大小
    @DatabaseField(columnName = "FILE_DESCRIBE")
    private String fileDescribe;        // 文件描述
    @DatabaseField(columnName = "FILE_TYPE")
    private String fileType;        // 文件类型
    @DatabaseField(columnName = "DOWNLOAD_TYPE")
    private String downloadType;        // 更新类型（强制更新、选择更新）
    @DatabaseField(columnName = "ID")
    protected String id;//主键
    @DatabaseField(columnName = "REMARKS")
    protected String remarks; // 备注
    @DatabaseField(columnName = "CREATE_BY", defaultValue = "hhg")
    protected String createBy; // 创建者
    @DatabaseField(columnName = "CREATE_DATE")
    protected Date createDate; // 创建日期
    @DatabaseField(columnName = "UPDATE_BY", defaultValue = "hhg")
    protected String updateBy; // 更新者
    @DatabaseField(columnName = "UPDATE_DATE")
    protected Date updateDate; // 更新日期

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
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


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }


}