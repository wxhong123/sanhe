package com.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

/**
 * 作者：徐宏明 on 2018/5/22.
 * 邮箱：294985925@qq.com
 */
@Dao
public interface DbInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upload(@NonNull DbInfo dbInfo);

    @Query("SELECT * FROM DbInfo")
    LiveData<DbInfo> loadAll();
}
