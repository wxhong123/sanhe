package com.qhd;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acc.sanheapp.R;
import com.etop.SIDCard.SIDCardAPI;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class IDNumActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    //private Button photo_button = null;
    private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/alpha/SIDCard/";
    private static final String UserID = "5DDDC395381FC1F48F49";
    private Camera mycamera;
    private SurfaceView surfaceView;
    private RelativeLayout re_c;
    private SurfaceHolder surfaceHolder;
    private SIDCardAPI api = null;
    private Bitmap bitmap;
    private int preWidth = 0;
    private int preHeight = 0;
    private int width;
    private int height;
    private TimerTask timer;
    private Timer timer2;
    private TimerTask timerT1;
    private Timer timer1;
    private ToneGenerator tone;
    private byte[] tackData;
    private IDNumViewfinderView myView = null;
    private long recogTime;
    private boolean isFatty = false;
    private boolean bInitKernal = false;
    private ImageButton back;
    private ImageButton flash;
    private ImageButton nflash;
    //private ImageButton tack_pic;
    AlertDialog alertDialog = null;
    Toast toast;
    String FilePath = "";
    private Vibrator mVibrator;
    private boolean bBackside = false;
    private String resultStr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        try {
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(PATH);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        Configuration cf = this.getResources().getConfiguration(); //获取设置的配置信息
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this).create();
        }
        {
            if (!bInitKernal) {
                if (api == null) {
                    api = new SIDCardAPI();
                }
                //FilePath =Environment.getExternalStorageDirectory().toString()+"/"+UserID+".lic";
                String cacheDir = (this.getCacheDir()).getPath();
                FilePath = cacheDir + "/" + UserID + ".lic";
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                int nRet = api.SIDCardKernalInit("", FilePath, UserID, 0x02, 0x02, telephonyManager, this);
                if (nRet != 0) {
                    Toast.makeText(getApplicationContext(), "激活失败" + nRet, Toast.LENGTH_SHORT).show();
                    System.out.print("nRet=" + nRet);
                    bInitKernal = false;
                } else {
                    System.out.print("nRet=" + nRet);
                    bInitKernal = true;
                }
            }
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // // 屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_idnum);
        findView();

    }

    public void copyDataBase() throws IOException {
        //  Common common = new Common();
        // String dst = Environment.getExternalStorageDirectory().toString() + "/"+UserID+".lic";
        String cacheDir = (this.getCacheDir()).getPath();
        String dst = cacheDir + "/" + UserID + ".lic";
        File file = new File(dst);
        if (!file.exists()) {
            // file.createNewFile();
        } else {
            file.delete();
        }

        try {
            InputStream myInput = getAssets().open(UserID + ".lic");
            OutputStream myOutput = new FileOutputStream(dst);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            System.out.println(UserID + ".lic" + "is not found");
        }
    }

    protected void onRestart() {
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        super.onRestart();
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    private void findView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceViwe);
        re_c = (RelativeLayout) findViewById(R.id.re_c);
        back = (ImageButton) findViewById(R.id.back);
        //tack_pic = (ImageButton) findViewById(R.id.take_pic);
        flash = (ImageButton) findViewById(R.id.flash);
        nflash = (ImageButton) findViewById(R.id.nflash);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        height = metric.heightPixels; // 屏幕高度（像素）

        int back_w = (int) (height * 0.066796875);
        int back_h = (int) (back_w * 1);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        int Fheight = height;
        layoutParams.leftMargin = (int) (width * 0.15);
        layoutParams.topMargin = (int) ((back_h / 2));
        back.setLayoutParams(layoutParams);

        int flash_h = (int) (height * 0.066796875);
        int flash_w = (int) (flash_h * 96 / 106);
        layoutParams = new RelativeLayout.LayoutParams(flash_w, flash_h);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParams.rightMargin = (int) (width * 0.15);
        layoutParams.topMargin = (int) ((back_h / 2));
        flash.setLayoutParams(layoutParams);
        nflash.setLayoutParams(layoutParams);
        nflash.setVisibility(View.INVISIBLE);

        if (myView == null) {
            if (isFatty) {
                myView = new IDNumViewfinderView(IDNumActivity.this, width, height, isFatty);
            } else {
                myView = new IDNumViewfinderView(IDNumActivity.this, width, height);
            }
            re_c.addView(myView);
        }

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                api.SIDCardKernalUnInit();
                //Intent intent = new Intent(IDNumActivity.this, readCard.class);
                //startActivity(intent);
                finish();
            }
        });
        nflash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    String mess = "当前设备不支持闪光灯";
                    Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
                } else {
                    if (mycamera != null) {
                        Camera.Parameters parameters = mycamera.getParameters();
                        String flashMode = parameters.getFlashMode();

                        if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            try {
                                mycamera.setParameters(parameters);
                            } catch (Exception e) {
                                String mess = "当前设备不支持闪光灯";
                                Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
                            }
                            nflash.setVisibility(View.INVISIBLE);
                            flash.setVisibility(View.VISIBLE);
                            //	parameters.setExposureCompensation(0);

                        }
                        //						else {
//							parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);// 闪光灯常亮
//							parameters.setExposureCompensation(-1);
//						//}

                        //mycamera.startPreview();
                    }
                }
            }
        });
        flash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    String mess = "当前设备不支持闪光灯";
                    Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
                } else {
                    if (mycamera != null) {

                        Camera.Parameters parameters = mycamera.getParameters();
                        String flashMode = parameters.getFlashMode();
                        //if () {
                        //	parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        //	parameters.setExposureCompensation(0);
                        //} else {
                        if (!(flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH))) {
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);// 闪光灯常亮
                            try {
                                mycamera.setParameters(parameters);
                            } catch (Exception e) {
                                String mess = "当前设备不支持闪光灯";
                                Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
                            }
                            flash.setVisibility(View.INVISIBLE);
                            nflash.setVisibility(View.VISIBLE);
                            //	parameters.setExposureCompensation(-1);

                        }
                        //}

                        //	mycamera.startPreview();
                    }
                }
            }
        });

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(IDNumActivity.this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.setFocusable(true);
        //surfaceView.invali.date();

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (api == null) {
            api = new SIDCardAPI();
            String cacheDir = (this.getCacheDir()).getPath();
            //FilePath =Environment.getExternalStorageDirectory().toString()+"/"+UserID+".lic";
            FilePath = cacheDir + "/" + UserID + ".lic";
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int nRet = api.SIDCardKernalInit("", FilePath, UserID, 0x02, 0x02, telephonyManager, this);
            if (nRet != 0) {
                Toast.makeText(getApplicationContext(), "激活失败", Toast.LENGTH_SHORT).show();
                System.out.print("nRet=" + nRet);
                bInitKernal = false;
            } else {
                System.out.print("nRet=" + nRet);
                bInitKernal = true;
            }
        }
        if (mycamera == null) {
            try {
                mycamera = Camera.open();
            } catch (Exception e) {
                e.printStackTrace();
                String mess = getResources().getString(R.string.toast_camera);
                Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (mycamera != null) {
            try {

                mycamera.setPreviewDisplay(holder);
                //mycamera.setDisplayOrientation(180);
                timer2 = new Timer();
                if (timer == null) {
                    timer = new TimerTask() {
                        public void run() {
                            if (mycamera != null) {
                                try {
                                    mycamera.autoFocus(new AutoFocusCallback() {
                                        public void onAutoFocus(boolean success, Camera camera) {
                                            //  mycamera.setOneShotPreviewCallback(MainActivity.this);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        ;
                    };
                }
                timer2.schedule(timer, 500, 3500);
                initCamera();
                //mycamera.startPreview();
                //mycamera.autoFocus(null);

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this).create();
        }

    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {

//		if (mycamera != null) {
//			mycamera.autoFocus(new AutoFocusCallback() {
//				@Override
//				public void onAutoFocus(boolean success, Camera camera) {
//					if (success) {
//   						//initCamera();
//   						//mycamera.cancelAutoFocus();
//						mycamera.setOneShotPreviewCallback(MainActivity.this);
//						//mycamera.cancelAutoFocus();
//					}
//				}
//			});
//		}
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            if (mycamera != null) {
                mycamera.setPreviewCallback(null);
                mycamera.stopPreview();
                mycamera.release();
                mycamera = null;
            }
        } catch (Exception e) {
        }
        if (bInitKernal) {
            api.SIDCardKernalUnInit();
            bInitKernal = false;
            api = null;
        }
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        if (timer2 != null) {
            timer2.cancel();
            timer2 = null;
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog.cancel();
            alertDialog = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                if (mycamera != null) {
                    mycamera.setPreviewCallback(null);
                    mycamera.stopPreview();
                    mycamera.release();
                    mycamera = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bInitKernal) {
                api.SIDCardKernalUnInit();
                bInitKernal = false;
                api = null;
            }
            //finish();
            if (toast != null) {
                toast.cancel();
                toast = null;
            }
            if (timer2 != null) {
                timer2.cancel();
                timer2 = null;
            }
            if (alertDialog != null) {
                alertDialog.cancel();
                alertDialog = null;
            }
            //Intent intent = new Intent(IDNumActivity.this, readCard.class);
            //startActivity(intent);
            finish();
            // android.os.Process.killProcess(android.os.Process.myPid());
            // System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    @TargetApi(14)
    private void initCamera() {
        Camera.Parameters parameters = mycamera.getParameters();
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
        Camera.Size size;
        Size tmpsize = getOptimalPreviewSize(list, height, width);
        int length = list.size();
        int previewWidth = list.get(0).width;
        int previewheight = list.get(0).height;
        int second_previewWidth = 0;
        int second_previewheight = 0;
        int nlast = -1;
        int nThird = -1;
        int Third_previewWidth = 0;
        int Third_previewheight = 0;
        previewWidth = tmpsize.width;
        previewheight = tmpsize.height;
//		if(tmpsize.height<700)
//         {
//			
//			for (int i = 0; i < length; i++) {
//				size = list.get(i);
//				if (size.width * height == size.height * width) {
//
//					if(size.height >700){
//	                     if(size.width== width ){
//	                    	 previewWidth = size.width;
//							 previewheight = size.height;
//							 nlast =i;
//	                     }
//	                     else if(size.width!= width && size.width>previewWidth &&nlast==-1){
//	                    	 previewWidth = size.width;;
//							 previewheight = size.height;
//							 nlast =i;
//	                     }
//           
//					}
//				}
//				else if(size.width==width&&nlast==-1)
//				{
//					if(size.height >=height)
//					{
//						previewWidth  = size.width;
//						previewheight = size.height;
//						nThird =i;
//					}
//				}
//				else if(size.height ==height &&nlast==-1&&nThird ==-1)
//				{
//					if(size.width >=width)
//					{
//						previewWidth  = size.width;
//						previewheight = size.height;
//						nThird =i;
//					}
//				}
//			}
//		}
        if (length == 1) {
            preWidth = previewWidth;
            preHeight = previewheight;
        } else {
            second_previewWidth = previewWidth;
            second_previewheight = previewheight;
            for (int i = 0; i < length; i++) {
                size = list.get(i);
                if (size.height > 700 && size.height < previewheight) {
                    if (size.width * previewheight == size.height * previewWidth && size.height < second_previewheight) {
                        second_previewWidth = size.width;
                        second_previewheight = size.height;
                    }
                }
            }
            preWidth = second_previewWidth;
            preHeight = second_previewheight;
        }
        //   preWidth = 1280;
        //   preHeight = 720;

        parameters.setPictureFormat(PixelFormat.JPEG);

        parameters.setPreviewSize(preWidth, preHeight);
//		if (parameters.getSupportedFocusModes().contains(
//				parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//			if (timer2 != null) {
//				timer2.cancel();
//				timer2 = null;
//			}
//			if (timer != null) {
//				timer.cancel();
//				timer = null;
//			}
//			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//		}
//		else 
        if (parameters.getSupportedFocusModes().contains(
                parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);// 1连续对焦
        }
        if (parameters.isZoomSupported()) {
            parameters.setZoom(2);
        }
        mycamera.setPreviewCallback(IDNumActivity.this);
        mycamera.setParameters(parameters);
        mycamera.setDisplayOrientation(90);
        mycamera.startPreview();
        if (api != null) {
            int t;
            int b;
            int l;
            int r;

            l = 0;
            r = width - l;
            int ntmpH = (r - l) * 58 / (88 * 3);
            t = (height - ntmpH) / 2;
            b = t + ntmpH;
            double proportion = (double) height / (double) preWidth;
            //	double hproportion=(double) width / (double) preHeight;
            t = (int) (t / proportion);
            b = (int) (b / proportion);
            //api.SIDCardSetROI(0,t , preHeight, b);
            api.SIDCardSetIDNumROI(0, t, preHeight, b);
        }

        //mycamera.cancelAutoFocus();
    }

    public String savePicture(Bitmap bitmap, String tag) {
        String strCaptureFilePath = PATH + tag + "_SIDCardNum_" + pictureName() + ".jpg";
        //api.SIDCardSaveCardImage(strCaptureFilePath);
        //int nRet =api.SIDCardSaveHeadImage(strCaptureFileHeadPath);
//		if(nRet ==0){
//			System.out.print("1111");
//		}
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(strCaptureFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "图片存储失败,请检查SD卡", Toast.LENGTH_SHORT).show();
        }
        return strCaptureFilePath;
    }

    public String pictureName() {
        String str = "";
        Time t = new Time();
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        if (month < 10)
            str = String.valueOf(year) + "0" + String.valueOf(month);
        else {
            str = String.valueOf(year) + String.valueOf(month);
        }
        if (date < 10)
            str = str + "0" + String.valueOf(date + "_");
        else {
            str = str + String.valueOf(date + "_");
        }
        if (hour < 10)
            str = str + "0" + String.valueOf(hour);
        else {
            str = str + String.valueOf(hour);
        }
        if (minute < 10)
            str = str + "0" + String.valueOf(minute);
        else {
            str = str + String.valueOf(minute);
        }
        if (second < 10)
            str = str + "0" + String.valueOf(second);
        else {
            str = str + String.valueOf(second);
        }
        return str;
    }


    private int counter = 0;
    private int counterCut = 0;

    @SuppressWarnings("deprecation")
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        //surfaceView.invalidate();
        tackData = data;
        resultStr = "";
        if (!alertDialog.isShowing()) {
            int buffl = 256;
            char recogval[] = new char[buffl];
            Date dt = new Date();
            Long timeStart = System.currentTimeMillis();
            int r = api.SIDCardRecognizeIDNumNV21(tackData, preWidth, preHeight
                    , 1);
            Long timeEnd = System.currentTimeMillis();
            recogTime = timeEnd - timeStart;
            if (r == 0) {
                Camera.Parameters parameters = mycamera.getParameters();
                mVibrator = (Vibrator) getApplication().getSystemService(
                        Service.VIBRATOR_SERVICE);
                mVibrator.vibrate(50);
                // 删除正常识别保存图片功能
                int[] datas = convertYUV420_NV21toARGB8888(tackData,
                        parameters.getPreviewSize().width,
                        parameters.getPreviewSize().height);

                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inInputShareable = true;
                opts.inPurgeable = true;
                bitmap = Bitmap.createBitmap(datas,
                        parameters.getPreviewSize().width,
                        parameters.getPreviewSize().height,
                        android.graphics.Bitmap.Config.ARGB_8888);
                //下面是保存原图代码
//				if(api.SIDCardGetImgDirection()==1)
//				{
//					Matrix matrix=new Matrix();
//					matrix.postRotate(180);
//					Bitmap dstbmp=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//					savePicture(dstbmp, "M");	
//				}
//				else
//				{
//					savePicture(bitmap, "M");
//				}


                resultStr = "";
                resultStr += api.SIDCardGetResult(5);
                resultStr += "\r\n";
                resultStr += "\r\n识别时间:" + recogTime + "ms";
                resultStr += "（点击屏幕继续识别）";
                if (resultStr != "") {
                    alertDialog.setMessage(resultStr);
                    alertDialog.show();
                    Window window = alertDialog.getWindow();
                    WindowManager.LayoutParams lp = window.getAttributes();
                    // 设置透明度为0.3
                    lp.alpha = 0.8f;
                    lp.width = width;
                    // lp.flags= 0x00000020;
                    window.setAttributes(lp);
                    window.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                    Intent intent = new Intent();
                    intent.putExtra("xm", api.SIDCardGetResult(0));
                    intent.putExtra("xb", api.SIDCardGetResult(1));
                    intent.putExtra("mz", api.SIDCardGetResult(2));
                    intent.putExtra("sr", api.SIDCardGetResult(3));
                    intent.putExtra("addr", api.SIDCardGetResult(4));
                    intent.putExtra("sfzh", api.SIDCardGetResult(5));
                    intent.putExtra("qfjg", api.SIDCardGetResult(6));
                    intent.putExtra("yxq", api.SIDCardGetResult(7));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
//			else{
//				Camera.Parameters parameters = mycamera.getParameters();
//				// 删除正常识别保存图片功能
//				int[] datas = convertYUV420_NV21toARGB8888(tackData,
//						parameters.getPreviewSize().width,
//						parameters.getPreviewSize().height);
//
//				BitmapFactory.Options opts = new BitmapFactory.Options();
//				opts.inInputShareable = true;
//				opts.inPurgeable = true;
//				bitmap = Bitmap.createBitmap(datas,
//						parameters.getPreviewSize().width,
//						parameters.getPreviewSize().height,
//						android.graphics.Bitmap.Config.ARGB_8888);
//				savePicture(bitmap,"M");
//			}

        }

    }

    @Override
    protected void onStop() {

        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timer2 != null) {
            timer2.cancel();
            timer2 = null;
        }
        if (alertDialog != null) {
            alertDialog.cancel();
            alertDialog.dismiss();
        }
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        try {
            if (mycamera != null) {
                mycamera.setPreviewCallback(null);
                mycamera.stopPreview();
                mycamera.release();
                mycamera = null;
            }
        } catch (Exception e) {
        }
//		api.SIDCardKernalUnInit();
//		finish();
    }

    @TargetApi(14)
    private void NewApis(Camera.Parameters parameters) {
        if (Build.VERSION.SDK_INT >= 14) {
            parameters.setFocusMode(parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
    }

    public int[] convertYUV420_NV21toARGB8888(byte[] data, int width, int height) {
        int size = width * height;
        int offset = size;
        int[] pixels = new int[size];
        int u, v, y1, y2, y3, y4;

        // i along Y and the final pixels
        // k along pixels U and V
        for (int i = 0, k = 0; i < size; i += 2, k += 2) {
            y1 = data[i] & 0xff;
            y2 = data[i + 1] & 0xff;
            y3 = data[width + i] & 0xff;
            y4 = data[width + i + 1] & 0xff;

            u = data[offset + k] & 0xff;
            v = data[offset + k + 1] & 0xff;
            u = u - 128;
            v = v - 128;

            pixels[i] = convertYUVtoARGB(y1, u, v);
            pixels[i + 1] = convertYUVtoARGB(y2, u, v);
            pixels[width + i] = convertYUVtoARGB(y3, u, v);
            pixels[width + i + 1] = convertYUVtoARGB(y4, u, v);

            if (i != 0 && (i + 2) % width == 0)
                i += width;
        }

        return pixels;
    }

    private int convertYUVtoARGB(int y, int u, int v) {
        int r, g, b;

        r = y + (int) 1.402f * u;
        g = y - (int) (0.344f * v + 0.714f * u);
        b = y + (int) 1.772f * v;
        r = r > 255 ? 255 : r < 0 ? 0 : r;
        g = g > 255 ? 255 : g < 0 ? 0 : g;
        b = b > 255 ? 255 : b < 0 ? 0 : b;
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (size.height < 700) continue;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (size.height < 700) continue;
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                } else if (Math.abs(size.height - targetHeight) == minDiff && size.width > optimalSize.width) {
                    optimalSize = size;
                }
            }
        }
        return optimalSize;
    }

}


