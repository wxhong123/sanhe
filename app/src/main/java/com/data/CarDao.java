package com.data;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.content.Context;
import android.support.annotation.NonNull;

import com.acc.common.database.DatabaseHelper;
import com.acc.common.util.DateUtil;
import com.acc.common.util.Md5Util;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 作者：徐宏明 on 2018/5/22.
 * 邮箱：294985925@qq.com
 */

public class CarDao {
    private String LogClassname = "CarDao";
    private Context context;
    private com.j256.ormlite.dao.Dao<Car, Integer> dao;
    private DatabaseHelper helper;

    public CarDao(Context mContext) {
        if (this.context == null || this.dao == null) {
            this.context = mContext;
            helper = DatabaseHelper.getHelper(context);
            try {
                dao = helper.getDao(Car.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Car> loadAll(String cphm, String hpzl) {
        try {
            String time = DateUtil.getFullDateToString(new Date());
            return dao.queryBuilder().where().eq("CPHM", Md5Util.sfzhofMd5(cphm))
                    .and().eq("HPZL", hpzl)
                    .and().eq("DEL_FLAG", "0")
                    .and().ge("BKKSSJ", time)
                    .and().le("BKJSSJ", time)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Car queryLastCar() {
        try {
            return dao.queryBuilder().orderBy("CREATE_DATE", false).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
