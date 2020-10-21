package com.acc.common.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by androider on 2018/8/2.
 * 内容：
 */
public class JumpUtil {

    public static void jumptoact(Activity activity1, Class activity2) {
        Intent intent = new Intent(activity1, activity2);
        activity1.startActivity(intent);
    }

    public static void jumptoact(Activity activity1, Class activity2, Bundle bundle) {
        Intent intent = new Intent(activity1, activity2);
        intent.putExtras(bundle);
        activity1.startActivity(intent);
    }

    public static void jumptoactforresult(Activity activity1, Class activity2, int requestcode) {
        Intent intent = new Intent(activity1, activity2);
        activity1.startActivityForResult(intent, requestcode);
    }
}
