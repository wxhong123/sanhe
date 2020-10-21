package com.qhd;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.acc.common.URL;
import com.acc.common.base.MyApplication;
import com.acc.common.event.ExplodeEvent;
import com.acc.common.myokhttp.response.DownloadResponseHandler;
import com.acc.common.myokhttp.util.LogUtils;
import com.acc.common.util.ToastUtil;
import com.data.SourceJsPackage;
import com.data.SourceJsPackageDao;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.NumberFormat;

/**
 * Created by androider on 2018/8/29.
 * 内容：
 */
public class DownstdataService extends Service {

    private boolean begin;
    SourceJsPackage gadata;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String path = intent.getStringExtra("path");
        String name = intent.getStringExtra("name");
        gadata = (SourceJsPackage) intent.getSerializableExtra("bean");
        if (!begin) {
            downloadData(path, name);
            begin = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void downloadData(String path, String fileName) {
        Toast.makeText(DownstdataService.this, "开始更新", Toast.LENGTH_SHORT).show();
        MyApplication.mMyOkhttp.download()
                .url(URL.RootUrl + path)
                .filePath(Environment.getExternalStorageDirectory() + "/anrong/infocheck/datas/" + fileName)
                .tag(this)
                .enqueue(new DownloadResponseHandler() {
                    @Override
                    public void onFinish(File downloadFile) {
                        SourceJsPackageDao sourceJsPackageDao = new SourceJsPackageDao(DownstdataService.this);
                        int add = sourceJsPackageDao.add(gadata);
                        LogUtils.d("dfsdfsdf=" + add);
                        if (downloadFile != null && downloadFile.isFile()) {
                            downloadFile.renameTo(new File(Environment.getExternalStorageDirectory() + "/anrong/infocheck/datas/" + "wholePackage.zip"));
                            EventBus.getDefault().post(new ExplodeEvent());
                        }
                        begin = false;
                        stopSelf();
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        NumberFormat nt = NumberFormat.getPercentInstance();
                        // 设置百分数精确度2即保留两位小数
                        nt.setMinimumFractionDigits(2);
                        // nt.format((float) curr/total);
//                        progressDialog.setMessage(nt.format((float) currentBytes / totalBytes));
                        EventBus.getDefault().post(new DataUpdatingActivity.ProgressEvent(currentBytes * 100 / totalBytes));
                    }

                    @Override
                    public void onFailure(String error_msg) {
                        begin = false;
                        Toast.makeText(DownstdataService.this, "更新失败", Toast.LENGTH_SHORT).show();
                        stopSelf();
                    }
                });

    }


}
