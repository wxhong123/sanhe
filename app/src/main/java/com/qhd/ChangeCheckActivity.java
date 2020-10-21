package com.qhd;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.acc.common.myokhttp.util.LogUtils;
import com.acc.common.util.SPHelper;
import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;


/**
 * Created by androider on 2018/8/9.
 * 内容：
 */
public class ChangeCheckActivity extends Activity {

    TitleBar titlebar;
    LinearLayout ll_dianchi;
    RadioButton cb2, cb3, cb4, cb5, cb6;
    RadioGroup rg1, rg2;
    CheckBox cb1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changecheck);

        rg1 = findViewById(R.id.rg1);
        rg2 = findViewById(R.id.rg2);
        ll_dianchi = findViewById(R.id.ll_dianchi);
        cb1 = findViewById(R.id.ckb1);
        cb2 = findViewById(R.id.ckb2);
        cb3 = findViewById(R.id.ckb3);
        cb4 = findViewById(R.id.ckb4);
        cb5 = findViewById(R.id.ckb5);
        cb6 = findViewById(R.id.ckb6);

        cb1.setChecked(true);
        cb1.setClickable(false);

        titlebar = findViewById(R.id.titlebar);
        titlebar.setTitle("核查模式设置");
        titlebar.setBack(true);

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ckb1:
                        ll_dianchi.setVisibility(View.GONE);
                        SPHelper.getHelper(ChangeCheckActivity.this).setCheckStyle(0);
                        break;
                    case R.id.ckb2:
                        ll_dianchi.setVisibility(View.GONE);
                        SPHelper.getHelper(ChangeCheckActivity.this).setCheckStyle(1);
                        clearColor1();
                        cb2.setTextColor(getResources().getColor(R.color.tv_black));
                        break;
                    case R.id.ckb3:
                        ll_dianchi.setVisibility(View.VISIBLE);
                        SPHelper.getHelper(ChangeCheckActivity.this).setCheckStyle(2);
                        clearColor1();
                        cb3.setTextColor(getResources().getColor(R.color.tv_black));
                        break;
                }
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                clearColor2();
                switch (checkedId) {
                    case R.id.ckb4:
                        LogUtils.d("=====123点击");
                        SPHelper.getHelper(ChangeCheckActivity.this).setCheckInterval(200);
                        cb4.setTextColor(getResources().getColor(R.color.tv_black));
                        break;
                    case R.id.ckb5:
                        SPHelper.getHelper(ChangeCheckActivity.this).setCheckInterval(500);
                        cb5.setTextColor(getResources().getColor(R.color.tv_black));
                        break;
                    case R.id.ckb6:
                        SPHelper.getHelper(ChangeCheckActivity.this).setCheckInterval(1000);
                        cb6.setTextColor(getResources().getColor(R.color.tv_black));
                        break;
                }
            }
        });

        int style = SPHelper.getHelper(this).getCheckStyle();
        switch (style) {
//            case 0:
//                cb1.setChecked(true);
//                break;
            case 1:
                cb2.setChecked(true);
                break;
            case 2:
                cb3.setChecked(true);
                break;
        }

        int interval = SPHelper.getHelper(this).getCheckInterval();
        switch (interval) {
            case 200:
                cb4.setChecked(true);
                break;
            case 500:
                cb5.setChecked(true);
                break;
            case 1000:
                cb6.setChecked(true);
                break;
        }
    }


    private void clearColor1() {
        cb2.setTextColor(getResources().getColor(R.color.tv_gray));
        cb3.setTextColor(getResources().getColor(R.color.tv_gray));
    }

    private void clearColor2() {
        cb4.setTextColor(getResources().getColor(R.color.tv_gray));
        cb5.setTextColor(getResources().getColor(R.color.tv_gray));
        cb6.setTextColor(getResources().getColor(R.color.tv_gray));
    }
}
