package com.cnnk.IdentifyCard;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;


public class RequestParameter {

	private static RequestParameter requestParametern = new RequestParameter();

	// reqeust請求參數
	private static Map paramap = new HashMap();

	// session请求参数
	private static Map sessionMap = new HashMap();

	// atrribute请求参数
	private static Map attributeMap = new HashMap();

	public  void put(String key, String value) {
		paramap.put(key, value);
	}

	/**
	 * 获得变量值，先Atrribute->request->sesion
	 * @param key 健值
	 * @return
	 */
	public String get(String key) {
		String str = getAttr(key);
		if (!TextUtils.isEmpty(str))
			return str;
		str = getRequest(key);
		if (!TextUtils.isEmpty(str))
			return str;
		return getSession(key);
	}

	public static void putRequest(String key, String value) {
		paramap.put(key, value);
	}

	public String getRequest(String key) {
		return MapUtils.getString(paramap, key);
	}

	public void putSessoin(String key, String value) {
		sessionMap.put(key, value);
	}

	public String getSession(String key) {
		return MapUtils.getString(sessionMap, key);
	}

	public void putAttr(String key, String value) {
		attributeMap.put(key, value);
	}

	public String getAttr(String key) {
		String str = MapUtils.getString(attributeMap, key);
		if (attributeMap.containsKey(key)) {
			attributeMap.remove(key);
		}
		return str;
	}

	public static RequestParameter getRequestParameter() {

		return requestParametern;

	}
	
	/**
	 * 登录状态
	 */
	public static boolean loginStatus = false;
	
	/**
	 * 分辨率
	 */
	public static String HEIGHTWIDTH = "";
	
	/**
	 * 在线离线
	 */
	//public static String onlineoffline = "";
}
