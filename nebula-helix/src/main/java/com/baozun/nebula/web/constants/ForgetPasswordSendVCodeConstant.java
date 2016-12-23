package com.baozun.nebula.web.constants;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.manager.system.SMSManager.CaptchaType;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

public class ForgetPasswordSendVCodeConstant{

	/* 验证码的生存时间 */
	public static final Integer	MAX_EXIST_TIME;

	/* 验证码的位数 */
	public static final Integer	VALIDATE_CODE_LENGTH;

	/* 忘记密码发送验证码短信模板 */
	public static final String	SMS_FORGET_PASSWORD_CAPTCHA;
	
	/* 忘记密码短信累心  */
	private static CaptchaType captchaType;

	static{
		
		Properties pro = ProfileConfigUtil.findPro("config/metainfo.properties");

		MAX_EXIST_TIME = Integer.valueOf(StringUtils.trim(pro.getProperty("sms.vcode.max.exist.time")));

		VALIDATE_CODE_LENGTH = Integer.valueOf(StringUtils.trim(pro.getProperty("sms.vcode.length")));

		SMS_FORGET_PASSWORD_CAPTCHA = StringUtils.trim(pro.getProperty("sms.forget.password.captcha"));

	}
	
	/**
	 * @return the captchaType
	 */
	public static CaptchaType getForgetPasswordCaptchaType() {
		if(null==captchaType){
			return CaptchaType.MIXED;
		}
		return captchaType;
	}

	/**
	 * @param captchaType the captchaType to set
	 */
	public static void setForgetPasswordCaptchaType(CaptchaType captchatype) {
		captchaType = captchatype;
	}

}
