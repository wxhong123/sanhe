package com.service.uploaddata;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.acc.common.Constants;
import com.acc.common.URL;
import com.acc.common.base.MyApplication;
import com.acc.common.dao.CheckInfoDao;
import com.acc.common.myokhttp.util.LogUtils;
import com.acc.common.util.NetUtil;
import com.acc.common.util.SPHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.response.JsonDataResult;
import com.response.db_beans.CheckInfo;
import com.response.db_beans.OffLineCheckVo;
import com.service.check.CheckInfoBusiness;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 本地数据上传服务
 */
public class CheckInfoService extends Service {

    private Thread synTh;
    private TimerTask taskSyncData;
    private Timer timerSyncData = new Timer();

    public CheckInfoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onCreate();
        // 1分钟上传一次
        int anHour = 60 * 1000;
        taskSyncData = new TimerTask() {
            @Override
            public void run() {
                uploadCheckInfo();
            }
        };
        timerSyncData.schedule(taskSyncData, 0, anHour);
        return START_REDELIVER_INTENT;
    }

    public void uploadCheckInfo() {
        if (NetUtil.isNetAvailable(this) && SPHelper.getHelper(this).isOnline()) {
            CheckInfoDao checkInfoDao = new CheckInfoDao(this);
            try {
                //查询未上传记录总数
                List<CheckInfo> checkInfoList = checkInfoDao.queryForAll();
                //线程休眠15秒，尽量保证所有记录已经完成处理(10秒网络请求、5秒数据库处理)
                if (checkInfoList != null && checkInfoList.size() > 0) {
                    LogUtils.d("====>>>" + checkInfoList.size());
                    TimeUnit.SECONDS.sleep(15);
                    for (CheckInfo checkInfo : checkInfoList) {
                        if (checkInfo == null)
                            continue;
                        CheckInfoBusiness checkInfoBusiness = new CheckInfoBusiness(this);
                        OffLineCheckVo uploadVo = checkInfoBusiness.queryRecodeDetail(checkInfo);
                        boolean uploadState = uploadService(uploadVo);
                        //数据上传成功，删除本地离线数据
                        if (uploadState) {
                            checkInfoBusiness.deleteByCheckInfo(checkInfo.getId());
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean uploadService(OffLineCheckVo uploadVo) {
        boolean uploadState = false;
        try {
            if (uploadVo != null) {
                String json = JSON.toJSONString(uploadVo);
                OkHttpClient okHttpClient = MyApplication.mMyOkhttp.getOkHttpClient();
                MediaType type = MediaType.parse("application/json;charset=utf-8");
                RequestBody body = RequestBody.create(type, json);
                Request request = new Request.Builder()
                        .url(URL.host + URL.UPLOADOFFLINE)
                        .post(body)
                        .build();

                Call call = okHttpClient.newCall(request);
                Response response = call.execute();
                String responseStr = response.body().string();
                JsonDataResult jsonDataResult = JSONObject.parseObject(responseStr, JsonDataResult.class);
                if (jsonDataResult != null && Constants.sucessCode.equals(jsonDataResult.getCode())) {
                    uploadState = true;
                }
            }
        } catch (Exception ex) {

        }
        return uploadState;
    }

}
