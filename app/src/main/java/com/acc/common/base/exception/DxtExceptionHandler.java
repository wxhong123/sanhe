package com.acc.common.base.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.acc.common.URL;
import com.acc.common.base.MyApplication;
import com.acc.common.myokhttp.response.RawResponseHandler;
import com.acc.common.util.SystemUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;


/**
 * UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。 如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框
 * 实现该接口并注册为程序中的默认未捕获异常处理 这样当未捕获异常发生时，就可以做些异常处理操作 例如：收集异常信息，发送错误报告 等。
 * <p>
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 */
public class DxtExceptionHandler implements UncaughtExceptionHandler {
    /**
     * Debug Log Tag
     */
    public static final String TAG = "CrashHandler";
    /**
     * 是否开启日志输出, 在Debug状态下开启, 在Release状态下关闭以提升程序性能
     */
    public static final boolean DEBUG = true;
    /**
     * CrashHandler实例
     */
    private static DxtExceptionHandler INSTANCE;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * 使用Properties来保存设备的信息和错误堆栈信息
     */
    // private Properties mDeviceCrashInfo = new Properties();
    // private Map<String,Object> mDeviceCrashInfo = new
    // HashMap<String,Object>();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = ".cr";

    // StringBuffer sendInfoBuffer = new StringBuffer();

