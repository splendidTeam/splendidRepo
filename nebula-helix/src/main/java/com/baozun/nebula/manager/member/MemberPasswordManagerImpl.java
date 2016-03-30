package com.baozun.nebula.manager.member;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.manager.system.TokenManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.SdkSMSManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;
import com.baozun.nebula.web.controller.member.validator.ForgetPasswordFormValidator;
import com.feilong.core.util.RandomUtil;

public class MemberPasswordManagerImpl implements MemberPasswordManager{

	/* 验证码的生存时间 */
	private static final int	MAX_EXIST_TIME	= 60;

	@Autowired
	private SdkMemberManager	sdkMemberManager;

	@Autowired
	private MemberEmailManager	memberEmailManager;

	@Autowired
	private SdkSMSManager		smsManager;

	@Autowired
	private TokenManager		tokenManager;

	@Autowired
	private MemberExtraManager	memberExtraManager;

	@Override
	public boolean sendValidateCode(ForgetPasswordForm forgetPasswordForm) throws Exception{
		boolean flag = false;
		if (forgetPasswordForm.getType() == ForgetPasswordFormValidator.MOBILE){
			flag = this.mobileSendValidateCode(forgetPasswordForm.getMobile());
		}else if (forgetPasswordForm.getType() == ForgetPasswordFormValidator.EMAIL){
			flag = this.emailSendValidateCode(forgetPasswordForm.getEmail());
		}
		return flag;
	}

	private boolean emailSendValidateCode(String email) throws Exception{
		boolean flag = false;
		// 邮箱规则正确，通过邮箱查询用户是否存在
		MemberCommand memberCommand = sdkMemberManager.findMemberByLoginEmail(email);
		// 对应邮箱的用户不存在
		if (memberCommand != null){
			// 邮箱用户存在：则调用发送邮件的方法，发送相应的修改密码的链接和验证码到邮箱中

			String code = RandomUtil.createRandomFromString("待定", 4);
			// String code = SecurityCodeUtil.createSecurityCode(
			// MessageConstants.SECURITY_CODE_ORIGINAL_STRING,
			// MessageConstants.SECURITY_CODE_LENGTH);
			tokenManager.saveToken(null, email, MAX_EXIST_TIME, code);

			// 发送验证码
			memberEmailManager.sendEmailValidateCode(code, email);
			flag = true;
		}
		return flag;
	}

	private boolean mobileSendValidateCode(String mobile) throws Exception{
		boolean flag = false;
		// 手机规则正确，通过手机号码查询用户是否存在
		MemberCommand memberCommand = sdkMemberManager.findMemberByLoginMobile(mobile);
		// 对应手机号的用户不存在
		if (memberCommand != null){
			// 手机用户存在：则调用发送手机验证码的方法，发送相应的验证码到邮箱中
			String code = RandomUtil.createRandomFromString("待定", 4);
			// String code = SecurityCodeUtil.createSecurityCode(
			// MessageConstants.SECURITY_CODE_ORIGINAL_STRING,
			// MessageConstants.SECURITY_CODE_LENGTH);

			// 保存验证码到redis中，并且设置验证码的生存时间
			tokenManager.saveToken(null, mobile, MAX_EXIST_TIME, code);

			// 调用发送短信验证码的方式发送短信验证码
			SMSCommand smsCommand = new SMSCommand();
			smsCommand.addVar("securityCode", code);
			smsCommand.setMobile(mobile);

			// 发送验证码结束
			smsManager.send(smsCommand);

			flag = true;
		}
		return flag;
	}

	@Override
	public boolean resetPassword(ForgetPasswordForm forgetPasswordForm,String password){
		// 初始化id
		MemberCommand memberCommand = null;
		boolean resetPassword = false;
		/*
		 * 两次输入的密码相等，则判断是邮箱验证还是手机验证， 若是手机验证方式，则通过登录手机号查询登录的用户信息（获取到用户id）； 若是邮箱验证方式，则通过登录邮箱获取用户信息（获取到用户id）
		 */
		if (forgetPasswordForm.getType() == ForgetPasswordFormValidator.EMAIL){
			memberCommand = sdkMemberManager.findMemberByLoginEmail(forgetPasswordForm.getEmail());

		}else if (forgetPasswordForm.getType() == ForgetPasswordFormValidator.MOBILE){
			memberCommand = sdkMemberManager.findMemberByLoginMobile(forgetPasswordForm.getMobile());
		}
		if (memberCommand != null){
			// 加密新密码
			String codePassword = EncryptUtil.getInstance().hash(password, memberCommand.getLoginName());
			// 更新密码
			resetPassword = sdkMemberManager.resetPasswd(memberCommand.getId(), codePassword);
			memberExtraManager.rememberPwd(memberCommand.getId(), new Date().getTime() + "");
		}
		return resetPassword;
	}

}
