package com.acc.common.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by androider on 2017/7/27.
 * 内容：
 */

public class MyPopupWindow extends PopupWindow {

    public Activity activity;
    public View view;
    onDismissListenr onDismissListenr;

    public void setOnDismissListenr(MyPopupWindow.onDismissListenr onDismissListenr) {
        this.onDismissListenr = onDismissListenr;
    }

    public MyPopupWindow(Activity activity, int res) {
        this.activity = activity;
        this.view = View.inflate(activity, res, null);
        initView();
    }

    public MyPopupWindow(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
        initView();
    }


    public MyPopupWindow(Activity activity, int res,
                         int width, int height) {
        this.activity = activity;
        this.view = View.inflate(activity, res, null);
        initView(width, height);
    }

    private void initView() {
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
                if (onDismissListenr != null) {
                    onDismissListenr.onDismiss();
                }
            }
        });

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setFocusable(true);
    }

    private void initView(int width, int height) {
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
                if (onDismissListenr != null) {
                    onDismissListenr.onDismiss();
                }
            }
        });

        setContentView(view);
        setWidth(width);
        setHeight(height);

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setFocusable(true);
    }

    public void showShadow() {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        activity.getWindow().setAttributes(lp);
    }


    public interface onDismissListenr {
        void onDismiss();
    }


}
