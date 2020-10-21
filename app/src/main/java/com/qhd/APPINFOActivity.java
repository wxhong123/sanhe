package com.qhd;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;


/**
 * Created by androider on 2018/8/2.
 * 内容：
 */
public class APPINFOActivity extends Activity {

    TextView tv_back;
    TextView tv_edition;
    TitleBar titlebar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appinfo);

        titlebar = findViewById(R.id.titlebar);
        titlebar.setBack(true);
        titlebar.setTitle(R.string.app_name);
        tv_back = findViewById(R.id.tv_back);
        tv_edition = findViewById(R.id.tv_edition);
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            tv_edition.setText(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
