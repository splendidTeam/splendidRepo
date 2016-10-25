package com.baozun.nebula.manager.member;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.constant.EmailConstants;
import com.baozun.nebula.manager.member.CommonEmailManager.SendEmailResultCode;
import com.baozun.nebula.manager.system.SMSManager;
import com.baozun.nebula.manager.system.SMSManager.CaptchaType;
import com.baozun.nebula.manager.system.TokenManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.web.constants.ForgetPasswordSendVCodeConstant;
import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;
import com.feilong.core.Alphabet;
import com.feilong.core.Validator;
import com.feilong.core.util.RandomUtil;

/**
 * <pre>
 * 类名：MemberPasswordManagerImpl
 * 功能：用户密码相关操作的manager实现 
 * 相关方法：  
 *  发送验证码的方法
 *  重置密码的方法
 *  查询旧密码是否正确的方法
 *  修改密码的方法
 * </pre>
 * 
 * @author Wanrong.Wang
 * @Date 2016/04/01
 */

@Transactional
@Service("memberPasswordManager")
public class MemberPasswordManagerImpl implements MemberPasswordManager{

	private static final Logger	LOG				= LoggerFactory.getLogger(MemberPasswordManagerImpl.class);

	// /* 验证码的生存时间 */
	// protected Integer MAX_EXIST_TIME;
	//
	// /* 验证码的位数 */
	// protected Integer VALIDATE_CODE_LENGTH;

	@Autowired
	private SdkMemberManager	sdkMemberManager;

	@Autowired
	private CommonEmailManager	commonEmailManager;

	@Autowired
	private SMSManager			smsManager;

	@Autowired
	private TokenManager		tokenManager;

	public static final String	BUSINESS_CODE	= "FORGET_PASSWORD_BUSINESS";

	/**
	 * 发送验证码的方法
	 */
	@Override
	public boolean sendValidateCode(ForgetPasswordForm forgetPasswordForm){
		boolean flag = false;
		if (forgetPasswordForm.getType() == ForgetPasswordForm.MOBILE){
			// 是手机验证方式，则调用手机发送验证码的方法
			flag = this.mobileSendValidateCode(forgetPasswordForm.getMobile());
		}
		if (forgetPasswordForm.getType() == ForgetPasswordForm.EMAIL){
			// 是邮箱验证方式，则调用邮箱发送验证码的方法
			flag = this.emailSendValidateCode(forgetPasswordForm.getEmail());
		}
		return flag;
	}

