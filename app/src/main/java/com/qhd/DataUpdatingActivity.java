package com.qhd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acc.common.Constants;
import com.acc.common.URL;
import com.acc.common.base.MyApplication;
import com.acc.common.myokhttp.DownloadMgr;
import com.acc.common.myokhttp.download_mgr.AbstractDownloadMgr;
import com.acc.common.myokhttp.download_mgr.DownloadStatus;
import com.acc.common.myokhttp.download_mgr.DownloadTask;
import com.acc.common.myokhttp.download_mgr.DownloadTaskListener;
import com.acc.common.util.DateUtil;
import com.acc.common.util.StringUtil;
import com.acc.common.util.ToastUtil;
import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;
import com.data.SourceJsPackage;
import com.data.SourceJsPackageDao;
import com.qhd.down.ProgressDownloader;
import com.qhd.down.ProgressResponseBody;
import com.service.syncdata.DownloadDataService;
import com.service.syncdata.SynceNotice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;


public class DataUpdatingActivity extends AppCompatActivity implements View.OnClickListener, SynceNotice, ProgressResponseBody.ProgressListener {
    private final static String TAG = "DataUpdateActivity";
    private TitleBar titlebar;
    private TextView txtGaVersion;
    private TextView txtGaReltxtDate;
    private TextView txtGaFilesize;
    private TextView txtGaVersoinDetail;
    private TextView txtLocalVersion;
    private TextView txtLocalReltxtDate;
    private TextView txtLocalFileCount;

    private TextView txtgadownload, txtgaunzip;
    private ProgressBar progressBarGaDonwload;
    private ProgressBar progressBarGaUnzip;
    private ProgressBar progressBarLocalDownload;
    private Button btnAllSync;

    private LinearLayout gaItems;
    private LinearLayout localItems;

    //Intnet 传过来人参数
    private int fileCount;
    private boolean isLocalNewVersion;
    private boolean isGaNewVersion;
    private String openPage;

    private int updataCount = 0;

    //数据文件下载
    private long breakPoints;
    private ProgressDownloader downloader;
    private File file;
    private long totalBytes;
    private long contentLength;

    Handler mMyHandler = new Handler();
    int MSG_DELAY_NOTIFICAION = 100;
    int UNZIP = 10086;

    SourceJsPackage gadata;

    TextView tv_down;
    //    boolean blockback = false;
//    boolean canDownLoading = true;
    boolean downLoadOk = false;
    DecimalFormat df;
    NetReceiver receiver;
    private DownloadTask mDownloadTask;
    private DownloadMgr mDownloadMgr;
    private DownloadTaskListener mDownloadTaskListener;

    private Handler requestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    updataCount++;
                    progressBarLocalDownload.setProgress(updataCount);
                    txtLocalFileCount.setText("当前更新：" + updataCount + "/" + fileCount + "总数");
                    break;
                case 2:
                    updataCount++;
                    progressBarLocalDownload.setProgress(updataCount);
                    txtLocalFileCount.setText("当前更新：" + updataCount + "/" + fileCount + "总数");
                    break;
                default:
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataupdating);
        df = new DecimalFormat("0.0%");
        Intent intent = getIntent();
        openPage = intent.getStringExtra("openPage");
        fileCount = intent.getIntExtra("fileCount", 0);
        isLocalNewVersion = intent.getBooleanExtra("isLocalNewVersion", false);
        isGaNewVersion = intent.getBooleanExtra("isGaNewVersion", false);
        initView();

        //点击打开省厅数据
        if (Constants.UPDATA_GA.equals(openPage)) {
            localItems.setVisibility(View.GONE);
            gadata = (SourceJsPackage) intent.getSerializableExtra("gaData");
            this.setGaTextValue(gadata);
        }

        //点击打开本地布控信息
        if (Constants.UPDATA_LOCAL.equals(openPage)) {
            gaItems.setVisibility(View.GONE);
            Map map = (Map) intent.getSerializableExtra("localDataInfo");
            this.setLocalTextValue(map);
        }

        //点击打开一键更新信息
        if (Constants.UPDATA_ALL.equals(openPage)) {
            gaItems.setVisibility(View.VISIBLE);
            localItems.setVisibility(View.VISIBLE);
            gadata = (SourceJsPackage) intent.getSerializableExtra("gaData");
            Map map = (Map) intent.getSerializableExtra("localDataInfo");
            this.setGaTextValue(gadata);
            this.setLocalTextValue(map);
        }
