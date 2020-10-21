package com.qqhes.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;

/**
 * 数据采集
 *
 * @author 徐宏明
 * @email ：294985925@qq.com
 * @date 2020/2/12
 */
public class CollectionPersonActivity extends Activity {
    private TitleBar titlebar;
    private EditText mStart, mEnd, mPhone, mOther;
    private Button mSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_collection_person_qqhe);
        initView();
    }

    private void initView() {
        titlebar = findViewById(R.id.titlebar);
        titlebar.setBack(true);
        titlebar.setTitle("信息采集");

        mStart = findViewById(R.id.editText0);
        mEnd = findViewById(R.id.editText1);
        mPhone = findViewById(R.id.editText2);
        mOther = findViewById(R.id.editText3);

        mSave = findViewById(R.id.button0);
    }
}
