/**
 * 
 */
package com.baozun.nebula.manager.system;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.manager.captcha.CaptchaValidate;
import com.baozun.nebula.manager.system.TokenManager.VerifyResult;

/**
 * 手机短信验证码验证类。此类继承自{@link CaptchaValidate},所以需要配置在xml中，参考Botdetect captcha 配置<br/>
 * WIKI : http://git.baozun.cn/nebula/doc/wikis/common-captcha-validate
 * 
 * @author Viktor Huang
 * @date 2016年4月19日 上午11:19:57
 */
public class SMSCaptchaValidate implements CaptchaValidate{

	@SuppressWarnings("unused")
	private static final Logger	LOGGER						= LoggerFactory.getLogger(SMSCaptchaValidate.class);

	private static final String	SMS_REGISTER_CAPTCHA_CODE	= "SMS_REGISTER_CAPTCHA_CODE";

	/**
	 * 手机号码在页面input元素的name，需要配置在xml中
	 */
	private String				mobileParamName;

	@Autowired
	private TokenManager		tokenManager;

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.captcha.CaptchaValidate#validate(java.lang.String, java.lang.String,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean validate(String captchaId,String userInput,HttpServletRequest request){
		return validate(userInput, request);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.captcha.CaptchaValidate#validateAjax(java.lang.String, java.lang.String, java.lang.String,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean validateAjax(String captchaId,String userInput,String instanceId,HttpServletRequest request){
		return validate(userInput, request);
	}

	private boolean validate(String captcha,HttpServletRequest request){
		String mobile = request.getParameter(mobileParamName);
		
		String businessCode = SMS_REGISTER_CAPTCHA_CODE + mobile;
		VerifyResult verifyToken = tokenManager.verifyToken(businessCode, mobile, captcha);
		
		return verifyToken == VerifyResult.SUCESS;
	}

	/**
	 * @param mobileParamName
	 *            the mobileParamName to set
	 */
	public void setMobileParamName(String mobileParamName){
		this.mobileParamName = mobileParamName;
	}
}
