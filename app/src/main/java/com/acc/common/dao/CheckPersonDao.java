package com.acc.common.dao;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.acc.common.Constants;
import com.acc.common.database.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.response.db_beans.CheckPerson;

import java.sql.SQLException;
import java.util.List;

public class CheckPersonDao {

    private String LogClassname = "checkPersonDao";

    private Context context;

    private Dao<CheckPerson, Integer> checkPersonDao;

    private DatabaseHelper helper;

    public CheckPersonDao(Context mContext) {
        this.context = mContext;
        helper = DatabaseHelper.getHelper(context);
        try {
            checkPersonDao = helper.getDao(CheckPerson.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入并返回当前对象
     *
     * @param bean 数据对象
     * @return 返回当前对象
     */
    public CheckPerson addReturnCP(CheckPerson bean) {
        if (bean != null) {
            try {
                return checkPersonDao.createIfNotExists(bean);
            } catch (SQLException e) {
                e.printStackTrace();
                Log.d(LogClassname, e.getMessage());
                return null;
            }
        }
        return null;
    }

    public boolean add(CheckPerson bean) {
        int insert = 0;
        if (bean != null) {
            try {
                insert = checkPersonDao.create(bean);
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
            DeleteBuilder<CheckPerson, Integer> deleteBuilder = checkPersonDao
                    .deleteBuilder();
            deleteBuilder.where().le("CHECK_TIME", checktime);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (delete >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询所有
     */
    public List<CheckPerson> queryAll() {
        try {
            return checkPersonDao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据条件查询Msg
     */
    public List<CheckPerson> queryReader(String parentId) {

        try {
            return checkPersonDao.queryBuilder().where().eq("parent_id", parentId).query();
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

    public boolean update(CheckPerson bean) {
        int update = 0;
        try {
            update = checkPersonDao.update(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update == 1;
    }


    /**
     * 根据条件查询
     */
    public List<CheckPerson> queryPage(String start, String end, String sfzh, int pageNumber) {

        try {
            QueryBuilder<CheckPerson, Integer> builder = checkPersonDao.queryBuilder();
            Where<CheckPerson, Integer> wheres = builder
                    .offset((pageNumber - 1) * Constants.pageRecorders)
                    .limit(Constants.pageRecorders)
                    .where();
            wheres.ge("CHECK_TIME", start);
            wheres.and();
            wheres.le("CHECK_TIME", end);
            if (!TextUtils.isEmpty(sfzh)) {
                wheres.and();
                wheres.like("sfzh", "%" + sfzh + "%");
            }
            builder.orderBy("CHECK_TIME", false);
            return wheres.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据条件查询总数
     */
    public Long queryCount(String start, String end, String sfzh) {

        try {
            Where<CheckPerson, Integer> wheres = checkPersonDao.queryBuilder().where();
            wheres.ge("CHECK_TIME", start);
            wheres.and();
            wheres.le("CHECK_TIME", end);
            if (!TextUtils.isEmpty(sfzh)) {
                wheres.and();
                wheres.like("sfzh", "%" + sfzh + "%");
            }
            return wheres.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public CheckPerson queryById(int id) {
        try {
            return checkPersonDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CheckPerson queryCheckPersonByCheckId(String checkId) {
        try {
            return checkPersonDao.queryBuilder().where().eq("CHECK_ID", checkId).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteByCheckId(String checkId) {
        try {
            DeleteBuilder<CheckPerson, Integer> deleteBuilder = checkPersonDao.deleteBuilder();
            deleteBuilder.where().eq("CHECK_ID", checkId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
