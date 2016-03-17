package com.baozun.nebula.sdk.utils;

/**
 * @author jun.lu
 * @creattime 2013-11-20
 */
public class StringUtil {

	public static boolean isNull(String str) {
		boolean flag = false;
		if (null == str) {
			flag = true;
		} else if (str.trim().length() <= 0) {
			flag = true;
		}

		return flag;
	}

	public static boolean isNotNull(String str) {
		boolean flag = false;
		if (null != str && str.trim().length() > 0) {
			flag = true;
		}

		return flag;
	}

}
