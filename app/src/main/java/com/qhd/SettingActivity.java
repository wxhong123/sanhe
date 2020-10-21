package com.qhd;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acc.common.URL;
import com.acc.common.base.MyApplication;
import com.acc.common.myokhttp.response.RawResponseHandler;
import com.acc.common.util.AndroidUtils;
import com.acc.common.util.AppInfo;
import com.acc.common.util.JumpUtil;
import com.acc.common.util.ToastUtil;
import com.acc.common.util.UpdateAppHttpUtil;
import com.acc.common.util.WpsModel;
import com.acc.common.widget.CustomerDialog;
import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.response.BaseResponse;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import java.io.File;


/**
 * Created by androider on 2018/8/1.
 * 内容：
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout edition, book, check, info, download, checkset;
    TitleBar titleBar;
    TextView tvUpdate, tv, tv_quit;
    TextView tv_appinfo;
    boolean isUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        initview();
        getVersioninfo();
    }

    private void initview() {
        titleBar = findViewById(R.id.titlebar);
        titleBar.setBack(true);
        titleBar.setTitle("软件设置");

        edition = findViewById(R.id.ll_edition);
        edition.setVisibility(View.GONE);
        book = findViewById(R.id.ll_book);
        check = findViewById(R.id.ll_check);
        info = findViewById(R.id.ll_info);
        checkset = findViewById(R.id.ll_setcheck);
        download = findViewById(R.id.ll_download);
        tv_quit = findViewById(R.id.tv_quit);
        tvUpdate = findViewById(R.id.tv_edition);
        tv = findViewById(R.id.tv);
        tv_appinfo = findViewById(R.id.tv_appinfo);

        AppInfo appInfo = AppInfo.getInstance();
        String versionName = appInfo.getVersionName();
        tv_appinfo.setText(String.format("应用信息:%s%s", getString(R.string.app_name), versionName));

        check.setOnClickListener(this);
        edition.setOnClickListener(this);
        info.setOnClickListener(this);
        book.setOnClickListener(this);
        download.setOnClickListener(this);
        checkset.setOnClickListener(this);
        tv_quit.setOnClickListener(this);

        progressDialog = new CustomerDialog(SettingActivity.this, R.style.MyDialog);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_book:
                showProgressDialog("");
//                String path = AndroidUtils.getFilePath(this, "jingwuppt.pptx");
                String path = Environment.getExternalStorageDirectory().toString() + File.separator + "jingwuppt.pptx";
                AndroidUtils.copyAssetFile(this, "jingwuppt.pptx", path);
                dismissProgressDialog();
                boolean flag = openFile(path);
                if (flag == true) {
                    Toast.makeText(SettingActivity.this, " 打开文件成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingActivity.this, "打开文件失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_info:
                JumpUtil.jumptoact(this, APPINFOActivity.class);
                break;
            case R.id.ll_check:
                JumpUtil.jumptoact(this, ChangeCheckActivity.class);
                break;
            case R.id.ll_edition:
                JumpUtil.jumptoact(this, ChangeNetActivity.class);
                break;
            case R.id.ll_download:
                if (isUpdate) {
                    getNewApp();
                } else {
                    ToastUtil.showToast(this, "当前已是最新版本");
                }
                break;
            case R.id.ll_setcheck:
                JumpUtil.jumptoact(this, SetCheckActivity.class);
                break;
            case R.id.tv_quit:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("确认退出？")
                        .setPositiveButton(" 是 ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                                intent.putExtra("quit", true);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(" 否 ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();

                break;

            default:
                break;
        }
    }


    boolean openFile(String path) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL); // 打开模式
        bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // 关闭时是否发送广播
        bundle.putString(WpsModel.THIRD_PACKAGE, getPackageName()); // 第三方应用的包名，用于对改应用合法性的验证
        bundle.putBoolean(WpsModel.CLEAR_TRACE, true);// 清除打开记录
        // bundle.putBoolean(CLEAR_FILE, true); //关闭后删除打开文件
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);

        File file = new File(path);
        if (file == null || !file.exists()) {
            System.out.println("文件为空或者不存在");
            return false;
        }

        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtras(bundle);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            System.out.println("打开wps异常：" + e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void getNewApp() {
        new UpdateAppManager
                .Builder()
                .setActivity(this)
                .setHttpManager(new UpdateAppHttpUtil())
                .setUpdateUrl(URL.host + URL.UPDATEAPP)
                .setPost(false)
                .hideDialogOnDownloading()
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
                .setTopPic(R.mipmap.ic_biglauncher)
                .build()
                .checkNewApp(new UpdateCallback() {
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        Log.d("myresonse", "====>>>new===" + json);
                        BaseResponse response = JSON.parseObject(json, BaseResponse.class);
                        String isUpdate = "No";
                        String version = "";
                        String url = "";
                        String log = "";
                        BaseResponse.DataBean data = response.getData();
                        if (data != null) {
                            isUpdate = "Yes";
                            version = data.getVersionName();
                            url = URL.RootUrl + data.getApkPath();
                            log = data.getVersionDesc();
                        }
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate(isUpdate)
                                    //（必须）新版本号，
                                    .setNewVersion(version)
                                    //（必须）下载地址
                                    .setApkFileUrl(url)
                                    .setUpdateLog(log);
//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                    }

                    @Override
                    protected void noNewApp(String error) {
                        super.noNewApp(error);
                    }
                });

    }


    private void getVersioninfo() {
        String url = URL.host + URL.UPDATEAPP;
        AppInfo.init(MyApplication.application);
        final AppInfo appInfo = AppInfo.getInstance();

        MyApplication.mMyOkhttp.get()
                .url(url)
                .addParam("versionName", appInfo.getVersionName())
                .addParam("packageName", appInfo.getApplicationId())
                .addParam("versionCode", appInfo.getVersionCode())
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        tvUpdate.setText(appInfo.getVersionName());
                    }

                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.d("myresonse", "====>>>" + response);
                        BaseResponse bean = JSON.parseObject(response, BaseResponse.class);
                        if ("000".equals(bean.getCode()) && bean.getData() != null) {
                            BaseResponse.DataBean data = bean.getData();
                            tvUpdate.setText(data.getVersionName());
                            tv.setText("有新版本");
                            isUpdate = true;
                        } else {
                            tvUpdate.setText(appInfo.getVersionName());
                        }

                    }
                });
    }

    private CustomerDialog progressDialog;

    public void showProgressDialog(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog.setMessage(msg);
        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    public void dismissProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing() == true) {
            progressDialog.dismiss();
        }
    }


}
