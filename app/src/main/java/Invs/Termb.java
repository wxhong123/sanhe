package Invs;

import android.util.Log;

public class Termb {
	/*
	 * #define ERR_SUCCESS 1//成功 #define ERR_FAIL 0//失败 #define ERR_OPENECOMM
	 * -1//未找到设备 #define ERR_SERIALNO -2//未授权机具 #define ERR_SAMID -3//取samID失败
	 */
	static public native int PowerOpen();

	static public native void PowerClose();

	static public native int PowerIoCtl(int led_num, int on_off);// 1 on, 2 off

	/* Serial Port */
	static public native void InitDebug(String sFile);

	static public native byte[] GetUsbPath();

	static public native int InitComm(String sPort);// InitComm1("/dev/ttyS0");

	static public native int FindCardCmd();// 寻卡命令

	static public native int SelCardCmd();// 选卡命令

	static public native void Authenticate();// 鉴权命令，内部调用FindCardCmd和SelCardCmd

	static public native void CloseComm();// 关闭设备

	static public native byte[] ReadSamidCmd();// 读安全模块号

	static public native int ReadCard(byte[] txt, byte[] wlt, byte[] bmp);// 读普通卡

	static public native int ReadCardExt(byte[] txt, byte[] wlt, byte[] bmp);// 读指纹卡

	static public native int GetFingerData(byte[] finger);// 返回指纹长度和指纹数据

	static public native byte[] Wlt2Bmp(byte[] szWlt);

	static public native int ReadApp(byte[] txt);// 追加地址

	static {
		try {
			System.loadLibrary("Termb");
		} catch (UnsatisfiedLinkError e) {
			Log.d("Termb", "invs termb library not found!");
		}
	}

	static boolean SwitchPower(String paramString) {
		try {
			Process localProcess = Runtime.getRuntime().exec("/system/bin/sh");
			String str = "echo " + paramString + " > /sys/devices/platform/module_power_ctl.0/power_enable\n" + "exit\n";
			localProcess.getOutputStream().write(str.getBytes());
			int i = localProcess.waitFor();
			if (i != 0)
				return false;
		} catch (Exception localException) {
			localException.printStackTrace();
			return false;
		}
		return true;
	}

	static public boolean ChkMod(String szPath) {
		try {
			Process su;
			su = Runtime.getRuntime().exec("/system/bin/sh");
			String cmd = "chmod 666 " + szPath + "\n" + "exit\n";
			su.getOutputStream().write(cmd.getBytes());
			int i = su.waitFor();
			if (i != 0)
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	static public int powerOn() {
		if (SwitchPower("829511"))
			return 8;
		return -1;
	}

	static public void powerOff() {
		SwitchPower("862510");
	}
}