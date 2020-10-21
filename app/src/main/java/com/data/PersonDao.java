package com.data;


import android.content.Context;

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

public class PersonDao {

    private String LogClassname = "PersonDao";
    private Context context;
    private com.j256.ormlite.dao.Dao<Person, Integer> dao;
    private DatabaseHelper helper;

    public PersonDao(Context mContext) {
        if (this.context == null || this.dao == null) {
            this.context = mContext;
            helper = DatabaseHelper.getHelper(context);
            try {
                dao = helper.getDao(Person.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public List<Person> loadAll(String sfhm) {
        try {
            String time = DateUtil.getFullDateToString(new Date());
            return dao.queryBuilder().where().eq("SFHM", Md5Util.sfzhofMd5(sfhm))
                    .and().eq("DEL_FLAG", "0")
                    .and().ge("BKKSSJ", time)
                    .and().le("BKJSSJ", time).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Person queryLastPerson() {
        try {
            return dao.queryBuilder().orderBy("CREATE_DATE", false).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
