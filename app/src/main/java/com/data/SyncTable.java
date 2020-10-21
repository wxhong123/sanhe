package com.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by hanhg on 2017/7/22.
 */
@DatabaseTable(tableName = "SYNC_TABLE")
public class SyncTable {

    @DatabaseField(columnName = "ID", generatedId = true)
    private Integer id;

    @DatabaseField(columnName = "TABLENAME")
    private String tableName;

    @DatabaseField(columnName = "LASTUPDATE")
    private String lastupdate;

    @DatabaseField(columnName = "UPDATENUMBER")
    private int updateNumber;


    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getTableName() {
        return tableName;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUpdateNumber() {
        return updateNumber;
    }

    public void setUpdateNumber(int updateNumber) {
        this.updateNumber = updateNumber;
    }

}
