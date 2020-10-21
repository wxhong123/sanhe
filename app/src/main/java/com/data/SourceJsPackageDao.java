package com.data;

import android.content.Context;

import com.acc.common.database.DatabaseHelper;
import com.acc.common.myokhttp.util.LogUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


public class SourceJsPackageDao {

    private String LogClassname = "SourceJsPackageDao";
    private Context context;
    private Dao<SourceJsPackage, Integer> dao;
    private DatabaseHelper helper;

    public SourceJsPackageDao(Context mContext) {
        if (this.context == null || this.dao == null) {
            this.context = mContext;
            helper = DatabaseHelper.getHelper(context);
            try {
                dao = helper.getDao(SourceJsPackage.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public SourceJsPackage queryLastPackage() {
        try {
            return dao.queryBuilder().orderBy("VERSION_NO", false).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int update(SourceJsPackage sourceJsPackage) {
        try {
            return dao.update(sourceJsPackage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int add(SourceJsPackage sourceJsPackage) {
        try {
            return dao.create(sourceJsPackage);
        } catch (SQLException e) {
            LogUtils.d("======>>>>" + e.getMessage() + "");
            e.printStackTrace();
        }
        return 0;
    }

    public List<SourceJsPackage> queryAll() {
        try {
            return dao.queryBuilder().orderBy("VERSION_NO", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
