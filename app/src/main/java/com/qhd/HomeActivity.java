package com.qhd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acc.common.Constants;
import com.acc.common.URL;
import com.acc.common.base.MyApplication;
import com.acc.common.myokhttp.response.RawResponseHandler;
import com.acc.common.myokhttp.util.LogUtils;
import com.acc.common.util.JumpUtil;
import com.acc.common.util.SPHelper;
import com.acc.common.util.StringUtil;
import com.acc.common.util.ToastUtil;
import com.acc.common.util.UpdateAppHttpUtil;
import com.acc.common.widget.CustomerDialog;
import com.acc.common.widget.TitleBar;
import com.acc.sanheapp.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.arong.swing.db.entity.StaffUserVO;
import com.data.BaseDicValue;
import com.data.BaseDicValueDao;
import com.data.SourceJsPackage;
import com.data.SourceJsPackageDao;
import com.response.BaseResponse;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import guoTeng.readCard.readCard;

/**
 * 主界面，用于功能路由。
 * 功能：跳转人员核查、跳转车辆核查、跳转核查记录
 *
 * @author biehuan
 * @date 2016-11-07
 * @since 1.0
 */
public class HomeActivity extends AppCompatActivity implements OnClickListener {

    private ImageView ivHead;
    private LinearLayout imgPerson;
    private LinearLayout imgCar;
    private LinearLayout imgRecord, record;
    private LinearLayout ll_refresh;
    private LinearLayout ll_setting;
    private TitleBar titleBar;
    private TextView tvname, tvpoliceid, tvdanwei, tv_level, tv_addr;

    private CustomerDialog progressDialog;


    BaseDicValueDao baseDicValueDao;
    List<BaseDicValue> check_address;
    List<BaseDicValue> check_task_type;
    private AlertDialog alertDialog;
    AlertDialog typeDialog;
    AlertDialog addrDialog;
    //    private CustomerDialog alertDialog;
    AlertDialog dataDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initCheckInfo();
        deleteDB();
        getNewApp();

