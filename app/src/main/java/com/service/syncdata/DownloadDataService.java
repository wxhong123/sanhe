package com.service.syncdata;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.acc.common.URL;
import com.acc.common.myokhttp.util.LogUtils;
import com.acc.common.util.NetUtil;
import com.alibaba.fastjson.JSON;
import com.data.DynamicDao;
import com.data.SyncTable;
import com.data.SyncTableDao;
import com.qhd.bean.MsgBean;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadDataService implements Runnable {
    private Context context;
    private SynceNotice synceNotice;
    private Handler handler;

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    private boolean isRun = false;

    public DownloadDataService(Context _context) {
        this.context = _context;
    }

    public DownloadDataService(Context _context, SynceNotice _synceNotice) {
        this.context = _context;
        this.synceNotice = _synceNotice;
    }

    public DownloadDataService(Context _context, Handler _handler) {
        this.context = _context;
        this.handler = _handler;
    }

    public void syncData() {
        isRun = true;
        List<SyncTable> list = this.queryListTables();
        SyncTableDao syncTableDao = new SyncTableDao(this.context);
        if (list != null) {
            for (SyncTable syncTable : list) {
                int updateNumber = getDataByURL(syncTable.getTableName(), syncTable.getUpdateNumber());
                if (updateNumber > 0) {
//                    syncTable.setLastupdate(new Date().getTime() + "");
                    syncTable.setLastupdate(System.currentTimeMillis() + "");
                    syncTable.setUpdateNumber(updateNumber);
                    syncTableDao.updateLastUpdate(syncTable);
                }
            }
        }
        isRun = false;
//        EventBus.getDefault().post(new MsgBean());
    }

    private int updateDataBase(List<Map> list) {
        int updatenumber = 0;
        DynamicDao dynamicDao = new DynamicDao(context);
        for (int i = 0; list != null && i < list.size(); i++) {
            Map rowMap = list.get(i);
            if (rowMap.containsKey("CHANGEINFO")) {
                Map changeMap = (Map) rowMap.get("CHANGEINFO");
                String operation = changeMap.get("OPERATION") + "";
                String tablename = changeMap.get("TABLENAME") + "";
                String tableid = changeMap.get("TABLEID") + "";
                updatenumber = Integer.valueOf(changeMap.get("ID") + "");
                String tableidvalue = changeMap.get("TABLEIDVALUE") + "";
                if ("1".equals(operation) || "2".equals(operation)) {//插入或更新数据

                    boolean flg = dynamicDao.check(tablename, tableid, tableidvalue);
                    if (!flg) {//数据不存在
                        if (rowMap.containsKey("CHANGEVALUE")) {
                            Map changevalueMap = (Map) rowMap.get("CHANGEVALUE");
                            int updateCount = dynamicDao.insertMap(changevalueMap, tablename, tableid, tableidvalue);
                            if (updateCount < 1) {
                                Toast.makeText(context, "数据同步未知异常！", Toast.LENGTH_SHORT).show();
                                return updateCount;
                            }
                        }
                    } else {
                        if (rowMap.containsKey("CHANGEVALUE")) {
                            Map changevalueMap = (Map) rowMap.get("CHANGEVALUE");
                            dynamicDao.updateMap(changevalueMap, tablename, tableid, tableidvalue);
                        }
                    }
                } else//数据标示为删除
                {
                    dynamicDao.deleteById(tablename, tableid, tableidvalue);
                }
            }
            //如果通知类不为空，则通知控件更改状态
            notice();

        }
        return updatenumber;
    }

    private void notice() {
        if (handler != null) {
            Message msg = handler.obtainMessage();
            msg.what = 1;
            msg.sendToTarget();
        }
    }


    private List<SyncTable> queryListTables() {
        SyncTableDao syncTableDao = new SyncTableDao(this.context);
        List<SyncTable> syncTables = syncTableDao.queryAll();
        int i = syncTables.size();
        return syncTableDao.queryAll();
    }

    int sum = 0;

    private int getDataByURL(String tableName, int updateNumber) {
        if (NetUtil.isNetAvailable(this.context)) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(URL.host + URL.SYNC_DATA_URL + "?tableName=" + tableName + "&updateNumber=" + updateNumber)
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                String json = response.body().string();
                List<Map> list = JSON.parseArray(json, Map.class);
                int count = list.size();
                updateNumber = updateDataBase(list);
                sum = sum + count;
                if (count == 50) {//后台数据一次返回50条记录，如果count==50可能本次数据还没有更新完成
                    updateNumber = getDataByURL(tableName, updateNumber);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e1) {
                e1.printStackTrace();
            }
        }
        return updateNumber;
    }

    @Override
    public void run() {
        syncData();
    }
}
