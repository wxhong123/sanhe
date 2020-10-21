package com.data;

import android.content.Context;
import android.util.Log;


import com.acc.common.database.DatabaseHelper;
import com.acc.common.util.Reflections;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.PreparedQuery;
import com.response.CheckListResonse;

import org.apache.commons.lang.ArrayUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 同步数据字典Dao
 * Created by hanhg on 2017/7/22.
 */

public class DynamicDao {
    private String LogClassname = "DynamicDao";

    private Context context;

    private Dao<Map, Integer> dao;

    private DatabaseHelper helper;

    /**
     * 初始化 构造
     *
     * @param mContext
     */
    public DynamicDao(Context mContext) {
        if (this.context == null || this.dao == null) {
            this.context = mContext;
            try {
                helper = DatabaseHelper.getHelper(context);
                dao = helper.getDao(BaseDicValue.class);
            } catch (SQLException e) {
            }
        }
    }

    /**
     * 通过主键查询数据是否存在
     *
     * @param tableName 表名
     * @param idkey     主键名称
     * @param idValue   主键值
     * @return false :不存在 ，true :存在
     */
    public boolean check(String tableName, String idkey, String idValue) {
        try {
            String sql = "SELECT count(*) as num  FROM " + tableName + "  WHERE  " + idkey + " = ? ";

            GenericRawResults<String[]> results = dao.queryRaw(sql, idValue);
            String num = results.getFirstResult()[0];
            int i = Integer.valueOf(num);
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int deleteById(String tableName, String idkey, String idValue) {
        try {
            String sql = "DELETE  FROM " + tableName + "  WHERE " + idkey + " = ?";
            int i = dao.updateRaw(sql, idValue);
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Insert Map数据
     *
     * @param map
     * @param tableName
     * @param idkey
     * @param idValue
     * @return
     */
    public int insertMap(Map map, String tableName, String idkey, String idValue) {
        try {
            String keyStr = "( ";
            String valueStr = "( ";
            String[] args = new String[map.size()];
            Iterator it = map.keySet().iterator();
            for (int j = 0; it.hasNext(); j++) {
                String key = (String) it.next();
                keyStr = keyStr + key + ",";
                valueStr = valueStr + "?,";
                args[j] = map.get(key) == null ? "" : map.get(key) + "";
            }
            keyStr = keyStr.substring(0, keyStr.length() - 1) + " ) ";
            valueStr = valueStr.substring(0, valueStr.length() - 1) + " ) ";
            String insertSql = "insert into " + tableName + " " + keyStr + " values " + valueStr;
            String sqlval = ArrayUtils.toString(args);
            Log.d("sql:", sqlval);
            return dao.updateRaw(insertSql, args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 更新Map数据
     *
     * @param map
     * @param tableName
     * @param idkey
     * @param idValue
     * @return
     */
    public int updateMap(Map map, String tableName, String idkey, String idValue) {
        try {
            String[] args = new String[map.keySet().size()];
            Iterator it = map.keySet().iterator();
            StringBuffer updateSql = new StringBuffer("UPDATE " + tableName + " SET ");
            int i = 0;
            while (it.hasNext()) {
                String keyName = (String) it.next();
                if (idkey.equalsIgnoreCase(keyName.toUpperCase()))
                    continue;
                updateSql.append(keyName + " = ?,");
                args[i] = map.get(keyName) == null ? "" : map.get(keyName) + "";
                i++;
            }
            args[map.size() - 1] = idValue;
            updateSql.deleteCharAt(updateSql.length() - 1);
            updateSql.append(" WHERE " + idkey + "  = ? ");
            return dao.updateRaw(updateSql.toString(), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long queryCountBySql(String sql) {
        try {
            String[] result = dao.queryRaw(sql).getFirstResult();
            if (result != null && result.length > 0)
                return Long.valueOf(result[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public <T> List<T> queryForList(String sql, final Class<T> classz) {
        GenericRawResults<T> results = null;
        try {
            results = dao.queryRaw(sql, new RawRowMapper<T>() {
                @Override
                public T mapRow(String[] colNames, String[] colValue) {
                    try {
                        T object = classz.newInstance();
                        for (int i = 0; i < colNames.length; i++) {
                            //  System.out.println("FeildName:"+ colNames[i]+":"+ colValue[i]);
                            if (colValue[i] != null) {
                                Reflections.setFieldValue(object, colNames[i], colValue[i]);
                            }
                        }
                        return object;
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<T> dataBeanList = new ArrayList<T>();
        if (results != null) {
            Iterator<T> iterator = results.iterator();
            while (iterator.hasNext()) {
                T t = iterator.next();
                dataBeanList.add(t);
            }
        }
        return dataBeanList;
    }

}
