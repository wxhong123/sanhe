/***********************************************************************
 * Module:  Constants.java
 * Author:  Helong
 * Purpose: Defines the interface Constants
 ***********************************************************************/
package com.cnnk.IdentifyCard;

public interface Constants {

	String INIT_MAP = "INIT_MAP";//初始化地图类型

	String INIT_MAP_TYPE = "1";//初始化地图标识
	// 蓝牙读卡
	public static class Cvr100
	{
		public static final String BLUETOOTH_CONN_EXCEPTION = "蓝牙连接异常";
		public static final String BLUETOOTH_NOT_OPEN = "蓝牙未开启！";
		public static final String DEVICE_CONN_EXCEPTION = "设备连接异常！";
		public static final String DEVICE_CONN_FAIL = "设备连接失败！";
		public static final String DEVICE_CONN_SUCCESS = "DEVICE_CONN_SUCCESS";
		public static final String NOCARD_OR_READ = "无卡或卡片已读过";
		public static final String OPERATION_EXCEPTION = "操作异常";
		public static final String READCARD_DATA_EXCEPTION = "读取数据异常！";
		public static final String READCARD_FAIL = "读卡失败";
		public static final String READCARD_SUCCESS = "READCARD_SUCCESS";
	}
	/**
	 * 工作状态数据持久化标识
	 */
	String SP_WORKSTATUS = "SP_WORKSTATUS";
	String SP_START_WORK_TIME = "SP_START_WORK_TIME";
	@interface WorkStatus {
		String START_WORK = "1";
		String START_COMMUNITY = "2";
		String STOP_COMMUNITY = "3";
		String STOP_WORK = "4";
	}

}
