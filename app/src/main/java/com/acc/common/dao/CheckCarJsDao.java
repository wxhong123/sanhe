package com.acc.common.dao;

import android.content.Context;

import com.acc.common.database.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.response.db_beans.CheckCarJs;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by androider on 2018/7/24.
 * 内容：
 */
public class CheckCarJsDao {

    private Context context;

    private Dao<CheckCarJs, Integer> checkCarJsDao;

    private DatabaseHelper helper;

    public CheckCarJsDao(Context mContext) {
        this.context = mContext;
        helper = DatabaseHelper.getHelper(context);
        try {
            checkCarJsDao = helper.getDao(CheckCarJs.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CheckCarJs> queryCheckCarJsListByCheckId(String checkId) {
        try {
            return checkCarJsDao.queryBuilder().where().eq("CHECK_ID", checkId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteByCheckId(String checkId) {
        try {
            DeleteBuilder<CheckCarJs, Integer> deleteBuilder = checkCarJsDao.deleteBuilder();
            deleteBuilder.where().eq("CHECK_ID", checkId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean add(CheckCarJs bean) {
        int insert = 0;
        if (bean != null) {
            try {
                insert = checkCarJsDao.create(bean);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (insert >= 1) {
            return true;
        } else {
            return false;
        }
    }
}
