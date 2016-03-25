package com.baozun.nebula.utils;

import com.baozun.nebula.exception.CodeLengthException;
import com.baozun.nebula.utilities.common.StringUtil;
import com.feilong.core.util.RandomUtil;


/**
 * 验证码的生成
 * @author shouqun.li
 * 2016年3月24日 上午9:54:17
 */
public class SecurityCodeUtil {
	/**
	 * 验证码的生成方法
	 * @param originalStr   验证码生成的原始字符串
	 * @param codeLength    验证码的长度
	 * @return
	 * @throws Exception 
	 */
	public static String createSecurityCode(String originalStr, int codeLength) throws Exception{
		if(StringUtil.isNull(originalStr)){
			throw new NullPointerException("the originalStr is null");
		}
		if(codeLength <= 0){
			throw new CodeLengthException("the codeLength must be positive integer");
		}
		return RandomUtil.createRandomFromString(originalStr, codeLength);
	}
}
