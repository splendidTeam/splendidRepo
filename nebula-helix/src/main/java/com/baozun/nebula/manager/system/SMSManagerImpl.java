/**
 * 
 */
package com.baozun.nebula.manager.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.sdk.manager.SdkSMSManager;
import com.feilong.core.util.RandomUtil;

/**
 * 短信校验码‘发送’;
 * <p>
 * ‘验证’的过程请参考{@link SMSCaptchaValidate}
 * </p>
 * 
 * @author Viktor Huang
 * @date 2016年3月30日 下午6:28:04
 */
@Service("smsManager")
@Transactional
public class SMSManagerImpl implements SMSManager{

	@SuppressWarnings("unused")
	private static final Logger	LOGGER						= LoggerFactory.getLogger(SMSManagerImpl.class);

	public static final String	SMS_REGISTER_CAPTCHA_CODE	= "SMS_REGISTER_CAPTCHA_CODE";

	@Autowired
	private SdkSMSManager		sdkSMSManager;

	@Autowired
	private TokenManager		tokenManager;

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.system.SMSManager#send(com.baozun.nebula.command.SMSCommand, java.lang.String, int)
	 */
	@Override
	public boolean send(SMSCommand smsCommand,String captcha,int validity){

        boolean result = sdkSMSManager.send(smsCommand, captcha);
		// 发送短信成功，保存captcha到redies
       // if (SendResult.SUCESS.equals(result)){
       if (result){
			String businessCode = SMS_REGISTER_CAPTCHA_CODE + smsCommand.getMobile();
			tokenManager.saveToken(businessCode, smsCommand.getMobile(), validity, captcha);
			return true;
		} 
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.system.SMSManager#send(com.baozun.nebula.command.SMSCommand,
	 * com.baozun.nebula.manager.system.SMSManager.RandomType, int, int)
	 */
	@Override
	public boolean send(SMSCommand smsCommand,CaptchaType type,int length,int validity,String businessCode){
	    
	    String captcha = createSMSCaptcha(type, length);
	    
	    boolean result = sdkSMSManager.send(smsCommand, captcha);
		// 发送短信成功，保存captcha到redies
		//if (SendResult.SUCESS.equals(result)){
		if (result){    
			tokenManager.saveToken(businessCode, smsCommand.getMobile(), validity, captcha);
			return true;
		}
		return false;
	}

	/**
	 * 根据要生成的验证码类型和长度生成captcha
	 * 
	 * @param type
	 *            验证码类型
	 * @param length
	 *            长度
	 * @return
	 */
	private String createSMSCaptcha(CaptchaType type,int length){
		String chars = "abcdefghijklmnopqrstuvwxyz";
		String mixedChar = "abcdefghijklmnopqrstuvwxyz0123456789";

		String captcha;
		if (type.equals(CaptchaType.NUMBER)){
			long randomLong = RandomUtil.createRandomWithLength(length);
			captcha = String.valueOf(randomLong);
		}else if (type.equals(CaptchaType.CHARACTER)){
			captcha = RandomUtil.createRandomFromString(chars, length);
		}else{
			captcha = RandomUtil.createRandomFromString(mixedChar, length);
		}

		return captcha;
	}
}