//        FileDownloader.setup(this);

        mDownloadMgr = MyApplication.application.getDownloadMgr();

        receiver = new NetReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);

        initDownloadListener();


    }

    private void initDownloadListener() {
        //显示activity时加入监听
        mDownloadTaskListener = new DownloadTaskListener() {
            @Override
            public void onStart(String taskId, long completeBytes, long totalBytes) {
                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
//                ToastUtil.showToast(DataUpdatingActivity.this, "下载开始");
                btnAllSync.setText("正在下载");
            }

            @Override
            public void onProgress(String taskId, long currentBytes, long totalBytes) {
                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
                if (!mMyHandler.hasMessages(MSG_DELAY_NOTIFICAION)) {
                    Message message = new Message();
                    message.what = MSG_DELAY_NOTIFICAION;
                    mMyHandler.sendMessageDelayed(message, 300);
                    if (progressBarGaDonwload == null) {
                        return;
                    }
                    txtgadownload.setText("" + df.format(currentBytes * 1.0 / totalBytes));
                    progressBarGaDonwload.setProgress((int) (currentBytes * 100.0 / totalBytes));
                }

            }

            @Override
            public void onPause(String taskId, long currentBytes, long totalBytes) {
                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
                ToastUtil.showToast(DataUpdatingActivity.this, "下载暂停");
            }

            @Override
            public void onFinish(String taskId, File file) {
                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);///mDownloadTask已经=null
                if (progressBarGaDonwload != null) {
                    txtgadownload.setText("" + (int) 100 + "%");
                    progressBarGaDonwload.setProgress((int) 100);
                    ToastUtil.showToast(DataUpdatingActivity.this, "下载完毕");
                    btnAllSync.setText("下载完毕");
                }

                explodeFile();
            }

            @Override
            public void onFailure(String taskId, String error_msg) {
//                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
//                Long completeBytes = mDownloadTask.getCompleteBytes();
                long currentBytes = mDownloadTask.getCurrentBytes();
                mDownloadTask.setCompleteBytes(currentBytes);
//                canDownLoading = true;
//                blockback = false;
//                tv_down.setVisibility(View.INVISIBLE);

                ToastUtil.showToast(DataUpdatingActivity.this, "网络连接异常");
                btnAllSync.setText("继续下载");
                boolean available = isNetworkAvailable(DataUpdatingActivity.this);
                if (available) {
                    if (mDownloadMgr != null && mDownloadTask != null) {
//                        ToastUtil.showToast(DataUpdatingActivity.this, "网络已连接，开始下载");
                        mDownloadMgr.startTask(mDownloadTask.getTaskId());
                    }
                } else {
//                ToastUtil.showToast(context, "网络连接断开");
                }
            }
        };

        mDownloadMgr.addListener(mDownloadTaskListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不显示时移除监听 放置内存泄露
        mDownloadMgr.removeListener(mDownloadTaskListener);
        mDownloadTaskListener = null;

        MyApplication.mMyOkhttp.cancel(DataUpdatingActivity.this);
        if (receiver != null) {
            unregisterReceiver(receiver); //取消监听
        }
    }

    private void initView() {
        titlebar = findViewById(R.id.titlebar);
        titlebar.setBack(true);
        titlebar.setTitle("数据同步");
        titlebar.findViewById(R.id.iv_back).setOnClickListener(this);

        txtgadownload = findViewById(R.id.txtgadownload);
        txtgaunzip = findViewById(R.id.txtgaunzip);

        txtGaVersion = findViewById(R.id.txtGaVersion);
        txtGaReltxtDate = findViewById(R.id.txtGaDate);
        txtGaFilesize = findViewById(R.id.txtGaFileSize);
        txtGaVersoinDetail = findViewById(R.id.txtVersionDetail);

        txtLocalVersion = findViewById(R.id.txtLocalVersion);
        txtLocalReltxtDate = findViewById(R.id.txtLocalDate);
        txtLocalFileCount = findViewById(R.id.txtLocalFileCount);
        btnAllSync = findViewById(R.id.btnAllSync);
        btnAllSync.setOnClickListener(this);
        gaItems = findViewById(R.id.gaItems);
        localItems = findViewById(R.id.localItems);
        tv_down = findViewById(R.id.tv_down);
        progressBarGaDonwload = findViewById(R.id.progressgadownload);
        progressBarGaUnzip = findViewById(R.id.progressgaunzip);
        progressBarLocalDownload = findViewById(R.id.progresslocaldownload);
    }

    public void setGaTextValue(SourceJsPackage gaDataInfo) {
        if (gaDataInfo != null) {
            txtGaVersion.setText(gaDataInfo.getVersionNo());
            txtGaFilesize.setText(StringUtil.bytes2kb(gaDataInfo.getFileSize()));
            txtGaReltxtDate.setText(DateUtil.getFullDateToString(gaDataInfo.getCreateDate()));
//            txtGaReltxtDate.setText(gaDataInfo.getCreateDate());
            txtGaVersoinDetail.setText(gaDataInfo.getFileDescribe());
        }
    }

    public void setLocalTextValue(Map map) {
        if (map != null && map.size() > 0) {
            txtLocalVersion.setText(map.get("ID").toString());
            txtLocalReltxtDate.setText(map.get("UPDATETIME").toString());
            txtLocalFileCount.setText(fileCount + "");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAllSync:
                //点击打开本地布控信息
                if (Constants.UPDATA_LOCAL.equals(openPage)) {
                    if (isLocalNewVersion) {
                        updataLocalData();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.noupdata), Toast.LENGTH_SHORT).show();
                    }
                }

                if (Constants.UPDATA_GA.equals(openPage)) {
                    if (isGaNewVersion) {
                        updataGaData();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.noupdata), Toast.LENGTH_SHORT).show();
                    }

                }
                //点击打开一键更新信息
                if (Constants.UPDATA_ALL.equals(openPage)) {
                    if (isGaNewVersion || isLocalNewVersion) {
                        if (isGaNewVersion) updataGaData();
                        if (isLocalNewVersion) updataLocalData();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.noupdata), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.iv_back:
                if (isGaNewVersion && !downLoadOk) {
                    AlertDialog dialog = new AlertDialog.Builder(this).setTitle("退出将会取消本次下载，确定？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (mDownloadTask != null) {
//                                mDownloadMgr.removeListener(mDownloadTaskListener);
//                                mDownloadTaskListener = null;
                                        mDownloadMgr.deleteTask(mDownloadTask.getTaskId());
                                        mDownloadTask.doDestroy();
                                    }
                                    finish();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                } else {
                    finish();
                    return;
                }

                break;
            default:
                break;
        }
    }

    private void updataGaData() {
//        if (sourceJsPackage != null) {
//            breakPoints = 0L;
//            String PACKAGE_URL = URL.host + sourceJsPackage.getFilePath();
//            file = new File(Environment.getExternalStorageDirectory().toString() + "/anrong/infocheck/datas/", "sample.apk");
//            downloader = new ProgressDownloader(PACKAGE_URL, file, this);
//            downloader.download(0L);
//        }

//        Intent intent = new Intent(DataUpdatingActivity.this, DownstdataService.class);
//        intent.putExtra("name", gadata.getFileName());
//        intent.putExtra("path", gadata.getFilePath());
//        intent.putExtra("bean", gadata);
//        startService(intent);


        /******************************************************************************/
//        if (!canDownLoading) {
//            ToastUtil.showToast(this, "正在下载");
//            return;
//        }
//
        if (gadata == null) {
            ToastUtil.showToast(this, "数据有误，请退出到之前的页面");
            return;
        }


        if (downLoadOk) {
            ToastUtil.showToast(this, "数据更新完毕，请退出本页面");
            return;
        }


        String filePath = gadata.getFilePath();
//        BaseDownloadTask singleTask;
//        String downloadPath = Environment.getExternalStorageDirectory() + "/wholePackage.zip";
        String url = URL.RootUrl + filePath;

//        canDownLoading = false;
//        blockback = true;
//        tv_down.setVisibility(View.VISIBLE);

        /*****************************************************************/

        if (mDownloadTask == null) {     //还没有加入任务
            File file = new File(Environment.getExternalStorageDirectory() + "/wholePackage.zip");
            if (file.exists()) {
                file.delete();
            }

            AbstractDownloadMgr.Task task = new AbstractDownloadMgr.Task();
            task.setTaskId(mDownloadMgr.genTaskId());
            task.setUrl(url);
            task.setFilePath(Environment.getExternalStorageDirectory() + "/wholePackage.zip");
            task.setCompleteBytes(0L);      //如果新加任务的可以不设置 默认是0L（当恢复任务的时候需要设置已经完成的bytes 从本地读取）
            task.setDefaultStatus(DownloadMgr.DEFAULT_TASK_STATUS_START);       //默认添加进对了自动开始
            mDownloadTask = mDownloadMgr.addTask(task);
            ToastUtil.showToast(this, "点击下载");
        } else {
            switch (mDownloadTask.getStatus()) {
                case DownloadStatus.STATUS_DOWNLOADING:
                    ToastUtil.showToast(this, "正在下载");
                    break;

                case DownloadStatus.STATUS_FAIL:       //开始任务
                    mDownloadMgr.startTask(mDownloadTask.getTaskId());
                    ToastUtil.showToast(this, "点击下载");
                    break;
                case DownloadStatus.STATUS_FINISH:
                    ToastUtil.showToast(this, "下载完毕");
                    break;
                default:
                    break;
            }
        }
        /*****************************************************************/

    }


    private void explodeFile() {
        boolean copyFile = copyFile(Environment.getExternalStorageDirectory() + "/wholePackage.zip",
                Environment.getExternalStorageDirectory() + "/anrong/infocheck/datas/" + "wholePackage.zip");

        if (copyFile) {
            Intent intent = new Intent();
            intent.setClass(this, LoadingActivity.class);// 跳转到加载界面
            intent.putExtra("download", true);
            startActivityForResult(intent, UNZIP);

        } else {
            ToastUtil.showToast(this, "解压失败，请重新下载");

            mDownloadTask = null;
//            canDownLoading = true;
//            blockback = false;
//            tv_down.setVisibility(View.INVISIBLE);
            if (progressBarGaDonwload == null) {
                return;
            }
            txtgadownload.setText("0");
            progressBarGaDonwload.setProgress(0);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNZIP) {
//            canDownLoading = true;
//            blockback = false;

            if (resultCode == RESULT_OK) {
                tv_down.setVisibility(View.INVISIBLE);
                deleteDownloadFile(Environment.getExternalStorageDirectory() + "/wholePackage.zip");
                SourceJsPackageDao sourceJsPackageDao = new SourceJsPackageDao(this);
                int add = sourceJsPackageDao.add(gadata);
                downLoadOk = true;
                ToastUtil.showToast(this, "数据更新完毕，请退出本页面");
            } else {
                ToastUtil.showToast(this, "解压失败，请重新下载");
                mDownloadTask = null;

                if (progressBarGaDonwload == null) {
                    return;
                }
                txtgadownload.setText("0");
                progressBarGaDonwload.setProgress(0);
            }
        }
    }

    //屏蔽返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (blockback) {