	/**
	 * 发送邮箱链接的方式找回密码的方法
	 * 
	 * @param email
	 * @return
	 */
	public boolean sendForgetPasswordValidateEmailURL(String url,ForgetPasswordForm forgetPasswordForm){

		boolean flag = false;
		// 通过邮箱查询用户是否存在
		MemberCommand memberCommand = sdkMemberManager.findMemberByLoginEmail(forgetPasswordForm.getEmail());
		// 对应邮箱的用户存在
		if (memberCommand != null){
			// 则调用发送邮件的方法，发送相应的链接到对应的邮箱中
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("url", url);
			SendEmailResultCode sendEmail = commonEmailManager.sendEmail(
					forgetPasswordForm.getEmail(),
					EmailConstants.FORGET_PASSWORD,
					dataMap);
			if (sendEmail == SendEmailResultCode.SUCESS){
				// 发送成功，flag设置为true
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 邮箱方式发送验证码
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	private boolean emailSendValidateCode(String email){

		boolean flag = false;

		// 通过邮箱查询用户是否存在
		MemberCommand memberCommand = sdkMemberManager.findMemberByLoginEmail(email);
		// 对应邮箱的用户存在
		if (memberCommand != null){

			// 所有数字和大小写字母的组合
			String str = Alphabet.DECIMAL_AND_LETTERS;
			// 生成验证码
			String code = RandomUtil.createRandomFromString(str, ForgetPasswordSendVCodeConstant.VALIDATE_CODE_LENGTH);

			// 保存发送的验证码到redis中
			tokenManager.saveToken(BUSINESS_CODE, email, ForgetPasswordSendVCodeConstant.MAX_EXIST_TIME, code);

			// 则调用发送邮件的方法，发送相应的修改密码的链接和验证码到邮箱中
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("code", code);
			dataMap.put("email", email);
			SendEmailResultCode sendEmail = commonEmailManager.sendEmail(email, EmailConstants.FORGET_PASSWORD, dataMap);
			if (sendEmail == SendEmailResultCode.SUCESS){
				// 发送成功，flag设置为true
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 手机方式发送验证码
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	private boolean mobileSendValidateCode(String mobile){

		boolean flag = false;

		// 通过手机号码查询用户是否存在
		MemberCommand memberCommand = sdkMemberManager.findMemberByLoginMobile(mobile);

		// 对应手机号的用户存在
		if (memberCommand != null){

			// 调用发送手机验证码的方法，发送相应的验证码到邮箱中
			SMSCommand smsCommand = new SMSCommand();
			smsCommand.setTemplateCode(ForgetPasswordSendVCodeConstant.SMS_FORGET_PASSWORD_CAPTCHA);
			smsCommand.setMobile(mobile);
			smsCommand.addVar("maxsurvivaltime", ForgetPasswordSendVCodeConstant.MAX_EXIST_TIME/60);
			// 发送验证码结束
			boolean sendFailOrSuccess = smsManager.send(
					smsCommand,
					ForgetPasswordSendVCodeConstant.getForgetPasswordCaptchaType(),
					ForgetPasswordSendVCodeConstant.VALIDATE_CODE_LENGTH,
					ForgetPasswordSendVCodeConstant.MAX_EXIST_TIME,BUSINESS_CODE);
			flag = sendFailOrSuccess;
		}
		return flag;
	}
	
	/**
	 * 重置密码的方法
	 */
	@Override
	public boolean resetPassword(ForgetPasswordForm forgetPasswordForm,String password){

		MemberCommand memberCommand = null;
		boolean resetPassword = false;
		/*
		 * 两次输入的密码相等，则判断是邮箱验证还是手机验证， 若是手机验证方式，则通过登录手机号查询登录的用户信息（获取到用户id）； 若是邮箱验证方式，则通过登录邮箱获取用户信息（获取到用户id）
		 */
		if (forgetPasswordForm.getType() == ForgetPasswordForm.EMAIL){
			// 邮箱验证方式，则通过邮箱查找对应用户是否存在
			memberCommand = sdkMemberManager.findMemberByLoginEmail(forgetPasswordForm.getEmail());

		}else if (forgetPasswordForm.getType() == ForgetPasswordForm.MOBILE){
			// 手机验证方式，则通过手机号码查找对应的用户是否存在
			memberCommand = sdkMemberManager.findMemberByLoginMobile(forgetPasswordForm.getMobile());
		}
		// 如果对应的用户都是存在的，则执行如下的方法
		if (memberCommand != null){
			LOG.info("[The member try to reset password,memberId: ] {} [{}]", memberCommand.getId(), new Date());
				//生成新的盐值，用新的加密算法进行加密，(仅适用于更新加密算法之后，第一次重置密码) add by ruichao.gao
			if(Validator.isNullOrEmpty(memberCommand.getSalt())){
				String salt = RandomUtil.createRandomFromString(Alphabet.DECIMAL_AND_LETTERS, 88);
				String encodePassword = EncryptUtil.getInstance().hashSalt(password, salt);
				resetPassword = sdkMemberManager.resetPasswdWithSalt(memberCommand.getId(), encodePassword, salt);
			}else{
				// 重置密码
				String encodePassword = EncryptUtil.getInstance().hashSalt(password, memberCommand.getSalt());
				resetPassword = sdkMemberManager.resetPasswd(memberCommand.getId(), encodePassword);
			}
			
			
		}
		return resetPassword;
	}

	/**
	 * 查询旧密码是否正确
	 */
	@Override
	public boolean checkOldPassword(Long memberId,String oldPassword){
		// 根据用户id查询出数据库里的密码信息
		MemberCommand memberCommand = sdkMemberManager.findMemberById(memberId);
		String dbPassword = memberCommand.getPassword();
		String codePassword = null;
		//如果盐值为空，对密码验证走老的加密验证逻辑 add by ruichao.gao
		if(Validator.isNullOrEmpty(memberCommand.getSalt())){
			// 对页面传过来的密码进行解密加密，再两者进行比较
			 codePassword = EncryptUtil.getInstance().hash(oldPassword, memberCommand.getLoginName());
		}else{
			//新的加密算法加密
			codePassword = EncryptUtil.getInstance().hashSalt(oldPassword, memberCommand.getSalt());
		}
		

		// 确认数据库里查询到的密码的存在性
		if (dbPassword != null && StringUtils.isNotEmpty(dbPassword)){
			if (dbPassword.equals(codePassword)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 修改密码
	 */
	@Override
	public boolean modifyPassword(String oldPassword,String newPassword,String confirmPassword,Long memberId){

		boolean flag = false;

		boolean checkOldPasswordflag = this.checkOldPassword(memberId, oldPassword);
		if (checkOldPasswordflag){
			// 旧密码验证成功，调用service层的方法修改密码
			boolean updatePasswd = sdkMemberManager.updatePasswd(memberId, oldPassword, newPassword, confirmPassword);

			// 如果修改成功
			if (updatePasswd){
				LOG.info("[The member have modified password success,memberId: ] {} [{}]", memberId, new Date());
				flag = true;
			}
		}else{
			// 旧密码错误
			LOG.info("[The member try to modify password but failed ,memberId: ] {} [{}]", memberId, new Date());
		}
		return flag;
	}

}
