package com.acc.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.acc.sanheapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Pattern;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 2018/3/29
 * <p>
 * 工具类
 * 常用工具类整理，所有项目可以直接使用，无需依赖其他类。
 */

public final class AccTool {
    /**
     * 2018/3/28
     * <p>
     * 将纯的数字字符串转换为Int类型。
     * <p>
     * 如果转换异常，将返回-1。
     *
     * @param value 需要转换的字符串。
     * @return -1为异常信息。
     */
    public static int String2Int(String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 2018/3/28
     * <p>
     * 创建随机字符串：当前时间年月日时分秒+100000以内随机数。
     *
     * @return 随机字符串
     */
    public static String getRandomString() {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        return String.format(Locale.CHINA, "%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", date, new Random().nextInt(100000));
    }

    /**
     * 2018/3/28
     * <p>
     * 创建固定长度的随机字符串：英文大小写和数字组成。
     *
     * @param length 字符串长度
     * @return 字符串
     */
    public static String RandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++)
            buf.append(str.charAt(random.nextInt(62)));
        return buf.toString();
    }

    /**
     * 2018/3/28
     * <p>
     * 获取设备识别号，用于标识设备。如果获取失败，那么使用UUID来代替。
     * <p>
     * 1、7.0以下可以随意获取。
     * 2、7.0以上需要动态申请权限：android.permission.READ_PHONE_STATE。
     *
     * @param context 上下文
     * @return 设备号
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getDeviceID(Context context) {
        String result = "设备号为空";
        if (null != context) {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            try {
                if (null != telephonyManager)
                    result = telephonyManager.getDeviceId();
            } catch (Exception e) {
                result = getUUID(context);
            }
        } else {
            result = "上下文为空";
        }
        return result;
    }


    /**
     * 2018/3/28
     * <p>
     * 判断字符穿是否为空。
     *
     * @param string 需要判断的字符串。
     * @return 返回结果
     */
    public static boolean nullOrEmpty(String string) {
        return null == string || string.isEmpty();
    }


    /**
     * 将SD卡中的文件导入到DataBases中
     *
     * @param sdcard    文件在SD卡路径
     * @param databases 文件要保存在DataBases
     * @param isDelete  是否删除SD卡中的文件
     */
    public static void SDCard2Databases(File sdcard, File databases, boolean isDelete) {
        try {
            if (sdcard.exists()) {
                if (!databases.getParentFile().exists())
                    databases.getParentFile().mkdirs();
                if (databases.exists())
                    databases.delete();
                if (databases.createNewFile()) {
                    FileChannel from = new FileInputStream(sdcard).getChannel();
                    FileChannel to = new FileOutputStream(databases).getChannel();
                    to.transferFrom(from, 0, from.size());
                    if (isDelete) sdcard.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 2018/3/28
     * <p>
     * 安装本地APK文件。
     * <p>
     * 1、6.0使用常规安装方式。
     * <p>
     * 2、7.0以上需要使用FileProvider分享路径安装。
     * <p>
     * 3、8.0以上需要加上新的权限：android.permission.REQUEST_INSTALL_PACKAGES
     *
     * @param context   上下文
     * @param file      安装文件
     * @param authority 分享标识
     * @return 安装状态
     */
    public static String InstallAPK(Context context, File file, String authority) {
        String result;
        if (null == file || null == authority || authority.isEmpty()) {
            result = "file不存在或authority为空";
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri fileUri = FileProvider.getUriForFile(context, authority, file);
            String type = context.getContentResolver().getType(fileUri);
            //当版本大于等于7.0的时候使用FileProvider创建Uri来安装APK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //添加临时读取权限
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(fileUri, type);
            } else {
                intent.setDataAndType(Uri.fromFile(file), type);
            }
            try {
                context.startActivity(intent);
                result = "正在安装";
                System.exit(0);
            } catch (Exception e) {
                result = e.getMessage();
            }
        }
        return result;
    }

    /**
     * 分享图片，做了8.0适配
     *
     * @param context   上下文
     * @param file      分享的图片地址
     * @param authority 认证名字
     * @return 分享状态
     */
    public static String SharePic(Context context, File file, String authority) {
        String result = null;
        if (null == file || null == authority || authority.isEmpty()) {
            result = "file不存在或authority为空";
        } else {
            Uri fileUri = FileProvider.getUriForFile(context, authority, file);
            String type = context.getContentResolver().getType(fileUri);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType(type);
            intent.putExtra(Intent.EXTRA_SUBJECT, "智采商户二维码");
            //当版本大于等于7.0的时候使用FileProvider创建Uri来安装APK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //添加临时读取权限
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, file);
            }
            context.startActivity(Intent.createChooser(intent, "来自智采"));
        }
        return result;
    }

    /**
     * 使用本地播放器播放视频
     *
     * @param context   上下文
     * @param file      分享的图片地址
     * @param authority 认证名字
     * @return 分享状态
     */
    public static String ShowVideo(Context context, String file, @Nullable String authority) {
        String result = null;
        if (null == file) {
            result = "file不存在或authority为空";
        } else if (!file.startsWith("http://") && !file.startsWith("https://") && null != authority) {
            Uri fileUri = FileProvider.getUriForFile(context, authority, new File(file));
            String type = context.getContentResolver().getType(fileUri);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType(type);
            //当版本大于等于7.0的时候使用FileProvider创建Uri来安装APK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //添加临时读取权限
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(fileUri, type);
            } else {
                intent.setDataAndType(Uri.fromFile(new File(file)), type);
            }
            context.startActivity(Intent.createChooser(intent, "来自智采"));
        } else if (file.startsWith("http://") || file.startsWith("https://")) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(file);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
            mediaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mediaIntent.setDataAndType(Uri.parse(file), mimeType);
            context.startActivity(mediaIntent);
        }
        return result;
    }

    /**
     * 2018/3/28
     * <p>
     * 判断字符串是否为纯数字，不带符号。
     *
     * @param str 要判断的字符串
     * @return 验证结果，true为数字
     */

    public static boolean isNum(String str) {
        for (int i = 0; i < str.length(); i++) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    /**
     * 2018/3/28
     * <p>
     * 将2的幂累加求和分解。1+2+4+16=23，分解23.
     *
     * @param num 被分解的数
     * @return 返回分析结果集合
     */
    public static List<String> Num2List(int num) {
        int j;
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            j = (num >>> i) % 2;
            j = j << i;
            if (j > 0) {
                result.add(j + "");
            }
        }
        return result;
    }

    /**
     * 2018/3/28
     * <p>
     * 弹出Toast
     *
     * @param context 上下文
     * @param str     弹出内容
     */
    public static void showToast(Context context, String str) {
        if (null != context && str != null && !str.isEmpty()) {
            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        } else if (null != context) {
            Toast.makeText(context, "Toast内容异常", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 2018/3/28
     * <p>
     * 弹出snackBar
     *
     * @param view 依赖的view
     * @param str  显示的文字
     */
    public static void showSnack(View view, String str) {
        if (null != view && str != null && !str.isEmpty()) {
            Snackbar.make(view, str, Snackbar.LENGTH_LONG).show();
        } else if (null != view) {
            Snackbar.make(view, "Snack内容异常", Snackbar.LENGTH_LONG).show();
        }
    }

    private static boolean isExit = false;

    /**
     * 2018/3/28
     * <p>
     * 点击返回按键两次退出应用
     * 重写onBackPressed()方法，删除supper
     *
     * @param context 上下文
     */
    public static void QuitAPP(Context context) {
        if (!isExit) {
            isExit = true;
            Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            // 延时2秒执行run中的方法，即isExit设置为false；
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            System.exit(0);
        }
    }


    /**
     * 2018/3/28
     * <p>
     * 获取当前的网络状态 ：没有网络0：WIFI网络1：4G网络4：3G网络3：2G网络2。
     *
     * @param context 上下文
     * @return 网络状态。
     */
    public static int TestInternet(Context context) {
        //结果返回值
        int netType = 0;
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == manager)
            return netType;
        //获取NetworkInfo对象
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (null == networkInfo) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {//WIFI网络
            netType = ConnectivityManager.TYPE_WIFI;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {//移动网络
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null == telephonyManager)
                return nSubType;
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 4;
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 3;
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 2;
            } else {
                netType = 2;
            }
        }
        return netType;
    }

    /**
     * 2018/3/28
     * <p>
     * 时间选择器，使用EditText来接受结果。
     *
     * @param context  上下文
     * @param dt       EditText
     * @param showTime 是否获得时间
     */
    public static void getData(final Context context, final EditText dt, final boolean showTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        getDate(context, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), dt, showTime);
    }

    /**
     * 2018/3/28
     * <p>
     * 时间选择器，使用TextView来接受结果。
     *
     * @param context  上下文
     * @param tv       TextView
     * @param showTime 是否获得时间
     */
    public static void getData(final Context context, final TextView tv, final boolean showTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        getDate(context, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), tv, showTime);
    }

    /**
     * 2018/3/28
     * <p>
     * 选择日期
     *
     * @param context   上下文
     * @param fromYear  起始年
     * @param fromMonth 起始月
     * @param fromDay   起始天
     * @param dt        EditText
     * @param showTime  是否显示时间
     */
    private static void getDate(final Context context, int fromYear, int fromMonth, int fromDay, final EditText dt, final boolean showTime) {
        final DatePickerDialog date = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                dt.setText(i + "-" + (i1 + 1 <= 9 ? "0" : "") + (i1 + 1) + "-" + (i2 <= 9 ? "0" : "") + i2);
                if (showTime)
                    getTime(context, dt);
            }
        }, fromYear, fromMonth - 1, fromDay);
        date.show();
    }

    /**
     * 2018/3/28
     * <p>
     * 选择日期
     *
     * @param context   上下文
     * @param fromYear  起始年
     * @param fromMonth 起始月
     * @param fromDay   起始天
     * @param tv        TextView
     * @param showTime  是否显示时间
     */
    private static void getDate(final Context context, int fromYear, int fromMonth, int fromDay, final TextView tv, final boolean showTime) {
        final DatePicker date = new DatePicker(context);
        date.init(fromYear, fromMonth - 1, fromDay, null);
        AlertDialog.Builder mDatePickerDialogBuilder = new AlertDialog.Builder(context);
        mDatePickerDialogBuilder.setView(date);
        mDatePickerDialogBuilder.setTitle("请选择日期");
        mDatePickerDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv.setText(date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDayOfMonth());
                if (showTime)
                    getTime(context, tv);

            }
        });
        mDatePickerDialogBuilder.setNegativeButton("取消", null);
        mDatePickerDialogBuilder.show();
    }

    /**
     * 2018/3/28
     * <p>
     * 通过时间选择器，选择时间，并赋值给EditText。
     *
     * @param context 上下文
     * @param dt      EditText
     */
    private static void getTime(Context context, final EditText dt) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String datetime = dt.getText().toString() + "  ";
                if (hourOfDay >= 0 && 10 > hourOfDay) {
                    datetime += ("0" + hourOfDay);
                } else {
                    datetime = datetime + hourOfDay;
                }
                datetime += ":";
                if (minute >= 0 && 10 > minute) {
                    datetime += ("0" + minute);
                } else {
                    datetime = datetime + minute;
                }
                datetime += ":00";
                dt.setText(datetime);
            }
        }, 6, 60, true); //构造设置的时间为默认的时间
        timePickerDialog.show();
    }

    /**
     * 2018/3/28
     * <p>
     * 通过时间选择器，选择时间，并赋值给TextView。
     *
     * @param context 上下文
     * @param tv      TextView
     */
    private static void getTime(Context context, final TextView tv) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String datetime = tv.getText().toString() + "  ";
                if (hourOfDay >= 0 && 10 > hourOfDay) {
                    datetime += ("0" + hourOfDay);
                } else {
                    datetime = datetime + hourOfDay;
                }
                datetime += ":";
                if (minute >= 0 && 10 > minute) {
                    datetime += ("0" + minute);
                } else {
                    datetime = datetime + minute;
                }
                datetime += ":00";
                tv.setText(datetime);
            }
        }, 6, 60, true); //构造设置的时间为默认的时间
        timePickerDialog.show();
    }

    /**
     * 2018/3/28
     * <p>
     * 从SharedPreferences查找信息。
     *
     * @param context 上下文
     * @param key     存储标识
     * @return 字符串
     */
    public static String Share2String(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    /**
     * 2018/3/28
     * <p>
     * 将字符串存储到本地持久化，使用默认share。
     *
     * @param context 上下文
     * @param key     存储标识
     * @param value   存储字符串
     */
    public static void String2Share(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 2018/3/28
     * <p>
     * 获取UUID，使用了持久化，保持一个UUID。
     *
     * @param ctx 上下文
     * @return UUID字符串
     */
    public static String getUUID(Context ctx) {
        String strResult = AccTool.Share2String(ctx, "UUID");
        if (null == strResult || strResult.isEmpty()) {
            strResult = UUID.randomUUID().toString();
            AccTool.String2Share(ctx, "UUID", strResult);
        }
        return strResult;
    }


    /**
     * 2018/3/28
     * <p>
     * 手机号正则表达式。
     *
     * @param mobiles 手机号
     * @return true为正确
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：139   138   137   136   135   134   147   150   151   152   157   158    159   178  182   183   184   187   188
    联通：130   131   132   155   156   185   186   145   176
    电信：133   153   177   173   180   181   189
    虚拟运营商：170  171
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return !TextUtils.isEmpty(mobiles) && mobiles.replaceAll(" ", "").matches(telRegex);

    }

    /**
     * 2018/3/28
     * <p>
     * 车牌号正则表达式验证
     *
     * @param vehicleNumber 车牌
     * @return true为正确
     */
    public static boolean IsVehicleNumber(String vehicleNumber) {
        String express = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z][A-Z][A-Z0-9]{4}[A-Z0-9挂学警港澳]$";
        return !(null == vehicleNumber || vehicleNumber.isEmpty()) && vehicleNumber.matches(express);
    }

    /**
     * 2018/3/28
     * <p>
     * 验证字符串是否为年月日：2018-03-09。
     *
     * @param nyr 验证的字符串
     * @return true为正确
     */
    public static boolean IsNYR(String nyr) {
        Pattern p = Pattern.compile("^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
        return !nullOrEmpty(nyr) && p.matcher(nyr).matches();
    }

    /**
     * 2018/3/28
     * <p>
     * 自定义加载Dialog
     *
     * @param context  上下文
     * @param text     加载文字
     * @param imageSrc 加载图片
     * @return 加载Dialog
     */
    public static Dialog CreateLoadingDialog(Context context, String text,
                                             int imageSrc) {
        @SuppressLint("InflateParams") View parent = LayoutInflater.from(context).inflate(
                R.layout.tool_loading_dialog, null);
        ImageView image = parent.findViewById(R.id.view1);
        image.setImageResource(imageSrc);
        image.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.loading_dialog));
        TextView textview = parent.findViewById(R.id.view2);
        textview.setTextColor(Color.WHITE);
        textview.setText(text);
        Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setContentView(parent);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }


    /**
     * 2018/3/28
     * <p>
     * 判断RecyclerView是否滑动到底部。
     *
     * @param recyclerView 带判断的对象
     * @return true为到达底部
     */
    public static boolean isRecyBottom(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        return visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1;
    }


    /**
     * 2018/3/28
     * <p>
     * 保存位图文件。
     *
     * @param savePath 保存文件路径
     * @param fileName 文件名称
     * @param bitmap   图片资源
     * @return 文件保存路径
     */
    public static void saveFile(String savePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        saveFile(savePath, fileName, bytes);
    }

    /**
     * 2018/3/28
     * <p>
     * 将位图转换为字节数组。Bitmap-->byte[]。
     * <p>
     * 采用jpeg编码，品质为100。
     *
     * @param bm 图片资源
     * @return 图片字节
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 2018/3/28
     * <p>
     * 将字节数组保存到本地文件中。
     *
     * @param savePath 保存文件路径
     * @param fileName 文件名称
     * @param bytes    字节数组
     * @return 保存的本地路径
     */
    public static void saveFile(String savePath, String fileName, byte[] bytes) {
        FileOutputStream fos = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), savePath);
            if (!file.exists()) {
                if (file.mkdirs()) {
                    File fullFile = new File(file, fileName);
                    if (fullFile.exists())
                        fullFile.delete();
                    if (fullFile.createNewFile()) {
                        fos = new FileOutputStream(fullFile);
                        fos.write(bytes);
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 2018/3/28
     * <p>
     * 获取视频文件第一帧。可以是网络视频或是本地视频。需要在工作线程使用。
     *
     * @param filePath 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    public static Bitmap getVideoThumb(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
            retriever.setDataSource(filePath, new Hashtable<String, String>());
        } else {
            retriever.setDataSource(filePath);
        }
        return retriever.getFrameAtTime();
    }

    /**
     * 2018/3/28
     * <p>
     * M5D加密。将字符串使用MD5加密。
     *
     * @param string 带加密字符串
     * @return 加密后的字符串
     */
    public static String MD5Encrypt(String string) {
        if (nullOrEmpty(string))
            return "";
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 2018/3/28
     * <p>
     * 将bitmap保存到相册中。
     *
     * @param context 上下文
     * @param bmp     图片
     * @param file    图片本地保存路径
     * @return true为保存成功
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 2018/3/28
     * <p>
     * 将数值转换为年月日。long-->String
     *
     * @return 返回字符串格式
     */

    public static String int2ydm(long data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        return dateFormat.format(new Date(data));
    }

    /**
     * 2018/3/28
     * <p>
     * 获取当前时间格式化为规定字符串。
     * <p>
     * 1、yyyy-MM-dd。
     * <p>
     * 2、yyyy-MM-dd HH:mm:ss
     * <p>
     * 3、HH:mm:ss
     *
     * @param format 格式化要求
     * @return 时间字符
     */
    public static String getStringDateShort(String format) {
        if (nullOrEmpty(format))
            format = "yyyy-MM-dd";
        Date currentTime = new Date();
        SimpleDateFormat formatter;
        try {
            formatter = new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE);
        } catch (Exception e) {
            formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        }
        return formatter.format(currentTime);
    }

    /**
     * 2018/3/28
     * <p>
     * 将String时间，转换为Date。
     * <p>
     * 1、yyyy-MM-dd。
     * <p>
     * 2、yyyy-MM-dd HH:mm:ss
     * <p>
     * 3、HH:mm:ss
     *
     * @param strDate 待转换的时间字符串
     * @param format  转换格式
     * @return Date
     */
    private static Date strToDate(String strDate, String format) {
        if (nullOrEmpty(strDate) || nullOrEmpty(format))
            return new Date();
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat formatter;
        try {
            formatter = new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE);
        } catch (Exception e) {
            formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        }
        return formatter.parse(strDate, pos);
    }

    /**
     * 2018/3/28
     * <p>
     * 判断两个时间，一个是否在另一个之后。
     * <p>
     * 时间格式为："yyyy-MM-dd"
     *
     * @param endTime   结束时间
     * @param startTime 开始时间
     * @return true第一个时间是第二个时间之后
     */
    public static boolean isBefore(String endTime, String startTime) {
        if (nullOrEmpty(endTime) || nullOrEmpty(startTime))
            return false;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        Date start;
        Date end;
        try {
            start = df.parse(startTime);
            end = df.parse(endTime);
            return end.before(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 2018/3/28
     * <p>
     * 获取当前时间的"时"或"分"。默认返回分
     *
     * @param hm "h"获取当前时，"m"获取当前分
     * @return 时或分
     */
    public static String getHour(String hm) {
        if (nullOrEmpty(hm))
            hm = "h";
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss", Locale.SIMPLIFIED_CHINESE);
        String dateString = formatter.format(currentTime);
        if (hm.equals("h")) {
            return dateString.substring(11, 13);
        } else {
            return dateString.substring(14, 16);
        }
    }

    /**
     * 2018/3/28
     * <p>
     * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟。
     *
     * @param st1 时间1
     * @param st2 时间2
     * @return 差值分钟
     */
    public static String getTwoHour(String st1, String st2) {
        if (nullOrEmpty(st1) || nullOrEmpty(st2))
            return "0";
        String[] kk;
        String[] jj;
        kk = st1.split(":");
        jj = st2.split(":");
        if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
            return "0";
        else {
            double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1]) / 60;
            double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1]) / 60;
            if ((y - u) > 0)
                return y - u + "";
            else
                return "0";
        }
    }

    /**
     * 2018/3/28
     * <p>
     * 计算两个时间的天数间隔。格式必须为yyyy-MM-dd。
     *
     * @param sj1 开始时间
     * @param sj2 结束时间
     * @return 间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        if (nullOrEmpty(sj1) || nullOrEmpty(sj1))
            return "0";
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        long day;
        try {
            Date date = myFormatter.parse(sj1);
            Date date2 = myFormatter.parse(sj2);
            day = (date.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 2018/3/28
     * <p>
     * 返回一个给定长度的随机数，纯数字。
     *
     * @param i 数字长度
     * @return 数字字符串
     */
    public static String getRandom(int i) {
        Random jjj = new Random();
        if (i == 0)
            return "";
        StringBuilder jj = new StringBuilder();
        for (int k = 0; k < i; k++) {
            jj.append(jjj.nextInt(9));
        }
        return jj.toString();
    }

    /**
     * 创建删除Dialog
     *
     * @param activity
     * @param sure
     */
    public static void deleteDialog(Activity activity, DialogInterface.OnClickListener sure) {
        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(activity)
                .setTitle("是否确认删除？")
                .setMessage("删除将无法找回")
                .setPositiveButton("删除", sure)
                .setNegativeButton("保留", null)
                .create();
        dialog.show();
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }


    /**
     * 获得带水印的Bitmap
     *
     * @param context 上下文
     * @param content 水印内容
     * @return BitmapDrawable 对象
     */
    public static BitmapDrawable getWaterMark(Context context, String content) {
        int width = context.getResources().getDisplayMetrics().widthPixels / 2;
        int height = context.getResources().getDisplayMetrics().heightPixels / 5;
        int gap = (int) context.getResources().getDisplayMetrics().density;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setAlpha(80);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(9);
        Path path = new Path();
        path.moveTo(gap, height - gap);
        path.lineTo(width - gap, gap);
        canvas.drawTextOnPath(content, path, 0, 30, paint);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bitmapDrawable.setDither(true);
        return bitmapDrawable;
    }

    /**
     * 跳转到打开GPS页面
     *
     * @param context 上下文
     */
    public static void openGps(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("GPS未打开")
                .setMessage("打开方法：\n\n模式->高精确度")
                .setCancelable(false)
                .setPositiveButton("去打开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                })
                .show();
    }

    /**
     * 2018/3/28
     * <p>
     *
     * @param downUrl 文件下载地址
     * @param saveUrl 文件保持地址
     * @param result  下载进度
     * @return RX对象
     */
    public static Disposable downFile(String downUrl, final String saveUrl,
                                      final SingleLiveEvent<Integer> result) {
        return ServiceGenerator.createService(BimoClient.class, null)
                .downFile(downUrl)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        AccFileIOUtil.writeResponseBodyToDisk(responseBody, saveUrl, result);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
