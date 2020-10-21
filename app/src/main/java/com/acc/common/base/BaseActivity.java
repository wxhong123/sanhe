package com.acc.common.base;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by androider on 2018/7/19.
 * 内容：
 */
public class BaseActivity extends Activity {


    public void setTranslucentStatus(int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(colorResId);// 状态栏无背景
    }
}
