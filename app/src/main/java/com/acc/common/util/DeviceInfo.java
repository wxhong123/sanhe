package com.acc.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 设备信息。
 * 作者：徐宏明 on 2018/5/15.
 * 邮箱：294985925@qq.com
 */
public class DeviceInfo implements Serializable {
    private static DeviceInfo INSTANCE = null;//使用单例模式
    private String deviceId;//设备ID
    private String model;//设备型号
    private String version;//系统版本
    private String rom;//系统rom
    private String brand;//设备品牌

    @SuppressLint("MissingPermission")
    private DeviceInfo(Context context) {
        this.model = Build.MODEL;
        this.version = Build.VERSION.RELEASE;
        this.rom = Build.MANUFACTURER;
        this.brand = Build.BRAND;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (null != telephonyManager)
                deviceId = telephonyManager.getDeviceId();
        } catch (Exception e) {
            deviceId = "unknown";
        }
    }

    public static synchronized DeviceInfo getInstance() {
        checkNotNull(INSTANCE, "DeviceInfo is not init.");
        return INSTANCE;
    }

    public static synchronized void init(Context context) {
        INSTANCE = new DeviceInfo(context);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
