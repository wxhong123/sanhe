package com.service.syncdata;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.acc.common.myokhttp.util.LogUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 同步数据字典Service
 */
public class SyncDataService extends Service {
    private Thread synTh;
    private TimerTask taskSyncData;
    private Timer timerSyncData = new Timer();
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public SyncDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onCreate();
        int anHour = 60 * 60 * 1000; // 这是一小时的毫秒数

        if (timerSyncData != null) {
            timerSyncData.cancel();
            timerSyncData = null;
        }

        taskSyncData = new TimerTask() {
            @Override
            public void run() {
                LogUtils.d("调用sync");
                syncData();
            }
        };
        timerSyncData = new Timer();
        timerSyncData.schedule(taskSyncData, 0, anHour);
        return START_REDELIVER_INTENT;
    }

    private void syncData() {
        DownloadDataService downloadDataService = new DownloadDataService(this);
        downloadDataService.syncData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("调用sync" + " ondestroy");
        if (timerSyncData != null) {
            timerSyncData.cancel();
            timerSyncData = null;
        }
    }
}
