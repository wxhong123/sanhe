package com.data;

import android.content.Context;
import android.util.Log;

import com.acc.common.database.DatabaseHelper;
import com.acc.common.util.Md5Util;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.List;

public class TVehiclesControlDao {
    private String LogClassname = "TVehiclesControlDao";
    private Context context;
    private Dao<TVehiclesControl, String> dao;
    private DatabaseHelper helper;

    public TVehiclesControlDao(Context mContext) {
        if (this.context == null || this.dao == null) {
            this.context = mContext;
            helper = DatabaseHelper.getHelper(context);
            try {
                dao = helper.getDao(TVehiclesControl.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<TVehiclesControl> queryZdclList(String cphm, String hpzl) {
        try {
            return dao.queryBuilder().where().eq("CPH", Md5Util.sfzhofMd5(cphm)).and().eq("HPZL", hpzl).and().eq("SPZT", "1").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
