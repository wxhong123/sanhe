package com.acc.common.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

/**
 * 2018/3/29
 * <p>
 * 检测是否有权限
 * 邮箱：294985925@qq.com
 */

public final class PermissionUtils {
    /**
     * 设备状态权限
     */
    public static final String[] REQUEST_PERMISSION_PHONE_STATE = new String[]{
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.ADD_VOICEMAIL};
    /**
     * 本地文件读写权限
     */
    public static final String[] REQUEST_PERMISSION_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,};
    /**
     * 相机权限
     */
    public static final String[] REQUEST_PERMISSION_CAMERA = new String[]{
            Manifest.permission.CAMERA};
    /**
     * 录音权限
     */
    public static final String[] REQUEST_PERMISSION_AUDIO = new String[]{
            Manifest.permission.RECORD_AUDIO};
    /**
     * 联系人权限组
     */
    public static final String[] REQUEST_PERMISSION_CONTANTS = new String[]{
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS
    };
    /**
     * 日历权限组
     */
    public static final String[] REQUEST_PERMISSION_CALENDAR = new String[]{
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };
    /**
     * 传感器权限组
     */
    public static final String[] REQUEST_PERMISSION_SENSORS = new String[]{
            Manifest.permission.BODY_SENSORS
    };
    /**
     * 定位权限组
     */
    public static final String[] REQUEST_PERMISSION_LOCATION = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    /**
     * SMS限组
     */
    public static final String[] REQUEST_PERMISSION_SMS = new String[]{
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
    };

    /**
     * 判断是否全部拥有这些权限
     *
     * @param permissions 权限集合
     * @return true表示拥有权限，false表示没有权限
     */
    public static boolean isHavePermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (!isHavePermission(context, permission))
                return false;
        }
        return true;
    }

    /**
     * 遍历权限请求结果，如果为true则授权成功，否则失败
     *
     * @param grantResults 请求返回结果集合
     * @return true为成功，false为失败。
     */
    public static boolean GrantResult(int[] grantResults) {
        boolean mBaseGrantResults = true;
        for (int grantResult : grantResults) {
            mBaseGrantResults &= grantResult == PackageManager.PERMISSION_GRANTED;
        }
        return mBaseGrantResults;
    }

    /**
     * 判断是否缺少权限
     *
     * @param permission 要判断的权限
     * @return true表示拥有权限，false表示没有权限
     */
    private static boolean isHavePermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断是否需要显示权限请求描述信息
     *
     * @param activity    上下文
     * @param permissions 权限集合
     * @return true表示需要显示权限请求描述，false不需要显示
     */
    private static boolean showRationales(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (showRationale(activity, permission))
                return true;
        }
        return false;
    }

    /**
     * 是否需要显示权限请求描述
     *
     * @param permission 权限
     * @param activity   上下文
     * @return true需要显示权限描述，false不需要显示权限描述
     */
    private static boolean showRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permission);
    }

    /**
     * 检查是否拥有这些权限，如果没有那么申请，并返回false。
     *
     * @param activity    所在activity
     * @param permissions 请求权限列表
     * @param requestCode 请求码，用于在activity中监听回调
     * @param message     请求权限描述信息
     * @return true表示有权限，不需要申请，否则需要申请。
     */
    public static boolean checkPermission(final Activity activity, final String[] permissions, final int requestCode, String message) {
        //检查是否有权限，当没有权限的时候需要申请，拥有权限则返回
        if (isHavePermissions(activity, permissions)) return true;
        //检测是否需要显示请求权限的描述
        if (showRationales(activity, permissions)) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle("权限设置")
                    .setMessage(message)
                    .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    })
                    .setNegativeButton("授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity, permissions, requestCode);
                        }
                    }).create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        } else {
            ActivityCompat.requestPermissions(activity, permissions,
                    requestCode);
        }
        return false;
    }
}
