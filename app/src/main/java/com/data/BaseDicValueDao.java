package com.data;


import android.content.Context;

import com.acc.common.database.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;


/**
 * 数据字典值Dao
 * Created by Administrator on 2016/9/7.
 */
public class BaseDicValueDao {
    private String LogClassname = "BaseDicValueDao";
    private Context context;
    private Dao<BaseDicValue, Integer> dao;
    private DatabaseHelper helper;

    public BaseDicValueDao(Context mContext) {
        if (this.context == null || this.dao == null) {
            this.context = mContext;
            helper = DatabaseHelper.getHelper(context);
            try {
                dao = helper.getDao(BaseDicValue.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 通过字典ID，获得字典值集合。
     *
     * @param dictyTyeId 字典类型名称
     * @return 字典集合
     */
    public List<BaseDicValue> queryDictListByType(String dictyTyeId) {
        try {
            return dao.queryBuilder().orderBy("SORT", true).where().eq("TYPE", dictyTyeId).and().eq("DEL_FLAG","0").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 查询所有
     */
    public List<BaseDicValue> queryAll() {
        try {
            return dao.queryBuilder().limit((long) 10).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字段名和字段值查询表
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @return 字典集合
     */
    public List<BaseDicValue> queryDicListByKeyAndValue(String fieldName, Object fieldValue) {
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq(fieldName, fieldValue);
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据字段名和字段值查询表
     *
     * @param dictType  数据字典类型名称
     * @param dictValue 数据字典类型值
     * @return 字典集合
     */
    public String queryLableBydictTypeAndValue(String dictType, String dictValue, String defalut) {
        try {
            BaseDicValue baseDicValue = dao.queryBuilder().where().eq("TYPE", dictType).and().eq("value", dictValue).queryForFirst();
            if (baseDicValue == null)
                return defalut;
            return baseDicValue.getDicValueName();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据字段名和字段值查询表
     *
     * @param dictType  数据字典类型名称
     * @param dictValue 数据字典类型值
     * @return 字典集合
     */
    public String queryCodeBydictTypeAndValue(String dictType, String dictValue, String defalut) {
        try {
            BaseDicValue baseDicValue = dao.queryBuilder().where().eq("TYPE", dictType).and().eq("LABEL", dictValue).queryForFirst();
            if (baseDicValue == null)
                return defalut;
            return baseDicValue.getDicValueCode();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}