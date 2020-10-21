package com.qhd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.acc.common.Constants;
import com.acc.common.myokhttp.util.LogUtils;
import com.acc.common.util.AccTool;
import com.acc.common.util.AndroidUtils;
import com.acc.common.util.PermissionUtils;
import com.acc.common.util.SPHelper;
import com.acc.common.util.ToastUtil;
import com.acc.sanheapp.R;
import com.anrong.sdk.SDKService;
import com.anrong.sdk.callback.InitCallBack;
import com.anrong.sdk.callback.ZipCallBack;

import java.io.File;
import java.text.NumberFormat;


public class LoadingActivity extends Activity {

    private final static String TAG = "LoadingActivity";

    private TextView txt;
    boolean download;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        if (!PermissionUtils.checkPermission(this, Constants.REQUEST_PERMISSION_EXTERNAL_STORAGE, 0, "应用升级需要存储权限")) {
            return;
        }
        txt = this.findViewById(R.id.txt);

        download = getIntent().getBooleanExtra("download", false);
        if (download) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    txt.setText("正在解压，请等待");
                    explode();
                }
            }, 1000);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    copyAnrongDbFile();
                    initSDK();
                }
            }, 1000);
        }


    }

    private void copyAnrongDbFile() {
        String path = Environment.getExternalStorageDirectory() + "/anrong/infocheck/datas/" + "anrongtec.db";
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            AndroidUtils.copyAssetFile(this, "anrongtec.db", path);
        }
    }

    /**
     * 初始化核查SDK
     */
    private void initSDK() {
        String appRootPath = Environment.getExternalStorageDirectory().toString();
        //获取设备号
        //TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        //String szImei = TelephonyMgr.getDeviceId();
        String szImei = "357952007458566";
        SDKService.init(getApplication(), appRootPath, szImei, "anrong", new InitCallBack() {

            @Override
            public void onSuccess(int arg0) {
                setResult(RESULT_OK, new Intent());
                LoadingActivity.this.finish();
                LogUtils.d("load==============" + "initonSuccess");
            }

            @Override
            public void onNeedExplode(int arg0) {
                explode();
                LogUtils.d("load==============" + "initonNeedExplode");
            }

            @Override
            public void onError(int arg0, String arg1) {
                AccTool.showToast(getApplicationContext(), arg1);
                LoadingActivity.this.finish();
                LogUtils.d("load==============" + "initonError");
            }
        });
    }

    /**
     * 解压警示信息数据包
     */
    public void explode() {
        SDKService.explode(LoadingActivity.this, new ZipCallBack() {

            @Override
            public void onSuccess() {
                if (download) {
                    ToastUtil.showToast(LoadingActivity.this, "解压完毕");
                }
                setResult(RESULT_OK, new Intent());
                LoadingActivity.this.finish();
                txt.setText("100%");
                SPHelper.getHelper(LoadingActivity.this).setExplodeTime(System.currentTimeMillis());
                LogUtils.d("load==============" + "explodeonSuccess");
            }

            @Override
            public void onReckonSizeFinished(long arg0) {
            }

            @Override
            public void onProgress(int arg0, long arg1, long arg2, String arg3) {
                Message message = new Message();
                message.what = 1;
                Bundle b = new Bundle();
                b.putLong("curr", arg1);
                b.putLong("total", arg2);
                message.setData(b);
                mHandler.sendMessage(message);
            }

            @Override
            public boolean onPrepare() {
                Log.i(TAG, "onPrepare");
                return false;
            }

            @Override
            public void onError(int arg0, String arg1) {
                if (download) {
                    ToastUtil.showToast(LoadingActivity.this, "解压完毕");
                }
                Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED, new Intent());
                LoadingActivity.this.finish();
                LogUtils.d("load==============" + "explodeononError");
            }
        });
    }

    /**
     * 设置解压进度百分比
     */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // String abc = (String) msg.getData().get("abc");
                    long curr = msg.getData().getLong("curr");
                    long total = msg.getData().getLong("total");

                    NumberFormat nt = NumberFormat.getPercentInstance();
                    // 设置百分数精确度2即保留两位小数
                    nt.setMinimumFractionDigits(2);
                    // nt.format((float) curr/total);
                    txt.setText(nt.format((float) curr / total));
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            // 由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
			/*Toast.makeText(TestActivity.this, "Home", Toast.LENGTH_SHORT).show();
			return false;*/
        }
        return super.onKeyDown(keyCode, event);
    }

}