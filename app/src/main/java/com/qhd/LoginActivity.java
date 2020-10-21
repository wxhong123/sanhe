package com.qhd;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acc.common.Constants;
import com.acc.common.Provider.MyContentResolver;
import com.acc.common.URL;
import com.acc.common.base.MyApplication;
import com.acc.common.myokhttp.response.RawResponseHandler;
import com.acc.common.myokhttp.util.LogUtils;
import com.acc.common.util.AccTool;
import com.acc.common.util.DateUtil;
import com.acc.common.util.NetUtil;
import com.acc.common.util.SPHelper;
import com.acc.common.util.SharedPreferencesUtils;
import com.acc.common.util.ToastUtil;
import com.acc.common.util.TokenInfo;
import com.acc.common.widget.CustomerDialog;
//import com.acc.sanheapp.BuildConfig;
import com.acc.sanheapp.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.anrong.sdk.SDKService;
import com.arong.swing.db.dao.DeptSDAO;
import com.arong.swing.db.dao.StaffUserSDAO;
import com.arong.swing.db.entity.Dept;
import com.arong.swing.db.entity.StaffUser;
import com.arong.swing.db.entity.StaffUserVO;
import com.arong.swing.util.MD5Impl;
import com.data.SyncTable;
import com.data.SyncTableDao;
import com.google.gson.Gson;
import com.qhd.bean.MsgBean;
import com.qhd.bean.SFZHLoginBean;
import com.response.AutheInfoBeans.AppCredential;
import com.response.AutheInfoBeans.AppInfo;
import com.response.AutheInfoBeans.UserCredential;
import com.response.AutheInfoBeans.UserInfo;
import com.response.JsonDataResult;
import com.service.syncdata.SyncDataService;
import com.service.uploaddata.CheckInfoService;
import com.ycgis.pclient.PService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hbfec.checklib.CheckManager;
import cn.hbfec.checklib.LoginManager;
import cn.hbfec.checklib.model.DataSynchVersion;
import cn.hbfec.checklib.model.LoginInfo;
import cn.qqtheme.framework.AppConfig;

/**
 * 用户登录
 * 逻辑：点击登录按钮检查三河数据是否同步。如果同步进行登录，如果没有同步，提示进行USB离线同步。
 *
 * @author biehuan
 * @date 2016-11-07
 * @since 1.0
 */
public class LoginActivity extends Activity implements OnClickListener {

    private final static String TAG = "LoginActivity";

    private final static String lastLoginUser = "LAST_LOGIN_USER";

    private static final int VALID = 1;
    private static final int LOAD = 2;

    public static final int REQUSET = 1;

    private Button main_login_btn;

    private EditText userText;
    private EditText etPwd;

    private TextView txtVersion, txtVersion2;
    private ImageView iv_set;
    private StaffUserVO staffUserVO;
    private CustomerDialog progressDialog;

    // add by xh.w 2020.10.21
    private UserInfo mUserInfo;
    private AppInfo mAppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
        initView();
        // 获取统一客户凭证
        getAutheInfo();

        Intent intent = new Intent();
        // 跳转到加载界面
        intent.setClass(LoginActivity.this, LoadingActivity.class);
        startActivityForResult(intent, REQUSET);

        saveAppDatabase("hebei.db");

        //启动数据同步Service
        Intent startIntent = new Intent(this, SyncDataService.class);
        startService(startIntent);
        //启动离线数据上传服务
        Intent uploadIntent = new Intent(this, CheckInfoService.class);
        startService(uploadIntent);

        forbinWifiAndPhone();

