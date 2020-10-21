package com.data;

import android.content.Context;

import com.acc.common.database.DatabaseHelper;
import com.acc.common.util.Md5Util;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class TPersonControlDao {
    private String LogClassname = "TPersonControlDao";
    private Context context;
    private Dao<TPersonControl, String> dao;
    private DatabaseHelper helper;

    public TPersonControlDao(Context mContext) {
        if (this.context == null || this.dao == null) {
            this.context = mContext;
            helper = DatabaseHelper.getHelper(context);
            try {
                dao = helper.getDao(TPersonControl.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<TPersonControl> queryZdrList(String sfzh) {
        //SPZT =1  (审批状态(0.未审批 1.审批通过 2.审批不通过))
        try {
            return dao.queryBuilder().where().eq("SFZ", Md5Util.sfzhofMd5(sfzh)).and().eq("SPZT", "1").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
