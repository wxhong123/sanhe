package com.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.acc.common.util.ToastUtil;
import com.response.BasePerson;

import cn.hbfec.checklib.CheckManager;
import cn.hbfec.checklib.DataManager;
import cn.hbfec.checklib.LoginManager;
import cn.hbfec.checklib.dialog.SynchDataDialog;
import cn.hbfec.checklib.dialog.UploadDataDialog;
import cn.hbfec.checklib.model.LibSfzInfo;
import cn.hbfec.checklib.model.LoginInfo;
import cn.hbfec.checklib.model.TaskCheckResult;
import cn.hbfec.checklib.socket.WebSocketBinder;
import cn.hbfec.checklib.socket.listener.OnLoginListener;

/**
 * 三道防线工具类
 *
 * @author 徐宏明
 * @email ：294985925@qq.com
 * @date 2020/3/6
 */
public class SanDaoFangXianUtils {
    /**
     * 同步数据
     *
     * @param context  上下文
     * @param binder   binder
     * @param handler  回调对象
     * @param username 用户名
     * @param password 密码
     */
    public static void SyncData(Context context, WebSocketBinder binder, Handler handler, String username, String password) {
        if (null != binder && binder.getServer().isClientConnected()) {
            //初始化登陆管理类
            LoginManager mLoginManager = new LoginManager();
            if (mLoginManager.isFirstLogin()) {
                //同步数据
                binder.getServer().addOnLoginListener(new OnLoginListener() {
                    @Override
                    public void onLoginDataReturn(LoginInfo info) {
                        if (info != null && "1".equals(info.code)) {
                            handler.sendEmptyMessage(200);
                        } else {
                            //登陆账户或密码不正确
                            handler.sendEmptyMessage(500);
                        }
                    }
                });
                binder.getServer().setLoginUser(username, password);
            } else {
                LoginInfo loginInfo = mLoginManager.login(username, password);
                if ("000".equals(loginInfo.code)) {
                    SyncData(context, binder, "SwitchData");
                } else if ("010".equals(loginInfo.code)) {
                    //授权认证失败
                    ToastUtil.showToastLong(context, "同步数据授权认证失败");
                } else if ("020".equals(loginInfo.code)) {
                    //登陆失败
                    ToastUtil.showToastLong(context, "同步数据登录失败");
                }
            }
        } else {
            ToastUtil.showToastLong(context, "请插入USB线到数据站点，然后同步列控数据");
        }
    }

    /**
     * 同步数据，如果是第一次登录，那么进行离线同步，否则不同步
     *
     * @param context  上下文
     * @param binder   binder
     * @param handler  回调对象
     * @param username 用户名
     * @param password 密码
     */
    public static void SyncDataOnLoginActivity(Context context, WebSocketBinder binder, Handler handler, String username, String password) {
        //初始化登陆管理类
        LoginManager mLoginManager = new LoginManager();
        //是第一次登录，进行数据离线同步
        if (mLoginManager.isFirstLogin()) {
            //判断服务是否连接
            if (null != binder && binder.getServer().isClientConnected()) {
                //登录回调
                binder.getServer().addOnLoginListener(new OnLoginListener() {
                    @Override
                    public void onLoginDataReturn(LoginInfo info) {
                        if (null != info) {
                            Message msg = handler.obtainMessage();
                            msg.what = 300;
                            msg.obj = info;
                            handler.sendMessage(msg);
                        } else {
                            handler.sendEmptyMessage(400);
                        }
                        if (info != null && "1".equals(info.code)) {
                            handler.sendEmptyMessage(200);
                        } else {
                            //登陆账户或密码不正确
                            handler.sendEmptyMessage(500);
                        }
                    }
                });
                //发起登录
                binder.getServer().setLoginUser(username, password);
            } else {
                ToastUtil.showToastLong(context, "请插入USB线到数据站点，然后同步列控数据");
            }
        } else {
            LoginInfo loginInfo = mLoginManager.login(username, password);
            if ("000".equals(loginInfo.code)) {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = loginInfo;
                handler.sendMessage(msg);
            } else if ("010".equals(loginInfo.code)) {
                //授权认证失败
                ToastUtil.showToastLong(context, "同步数据授权认证失败");
            } else if ("020".equals(loginInfo.code)) {
                //登陆失败
                ToastUtil.showToastLong(context, "同步数据登录失败");
            }
        }

    }