        // 获取TOKEN信息
        TokenInfo.createUserTocken(this);
        TokenInfo.createAppToken(this);
    }

    private void forbinWifiAndPhone() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.acc.sanhewifi", "com.acc.sanhewifi.PhoneListenService"));
        startService(intent);
    }

    private void initView() {
        main_login_btn = this.findViewById(R.id.login_btn);
        main_login_btn.setOnClickListener(this);

        userText = findViewById(R.id.userText);

        String lastUser = SharedPreferencesUtils.getValue(this, lastLoginUser);
        if (!TextUtils.isEmpty(lastUser)) {
            userText.setText(lastUser);
        }
        etPwd = findViewById(R.id.pwd);

//        if (BuildConfig.DEBUG) {
//            userText.setText("131082199311215814");
//            etPwd.setText("111111");
//        }

        txtVersion = findViewById(R.id.txt_version);
        txtVersion2 = findViewById(R.id.txt_version2);
        iv_set = findViewById(R.id.iv_set);
        iv_set.setOnClickListener(this);

        progressDialog = new CustomerDialog(LoginActivity.this, R.style.MyDialog);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if (TextUtils.isEmpty(etPwd.getText())) {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //login();
                break;
            case R.id.iv_set:
                Intent intent = new Intent(this, SetURLActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void login() {

        try {
            StaffUserVO staffUserVO = this.getStaffuserByName(userText.getText().toString().toUpperCase());
            if (staffUserVO == null || !AppConfig.USE_OFFLINE_LOGIN) {
                //如果离线数据登录失败，则需要在线进行登录并同步数据到本地
                if (NetUtil.isNetAvailable(this)) {
                    showProgressDialog("登录中，请稍后");
                    MyApplication.mMyOkhttp.get()
                            .url(URL.host + URL.USER_LOGIN)
                            .addParam("userName", userText.getText().toString().toUpperCase())
                            .addParam("password", etPwd.getText().toString())
                            .tag(this)
                            .enqueue(new RawResponseHandler() {
                                @Override
                                public void onFailure(int statusCode, String error_msg) {
                                    dismissProgressDialog();
                                    Toast.makeText(getApplicationContext(), "网络请求异常", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(int statusCode, String response) {
                                    dismissProgressDialog();
                                    //正常返回，并且数据不为空
                                    if (200 == statusCode && !TextUtils.isEmpty(response)) {
                                        try {
                                            JsonDataResult jsonDataResult = JSON.parseObject(response, JsonDataResult.class);
                                            if ("200".equals(jsonDataResult.getStatus())) {
                                                Map map = ((JSONArray) jsonDataResult.getData()).toJavaList(Map.class).get(0);
                                                //初始代安容数据Dao
                                                String appRootPath = Environment.getExternalStorageDirectory().toString();
                                                String ANRONG_DATAS_PATHDIR = appRootPath + "/anrong/infocheck/datas/";
                                                String ANRONGTEC_DB = ANRONG_DATAS_PATHDIR + "anrongtec.db";
                                                StaffUserSDAO staffUserSDAO = new StaffUserSDAO(ANRONGTEC_DB);
                                                DeptSDAO deptSDAO = new DeptSDAO(ANRONGTEC_DB);
                                                //处理安容用户表
                                                StaffUser staffUser = new StaffUser();
                                                staffUser.setStaffId(map.get("policemanid").toString());//警号
                                                staffUser.setLoginName(map.get("policename").toString());//姓名
                                                staffUser.setLoginPassword(new MD5Impl().encode(etPwd.getText().toString()));//默认为111111
                                                staffUser.setDeptId(map.get("policeorg").toString());//组织机构
                                                staffUser.setStaffName(map.get("policename").toString());//姓名
                                                staffUser.setStaffCode(map.get("policesfzh").toString());
                                                staffUser.setDepartments(map.get("org_name").toString());
                                                staffUser.setSex("1");
                                                staffUser.setSts("A");
                                                staffUser.setStsTime(new Timestamp(System.currentTimeMillis()));
                                                staffUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
                                                staffUser.setCreateStaffId("12080");
                                                staffUserSDAO.insert(staffUser);
                                                //处理安容组织机构表
                                                Dept dept = new Dept();
                                                dept.setDeptName(map.get("org_name").toString());
                                                List<Dept> deptList = deptSDAO.queryList(dept);
                                                //如果数据机构不存在 ，则插入数据
                                                if (deptList == null || deptList.size() < 1) {
                                                    dept = new Dept();
                                                    if (map.get("org_name") != null) {
                                                        dept.setDeptName(map.get("org_name").toString());
                                                    }
                                                    if (map.get("policeorg") != null) {
                                                        dept.setDeptId(map.get("policeorg").toString());
                                                    }
                                                    if (map.get("zzjgsj") != null) {
                                                        dept.setUpDeptId(map.get("zzjgsj").toString());
                                                    } else {
                                                        dept.setUpDeptId("131082000000");
                                                    }
                                                    deptSDAO.insert(dept);
                                                }
                                                //跳转到登录页面
                                                StaffUserVO staffUserVO = getStaffuserByName(userText.getText().toString().toUpperCase());
                                                offLineLogin(staffUserVO, etPwd.getText().toString());
                                            } else {
                                                //数据不为200，登录失败，则展示后台返回数据
                                                Toast.makeText(getApplicationContext(), jsonDataResult.getMsg(), Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception ex) {
                                            Toast.makeText(getApplicationContext(), "数据解析异常", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "离线无法登录！", Toast.LENGTH_SHORT).show();
                }
            } else {
                this.offLineLogin(staffUserVO, etPwd.getText().toString());
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "数据解析异常", Toast.LENGTH_SHORT).show();
        }
    }

    // 重载登录
    private void login(String userName, String password) {

        try {
            StaffUserVO staffUserVO = this.getStaffuserByName(userText.getText().toString().toUpperCase());
            if (staffUserVO == null || !AppConfig.USE_OFFLINE_LOGIN) {
                //如果离线数据登录失败，则需要在线进行登录并同步数据到本地
                if (NetUtil.isNetAvailable(this)) {
                    showProgressDialog("登录中，请稍后");
                    MyApplication.mMyOkhttp.get()
                            .url(URL.host + URL.USER_LOGIN)
                            .addParam("userName", userName)
                            .addParam("password", password)
                            .tag(this)
                            .enqueue(new RawResponseHandler() {
                                @Override
                                public void onFailure(int statusCode, String error_msg) {
                                    dismissProgressDialog();
                                    Toast.makeText(getApplicationContext(), "网络请求异常", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(int statusCode, String response) {
                                    dismissProgressDialog();
                                    //正常返回，并且数据不为空
                                    if (200 == statusCode && !TextUtils.isEmpty(response)) {
                                        try {
                                            JsonDataResult jsonDataResult = JSON.parseObject(response, JsonDataResult.class);
                                            if ("200".equals(jsonDataResult.getStatus())) {
                                                Map map = ((JSONArray) jsonDataResult.getData()).toJavaList(Map.class).get(0);
                                                //初始代安容数据Dao
                                                String appRootPath = Environment.getExternalStorageDirectory().toString();
                                                String ANRONG_DATAS_PATHDIR = appRootPath + "/anrong/infocheck/datas/";
                                                String ANRONGTEC_DB = ANRONG_DATAS_PATHDIR + "anrongtec.db";
                                                StaffUserSDAO staffUserSDAO = new StaffUserSDAO(ANRONGTEC_DB);
                                                DeptSDAO deptSDAO = new DeptSDAO(ANRONGTEC_DB);
                                                //处理安容用户表
                                                StaffUser staffUser = new StaffUser();
                                                staffUser.setStaffId(map.get("policemanid").toString());//警号
                                                staffUser.setLoginName(map.get("policename").toString());//姓名
                                                staffUser.setLoginPassword(new MD5Impl().encode(etPwd.getText().toString()));//默认为111111
                                                staffUser.setDeptId(map.get("policeorg").toString());//组织机构
                                                staffUser.setStaffName(map.get("policename").toString());//姓名
                                                staffUser.setStaffCode(map.get("policesfzh").toString());
                                                staffUser.setDepartments(map.get("org_name").toString());
                                                staffUser.setSex("1");
                                                staffUser.setSts("A");
                                                staffUser.setStsTime(new Timestamp(System.currentTimeMillis()));
                                                staffUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
                                                staffUser.setCreateStaffId("12080");
                                                staffUserSDAO.insert(staffUser);
                                                //处理安容组织机构表
                                                Dept dept = new Dept();
                                                dept.setDeptName(map.get("org_name").toString());
                                                List<Dept> deptList = deptSDAO.queryList(dept);
                                                //如果数据机构不存在 ，则插入数据
                                                if (deptList == null || deptList.size() < 1) {
                                                    dept = new Dept();
                                                    if (map.get("org_name") != null) {
                                                        dept.setDeptName(map.get("org_name").toString());
                                                    }
                                                    if (map.get("policeorg") != null) {
                                                        dept.setDeptId(map.get("policeorg").toString());
                                                    }
                                                    if (map.get("zzjgsj") != null) {
                                                        dept.setUpDeptId(map.get("zzjgsj").toString());
                                                    } else {
                                                        dept.setUpDeptId("131082000000");
                                                    }
                                                    deptSDAO.insert(dept);
                                                }
                                                //跳转到登录页面
                                                StaffUserVO staffUserVO = getStaffuserByName(userText.getText().toString().toUpperCase());
                                                offLineLogin(staffUserVO, etPwd.getText().toString());
                                            } else {
                                                //数据不为200，登录失败，则展示后台返回数据
                                                Toast.makeText(getApplicationContext(), jsonDataResult.getMsg(), Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception ex) {
                                            Toast.makeText(getApplicationContext(), "数据解析异常", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "离线无法登录！", Toast.LENGTH_SHORT).show();
                }
            } else {
                this.offLineLogin(staffUserVO, etPwd.getText().toString());
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "数据解析异常", Toast.LENGTH_SHORT).show();
        }
    }
    boolean isLoginok = false;
    String s_username;

    //离线数据登录
    public void offLineLogin(StaffUserVO staffUserVO, String pwd) {
        try {
            com.arong.swing.db.entity.JsonDataResult<String> result = null;
            result = SDKService.login(staffUserVO, pwd);
            //数据登录成功
            if ("000".equals(result.getCode())) {
                this.staffUserVO = staffUserVO;
                s_username = userText.getText().toString().toUpperCase();

                gotoHome();
            } else {
                Toast.makeText(getApplicationContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoHome() {
        SPHelper.getHelper(LoginActivity.this).saveInfo(staffUserVO);
        SharedPreferencesUtils.setValue(this, lastLoginUser, s_username);
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gotoinitData(MsgBean messageEvent) {
        dismissProgressDialog();
        if (isLoginok) {
            gotoHome();
        }
    }


    //通过身份证号或用户名获得用户对象
    public StaffUserVO getStaffuserByName(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        try {

            List<StaffUserVO> staffUserVOList = SDKService.getStaffUsrs();
            for (StaffUserVO staffUserVO : staffUserVOList) {
                if (name.equalsIgnoreCase(staffUserVO.getLoginName()) || name.equalsIgnoreCase(staffUserVO.getStaffCode())) {

                    String appRootPath = Environment.getExternalStorageDirectory().toString();
                    String ANRONG_DATAS_PATHDIR = appRootPath + "/anrong/infocheck/datas/";
                    String ANRONGTEC_DB = ANRONG_DATAS_PATHDIR + "anrongtec.db";
                    DeptSDAO deptSDAO = new DeptSDAO(ANRONGTEC_DB);
                    Dept dept = new Dept();
                    dept.setDeptId(staffUserVO.getDeptId());
                    Dept queryBean = deptSDAO.queryBean(dept);
                    staffUserVO.setDepartments(queryBean.getDeptName());
                    return staffUserVO;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET && resultCode == RESULT_OK && AppConfig.USE_SDFX) {
            initSdfxData(Constants.SDFX_USERNAME, Constants.SDFX_PASSWORD);
        }
    }

    //检查三道防线数据库
    private void initSdfxData(String username, String password) {
        txtVersion.setText("三道防线数据导入日期：未找到数据库");
        //初始化登陆管理类
        LoginManager mLoginManager = new LoginManager();
        LoginInfo loginInfo = mLoginManager.login(username, password);
        if ("000".equals(loginInfo.code)) {
            CheckManager checkManager = new CheckManager(this);
            DataSynchVersion dataSynchVersion = checkManager.loadDataSynchVersion();
            if (null != dataSynchVersion) {
                try {
                    txtVersion.setText("三道防线数据导入日期：" + AccTool.getStringDateShort("yyyy") + "-" + dataSynchVersion.getPersonTime());
                } catch (Exception e) {

                }
            }
        } else if ("010".equals(loginInfo.code)) {
            //授权认证失败
        } else if ("020".equals(loginInfo.code)) {
            //登陆失败
        }
    }

    private void loginBysfzh() {

        final PService user = new PService(this);
        StaffUserVO staffUserVO = this.getStaffuserByName(user.getSFZ());
        if (staffUserVO == null) {
            //如果离线数据登录失败，则需要在线进行登录并同步数据到本地
            if (NetUtil.isNetAvailable(this)) {
                showProgressDialog("端点登录中");
                MyApplication.mMyOkhttp.get()
                        .url(URL.host + URL.LoginBySFZH)
                        .addParam("cardNumber", user.getSFZ())
                        .tag(this)
                        .enqueue(new RawResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, String error_msg) {
                                Toast.makeText(getApplicationContext(), "网络请求异常" + statusCode + "   " + error_msg, Toast.LENGTH_SHORT).show();
                                dismissProgressDialog();
                            }

                            @Override
                            public void onSuccess(int statusCode, String response) {
                                LogUtils.d("=============>>>>" + response);
                                //正常返回，并且数据不为空
                                dismissProgressDialog();
                                if (200 == statusCode && !TextUtils.isEmpty(response)) {
                                    try {
                                        SFZHLoginBean sfzhLoginBean = JSON.parseObject(response, SFZHLoginBean.class);
                                        if ("200".equals(sfzhLoginBean.getStatus())) {
                                            if (sfzhLoginBean.getData() == null || sfzhLoginBean.getData().size() == 0) {
                                                return;
                                            }
                                            SFZHLoginBean.DataBean dataBean = sfzhLoginBean.getData().get(0);
                                            //初始代安容数据Dao
                                            String appRootPath = Environment.getExternalStorageDirectory().toString();
                                            String ANRONG_DATAS_PATHDIR = appRootPath + "/anrong/infocheck/datas/";
                                            String ANRONGTEC_DB = ANRONG_DATAS_PATHDIR + "anrongtec.db";
                                            StaffUserSDAO staffUserSDAO = new StaffUserSDAO(ANRONGTEC_DB);
                                            DeptSDAO deptSDAO = new DeptSDAO(ANRONGTEC_DB);
                                            //处理安容用户表
                                            StaffUser staffUser = new StaffUser();
                                            staffUser.setStaffId(dataBean.getPolicemanid());//警号
                                            staffUser.setLoginName(dataBean.getPolicename());//姓名
                                            staffUser.setLoginPassword(new MD5Impl().encode("111111"));//默认为111111
                                            staffUser.setDeptId(dataBean.getPoliceorg());//组织机构
                                            staffUser.setStaffName(dataBean.getPolicename());//姓名
                                            staffUser.setStaffCode(dataBean.getPolicesfzh());
                                            staffUser.setSex("1");
                                            staffUser.setSts("A");
                                            staffUser.setStsTime(new Timestamp(new Date().getTime()));
                                            staffUser.setCreateTime(new Timestamp(new Date().getTime()));
                                            staffUser.setCreateStaffId("12080");
                                            staffUserSDAO.insert(staffUser);
                                            //处理安容组织机构表
                                            Dept dept = new Dept();
                                            dept.setDeptName(dataBean.getOrg_name());
                                            List<Dept> deptList = deptSDAO.queryList(dept);
                                            //如果数据机构不存在 ，则插入数据
                                            if (deptList == null || deptList.size() < 1) {
                                                dept = new Dept();
                                                dept.setDeptName(dataBean.getOrg_name());
                                                dept.setDeptId(dataBean.getPoliceorg());
                                                dept.setUpDeptId(dataBean.getZzjgsj());
                                                deptSDAO.insert(dept);
                                            }
                                            //跳转到登录页面
                                            StaffUserVO staffUserVO = getStaffuserByName(user.getSFZ());
                                            offLineLogin(staffUserVO, "111111");
                                        } else {
                                            //数据不为200，登录失败，则展示后台返回数据
                                            Toast.makeText(getApplicationContext(), sfzhLoginBean.getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception ex) {
                                        Toast.makeText(getApplicationContext(), "数据解析异常", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "离线无法登录！", Toast.LENGTH_SHORT).show();
            }
        } else {
            this.offLineLogin(staffUserVO, "111111");
        }


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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 将SD卡中的DB文件复制到Databases中
     *
     * @param sd 文件在SD卡中的位置路径。
     */
    private void saveAppDatabase(String sd) {
        String mc = "三河市";
        //初始化数据库
        SyncTableDao syncTableDao = new SyncTableDao(getBaseContext());
        SyncTable syncTable = syncTableDao.queryLastData();
        if (syncTable == null) {
            txtVersion2.setText(mc + "数据导入日期：未找到数据库");
        } else {
            if (syncTable.getLastupdate() != null) {
                long longDate = Long.valueOf(syncTable.getLastupdate());
                txtVersion2.setText(String.format(mc + "数据导入日期：%s", DateUtil.getFullDateToString(new Date(longDate))));
            } else {
                txtVersion2.setText(mc + "数据导入日期：未找到数据库");
            }
        }
    }

    /**
     * 调用统一认证客户端获取凭证票据
     *
     * created by xh.w on 2020.10.20
     */
    private void getAutheInfo(){

        // 调用统一认证客户端获取凭证票据
        Bundle bundle = MyContentResolver.call(this, "call");

        // 应用客户端返回
        if (bundle != null){

            String messageId = bundle.getString("messageId");
            String version = bundle.getString("version");
            int resultCode = bundle.getInt("resultCode");
            String message = bundle.getString("message");
            String userCredential = bundle.getString("userCredential");
            String appCredential = bundle.getString("appCredential");

            if ("0".equals(resultCode)){

                ToastUtil.showToast(this, message);
                if (!"".equals(userCredential) || userCredential != null){

                    SharedPreferencesUtils.setValue(this, com.acc.common.AppConfig.USER_CREDENTIAL, userCredential);
                    //解析用户凭证
                    UserCredential mUserCredential = new Gson().fromJson(userCredential, UserCredential.class);
                    // 用户信息
                    mUserInfo = mUserCredential.getCredential().getLoad().getUserInfo();
                    // 把警号存起来后面获取appToken用
                    SharedPreferencesUtils.setValue(this, com.acc.common.AppConfig.JING_HAO, mUserInfo.getJh());
                }

                if (!"".equals(appCredential) || appCredential != null){

                    SharedPreferencesUtils.setValue(this, com.acc.common.AppConfig.APP_CREDENTIAL, appCredential);
                    //解析应用凭证
                    AppCredential mAppCredential = new Gson().fromJson(appCredential, AppCredential.class);
                    // 应用信息
                    mAppInfo = mAppCredential.getCredential().getLoad().getAppInfo();
                }

                login(mUserInfo.getJh(), mUserInfo.getJh()); // 调用登录,默认密码都设为警号了
            }else{
                ToastUtil.showToast(this, message);
                this.onDestroy();
            }
        }
    }
}
