package com.baozun.nebula.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailParamEnciphermentUtil {
	
	/**
	 * 该方法用于发送邮件时对传出参数进行加密
	 * @param token
	 * @param nowDate
	 * @param action
	 * @param key
	 * @param sequence //SELECT nextval ('serial');
	 * @return
	 */
	public static String enciphermentParam(String token,Date nowDate,String action,String key,String sequence){
		String param = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String q = sdf.format(nowDate);//时间参数
		
		String paramForT = "q="+q+sequence;
		String t = Md5Encrypt.md5(paramForT+key);//token参数
		
		String paramForS = "action="+action+"t="+t+"q="+q;
		
		String s= Md5Encrypt.md5(paramForS+key);//s参数
		
		param = "t="+t+"&q="+q+"&s="+s;
		return param;
	}
	
	/**
	 * 该方法用于检测由邮件传入的参数正确性
	 * @param action
	 * @param key
	 * @param token
	 * @param s
	 * @param date
	 * @return
	 */
	public static Boolean checkParam(String action,String key,String token,String s,String date){
		Boolean flag = false;
		String paramForS = "action="+action+"t="+token+"q="+date;
		
		String sp= Md5Encrypt.md5(paramForS+key);
		
		if(Validator.isNotNullOrEmpty(sp) && Validator.isNotNullOrEmpty(s)){
			if(sp.equals(s)){
				flag = true;
			}
		}
		
		return flag;
	}
}