//                ToastUtil.showToast(this, "正在下载，不能返回");
//                return blockback;
//            } else {
//                return super.onKeyDown(keyCode, event);
//            }
            if (isGaNewVersion && !downLoadOk) {
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("退出将会取消本次下载，确定？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mDownloadTask != null) {
                                    mDownloadMgr.deleteTask(mDownloadTask.getTaskId());
                                    mDownloadTask.doDestroy();
                                }
                                finish();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            } else {
                finish();
            }


        }
        return false;
    }


    private void updataLocalData() {
        DownloadDataService downloadDataService = new DownloadDataService(this, requestHandler);
        if (downloadDataService.isRun()) {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.updatingmsg), Toast.LENGTH_SHORT).show();
        } else {
            progressBarLocalDownload.setMax(fileCount);
            new Thread(downloadDataService).start();
            progressBarLocalDownload.setMax(fileCount);
            txtLocalFileCount.setText("当前更新：" + fileCount + "/" + fileCount + "总数");
        }
    }

    @Override
    public void notice() {
        updataCount++;
        progressBarLocalDownload.setProgress(updataCount);
        txtLocalFileCount.setText("当前更新：" + updataCount + "/" + fileCount + "总数");
    }

    @Override
    public void onPreExecute(long contentLength) {
        // 文件总长只需记录一次，要注意断点续传后的contentLength只是剩余部分的长度
        if (this.contentLength == 0L) {
            this.contentLength = contentLength;
            progressBarGaDonwload.setMax((int) (contentLength / 1024));
        }
    }

    @Override
    public void update(long totalBytes, boolean done) {
        // 注意加上断点的长度
        this.totalBytes = totalBytes + breakPoints;
        progressBarGaDonwload.setProgress((int) (totalBytes + breakPoints) / 1024);
        if (done) {
            // 切换到主线程
            Observable
                    .empty()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(new Action() {
                        @Override
                        public void run() {
                            Toast.makeText(DataUpdatingActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .subscribe();
        }
    }


    public static class ProgressEvent {
        private float progress;

        public ProgressEvent(float progress) {
            this.progress = progress;
        }

        public float getProgress() {
            return progress;
        }

        public void setProgress(float progress) {
            this.progress = progress;
        }
    }

    private void deleteDownloadFile(String s) {
        File file = new File(s);
        if (file.exists() && file.isFile()) {
            boolean delete = file.delete();
            if (delete) {
//                ToastUtil.showToast(this,"删除原下载文件");
            }
        }
    }

    public boolean copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }

        /* 如果不需要打log，可以使用下面的语句
        if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
            return false;
        }
        */

            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);    //读入原文件
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while ((byteRead = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    class NetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean available = isNetworkAvailable(context);
            if (available) {
                if (mDownloadMgr != null && mDownloadTask != null) {
                    ToastUtil.showToast(context, "网络变化");
                    mDownloadMgr.startTask(mDownloadTask.getTaskId());
                }
            } else {
//                ToastUtil.showToast(context, "网络连接断开");
            }
        }

    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.i("NetWorkState", "Unavailabel");
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.i("NetWorkState", "Availabel");
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