    /**
     * 保证只有一个CrashHandler实例
     */
    private DxtExceptionHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static DxtExceptionHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new DxtExceptionHandler();
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // Sleep一会后结束程序
            // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error : ", e);
            }

        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        collectAndSendInfo(mContext, ex);
        // Sleep一会后结束程序
        // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error : ", e);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
        return true;
    }

    /**
     * 功能描述：收集并发送错误信息
     *
     * @param ctx
     * @param tr
     */
    public static void collectAndSendInfo(final Context ctx, Throwable tr) {
        // 使用Toast来显示异常信息
//		new Thread() {
//			@Override
//			public void run() {
//				// Toast 显示需要出现在一个线程的消息队列中
//
//				Looper.prepare();
//
//				Toast.makeText(ctx, "出现意外错误，请您尽快与系统管理员联系！", Toast.LENGTH_LONG).show();
//
//				Looper.loop();
//			}
//		}.start();
        // 保存错误报告文件
        saveCrashInfoToFile(tr, ctx, collectCrashDeviceInfo(ctx));
        // 发送错误报告到服务器
        sendCrashReportsToServer(ctx);
    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param ctx
     */
    public static StringBuffer collectCrashDeviceInfo(Context ctx) {
        StringBuffer sendInfoBuffer = new StringBuffer();
        try {
            // Class for retrieving various kinds of information related to the
            // application packages that are currently installed on the device.
            // You can find this class through getPackageManager().
            PackageManager pm = ctx.getPackageManager();
            // getPackageInfo(String packageName, int flags)
            // Retrieve overall information about an application package that is
            // installed on the system.
            // public static final int GET_ACTIVITIES
            // Since: API Level 1 PackageInfo flag: return information about
            // activities in the package in activities.
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                // public String versionName The version name of this package,
                // as specified by the <manifest> tag's versionName attribute.
                // @wujp mDeviceCrashInfo.put(VERSION_NAME, pi.versionName ==
                // null ? "not set" : pi.versionName);
                sendInfoBuffer.append(VERSION_NAME).append("=[")
                        .append(pi.versionName == null ? "not set" : pi.versionName).append("]").append("\n");
                // public int versionCode The version number of this package,
                // as specified by the <manifest> tag's versionCode attribute.
                // @wujp mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
                sendInfoBuffer.append(VERSION_CODE).append("=[").append(pi.versionCode).append("]").append("\n");
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
        }
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                // setAccessible(boolean flag)
                // 将此对象的 accessible 标志设置为指示的布尔值。
                // 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常
                field.setAccessible(true);
                // @wujp mDeviceCrashInfo.put(field.getName(), field.get(null));
                sendInfoBuffer.append(field.getName()).append("=[").append(field.get(null)).append("]").append("\n");
                if (DEBUG) {
                    Log.d(TAG, field.getName() + " : " + field.get(null));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error while collect crash info", e);
            }
        }
        return sendInfoBuffer;
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */
    private static String saveCrashInfoToFile(Throwable ex, Context context, StringBuffer sendInfoBuffer) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        // printStackTrace(PrintWriter s)
        // 将此 throwable 及其追踪输出到指定的 PrintWriter
        ex.printStackTrace(printWriter);

        // getCause() 返回此 throwable 的 cause；如果 cause 不存在或未知，则返回 null。
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        // toString() 以字符串的形式返回该缓冲区的当前值。
        String result = info.toString();
        // @wujp mDeviceCrashInfo.put(STACK_TRACE, result);

        sendInfoBuffer.append(STACK_TRACE).append("=[").append(result).append("]").append("\n");
        printWriter.close();
        try {
            long timestamp = System.currentTimeMillis();
            String fileName = "crash-" + timestamp + CRASH_REPORTER_EXTENSION;
            // 保存文件
            FileOutputStream trace = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            trace.write(sendInfoBuffer.toString().getBytes());
            // mDeviceCrashInfo.store(trace, "");
            trace.flush();
            trace.close();
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
        }
        return null;
    }

    /**
     * 把错误报告发送给服务器,包含新产生的和以前没发送的.
     *
     * @param ctx
     */
    private static void sendCrashReportsToServer(final Context ctx) {
        String[] crFiles = getCrashReportFiles(ctx);
        if (crFiles != null && crFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet<String>();
            sortedFiles.addAll(Arrays.asList(crFiles));
            final File filesDir = ctx.getFilesDir();
            for (final String fileName : sortedFiles) {
                new Thread() {
                    @Override
                    public void run() {
                        synchronized (fileName) {
                            final File cr = new File(filesDir, fileName);
                            if (postReport(cr, ctx)) {
                                cr.delete();// 删除已发送的报告
                            }
                            ;
                        }
                    }
                }.start();
            }
        }
    }

    /**
     * 获取错误报告文件名
     *
     * @param ctx
     * @return
     */
    private static String[] getCrashReportFiles(Context ctx) {
        File filesDir = ctx.getFilesDir();
        // 实现FilenameFilter接口的类实例可用于过滤器文件名
        FilenameFilter filter = new FilenameFilter() {
            // accept(File dir, String name)
            // 测试指定文件是否应该包含在某一文件列表中。
            public boolean accept(File dir, String name) {
                return name.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        // list(FilenameFilter filter)
        // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中满足指定过滤器的文件和目录
        return filesDir.list(filter);
    }

    /**
     * 功能描述：发送错误报告到服务器
     *
     * @param file
     * @param context
     * @return
     */
    private static boolean postReport(File file, final Context context) {
        StringBuffer strBuff = new StringBuffer();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String str;
            while ((str = br.readLine()) != null) {
                strBuff.append(str).append("\n");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Map<String, Object> params = new HashMap<String, Object>();
        MyApplication ctx = null;
        if (context instanceof MyApplication) {
            ctx = (MyApplication) context;
        } else {
            ctx = (MyApplication) context.getApplicationContext();
        }
        params.put("filename", file.getName());
        params.put("deviceId", SystemUtils.getImei(context, ""));
        params.put("reportinfo", strBuff.toString());

        MyApplication.mMyOkhttp.get().addParam("filename", file.getName()).addParam("deviceId", SystemUtils.getImei(context, "")).addParam("reportinfo", strBuff.toString()).url(URL.host + URL.UPLOAD_EXCEPTION_URL).enqueue(new RawResponseHandler() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                Log.d("uploadException:", "====>>>" + error_msg);
                System.exit(0);
            }

            @Override
            public void onSuccess(int statusCode, String response) {
                Log.d("uploadException", "====>>>" + response);
                System.exit(0);
            }
        });
        return true;
    }

    /**
     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
     */
    public void sendPreviousReportsToServer() {
        sendCrashReportsToServer(mContext);
    }


}