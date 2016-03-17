package com.baozun.nebula.manager.email;

import java.util.Date;

import com.baozun.nebula.manager.BaseManager;

public interface EmailEnciphermentManager extends BaseManager{
	
	/**
	 * 该方法用于发送邮件时对传出参数进行加密
	 * @param token
	 * @param nowDate
	 * @param action
	 * @param key
	 * @param sequence
	 * @return
	 */
	public String getEncryptString(String token,Date nowDate,String action,String key,String sequence);
	
	
	/**
	 * 该方法用于检测由邮件传入的参数正确性
	 * @param action
	 * @param key
	 * @param token
	 * @param s
	 * @param date
	 * @return
	 */
	public Boolean checkParam(String action,String key,String token,String s,String date);

}
