/***********************************************************************
 * Module:  AndroidUtils.java
 * Author:  Helong
 * Purpose: Defines the Class AndroidUtils
 ***********************************************************************/
package com.acc.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.cjt2325.cameralibrary.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class AndroidUtils {

    private static String LOG_TAG = AndroidUtils.class.getName();

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            return bitmap;
        }
    }

    public static void copyAssetFile(Context ctx, String srcFileName, String targetFilePath) {
        AssetManager assetManager = ctx.getAssets();
        try {
            InputStream is = assetManager.open(srcFileName);
            File out = new File(targetFilePath);
            if (!out.exists()) {
                out.getParentFile().mkdirs();
                out.createNewFile();
            }
            OutputStream os = new FileOutputStream(out);
            JUtils.copy(is, os);
        } catch (IOException e) {
            Log.d(LOG_TAG, "error when copyAssetFile", e);
        }
    }

    public static boolean startPackage(Context ctx, String packageName) {
        if (ctx == null || packageName == null || "".equals(packageName))
            return false;

        PackageManager packageManager = ctx.getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            Log.w(LOG_TAG, "Intent to launch package:" + packageName + " not found.");
            return false;
        }
        ctx.startActivity(intent);

        return true;
    }

    public static void showShortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static String getUniqueId(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        String deviceId;
        deviceId = tm.getDeviceId();
        if (deviceId == null)
            deviceId = "UNKNOWN";
        return deviceId;
    }

//	public static void fetchLocation(Context ctx, StringBuffer lo, StringBuffer la) {
//		LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
//		Location ll = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//		if (ll != null && la != null) {
//			la.append(ll.getLatitude());
//		}
//		if (ll != null && lo != null) {
//			lo.append(ll.getLongitude());
//		}
//	}


    public static String getFilePath(Context context, String dir) {
        String directoryPath = "";
//判断SD卡是否可用 
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            directoryPath = context.getExternalFilesDir(dir).getAbsolutePath();
// directoryPath =context.getExternalCacheDir().getAbsolutePath() ;  
        } else {
//没内存卡就存机身内存  
            directoryPath = context.getFilesDir() + File.separator + dir;
// directoryPath=context.getCacheDir()+File.separator+dir;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在  
            file.mkdirs();
        }
        LogUtil.i("filePath====>" + directoryPath);
        return directoryPath;
    }

}
