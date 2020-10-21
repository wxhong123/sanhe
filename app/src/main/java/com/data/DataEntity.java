package com.data;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Date;

public class DataEntity  {
    @DatabaseField(columnName = "id")
    protected  String id;//主键
    @DatabaseField(columnName = "id")
    protected String remarks; // 备注
    @DatabaseField(columnName = "create_By")
    protected String createBy; // 创建者
    @DatabaseField(columnName = "create_Date")
    protected Date createDate; // 创建日期
    @DatabaseField(columnName = "update_By")
    protected String updateBy; // 更新者
    @DatabaseField(columnName = "update_Date")
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）

}
