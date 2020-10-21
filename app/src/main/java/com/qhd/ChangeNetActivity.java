package com.qhd;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.acc.common.util.SPHelper;
import com.acc.common.util.ToastUtil;
import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;

/**
 * Created by androider on 2018/8/9.
 * 内容：
 */
public class ChangeNetActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    TitleBar titlebar;
    RadioButton cb1, cb2;
    RadioGroup radiogroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changenet);

        titlebar = findViewById(R.id.titlebar);
        titlebar.setTitle("模式切换");
        titlebar.setBack(true);

        radiogroup = findViewById(R.id.radiogroup);
        cb1 = findViewById(R.id.ckb1);
        cb2 = findViewById(R.id.ckb2);

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ckb1:
                        SPHelper.getHelper(ChangeNetActivity.this).setOnline(true);
                        clearColor();
                        cb1.setTextColor(getResources().getColor(R.color.tv_black));
                        ToastUtil.showToast(ChangeNetActivity.this,"在线模式");
                        break;
                    case R.id.ckb2:
                        SPHelper.getHelper(ChangeNetActivity.this).setOnline(false);
                        clearColor();
                        cb2.setTextColor(getResources().getColor(R.color.tv_black));
                        ToastUtil.showToast(ChangeNetActivity.this,"离线模式");
                        break;
                }
            }
        });

        if (SPHelper.getHelper(ChangeNetActivity.this).isOnline()) {
            cb1.setChecked(true);
        } else {
            cb2.setChecked(true);
        }


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private void clearColor() {
        cb1.setTextColor(getResources().getColor(R.color.tv_gray));
        cb2.setTextColor(getResources().getColor(R.color.tv_gray));
    }
}
