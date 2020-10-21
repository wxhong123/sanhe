package com.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 作者：徐宏明 on 2018/5/23.
 * 邮箱：294985925@qq.com
 */
@Entity
public class DbInfo {
    @PrimaryKey
    @NonNull
    private String id;
    //创建时间
    private String create;

    @Nullable
    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }
}
