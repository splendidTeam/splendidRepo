package com.baozun.nebula.sdk.utils;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.feilong.core.RegexPattern;
/**
 * 
 * @deprecated pls use {@link com.feilong.core.RegexUtil} {@link com.feilong.core.RegexPattern} 
 */
@Deprecated
public class RegulareExpUtils {

	public static final String EMAIL_REG = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	
	public static final String MOBILE_REG=RegexPattern.MOBILEPHONE;
	/**
	 * 是手机号码返回true
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		String reg = getReg("mobile_reg", MOBILE_REG);
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 验证是否是合法格式的邮箱
	 * @param email
	 * @return
	 */
	public static boolean isSureEmail(String email) {
		String reg = getReg("email_reg", EMAIL_REG);
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	
	private static String getReg(String key,String defaultValue){
		Properties properties = ProfileConfigUtil.findCommonPro("config/common.properties");
		String reg =  properties.getProperty(key,defaultValue);
		return reg;
	}

}
