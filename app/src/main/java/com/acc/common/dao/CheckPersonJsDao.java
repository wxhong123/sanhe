package com.acc.common.dao;

import android.content.Context;
import android.util.Log;

import com.acc.common.database.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.response.db_beans.CheckPersonJs;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by androider on 2018/7/24.
 * 内容：
 */
public class CheckPersonJsDao {

    private String LogClassname = "checkPersonDao";

    private Context context;

    private Dao<CheckPersonJs, Integer> checkPersonJsDao;

    private DatabaseHelper helper;

    public CheckPersonJsDao(Context mContext) {
        this.context = mContext;
        helper = DatabaseHelper.getHelper(context);
        try {
            checkPersonJsDao = helper.getDao(CheckPersonJs.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CheckPersonJs> queryCheckCarJsListByCheckId(String checkId) {
        try {
            return checkPersonJsDao.queryBuilder().where().eq("CHECK_ID", checkId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteByCheckId(String checkId) {
        try {
            DeleteBuilder<CheckPersonJs, Integer> deleteBuilder = checkPersonJsDao.deleteBuilder();
            deleteBuilder.where().eq("CHECK_ID", checkId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean add(CheckPersonJs bean) {
        int insert = 0;
        if (bean != null) {
            try {
                insert = checkPersonJsDao.create(bean);
                Log.i("StrategyBean", "add: insert --- > " + insert);
            } catch (SQLException e) {
                e.printStackTrace();
                Log.d(LogClassname, e.getMessage());
            }
        }
        if (insert > 0) {
            return true;
        } else {
            return false;
        }
    }
}
