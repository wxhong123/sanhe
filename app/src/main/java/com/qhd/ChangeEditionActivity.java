package com.qhd;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;


/**
 * Created by androider on 2018/8/3.
 * 内容：
 */
public class ChangeEditionActivity extends Activity {

    TitleBar titlebar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeedtion);

        titlebar = findViewById(R.id.titlebar);
    }
}
