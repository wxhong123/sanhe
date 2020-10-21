package com.acc.common.dao;

import android.content.Context;

import com.acc.common.database.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.response.db_beans.CheckInfo;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by androider on 2018/7/24.
 * 内容：
 */
public class CheckInfoDao {

    private Context context;

    private Dao<CheckInfo, Integer> checkInfoDao;

    private DatabaseHelper helper;

    public CheckInfoDao(Context mContext) {
        this.context = mContext;
        helper = DatabaseHelper.getHelper(context);
        try {
            checkInfoDao = helper.getDao(CheckInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long queryCheckInfoCount() {
        long number = 0;
        try {
            number = checkInfoDao.queryBuilder().countOf();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return number;
    }

    public List<CheckInfo> queryForAll() {
        try {
            //有可能机器时间与服务器不成功,可以会出现问题
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            //查询2分钟之前的数据
            calendar.add(Calendar.SECOND, -60 * 2);
            Date date = calendar.getTime();
            return checkInfoDao.queryBuilder().where().le("CHECK_TIME", date).query();

//            return checkInfoDao.queryBuilder().query();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public CheckInfo queryTopFirstCheckInfo() {
        try {
            return checkInfoDao.queryBuilder().orderBy("CHECK_TIME", false).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CheckInfo queryCheckInfoById(String id) {
        try {
            return checkInfoDao.queryBuilder().where().eq("ID", id).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteById(String checkId) {
        try {
            DeleteBuilder<CheckInfo, Integer> deleteBuilder = checkInfoDao.deleteBuilder();
            deleteBuilder.where().eq("ID", checkId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void add(CheckInfo bean) {
        try {
            int i = checkInfoDao.create(bean);
//            CheckInfo checkInfo= checkInfoDao.queryBuilder().where().eq("id",bean.getId()).queryForFirst();
            List<CheckInfo> infos = checkInfoDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
