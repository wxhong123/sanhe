package com.cnnk.IdentifyCard;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;


import Invs.Termb;
import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

public class IC2ReadCard {
	public static final String G19 = "G19";
	public static final String G20 = "G20";
	public boolean g_bTerminate = false;
	public boolean g_bDeviceOk = false;
	public String g_szSamid = "";
	int GPIO_IDCARD6 = 58;
	// 开电源代码，如硬件没有设计该功能可以去掉该代码
	boolean mPoweronIsHigh = true;
	int mGpioFd = -1;
	String GPIO_FILE = "/dev/mtgpio";
	int GPIO_IDCARD = 114;
	String model = new Build().MODEL;
	private static final String GPIO_PATH = "/sys/devices/virtual/misc/mtgpio/pin";
	public void openPower() {
		
		//if (Constants.PHONE_NAME.contains(model)) {
			String mOsVersion = Build.VERSION.RELEASE;
			if (mOsVersion.startsWith("5")||mOsVersion.startsWith("4")) {
				mGpioFd = Gpio.GPIO_open(GPIO_FILE);
				if (mGpioFd < 0) {
					Log.e("IC2ReadCard", "  Open GPIO file " + GPIO_FILE + "  failed " + mGpioFd);
				} else {
					// Power on
					if (mPoweronIsHigh) // Pull GPIO to High will power on
						Gpio.GPIO_pullHigh(mGpioFd, GPIO_IDCARD);
					else
						// Pull GPIO to Low will power on
						Gpio.GPIO_pullLow(mGpioFd, GPIO_IDCARD);
				}// 开电源结束

				
			} else if(mOsVersion.startsWith("6"))//6.0系统
			{
				// 开电源代码，如硬件没有设计该功能可以去掉该代码
							mGpioFd = tf.test.Gpio.GPIO_open(GPIO_FILE);
							if (mGpioFd < 0) {
								//Log.e(TAG, "  Open GPIO file " + GPIO_FILE + "  failed " + mGpioFd);
							} else {
								// Power on
								tf.test.Gpio.GPIO_pullHigh(mGpioFd, GPIO_IDCARD6);
							}// 开电源结束
			}else if (mOsVersion.startsWith("7")) {
				poweron();
			}
		//}
		
	}
	void poweron() {
		try {
			BufferedWriter bufWriter = null;
			bufWriter = new BufferedWriter(new FileWriter(GPIO_PATH));
			bufWriter.write("-wdout7 1"); // 写操作
			bufWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	// 身份证模块断电
	void poweroff() {

		try {
			BufferedWriter bufWriter = null;
			bufWriter = new BufferedWriter(new FileWriter(GPIO_PATH));
			bufWriter.write("-wdout7 0"); // 写操作
			bufWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	public void closePower() {
		//Gpio.GPIO_pullLow(mGpioFd, GPIO_IDCARD);
		//if (Constants.PHONE_NAME.contains(model)) {
			String mOsVersion = Build.VERSION.RELEASE;
			if (mOsVersion.startsWith("5")||mOsVersion.startsWith("4")) {
				Gpio.GPIO_pullLow(mGpioFd, GPIO_IDCARD);
			} else if(mOsVersion.startsWith("6")) {
				/*Termb.powerOn();*/
				tf.test.Gpio.GPIO_pullLow(mGpioFd, GPIO_IDCARD6);
				if (mGpioFd < 0){
		           // Log.d(TAG, "onDestroy mGpioFd < 0");
		        }else{
		        	tf.test.Gpio.GPIO_close(mGpioFd);
		        //    Log.d(TAG, "onDestroy Gpio.GPIO_close");
		        }
			}else if(mOsVersion.startsWith("7")){
				poweroff();
			}
			stopScanMore();
	//	}
		
		 
	}

	public void stopScanMore() {
		g_bTerminate = true;
	}

	public void read() {

		InitDev();
		boolean flag = true;
		while (flag) {
			SystemClock.sleep(10);
			if (InitDev()) {
				ReadCard();
				flag = false;
			} else {
				flag = false;

			}
		}

	}


	private boolean InitDev() {
		if (g_bDeviceOk)
			return true;

		g_szSamid = "";



		int iError = 0;
		if (Build.MODEL.equals(G19)) {

				long iTick = SystemClock.uptimeMillis();
				iError = Termb.InitComm("/dev/ttyMT2");//invs200

				if (iError != 1) {
					iError = Termb.InitComm("/dev/ttyS1");//invs200
					if (iError != 1) {
						iError = Termb.InitComm("usb");//hid
						if (iError != 1) {
						}
					}

					iError = Termb.InitComm("/dev/ttyS1");//invs200
					if (iError != 1) {
						iError = Termb.InitComm("usb");//hid
						if (iError != 1) {
							Termb.ChkMod("/dev/ttySAC3");
							iError = Termb.InitComm("/dev/ttySAC3");//佳发
							if (iError != 1) {
								Termb.ChkMod("/dev/ttyHSL0");
								iError = Termb.InitComm("/dev/ttyHSL0");
								if (iError != 1) {
									Termb.ChkMod("/dev/ttyMT1");
									iError = Termb.InitComm("/dev/ttyMT1");
									if (iError != 1) {
										Termb.ChkMod("/dev/ttyS0");
										iError = Termb.InitComm("/dev/ttyS0");
									}
								}
							}
						}
					}

				}

		} else if (Build.MODEL.contains(G20)) {
			long iTick = SystemClock.uptimeMillis();
			iError = Termb.InitComm("/dev/ttyMT3");//invs200

		}

		if (iError != 1) {

			return false;
		}

		/*byte[] szSamid = Termb.ReadSamId();
		String sGbk = null;
		try {
			g_szSamid = new String(szSamid, "GBK");
		} catch (UnsupportedEncodingException e1) {
		}
*/
		g_bDeviceOk = true;

		return true;
	}

	private void ReadCard() {
		int iRet = 0;
		//找卡
    	iRet = Termb.FindCardCmd();
    	//没读过 159      放卡读过128     没卡128
    	if (iRet != 159){ // && iRet != 128
    		if (iRet != 128){
    			g_bDeviceOk = false;
    		}
    		return;
    	}
    	//选卡
    	iRet = Termb.SelCardCmd();
    	//没读过 144      放卡读过129     没卡129
    	if (iRet != 144){   //&& iRet != 129
    		if (iRet != 129){
    			g_bDeviceOk = false;
    		}
    		return;
    	}

		byte[] txt = new byte[256];
		byte[] wlt = new byte[2048];
		byte[] bmp = new byte[38862];
		byte[] finger = new byte[1024];
		byte[] app = new byte[256];
		iRet = Termb.ReadCard(txt, wlt, bmp);// 读普通卡
		if (1 != iRet) {
			return;
		}
		iRet = Termb.GetFingerData(finger);
		SucHandle(txt, wlt, bmp);
		/*while((!g_bTerminate)){
    		//读追加地址
    		iRet = Termb.ReadApp(app);
    		//144有追加地址信息  145没有
    		if (iRet != 144 && iRet != 145){
    			break;
    		}
    	}*/
		g_bDeviceOk = true;
	}

	@SuppressLint("NewApi")
	private void SucHandle(byte[] txt, byte[] wlt, byte[] bmp) {
		String sGbk = null;
		try {
			sGbk = new String(txt, "GBK");
			Log.i("sc", sGbk);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String sIdInfo[] = sGbk.split("\\|");
		Cvr100bUtil.getInstance().inv_300_read(sIdInfo);
		Cvr100bUtil.getInstance().setBase64Pic(Base64.encodeToString(bmp, Base64.DEFAULT));
	}

	private File writeBytesToFile(byte[] inByte, String pathAndNameString) throws IOException {
		File file = null;
		try {
			file = new File(pathAndNameString);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.delete();
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(inByte);
			fos.close();
		} catch (IOException e) {
			return null;
		}
		return file;
	}
}
