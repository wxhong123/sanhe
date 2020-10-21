package com.acc.common.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.acc.common.URL;
import com.alibaba.fastjson.JSON;
import com.arong.swing.db.entity.StaffUserVO;

/**
 * Created by androider on 2018/7/24.
 * 内容：
 */
public class SPHelper {

    private static SPHelper helper;
    private SharedPreferences sharedPreferences;

    private SPHelper(Context activity) {
        sharedPreferences = activity.getSharedPreferences("sp", Context.MODE_PRIVATE);
    }

    public static SPHelper getHelper(Context activity) {
        if (helper == null) {
            helper = new SPHelper(activity);
        }
        return helper;
    }

    public void saveInfo(StaffUserVO info) {
        sharedPreferences.edit().putString("userinfo", JSON.toJSONString(info)).commit();
    }

    public StaffUserVO getInfo() {
        String userinfo = sharedPreferences.getString("userinfo", "");
        return JSON.parseObject(userinfo, StaffUserVO.class);
    }

    public void setOnline(boolean online) {
        sharedPreferences.edit().putBoolean("online", online).commit();
    }

    public boolean isOnline() {
        return sharedPreferences.getBoolean("online", true);
    }

    public void setHOST(String s) {
        sharedPreferences.edit().putString("host", s).commit();
    }

    public String getHOST() {
        return sharedPreferences.getString("host", URL.DefUrl);
    }

    public void setCheckStyle(int style) {
        sharedPreferences.edit().putInt("style", style).commit();
    }

    public int getCheckStyle() {
        return sharedPreferences.getInt("style", 2);
    }

    public void setCheckInterval(int interval) {
        sharedPreferences.edit().putInt("interval", interval).commit();
    }

    public int getCheckInterval() {
        return sharedPreferences.getInt("interval", 200);
    }


    public void seDownloadUrl(String url) {
        sharedPreferences.edit().putString("DownloadUrl", url).commit();
    }

    public String getDownloadUrl() {
        return sharedPreferences.getString("DownloadUrl", "");
    }


    public void setDownloadentity(String url) {
        sharedPreferences.edit().putString("DownloadBean", url).commit();
    }

    public String getDownloadentity() {
        return sharedPreferences.getString("DownloadBean", "");
    }


    public void setCheckAddr(String addr) {
        sharedPreferences.edit().putString("checkaddr", addr).commit();
    }

    public String getCheckAddr() {
        return sharedPreferences.getString("checkaddr", "");
    }

    public void setCheckType(String addr) {
        sharedPreferences.edit().putString("checktype", addr).commit();
    }

    public String getCheckType() {
        return sharedPreferences.getString("checktype", "");
    }

    public void setExplodeTime(long addr) {
        sharedPreferences.edit().putLong("explodetime", addr).commit();
    }

    public long getExplodeTime() {
        return sharedPreferences.getLong("explodetime", 0);
    }
}
