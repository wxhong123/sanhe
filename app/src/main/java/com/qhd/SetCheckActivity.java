package com.qhd;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.acc.common.myokhttp.util.LogUtils;
import com.acc.common.util.SPHelper;
import com.acc.common.util.StringUtil;
import com.acc.common.util.ToastUtil;
import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;
import com.data.BaseDicValue;
import com.data.BaseDicValueDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetCheckActivity extends AppCompatActivity {

    TitleBar titleBar;
    LinearLayout ll_addr;
    LinearLayout ll_type;

    BaseDicValueDao baseDicValueDao;

    List<BaseDicValue> check_address;
    List<BaseDicValue> check_task_type;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setcheck);
        initData();
        initview();
    }

    private void initData() {
        baseDicValueDao = new BaseDicValueDao(this);
        check_address = baseDicValueDao.queryDictListByType("check_address");
        check_task_type = baseDicValueDao.queryDictListByType("check_task_type");
//        if (check_address != null && check_address.size() > 0) {

//            for (int i = 0; i < check_address.size(); i++) {
//                RadioButton rb = new RadioButton(SetCheckActivity.this);
//                rb.setTextColor(Color.BLACK);
//                BaseDicValue baseDicValue = check_address.get(i);
//                rb.setText(baseDicValue.getDicValueName());
//                rb.setTag(baseDicValue.getDicValueCode());
//                ll_addr.addView(rb);
//            }

//            String checkAddr = SPHelper.getHelper(this).getCheckAddr();
//            int childCount = ll_addr.getChildCount();
//            for (int i = 0; i < childCount; i++) {
//                RadioButton childAt = (RadioButton) ll_addr.getChildAt(i);
//                if (StringUtil.equals(checkAddr, (String) childAt.getTag())) {
//                    childAt.setChecked(true);
//                } else {
//                    childAt.setChecked(false);
//                }
//            }

//        }


//        if (check_task_type != null && check_task_type.size() > 0) {
//            for (int i = 0; i < check_task_type.size(); i++) {
//                RadioButton rb = new RadioButton(SetCheckActivity.this);
//                rb.setTextColor(Color.BLACK);
//                BaseDicValue baseDicValue = check_task_type.get(i);
//                rb.setText(baseDicValue.getDicValueName());
//                rb.setTag(baseDicValue.getDicValueCode());
//                ll_type.addView(rb);
//            }
//
//            String checkType = SPHelper.getHelper(this).getCheckType();
//            int childCount = ll_type.getChildCount();
//            for (int i = 0; i < childCount; i++) {
//                RadioButton childAt = (RadioButton) ll_type.getChildAt(i);
//                if (StringUtil.equals(checkType, (String) childAt.getTag())) {
//                    childAt.setChecked(true);
//                } else {
//                    childAt.setChecked(false);
//                }
//            }
//        }


    }

    private void initview() {
        titleBar = findViewById(R.id.titlebar);
        titleBar.setBack(true);
        titleBar.setTitle("设置检查信息");

        ll_addr = findViewById(R.id.ll_addr);
        ll_type = findViewById(R.id.ll_type);

        ll_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_address != null && check_address.size() > 0) {
                    List<String> addrs = new ArrayList<>();
                    for (int i = 0; i < check_address.size(); i++) {
                        BaseDicValue baseDicValue = check_address.get(i);
                        addrs.add(baseDicValue.getDicValueName());
                    }
                    String[] s = new String[check_address.size()];
                    addrs.toArray(s);
                    int checkedItem = 0;
                    String checkAddr = SPHelper.getHelper(SetCheckActivity.this).getCheckAddr();
                    for (int i = 0; i < check_address.size(); i++) {
                        BaseDicValue baseDicValue = check_address.get(i);
                        if (StringUtil.equals(checkAddr, baseDicValue.getDicValueCode())) {
                            checkedItem = i;
                        }
                    }
                    alertDialog = new AlertDialog.Builder(SetCheckActivity.this)
                            .setSingleChoiceItems(s, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SPHelper.getHelper(SetCheckActivity.this).setCheckAddr(check_address.get(i).getDicValueCode());
                                    ToastUtil.showToast(SetCheckActivity.this,check_address.get(i).getDicValueName());
                                    alertDialog.dismiss();
                                }
                            }).create();
                    alertDialog.show();
                }


            }
        });


        ll_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_task_type != null && check_task_type.size() > 0) {
                    List<String> types = new ArrayList<>();
                    for (int i = 0; i < check_task_type.size(); i++) {
                        BaseDicValue baseDicValue = check_task_type.get(i);
                        types.add(baseDicValue.getDicValueName());
                    }
                    String[] s = new String[types.size()];
                    types.toArray(s);
                    int checkedItem = 0;
                    String checkType = SPHelper.getHelper(SetCheckActivity.this).getCheckType();
                    for (int i = 0; i < types.size(); i++) {
                        BaseDicValue baseDicValue = check_task_type.get(i);
                        if (StringUtil.equals(checkType, baseDicValue.getDicValueCode())) {
                            checkedItem = i;
                        }
                    }


                    alertDialog = new AlertDialog.Builder(SetCheckActivity.this)
                            .setSingleChoiceItems(s, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SPHelper.getHelper(SetCheckActivity.this).setCheckType(check_task_type.get(i).getDicValueCode());
                                    ToastUtil.showToast(SetCheckActivity.this,check_task_type.get(i).getDicValueName());
                                    alertDialog.dismiss();
                                }
                            }).create();
                    alertDialog.show();
                }


            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String checkAddr = SPHelper.getHelper(SetCheckActivity.this).getCheckAddr();
            String checkType = SPHelper.getHelper(SetCheckActivity.this).getCheckType();
            if (StringUtil.isNullOrEmpty(checkAddr)) {
                ToastUtil.showToast(SetCheckActivity.this, "请选择检查地址");
                return true;
            }
            if (StringUtil.isNullOrEmpty(checkType)) {
                ToastUtil.showToast(SetCheckActivity.this, "请选择勤务级别");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
