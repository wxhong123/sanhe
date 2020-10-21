package com.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 基础字典表，包含国籍、民族、性别等，对应数据库中的SYS_DICT的表
 */
@DatabaseTable(tableName = "SYS_DICT")
public class BaseDicValue implements Serializable {
    /**
     * 属性serialVersionUID的声明
     */
    private static final long serialVersionUID = 7142093558033524010L;
    //ID
    @DatabaseField(columnName = "ID")
    private String id;
    //属性分类：国籍、民族、性别
    @DatabaseField(columnName = "TYPE")
    private String dicTypeId;
    //字典名字，如男、女
    @DatabaseField(columnName = "LABEL")
    private String dicValueName;
    //字典CODE，如man，woman
    @DatabaseField(columnName = "VALUE")
    private String dicValueCode;
    //字典属性描述，如guoji_type为国籍
    @DatabaseField(columnName = "DESCRIPTION")
    private String dicTypeDes;
    //排序，如1、2、3
    @DatabaseField(columnName = "SORT")
    private BigDecimal displayOrder;
    //父ID
    @DatabaseField(columnName = "PARENT_ID")
    private String parentId;
    //创建用户的ID
    @DatabaseField(columnName = "CREATE_BY")
    private String createUser;
    //创建时间
    @DatabaseField(columnName = "CREATE_DATE")
    private String createTime;
    //更新用户的ID
    @DatabaseField(columnName = "UPDATE_BY")
    private String updateUser;
    //更新时间
    @DatabaseField(columnName = "UPDATE_DATE")
    private String updateTime;
    //标记信息
    @DatabaseField(columnName = "REMARKS")
    private String remarks;
    //删除标识
    @DatabaseField(columnName = "DEL_FLAG")
    private String delete;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDicTypeId() {
        return dicTypeId;
    }

    public void setDicTypeId(String dicTypeId) {
        this.dicTypeId = dicTypeId;
    }

    public String getDicValueName() {
        return dicValueName;
    }

    public void setDicValueName(String dicValueName) {
        this.dicValueName = dicValueName;
    }

    public String getDicValueCode() {
        return dicValueCode;
    }

    public void setDicValueCode(String dicValueCode) {
        this.dicValueCode = dicValueCode;
    }

    public String getDicTypeDes() {
        return dicTypeDes;
    }

    public void setDicTypeDes(String dicTypeDes) {
        this.dicTypeDes = dicTypeDes;
    }

    public BigDecimal getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(BigDecimal displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return dicValueName;
    }
}