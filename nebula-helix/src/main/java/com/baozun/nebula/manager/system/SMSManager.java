/**
 * 
 */
package com.baozun.nebula.manager.system;

import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.manager.BaseManager;

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
	boolean send(SMSCommand smsCommand,CaptchaType type,int length,int validity,String businesscode);


	enum CaptchaType{
		NUMBER, CHARACTER, MIXED
	}
}
