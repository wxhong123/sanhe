package com.data;

import android.content.Context;


import com.acc.common.database.DatabaseHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


/**
 * 同步数据字典表Dao
 * Created by hanhg on 2017/7/22.
 */

public class SyncTableDao {

    private Context context;
    private Dao<SyncTable, Integer> dao;
    private DatabaseHelper helper;

    public SyncTableDao(Context mContext) {
        if (this.context == null || this.dao == null) {
            this.context = mContext;
            helper = DatabaseHelper.getHelper(context);
            try {
                dao = helper.getDao(SyncTable.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int updateLastUpdate(SyncTable syncTable) {

        try {
            return dao.update(syncTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<SyncTable> queryAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int querySize() {
        try {
            return dao.queryForAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 查询最后一条记录
     *
     * @return 返回最近一条记录
     */
    public SyncTable queryLastData() {
        try {
            return dao.queryBuilder().orderBy("UPDATENUMBER", false).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
