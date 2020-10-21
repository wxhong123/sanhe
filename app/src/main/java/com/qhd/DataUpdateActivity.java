package com.qhd;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Utils.SanDaoFangXianUtils;
import com.acc.common.Constants;
import com.acc.common.URL;
import com.acc.common.base.MyApplication;
import com.acc.common.myokhttp.response.RawResponseHandler;
import com.acc.common.util.AccTool;
import com.acc.common.util.DateUtil;
import com.acc.common.util.SPHelper;
import com.acc.common.util.ToastUtil;
import com.acc.common.widget.CustomerDialog;
import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;
import com.alibaba.fastjson.JSON;
import com.data.Car;
import com.data.CarDao;
import com.data.Person;
import com.data.PersonDao;
import com.data.SourceJsPackage;
import com.data.SourceJsPackageDao;
import com.data.SyncTable;
import com.data.SyncTableDao;

import org.apache.commons.lang.math.NumberUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hbfec.checklib.CheckManager;
import cn.hbfec.checklib.model.DataSynchVersion;
import cn.hbfec.checklib.socket.SynchSocketService;
import cn.hbfec.checklib.socket.WebSocketBinder;
import cn.qqtheme.framework.AppConfig;


public class DataUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "DataUpdateActivity";
    private TitleBar titlebar;
    private TextView txtGaVersion, txtSdfxVersion, txtSdfxDate, txtSdfxSyncDate, txtSdfxSyncState;
    private TextView txtGaReltxtDate;
    private TextView txtGaSyncDate;
    private TextView txtGaDataState;
    private TextView txtLocalVersion;
    private TextView txtLocalReltxtDate;
    private TextView txtLocalSyncDat;
    private TextView txtLocalDataState;
    private Button btnAllSync1, btnAllSync2, btnAllSync3, btnAllSync4, upload, uploadOnLine;
    private LinearLayout gaItems, sandaofangxian;
    private LinearLayout localItems;
    private CustomerDialog progressDialog;

    private boolean isGaNewVersion = false;
    private boolean isLocalNewVersion = false;

    private Map localDataInfo;
    private SourceJsPackage gaDataInfo;

    //记录可更新条数
    private int fileCount;
    private int localCount = 0;

    SourceJsPackage gadata;

    private WebSocketBinder webSocketBinder;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    SanDaoFangXianUtils.SyncData(DataUpdateActivity.this, webSocketBinder, "SynchData");
                    break;
                case 500:
                    ToastUtil.showToastLong(DataUpdateActivity.this, "同步数据使用的账户或密码不正确");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataupdate);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }


    //初始化页面控件
    public void initView() {
        initDialog();

        titlebar = findViewById(R.id.titlebar);
        titlebar.setBack(true);
        titlebar.setTitle("数据更新");
        titlebar.setBack(true);

        txtSdfxVersion = findViewById(R.id.txtSdfxVersion);
        txtSdfxDate = findViewById(R.id.txtSdfxDate);
        txtSdfxSyncDate = findViewById(R.id.txtSdfxSyncDate);
        txtSdfxSyncState = findViewById(R.id.txtSdfxSyncState);

        txtGaVersion = findViewById(R.id.txtGaVersion);
        txtGaReltxtDate = findViewById(R.id.txtGaDate);
        txtGaSyncDate = findViewById(R.id.txtGaSyncDate);
        txtGaDataState = findViewById(R.id.txtGaSyncState);

        txtLocalVersion = findViewById(R.id.txtLocalVersion);
        txtLocalReltxtDate = findViewById(R.id.txtLocalDate);
        txtLocalSyncDat = findViewById(R.id.txtLocalSyncDate);
        txtLocalDataState = findViewById(R.id.txtLocalSyncState);

        btnAllSync1 = findViewById(R.id.btnAllSync1);
        btnAllSync2 = findViewById(R.id.btnAllSync2);
        btnAllSync3 = findViewById(R.id.btnAllSync3);
        btnAllSync4 = findViewById(R.id.btnAllSync4);
        upload = findViewById(R.id.upload);
        uploadOnLine = findViewById(R.id.uploadOnLine);
        gaItems = findViewById(R.id.gaItems);
        sandaofangxian = findViewById(R.id.sandaofangxian);
        sandaofangxian.setVisibility(AppConfig.USE_SDFX ? View.VISIBLE : View.GONE);
        localItems = findViewById(R.id.localItems);
        btnAllSync1.setOnClickListener(this);
        btnAllSync2.setOnClickListener(this);
        btnAllSync3.setOnClickListener(this);
        btnAllSync4.setOnClickListener(this);
        upload.setOnClickListener(this);
        uploadOnLine.setOnClickListener(this);
        gaItems.setOnClickListener(this);
        localItems.setOnClickListener(this);
    }

    public void initData() {
        //初始化三河布控数据
        initLocalDataVersion();
        //检测省厅更新数据
//        initGaDataVersion();
        //绑定三道防线服务
        BindService();
        //检查三道防线数据库
        initSdfxData();
    }

    /**
     * 绑定三道防线数据管理服务
     */
    public void BindService() {
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                webSocketBinder = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                webSocketBinder = (WebSocketBinder) service;
            }
        };
        //绑定服务
        Intent bindIntent = new Intent(this, SynchSocketService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    //检查三道防线数据库
    private void initSdfxData() {
        CheckManager checkManager = new CheckManager(this);
        DataSynchVersion dataSynchVersion = checkManager.loadDataSynchVersion();
        txtSdfxVersion.setText(String.valueOf(dataSynchVersion.getPersonVerson()));
        txtSdfxDate.setText(AccTool.getStringDateShort("yyyy") + "-" + dataSynchVersion.getPersonTime());
        txtSdfxSyncDate.setText(String.valueOf(dataSynchVersion.getCarVersion()));
        txtSdfxSyncState.setText(AccTool.getStringDateShort("yyyy") + "-" + dataSynchVersion.getCarTime());
    }

    private void initLocalDataVersion() {

//         Car cars = AppDataBase.getInstance(getApplicationContext()).carDao().queryLastCar();
//         System.out.println(cars);
        //布控数据，直接查询同步数据
        SyncTableDao syncTableDao = new SyncTableDao(this);
        SyncTable syncTable = syncTableDao.queryLastData();
        if (syncTable != null) {
            txtLocalVersion.setText(syncTable.getUpdateNumber() + "");
            String time = syncTable.getLastupdate();
            Date date = DateUtil.getLongFormatDate(Long.valueOf(time));
            String fullSyncDate = DateUtil.getFullDateToString(date);
            txtLocalSyncDat.setText(fullSyncDate);
            txtLocalReltxtDate.setText(fullSyncDate);
            localCount = syncTable.getUpdateNumber();
            //查询最新一记录的同步时间
            if ("SOURCE_JS_PERSON".equals(syncTable.getTableName())) {
                PersonDao personDao = new PersonDao(this);// AppDataBase.getInstance(this).personDao();
                Person person = personDao.queryLastPerson();
                if (person != null) {
                    txtLocalReltxtDate.setText(person.getCreateDate());
                } else {

                    txtLocalReltxtDate.setText(fullSyncDate);
                }
            } else if ("SOURCE_JS_CAR".equals(syncTable.getTableName())) {
                CarDao bkCarDao = new CarDao(this);//AppDataBase.getInstance(this).carDao();
                Car car = bkCarDao.queryLastCar();
                if (car != null) {
                    txtLocalReltxtDate.setText(car.getCreateDate());
                } else {
                    txtLocalReltxtDate.setText(fullSyncDate);
                }
            }
        }
        //查询后台有无新数据
        if (SPHelper.getHelper(this).isOnline()) {
            List<SyncTable> syncTables = syncTableDao.queryAll();
            StringBuilder namebuilder = new StringBuilder();
            String tableName = "";
            if (syncTables != null && syncTables.size() > 0) {
                for (SyncTable e : syncTables) {
                    namebuilder.append(e.getTableName() + ",");
                }
                tableName = namebuilder.substring(0, namebuilder.length() - 1);
            }
            MyApplication.mMyOkhttp.get().url(URL.host + URL.SYNC_KUKONG_DATA_URL + "?tableName=" + tableName + "&updateNumber=" + localCount).enqueue(new RawResponseHandler() {
                @Override
                public void onFailure(int statusCode, String error_msg) {
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.request_fail_msg), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, error_msg);
                    txtLocalDataState.setTextColor(Color.parseColor("#9D9D9D"));
                    isLocalNewVersion = false;
                }

                @Override
                public void onSuccess(int statusCode, String response) {
                    if (200 == statusCode) {
                        //没有返回数据
                        if (!TextUtils.isEmpty(response)) {
                            Map map = JSON.parseObject(response, Map.class);
                            if (map != null && map.size() > 0) {
                                txtLocalDataState.setText("当前数据有最新版本为：" + map.get("ID").toString());
                                txtLocalDataState.setTextColor(Color.RED);
                                isLocalNewVersion = true;
                                localDataInfo = map;
                                fileCount = NumberUtils.toInt(map.get("CHANGECOUNT").toString());
                            } else {
                                txtLocalDataState.setText("当前数据已为最新版本");
                                txtLocalDataState.setTextColor(Color.parseColor("#9D9D9D"));
                                isLocalNewVersion = false;
                            }
                        } else {
                            txtLocalDataState.setText("当前数据已为最新版本");
                            txtLocalDataState.setTextColor(Color.parseColor("#9D9D9D"));
                            isLocalNewVersion = false;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.request_fail_msg), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, response);
                        txtLocalDataState.setTextColor(Color.parseColor("#9D9D9D"));
                        isLocalNewVersion = false;
                    }
                }
            });
        } else {
            txtLocalDataState.setText("当前处理离线状态或网络不可用！");
        }

    }

    //远程查询省厅离线数据
    public void initGaDataVersion() {
        SourceJsPackageDao sourceJsPackageDao = new SourceJsPackageDao(this);
        SourceJsPackage sourceJsPackage = sourceJsPackageDao.queryLastPackage();
        List<SourceJsPackage> sourceJsPackages = sourceJsPackageDao.queryAll();
        String versionNo = "0";
        if (sourceJsPackage == null) {
            txtGaVersion.setText("当前无离线数据");
            txtGaSyncDate.setText("当前无离线数据");
            txtGaReltxtDate.setText("当前无离线数据");
        } else {
            txtGaVersion.setText(sourceJsPackage.getVersionNo());
            txtGaSyncDate.setText(DateUtil.getFullDateToString(sourceJsPackage.getUpdateDate()));
            txtGaReltxtDate.setText(DateUtil.getFullDateToString(sourceJsPackage.getCreateDate()));
//            txtGaSyncDate.setText(sourceJsPackage.getCreateDate());
//            txtGaReltxtDate.setText(sourceJsPackage.getUpdateDate());
            versionNo = sourceJsPackage.getVersionNo();
        }
        //请求服务器最新数据包
        //查询后台有无新数据
        if (SPHelper.getHelper(this).isOnline()) {
            MyApplication.mMyOkhttp.get()
                    .url(URL.host + URL.SYNC_OFFLINE_DATA_URL)
                    .addParam("versionNo", versionNo)
                    .enqueue(new RawResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, String error_msg) {
                            Toast.makeText(getApplicationContext(), getResources().getText(R.string.request_fail_msg), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, error_msg);
                            txtGaDataState.setText("当前处理离线状态或网络不可用！");
                            txtGaDataState.setTextColor(Color.parseColor("#9D9D9D"));
                            isGaNewVersion = false;
                            gadata = null;
                        }

                        @Override
                        public void onSuccess(int statusCode, String response) {
                            if (200 == statusCode) {
                                UpdateDataBean updateDataBean = JSON.parseObject(response, UpdateDataBean.class);
//                                JsonDataResult<JSONObject> result = JSON.parseObject(response, JsonDataResult.class);
//                                //判断返回数据是否正常返回
                                SourceJsPackage data = updateDataBean.getData();
                                if (data != null && Constants.sucessCode.equals(updateDataBean.getCode())) {
//                                    SourceJsPackage sourceJsPackage = result.getData().toJavaObject(SourceJsPackage.class);
                                    //数据数据不为空，说明无新数据包
                                    txtGaDataState.setText("当前数据有最新版本为：" + data.getVersionNo());
                                    txtGaDataState.setTextColor(Color.RED);
                                    isGaNewVersion = true;
                                    gadata = data;
                                } else {
                                    txtGaDataState.setText("当前数据已为最新版本");
                                    txtGaDataState.setTextColor(Color.parseColor("#9D9D9D"));
                                    isGaNewVersion = false;
                                    gadata = null;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getText(R.string.request_fail_msg), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, response);
                                txtGaDataState.setText("当前处理离线状态或网络不可用！");
                                txtGaDataState.setTextColor(Color.parseColor("#9D9D9D"));
                                isGaNewVersion = false;
                                gadata = null;
                            }

                        }
                    });
        } else {
            txtGaDataState.setText("当前处理离线状态或网络不可用！");
            txtGaDataState.setTextColor(Color.parseColor("#9D9D9D"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAllSync1:
            case R.id.gaItems:
                if (isGaNewVersion) {
//                    Intent intent = new Intent(this, DataUpdatingActivity.class);
//                    intent.putExtra("gaDataInfo", (Serializable) gaDataInfo);
//                    intent.putExtra("openPage", Constants.UPDATA_GA);
//                    intent.putExtra("isGaNewVersion", isGaNewVersion);
//                    startActivity(intent);
                    Intent allintent = new Intent(this, DataUpdatingActivity.class);
                    allintent.putExtra("gaData", (Serializable) gadata);
                    allintent.putExtra("openPage", Constants.UPDATA_GA);
                    allintent.putExtra("isGaNewVersion", isGaNewVersion);
                    allintent.putExtra("fileCount", fileCount);
                    startActivity(allintent);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.noupdata), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnAllSync2:
            case R.id.localItems:
                if (isLocalNewVersion) {
                    Intent localintent = new Intent(this, DataUpdatingActivity.class);
                    localintent.putExtra("localDataInfo", (Serializable) localDataInfo);
                    localintent.putExtra("openPage", Constants.UPDATA_LOCAL);
                    localintent.putExtra("isLocalNewVersion", isLocalNewVersion);
                    localintent.putExtra("fileCount", fileCount);
                    startActivity(localintent);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.noupdata), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnAllSync3:
                SanDaoFangXianUtils.SyncData(this, webSocketBinder, mHandler, Constants.SDFX_USERNAME, Constants.SDFX_PASSWORD);
                break;
            case R.id.btnAllSync4:
                SanDaoFangXianUtils.updateDataOnLine(this, Constants.SDFX_USERNAME, Constants.SDFX_PASSWORD);
                break;
            case R.id.upload:
                SanDaoFangXianUtils.UploadData(this, webSocketBinder);
                break;
            case R.id.uploadOnLine:
                SanDaoFangXianUtils.uploadDataOnLine(this);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void initDialog() {
        progressDialog = new CustomerDialog(DataUpdateActivity.this, R.style.MyDialog);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

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
