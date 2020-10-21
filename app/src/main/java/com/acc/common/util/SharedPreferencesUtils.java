package com.acc.common.util;

import android.content.Context;
import android.content.SharedPreferences;

/*
* 保存数据到沙箱
* */
public class SharedPreferencesUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "CONFIG";
    private static final String FILE_TIME = "TIME";
    private static final String URL_KEY = "URL_KEY";//存储服务器域名
    private static final String IM_IP = "IM_IP";//存储openfire ip
    private static final String IM_PORT = "IM_PORT";//存储openfire port
    private static final String IS_LOGIN = "IS_LOGIN";//存储是否登录
    private static final String NETWORK_STATE="NETWORK_STATE"; //是否在线 true：在线 false：离线



    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *

     */
    public static void setParam(Context context, String key, Object object) {
       // String path;
        /*try {
             path = context.getFilesDir().getAbsolutePath() + "/sandbox/" ;
            File file = new File(path) ;
            if(!file.exists()){
                file.mkdirs() ;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }*/
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key,
                                  Object defaultObject) {
       /* String path = FILE_NAME;
        try {
            path = context.getFilesDir().getAbsolutePath() + "/sandbox/" ;
            File file = new File(path) ;
            if(!file.exists()){
                file.mkdirs() ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    public static String getMemberId(Context context) {
        /*String path = FILE_NAME;
        try {
            path = context.getFilesDir().getAbsolutePath() + "/sandbox/" ;
            File file = new File(path) ;
            if(!file.exists()){
                file.mkdirs() ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getString("memberId", null);
    }

    public static boolean hasBind(Context context) {
       /* String path = FILE_NAME;
        try {
            path = context.getFilesDir().getAbsolutePath() + "/sandbox/" ;
            File file = new File(path) ;
            if(!file.exists()){
                file.mkdirs() ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        String flag = sp.getString("bind_flag", "");
        if ("ok".equalsIgnoreCase(flag)) {
            return true;
        }
        return false;
    }

    public static void setIsShortCut(Context context, Boolean isLogin) {
       /* String path = FILE_NAME;
        try {
            path = context.getFilesDir().getAbsolutePath() + "/sandbox/" ;
            File file = new File(path) ;
            if(!file.exists()){
                file.mkdirs() ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isShortCut", isLogin);
        editor.commit();
    }

    public static Boolean isShortCut(Context context) {
       /* String path = FILE_NAME;
        try {
            path = context.getFilesDir().getAbsolutePath() + "/sandbox/" ;
            File file = new File(path) ;
            if(!file.exists()){
                file.mkdirs() ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean("isShortCut", false);
    }

    public static boolean setValue(Context context, String key, String value) {
        //String path = FILE_NAME;

        try {

            /*path = context.getFilesDir().getAbsolutePath() + "/sandbox/" ;
            File file = new File(path) ;
            if(!file.exists()){
                file.mkdirs() ;
            }*/
            SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static String getValue(Context context, String key, String defaultvalue) {
        String path = FILE_NAME;
        try {
            /*path = context.getFilesDir().getAbsolutePath() + "/sandbox/" ;
            File file = new File(path) ;
            if(!file.exists()){
                file.mkdirs() ;
            }*/
            SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, defaultvalue);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


    }
    public static String getValue(Context context, String key) {
        return getValue(context, key, "");
    }
    /**
     * 存储 服务器地址
     * @param context
     * @return
     */
    public static boolean setUrl(Context context, String value) {
        return setValue(context, URL_KEY,value);
    }
    /**
     * 获取 服务器地址
     * @param context
     * @return
     */
    public static String getUrl(Context context) {
        return getValue(context,URL_KEY, "");
    }
    /**
     * 存储 xmpp ip
     * @param context
     * @return
     */
    public static boolean setImIp(Context context, String value) {
        return setValue(context, IM_IP,value);
    }

    /**
     * 获取 xmpp ip
     * @param context
     * @return
     */
    public static String getImIp(Context context) {
        return getValue(context,IM_IP, "");
    }
    /**
     * 存储 xmpp Port
     * @param context
     * @return
     */
    public static boolean setImPort(Context context, String value) {
        return setValue(context, IM_PORT,value);
    }
    /**
     * 获取 xmpp Port
     * @param context
     * @return
     */
    public static String getImPort(Context context) {
        return getValue(context,IM_PORT, "");
    }
    /**
     * 设置 已经登录
     * @param context
     */
    public static void setIsLogin(Context context, boolean isLogin) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.commit();
    }
    /**
     * 获取是否 已经登录
     * @param context
     * @return
     */
    public static boolean getIsLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        return sp.getBoolean(IS_LOGIN, false);
    }


    public static boolean isOnLine(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(NETWORK_STATE, Context.MODE_PRIVATE);
        return sp.getBoolean(NETWORK_STATE, false);
    }


}
