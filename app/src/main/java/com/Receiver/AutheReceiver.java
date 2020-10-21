package com.Receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.acc.common.Provider.MyContentResolver;

/**
 * 内容：认证成功/认证注销广播
 *
 * Created by xh.w on 2020.10.20
 */

public class AutheReceiver extends BroadcastReceiver {

    private static final String ACTION_LOGIN = "com.ydjw.ua.ACTION_LOGIN";
    private static final String ACTION_LOGOUT = "com.ydjw.ua.ACTION_LOGOUT";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // 认证成功
        if (ACTION_LOGIN.equals(action)){
            // 再次调用获取凭证的接口
            Bundle bundle = MyContentResolver.call(context, "call");
        }

        // 认证注销
        if (ACTION_LOGOUT.equals(action)){
            // 执行自身的注销操作
            ActivityManager am = (ActivityManager)context.getSystemService (Context.ACTIVITY_SERVICE);
            am.restartPackage(context.getPackageName());

        }
    }
}
