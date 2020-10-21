package com.acc.common.base;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.acc.common.URL;
import com.acc.common.myokhttp.DownloadMgr;
import com.acc.common.myokhttp.MyOkHttp;
import com.acc.common.util.LoggerInterceptor;
import com.acc.common.util.SPHelper;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.response.db_beans.CheckInfo;

import java.util.concurrent.TimeUnit;

import cn.hbfec.checklib.CheckLib;
import okhttp3.OkHttpClient;

/**
 * Created by androider on 2018/7/19.
 * 内容：
 */
public class MyApplication extends Application {

    public static MyOkHttp mMyOkhttp;
    public LocationManager locationManager;
    public static Location location;
    public static MyApplication application;

    private DownloadMgr mDownloadMgr;
    static double latitude;
    static double longitude;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initOkhttp();
//        getGPSLocation();
//        DxtExceptionHandler crashHandler = DxtExceptionHandler.getInstance();
//        // 注册crashHandler
//        crashHandler.init(getApplicationContext());
//        // 发送以前没发送的报告(可选)
//        crashHandler.sendPreviousReportsToServer();


        mDownloadMgr = (DownloadMgr) new DownloadMgr.Builder()
                .myOkHttp(mMyOkhttp)
                .maxDownloadIngNum(5)       //设置最大同时下载数量（不设置默认5）
                .saveProgressBytes(50 * 1204)   //设置每50kb触发一次saveProgress保存进度 （不能在onProgress每次都保存 过于频繁） 不设置默认50kb
                .build();
        initLocation();
        //初始化访问地址
        URL.host = SPHelper.getHelper(MyApplication.application).getHOST() + "/check/";
        URL.RootUrl = SPHelper.getHelper(MyApplication.application).getHOST();
        try {
            //初始化三道防线SDK
            CheckLib.init(this).setCompany("北京卓华").setServer("http://192.168.11.10:10010");
        } catch (Exception e) {

        }
    }


    public DownloadMgr getDownloadMgr() {
        return mDownloadMgr;
    }

    private void initOkhttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .addInterceptor(new LoggerInterceptor("okhttp"))
                .build();

        mMyOkhttp = new MyOkHttp(okHttpClient);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void getGPSLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60 * 1000, 0, locationListener);
        //     locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);

    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                MyApplication.this.location = location;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public static double getLongitude() {
        AMapLocation aMapLocation = getLastLocation();
        if (aMapLocation == null) {
            return 0;
        } else {
            return aMapLocation.getLongitude();
        }

//        return location == null ? 0 : location.getLongitude();
//        return longitude;
    }

    public static double getlatitude() {
        AMapLocation aMapLocation = getLastLocation();
        if (aMapLocation == null) {
            return 0;
        } else {
            return aMapLocation.getLatitude();
        }
    }


    /**
     * 获取坐标时间间隔
     */
    public static final int interval = 60 * 1000;//C:\Program Files\Java\jdk1.8.0_111\bin
    /**
     * 声明mlocationClient对象
     */
    private static AMapLocationClient mlocationClient;
    /**
     * 声明mLocationOption对象
     */
    private static AMapLocationClientOption mLocationOption = null;


    private void initLocation() {
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(interval);
//        mLocationOption.setGpsFirst(true);//gps优先
        //初始化定位对象
        mlocationClient = new AMapLocationClient(this);

        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

                if (aMapLocation == null) {
                    return;
                }

                if (aMapLocation.getErrorCode() == 0) {
                    latitude = aMapLocation.getLatitude();
                    longitude = aMapLocation.getLongitude();
                }
            }
        });
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位
        // mlocationClient.startLocation();
        startLocation();
    }

    public static AMapLocation getLastLocation() {
        if (mlocationClient != null) {
            //启动定位
            AMapLocation lastKnownLocation = mlocationClient.getLastKnownLocation();
            if (lastKnownLocation == null) {
                return null;
            }
            return lastKnownLocation;

        }
        return null;
    }

    public void startLocation() {
        if (mlocationClient != null) {
            //启动定位
            mlocationClient.startLocation();
        }

    }

}
