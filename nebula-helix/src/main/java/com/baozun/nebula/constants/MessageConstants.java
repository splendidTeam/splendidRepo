package com.baozun.nebula.constants;

/**
 * 验证码生成所需的常量
 * @author shouqun.li
 * @version 2016年3月24日 上午9:50:46
 */
public interface MessageConstants {
	
	/**从字母和数字的组合中生成验证码*/
	public static final String SECURITY_CODE_ORIGINAL_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	/**单纯的数字生成验证码*/
	public static final String SECURITY_CODE_ORIGINAL_NUMBER = "0123456789";
	
	/**验证码字段的设置*/
	public static final int SECURITY_CODE_LENGTH = 6;
}
