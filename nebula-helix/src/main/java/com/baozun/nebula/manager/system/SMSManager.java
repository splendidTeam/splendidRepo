/**
 * 
 */
package com.baozun.nebula.manager.system;

import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.manager.system.TokenManager.VerifyResult;

/**
 * 短信校验码‘发送’;
 * <p>
 * ‘验证’的过程请参考{@link SMSCaptchaValidate}
 * </p>
 * 
 * @author Viktor Huang
 * @date 2016年3月30日 上午11:29:51
 */
public interface SMSManager extends BaseManager{

	/**
	 * 发送验证码短信，captcha会根据validity保存在redis中
	 * 
	 * @param smsCommand
	 *            短信对象
	 * @param captcha
	 *            验证码
	 * @param validity
	 *            验证码的有效期（ 单位为秒）
	 * @return
	 */
	boolean send(SMSCommand smsCommand,String captcha,int validity);

	/**
	 * 发送验证码短信，captcha会根据validity保存在redis中
	 * 
	 * @param smsCommand
	 *            短信对象
	 * @param type
	 *            验证码类型（纯数字，纯字母，数字字母混合）
	 * @param length
	 *            验证码长度
	 * @param validity
	 *            验证码的有效期（ 单位为秒）
	 * @return
	 */
	boolean send(SMSCommand smsCommand,CaptchaType type,int length,int validity);

	/**
	 * 验证手机短信验证码是否真确
	 * 
	 * @param mobile
	 *            手机
	 * @param captcha
	 *            发给上面手机的，需要验证的验证码
	 * @return
	 */
	@Deprecated
	VerifyResult validate(String mobile,String captcha);

	enum CaptchaType{
		NUMBER, CHARACTER, MIXED
	}
}
