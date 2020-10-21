package com.qhd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.acc.common.URL;
import com.acc.common.base.MyApplication;
import com.acc.common.util.SPHelper;
import com.acc.common.util.ToastUtil;
import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;
import com.service.syncdata.SyncDataService;
import com.service.uploaddata.CheckInfoService;


/**
 * Created by androider on 2018/8/2.
 * 内容：
 */
public class SetURLActivity extends Activity {

    TitleBar titleBar;
    EditText editText;
    TextView save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seturl);

        titleBar = findViewById(R.id.titlebar);
        titleBar.setBack(true);
        titleBar.setTitle("软件设置");

        editText = findViewById(R.id.et_url);
        String host = SPHelper.getHelper(this).getHOST();
        editText.setText(host);

        save = findViewById(R.id.tv_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editText.getText().toString().trim();
                if (url == null || "".equals(url)) {
                    ToastUtil.showToast(SetURLActivity.this, "服务器地址不能为空");
                    return;
                }
                SPHelper.getHelper(SetURLActivity.this).setHOST(url);
                ToastUtil.showToast(SetURLActivity.this, "修改成功");
                /*修改URL后需要更新数据*/
                URL.host = SPHelper.getHelper(MyApplication.application).getHOST() + "/check/";
                URL.RootUrl = SPHelper.getHelper(MyApplication.application).getHOST();
                //启动数据同步Service
                Intent startIntent = new Intent(SetURLActivity.this, SyncDataService.class);
                startService(startIntent);
                //启动离线数据上传服务
                Intent uploadIntent = new Intent(SetURLActivity.this, CheckInfoService.class);
                startService(uploadIntent);


                finish();
            }
        });
    }
}