    /**
     * 同步数据
     *
     * @param context 上下文
     * @param binder  绑定服务
     * @param context 标识信息：SynchData（首次同步），SwitchData（非首次同步）
     */
    public static void SyncData(Context context, WebSocketBinder binder, String tag) {
        SynchDataDialog synchDataDialog = new SynchDataDialog();
        if (null != binder && binder.getServer().isClientConnected()) {
            binder.addRequestDataListener(synchDataDialog);
            synchDataDialog.setSocketBinder(binder);
            synchDataDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), tag);
        } else {
            ToastUtil.showToastLong(context, "请插入USB线到数据站点，然后同步列控数据");
        }
    }

    /**
     * 上传数据
     *
     * @param context 上下文
     * @param context 服务绑定对象
     */
    public static void UploadData(Context context, WebSocketBinder binder) {
        //判断服务是否连接成功
        UploadDataDialog uploadDataDialog = new UploadDataDialog();
        if (null != binder && binder.getServer().isClientConnected()) {
            binder.addRequestDataListener(uploadDataDialog);
            uploadDataDialog.setSwitchServer(binder.getServer());
            uploadDataDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "UploadData");
        } else {
            ToastUtil.showToastLong(context, "请插入USB线到数据站点，然后上传数据");
        }
    }

    /**
     * 核查人员
     *
     * @param context  上下文
     * @param p        人员对象
     * @param isAlong  是否绑定车辆  true-绑定人车核查
     * @param username 用户名
     * @param password 密码
     * @return 人员信息对象
     */
    public static TaskCheckResult checkPerson(Context context, BasePerson p, boolean isAlong, String username, String password) {
        LoginManager mLoginManager = new LoginManager();
        LoginInfo loginInfo = mLoginManager.login(username, password);
        if ("000".equals(loginInfo.code)) {
            CheckManager mCheckManager = new CheckManager(context);
            LibSfzInfo idcardInfo = new LibSfzInfo(p.getSfzh(), p.getXm(), p.getXb(), p.getMz(), p.getCsrq(), p.getHjdz());
            //number（身份证号）、idcardInfo（身份证信息）、isAlong（是否绑定车辆  true-绑定人车核查）
            return mCheckManager.checkControlPerson(p.getSfzh(), idcardInfo, isAlong);
        } else if ("010".equals(loginInfo.code)) {
            //授权认证失败
        } else if ("020".equals(loginInfo.code)) {
            //登陆失败
        }
        return null;
    }

    /**
     * 核查车辆
     *
     * @param context  上下文
     * @param cphm     车牌号码
     * @param carType  车辆类型
     * @param username 用户名
     * @param password 密码
     * @return 核查信息
     */
    public static TaskCheckResult checkCar(Context context, String cphm, String carType, String username, String password) {
        LoginManager mLoginManager = new LoginManager();
        LoginInfo loginInfo = mLoginManager.login(username, password);
        if ("000".equals(loginInfo.code)) {
            CheckManager mCheckManager = new CheckManager(context);
            //核查车辆，cphm(车牌号码)、carType（车牌类型）
            return mCheckManager.checkControlCar(cphm, carType);
        } else if ("010".equals(loginInfo.code)) {
            //授权认证失败
        } else if ("020".equals(loginInfo.code)) {
            //登陆失败
        }
        return null;
    }

    /**
     * 更新数据
     *
     * @param context 上下文
     */
    public static void updateDataOnLine(Context context, String username, String password) {
        LoginManager mLoginManager = new LoginManager();
        //如果不是第一次登录，那么进行更新数据
        if (!mLoginManager.isFirstLogin()) {
            DataManager dataManager = new DataManager(context);
            dataManager.startSynchData();
        } else {
//            ToastUtil.showToastLong(context,"首次同步请使用离线方式");
            mLoginManager.login(username, password, new OnLoginListener() {
                @Override
                public void onLoginDataReturn(LoginInfo loginInfo) {
                    if ("1".equals(loginInfo.code)) {
                        //登陆成功
                        DataManager dataManager = new DataManager(context);
                        dataManager.startSynchData();
                    } else {
                        //登陆失败
                    }

                }
            });

        }
    }

    /**
     * 在线上传书
     *
     * @param context 上下文
     */
    public static void uploadDataOnLine(Context context) {
        DataManager dataManager = new DataManager(context);
        dataManager.startUploadData();
    }
}
