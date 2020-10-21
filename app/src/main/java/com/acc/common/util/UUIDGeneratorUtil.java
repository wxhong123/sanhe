package com.acc.common.util;

import java.util.UUID;

/** 
 * 生成UUID
 * <p></p> 
 * @author 何晓超
 * @version v1.0.0
 * <p>最后更新 by 何晓超 @ 2012年12月5日9:40:04 </p>
 * @since 
 */
public class UUIDGeneratorUtil {

	/**
	 * 生成一个UUID。
	 * <p> </p>
	 * @return strUuid String
	 */
	public static String getUUID() {
		String strUuid = UUID.randomUUID().toString();
		strUuid = strUuid.substring(0, 8) + strUuid.substring(9, 13) + strUuid.substring(14, 18)
				+ strUuid.substring(19, 23) + strUuid.substring(24);
		return strUuid;
	}

	/**
	 * 获得指定数目的UUID。
	 * <p>根据传入的数量，得到指定数量的UUID数组</p>
	 * @param number int 需要获得的UUID数量 
	 * @return String[]  UUID数组
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] strUuids = new String[number];
		for (int i = 0; i < number; i++) {
			strUuids[i] = getUUID();
		}
		return strUuids;
	}
}
