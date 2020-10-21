package com.acc.common.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.acc.common.Constants;

import com.acc.common.util.AndroidUtils;
import com.acc.common.util.SharedPreferencesUtils;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;


import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static String datapath;

    public static String DB_NAME="hebei.db";

    private Map<String, Dao> daos = new HashMap<String, Dao>();

    private Context mContext;
    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, Constants.DATABASE_VERSION_CODE);
        mContext = context;
        if (!checkDataBaseVersion()) {
            deleteDataBase();
        }
        try {
            datapath = context.getDatabasePath(DB_NAME).getPath();
            File dbf = new File(datapath);
            if (!dbf.exists()) {
                AndroidUtils.copyAssetFile(context, DB_NAME, datapath);
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(datapath, null);
                onCreate(db);
                db.close();
                SharedPreferencesUtils.setParam(mContext, Constants.DATABASE_VERSION_KEY, Constants.DATABASE_VERSION_CODE);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "创建数据库失败");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database,ConnectionSource connectionSource) {
//        try {
//            TableUtils.createTable(connectionSource, CheckPerson.class);
//            TableUtils.createTable(connectionSource, CheckCar.class);

//            TableUtils.createTable(connectionSource, CheckInfo.class);
//            TableUtils.createTable(connectionSource, com.response.db_beans.CheckCar.class);
//            TableUtils.createTable(connectionSource, com.response.db_beans.CheckCarJs.class);
//            TableUtils.createTable(connectionSource, com.response.db_beans.CheckPerson.class);
//            TableUtils.createTable(connectionSource, com.response.db_beans.CheckPersonJs.class);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
//        try {

//            TableUtils.dropTable(connectionSource, CheckPerson.class, true);
//            TableUtils.dropTable(connectionSource, CheckCar.class, true);

//            TableUtils.dropTable(connectionSource, CheckInfo.class, true);
//            TableUtils.dropTable(connectionSource, com.response.db_beans.CheckCar.class, true);
//            TableUtils.dropTable(connectionSource, com.response.db_beans.CheckCarJs.class, true);
//            TableUtils.dropTable(connectionSource, com.response.db_beans.CheckPerson.class, true);
//            TableUtils.dropTable(connectionSource, com.response.db_beans.CheckPersonJs.class, true);
          //  onCreate(database, connectionSource);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    private static DatabaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    @Override
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }


    /**
     * 检查数据库是否为最新版本；
     * true为最新版本不需覆盖
     * false则需删除原有数据库文件，重新从assets下copy一份数据库文件
     *
     * @return
     */
    public boolean checkDataBaseVersion() {
        boolean isNewVersion = false;
        int oldVersionCode = (int) SharedPreferencesUtils.getParam(mContext, Constants.DATABASE_VERSION_KEY, 0);
        if (oldVersionCode == Constants.DATABASE_VERSION_CODE) {
            isNewVersion = true;
        }
        return isNewVersion;
    }

    public void deleteDataBase() {
        String datapath = mContext.getDatabasePath(DB_NAME).getPath();
        deleteFilesByDirectory(new File(datapath));
    }

    /**
     * 删除文件
     *
     * @param directory
     */
    private void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists()) {
            directory.delete();
        }
    }
}
