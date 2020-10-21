package com.acc.common.dao;

import android.content.Context;
import android.util.Log;

import com.acc.common.Constants;
import com.acc.common.database.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.response.db_beans.CheckCar;

import java.sql.SQLException;
import java.util.List;

public class CheckCarDao {

    private String LogClassname = "checkCarDao";

    private Context context;

    private Dao<CheckCar, Integer> checkCarDao;

    private DatabaseHelper helper;

    public CheckCarDao(Context mContext) {
        this.context = mContext;
        helper = DatabaseHelper.getHelper(context);
        try {
            checkCarDao = helper.getDao(CheckCar.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean add(CheckCar bean) {
        int insert = 0;
        if (bean != null) {
            try {
                insert = checkCarDao.create(bean);
                Log.i("StrategyBean", "add: insert --- > " + insert);
            } catch (SQLException e) {
                e.printStackTrace();
                Log.d(LogClassname, e.getMessage());
            }
        }
        if (insert >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean delete(String checktime) {
        int delete = 0;
        try {
            DeleteBuilder<CheckCar, Integer> deleteBuilder = checkCarDao
                    .deleteBuilder();
            deleteBuilder.where().le("CHECK_TIME", checktime);
            delete = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (delete >= 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询所有
     */
    public List<CheckCar> queryAll() {
        try {
            return checkCarDao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据条件查询Msg
     */
    public List<CheckCar> queryReader(String parentId) {

        try {
            return checkCarDao.queryBuilder().where().eq("parent_id", parentId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CheckCar queryById(int id) {
        try {
            return checkCarDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新
     *
     * @param bean
     * @return
     */

    public boolean update(CheckCar bean) {
        int update = 0;
        try {
            update = checkCarDao.update(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (update >= 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据条件查询
     */
    public List<CheckCar> queryPage(String start, String end, int pageNumber) {

        try {
            return checkCarDao.queryBuilder().orderBy("create_time", false)
                    .offset((pageNumber - 1) * Constants.pageRecorders)
                    .limit(Constants.pageRecorders)
                    .where()
                    .ge("CHECK_TIME", start)
                    .and()
                    .le("CHECK_TIME", end)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据条件查询总数
     */
    public Long queryCount(String start, String end) {

        try {
            return checkCarDao.queryBuilder()
                    .where()
                    .ge("CHECK_TIME", start)
                    .and()
                    .le("CHECK_TIME", end)
                    .countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CheckCar queryCheckCarByCheckId(String checkId) {
        try {
            return checkCarDao.queryBuilder().where().eq("CHECK_ID", checkId).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteByCheckId(String checkId) {
        try {
            DeleteBuilder<CheckCar, Integer> deleteBuilder = checkCarDao.deleteBuilder();
            deleteBuilder.where().eq("CHECK_ID", checkId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
