package com.acc.common;

import android.Manifest;

public interface Constants {

    int pageRecorders = 10;// 每页10条数据

    String[] REQUEST_PERMISSION_EXTERNAL_STORAGE = new String[]{//本地文件读写权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    String REQUEST_PERMISSION_VIDEO[] = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};//录制视频需要的权限

    /**
     * 数据库SP文件存储KEY
     */
    String DATABASE_VERSION_KEY = "DATABASE_VERSION_KEY";

    /**
     * 数据库版本号（apk安装时需要数据库覆盖，更改此版本号即可）
     */
    int DATABASE_VERSION_CODE = 11;

    String sucessCode = "000";

    String UPDATA_GA = "UPDATA_GA";

    String UPDATA_LOCAL = "UPDATA_LOCAL";

    String UPDATA_ALL = "UPDATA_ALL";
    //三河三道防线用户名
    String SDFX_USERNAME = "SHSYDJWAPP";
    //三河三道防线用户名
    String SDFX_PASSWORD = "111111";
}
