package com.qhd;

import android.content.Context;

import com.acc.common.util.SPHelper;
import com.acc.common.widget.TitleBar;


public class TitleBarUtils {

    public static void setTile(Context context, TitleBar titleBar, String title, boolean cutTheme) {

        if (cutTheme) {
            boolean online = SPHelper.getHelper(context).isOnline();
//            titleBar.setTitle(title);
            if (online) {
                titleBar.setTitle(title + "(在线)");
//                titleBar.setBackground(R.color.title_bar_color);
            } else {
                titleBar.setTitle(title + "(离线)");
//                titleBar.setBackground(R.color.tab_text_gray1);
            }
        }
    }
}
