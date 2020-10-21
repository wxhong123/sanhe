package com.acc.common.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by androider on 2018/8/1.
 * 内容：
 */
public class ToastUtil {

    public static Toast toast;

    public static void showToast(Context activity, String s) {
        if (toast == null) {
            toast = Toast.makeText(activity, s, Toast.LENGTH_SHORT);
        } else {
            toast.setText(s);
        }
        toast.show();
    }

    public static void showToastLong(Context activity, String s) {
        if (toast == null) {
            toast = Toast.makeText(activity, s, Toast.LENGTH_LONG);
        } else {
            toast.setText(s);
        }
        toast.show();
    }

}
