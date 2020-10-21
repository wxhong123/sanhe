package com.qhd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.acc.common.util.NetUtil;
import com.acc.common.util.SPHelper;
import com.acc.common.util.ToastUtil;

/**
 * Created by androider on 2017/10/24.
 * 内容：
 */

public class NetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (SPHelper.getHelper(context).getAutoChangeNet()) {
//            if (NetUtil.isNetAvailable(context)) {
//                SPHelper.getHelper(context).setOnline(true);
//                Toast.makeText(context, "在线模式", Toast.LENGTH_SHORT).show();
//            } else {
//                SPHelper.getHelper(context).setOnline(false);
//                Toast.makeText(context, "离线模式", Toast.LENGTH_SHORT).show();
//            }
//        }

    }
}
