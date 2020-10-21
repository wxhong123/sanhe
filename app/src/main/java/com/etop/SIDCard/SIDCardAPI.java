package com.etop.SIDCard;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SIDCardAPI {
	static {
		System.loadLibrary("AndroidSIDCard");
	}
	
	
	public native int SIDCardKernalInit(String szSysPath,String FilePath,String CommpanyName,int nProductType,int nAultType,TelephonyManager telephonyManager,Context context);
	public native void SIDCardKernalUnInit();
	//public native int SIDCardRecognizeNV21(byte[] ImageStreamNV21, int Width, int Height, char[] Buffer, int BufferLen);
	public native int SIDCardRecognizeNV21Ex(byte[] ImageStreamNV21, int Width, int Height);
	public native int SIDCardSetROI(int left,int top,int right,int bottom);
	public native int SIDCardRecognizeIDNumNV21(byte[] ImageStreamNV21, int Width, int Height,int nType);
	public native int SIDCardSetIDNumROI(int left,int top,int right,int bottom);
	public native String SIDCardGetResult(int nIndex);
	public native int SIDCardSaveCardImage(String path);
	public native int SIDCardSaveHeadImage(String path);
	public native int SIDCardRecogOtherImgaeFileW(String path,char[] Buffer,int BufferLen);
	public native int SIDCardSetRecogType(int nType);
	public native int SIDCardGetRecogType();
	public native int SIDCardGetImgDirection();
}
