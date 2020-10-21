package com.acc.common.util;

import android.support.annotation.NonNull;

import com.acc.common.base.MyApplication;
import com.acc.common.myokhttp.response.DownloadResponseHandler;
import com.acc.common.myokhttp.response.RawResponseHandler;
import com.vector.update_app.HttpManager;

import java.io.File;
import java.util.Map;

/**
 * Created by androider on 2018/8/28.
 * 内容：
 */
public class UpdateAppHttpUtil implements HttpManager {


    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        AppInfo.init(MyApplication.application);
        AppInfo appInfo = AppInfo.getInstance();

        MyApplication.mMyOkhttp.get()
                .url(url)
                .addParam("versionName", appInfo.getVersionName())
                .addParam("packageName", appInfo.getApplicationId())
                .addParam("versionCode", appInfo.getVersionCode())
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        callBack.onError(error_msg);
                    }

                    @Override
                    public void onSuccess(int statusCode, String response) {
                        callBack.onResponse(response);
                    }
                });
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {


    }


    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
        MyApplication.mMyOkhttp.download()
                .url(url)
                .filePath(path)
                .tag(this)
                .enqueue(new DownloadResponseHandler() {
                    @Override
                    public void onFinish(File downloadFile) {
                        callback.onResponse(downloadFile);
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        callback.onProgress(currentBytes, totalBytes);
                        ToastUtil.showToastLong(MyApplication.application, "下载进度" + String.valueOf(currentBytes * 100 / totalBytes) + "%");
                    }

                    @Override
                    public void onFailure(String error_msg) {
                        callback.onError(error_msg);
                    }
                });

    }
}