        initSetCheckInfoDialog();

//        getGAdataVersion();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getBooleanExtra("quit", false)) {
            finish();
            JumpUtil.jumptoact(this, LoginActivity.class);
        }
    }

    private void initCheckInfo() {
        baseDicValueDao = new BaseDicValueDao(this);
        check_address = baseDicValueDao.queryDictListByType("check_address");
        check_task_type = baseDicValueDao.queryDictListByType("check_task_type");
    }

    private void initSetCheckInfoDialog() {
        String checkAddr = SPHelper.getHelper(this).getCheckAddr();
        String checkType = SPHelper.getHelper(this).getCheckType();
        if (StringUtil.isNullOrEmpty(checkAddr) || StringUtil.isNullOrEmpty(checkType)) {
            ToastUtil.showToast(this, "请选择核查参数");

            String[] s = new String[]{"勤务级别", "检查地址"};
            alertDialog = new AlertDialog.Builder(this)
                    .setTitle("设置检查信息")
                    .setItems(s, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    dontCloseDialog(alertDialog);
                                    choosetask_type();
                                    break;
                                case 1:
                                    dontCloseDialog(alertDialog);
                                    chooseaddr();
                                    break;
//                                case 2:
//                                    dontCloseDialog(alertDialog);
//                                    chooseOk();
//                                    break;
                            }
                        }
                    })
                    .setNegativeButton("保存检查信息", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dontCloseDialog(alertDialog);
                            chooseOk();
                        }
                    }).create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

    }

    private void chooseaddr() {
        if (check_address != null && check_address.size() > 0) {
            List<String> addrs = new ArrayList<>();
            for (int i = 0; i < check_address.size(); i++) {
                BaseDicValue baseDicValue = check_address.get(i);
                addrs.add(baseDicValue.getDicValueName());
            }
            String[] s = new String[check_address.size()];
            addrs.toArray(s);
            int checkedItem = -1;
            String checkAddr = SPHelper.getHelper(HomeActivity.this).getCheckAddr();
            for (int i = 0; i < check_address.size(); i++) {
                BaseDicValue baseDicValue = check_address.get(i);
                if (StringUtil.equals(checkAddr, baseDicValue.getDicValueCode())) {
                    checkedItem = i;
                }
            }
            addrDialog = new AlertDialog.Builder(HomeActivity.this)
                    .setSingleChoiceItems(s, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SPHelper.getHelper(HomeActivity.this).setCheckAddr(check_address.get(i).getDicValueCode());
                            ToastUtil.showToast(HomeActivity.this, check_address.get(i).getDicValueName());
                            addrDialog.dismiss();
                        }
                    }).create();
            addrDialog.show();
        }
    }

    private void choosetask_type() {
        if (check_task_type != null && check_task_type.size() > 0) {
            List<String> types = new ArrayList<>();
            for (int i = 0; i < check_task_type.size(); i++) {
                BaseDicValue baseDicValue = check_task_type.get(i);
                types.add(baseDicValue.getDicValueName());
            }
            String[] s = new String[types.size()];
            types.toArray(s);
            int checkedItem = -1;
            String checkType = SPHelper.getHelper(HomeActivity.this).getCheckType();
            for (int i = 0; i < types.size(); i++) {
                BaseDicValue baseDicValue = check_task_type.get(i);
                if (StringUtil.equals(checkType, baseDicValue.getDicValueCode())) {
                    checkedItem = i;
                }
            }


            typeDialog = new AlertDialog.Builder(HomeActivity.this)
                    .setSingleChoiceItems(s, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SPHelper.getHelper(HomeActivity.this).setCheckType(check_task_type.get(i).getDicValueCode());
                            ToastUtil.showToast(HomeActivity.this, check_task_type.get(i).getDicValueName());
                            typeDialog.dismiss();
                        }
                    }).create();
            typeDialog.show();
        }
    }


    private void chooseOk() {
        String checkAddr = SPHelper.getHelper(HomeActivity.this).getCheckAddr();
        String checkType = SPHelper.getHelper(HomeActivity.this).getCheckType();
        if (StringUtil.isNullOrEmpty(checkAddr)) {
            ToastUtil.showToast(HomeActivity.this, "请选择核查地址");
            return;
        } else if (StringUtil.isNullOrEmpty(checkType)) {
            ToastUtil.showToast(HomeActivity.this, "请选择勤务级别");
            return;
        } else {
            String diccheckType = SPHelper.getHelper(HomeActivity.this).getCheckType();
            String check_task_type = baseDicValueDao.queryLableBydictTypeAndValue("check_task_type", diccheckType, "");
            tv_level.setText(check_task_type);

            String diccheckAddr = SPHelper.getHelper(HomeActivity.this).getCheckAddr();
            String check_address = baseDicValueDao.queryLableBydictTypeAndValue("check_address", diccheckAddr, "");
            tv_addr.setText(check_address);

            ToastUtil.showToast(HomeActivity.this, "保存成功");
//            alertDialog.dismiss();
            closeDialog(alertDialog);
        }
    }

    private void dontCloseDialog(AlertDialog dialog) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeDialog(AlertDialog dialog) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void initView() {
        imgPerson = findViewById(R.id.check_person);
        imgPerson.setOnClickListener(this);
        imgCar = findViewById(R.id.check_car);
        imgCar.setOnClickListener(this);
        imgRecord = findViewById(R.id.check_record);
        imgRecord.setOnClickListener(this);
        record = findViewById(R.id.record);
        record.setOnClickListener(this);
        ll_refresh = findViewById(R.id.ll_refresh);
        ll_refresh.setOnClickListener(this);
        ll_setting = findViewById(R.id.ll_setting);
        ll_setting.setOnClickListener(this);
        ivHead = findViewById(R.id.iv_head);

        titleBar = findViewById(R.id.titlebar);
        titleBar.setTitle(R.string.app_name);
        tvname = findViewById(R.id.tv_name);
        tvpoliceid = findViewById(R.id.tv_policeid);
        tvdanwei = findViewById(R.id.tv_danwei);
        tv_level = findViewById(R.id.tv_level);
        tv_addr = findViewById(R.id.tv_addr);

        StaffUserVO info = SPHelper.getHelper(this).getInfo();
        tvname.setText(info.getStaffName());
        tvpoliceid.setText(info.getStaffId());
        tvdanwei.setText(info.getDepartments());
        byte[] ZpData = info.getPhoto();
        if (ZpData == null)
            return;
        Bitmap bm = BitmapFactory.decodeByteArray(ZpData, 0, ZpData.length);
        ivHead.setImageBitmap(bm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String diccheckType = SPHelper.getHelper(this).getCheckType();
        String check_task_type = baseDicValueDao.queryLableBydictTypeAndValue("check_task_type", diccheckType, "");
        tv_level.setText(check_task_type);

        String diccheckAddr = SPHelper.getHelper(this).getCheckAddr();
        String check_address = baseDicValueDao.queryLableBydictTypeAndValue("check_address", diccheckAddr, "");
        tv_addr.setText(check_address);

    }

    private void deleteDB() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String dir = externalStorageDirectory + "/anrong/infocheck/datas/";
        File file = new File(dir);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isFile() && !f.getName().equals("anrongtec.db")) {
                        boolean delete = f.delete();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.check_person:
                intent.setClass(HomeActivity.this, readCard.class);
                startActivity(intent);
                break;
            case R.id.check_car:
                intent.setClass(HomeActivity.this, CheckCarActivity.class);
                startActivity(intent);
                break;
            case R.id.check_record:
                intent.setClass(HomeActivity.this, CheckRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.record:
                break;
            case R.id.ll_refresh:
                intent.setClass(HomeActivity.this, DataUpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_setting:
                intent.setClass(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void getNewApp() {
        UpdateAppManager manager = new UpdateAppManager
                .Builder()
                .setActivity(this)
                .setHttpManager(new UpdateAppHttpUtil())
                .setUpdateUrl(URL.host + URL.UPDATEAPP)
                .setPost(false)
                .hideDialogOnDownloading()
                .dismissNotificationProgress()
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
                .setTopPic(R.mipmap.ic_biglauncher)
                .build();
        manager.checkNewApp(new UpdateCallback() {
            @Override
            protected UpdateAppBean parseJson(String json) {
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


    private void initDialog() {
        progressDialog = new CustomerDialog(HomeActivity.this, R.style.MyDialog);
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

    /**
     * 更新省厅（anrong)数据
     */
    private void getGAdataVersion() {
        //远程查询省厅离线数据
        SourceJsPackageDao sourceJsPackageDao = new SourceJsPackageDao(this);
        SourceJsPackage sourceJsPackage = sourceJsPackageDao.queryLastPackage();
        String versionNo = "0";
        if (sourceJsPackage != null) {
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
                        }

                        @Override
                        public void onSuccess(int statusCode, String response) {
                            if (200 == statusCode) {
                                LogUtils.d("123======" + response);
                                UpdateDataBean updateDataBean = JSON.parseObject(response, UpdateDataBean.class);
//                                JsonDataResult<JSONObject> result = JSON.parseObject(response, JsonDataResult.class);
//                                //判断返回数据是否正常返回
                                SourceJsPackage data = updateDataBean.getData();
                                if (data != null && Constants.sucessCode.equals(updateDataBean.getCode())) {
//                                    SourceJsPackage sourceJsPackage = result.getData().toJavaObject(SourceJsPackage.class);
                                    //数据数据不为空，说明无新数据包
                                    LogUtils.d("======>>>>" + response);
                                    HomeActivity homeActivity = HomeActivity.this;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        if (homeActivity == null || homeActivity.isDestroyed() || homeActivity.isFinishing()) {
                                            return;
                                        }
                                    } else {
                                        if (homeActivity == null || homeActivity.isFinishing()) {
                                            return;
                                        }
                                    }
                                    dataDialog = new AlertDialog.Builder(HomeActivity.this)
                                            .setTitle("有新数据包需要下载，点击更新")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    JumpUtil.jumptoact(HomeActivity.this, DataUpdateActivity.class);
                                                }
                                            }).create();
                                    if (dataDialog != null && !dataDialog.isShowing()) {
                                        dataDialog.show();
                                    }
                                }
                            }

                        }
                    });


        }

    }


}
