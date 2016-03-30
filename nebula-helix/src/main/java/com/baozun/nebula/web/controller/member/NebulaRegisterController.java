/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.member;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.system.TokenManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.SdkSMSManager;
import com.baozun.nebula.sdk.utils.RegulareExpUtils;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.event.RegisterSuccessEvent;
import com.baozun.nebula.web.controller.member.form.RegisterForm;
import com.baozun.nebula.web.controller.member.validator.RegisterFormMobileValidator;
import com.baozun.nebula.web.controller.member.validator.RegisterFormNormalValidator;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestUtil;

/**
 * 会员注册基类控制器
 * 
 * @author Viktor Huang
 * @author D.C
 * @time 2016年3月20日 下午6:25:09
 */
public class NebulaRegisterController extends NebulaLoginController{

	private static final Logger			LOGGER								= LoggerFactory.getLogger(NebulaRegisterController.class);

	/* Register Page 的默认定义 */
	public static final String			VIEW_MEMBER_REGISTER				= "member.register";

	public static final String			VIEW_MEMBER_REGISTER_ACTIVE_EMAIL	= "member.registerActiveEmail";

	/**
	 * PC || Tablet <br/>
	 * 会员注册Form的校验器
	 */
	@Autowired
	@Qualifier("registerFormNormalValidator")
	private RegisterFormNormalValidator	registerFormNormalValidator;

	/**
	 * Mobile <br/>
	 * 会员注册Form的校验器
	 */
	@Autowired
	@Qualifier("registerFormMobileValidator")
	private RegisterFormMobileValidator	registerFormMobileValidator;

	/**
	 * 会员业务管理类
	 */
	@Autowired
	private MemberManager				memberManager;

	@Autowired
	private SdkMemberManager			sdkMemberManager;

	@Autowired
	private SdkSMSManager					smsManager;

	@Autowired
	private TokenManager				tokenManager;

	/**
	 * 注册页面，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/register", method = RequestMethod.GET)
	 * @param memberDetails
	 * @param model
	 * @param request
	 * @return
	 */
	public String showRegister(@LoginMember MemberDetails memberDetails,Model model,HttpServletRequest request){
		// ① 判断用户是否登陆
		if (!Validator.isNullOrEmpty(memberDetails)){
			return super.getShowPage4LoginedUserViewLoginPage(memberDetails, request, model);
		}
		init4SensitiveDataEncryptedByJs(request, model);
		return VIEW_MEMBER_REGISTER;
	}

	/**
	 * 注册时验证邮箱是否可用
	 * 
	 * @ResponseBody
	 * @RequestMapping(value = "/member/checkLoginEmailAvailable.json",method = RequestMethod.GET)
	 * @param email
	 *            注册eamil
	 * @return
	 */
	public NebulaReturnResult checkLoginEmailAvailable(@RequestParam(value = "email",required = true) String email){

		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;
		MemberCommand findMemberByLoginEmail = sdkMemberManager.findMemberByLoginEmail(email);
		if (Validator.isNotNullOrEmpty(findMemberByLoginEmail)){
			// eamil不可用
			defaultReturnResult = new DefaultReturnResult();
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("register.loginemail.unavailable");
			return defaultReturnResult;
		}

		return defaultReturnResult;
	}

	/**
	 * 注册时验证mobile是否可用
	 * 
	 * @ResponseBody
	 * @RequestMapping(value = "/member/checkLoginmobileAvailable.json",method = RequestMethod.GET)
	 * @param mobile
	 *            注册mobile
	 * @return
	 */
	public NebulaReturnResult checkLoginMobileAvailable(@RequestParam(value = "mobile",required = true) String mobile){

		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;
		MemberCommand findMemberByLoginMobile = sdkMemberManager.findMemberByLoginMobile(mobile);
		if (Validator.isNotNullOrEmpty(findMemberByLoginMobile)){
			// mobile不可用
			defaultReturnResult = new DefaultReturnResult();
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("register.loginmobile.unavailable");
			return defaultReturnResult;
		}

		return defaultReturnResult;
	}

	/**
	 * 注册时发送短信验证码
	 * 
	 * @ResponseBody
	 * @RequestMapping(value = "/member/sendRegisterMobileMessage.json",method = RequestMethod.GET)
	 * @param request
	 * @param response
	 * @param model
	 * @param mobile
	 *            手机号码
	 * @return
	 */
	public NebulaReturnResult sendRegisterMobileMessage(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@RequestParam(value = "mobile",required = true) String mobile){

		DefaultReturnResult defaultReturnResult = new DefaultReturnResult();

		if (!RegulareExpUtils.isMobileNO(mobile)){
			// 手机号不合法
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("register.mobile.illegal");
			return defaultReturnResult;
		}
		// 判断是否可以给指定的mobile发生短信
		boolean ableSendMessageToMobile = isAbleSendMessageToMobile(request, mobile);
		if (!ableSendMessageToMobile){
			// 不能连续发送短信
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("register.sendMsg.over.counts");
			return defaultReturnResult;
		}

		boolean sendMsgResult = sendRegisterMessage(request, mobile);
		if (!sendMsgResult){
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("register.sendMsg.error");
			return defaultReturnResult;
		}

		defaultReturnResult.setResult(true);
		return defaultReturnResult;
	}

	/**
	 * 注册处理，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/register.json", method = RequestMethod.POST)
	 * @param registerForm
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public NebulaReturnResult register(
			@ModelAttribute RegisterForm registerForm,
			BindingResult bindingResult,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			Device device){

		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;
		/** 数据校验 */
		// Device device = getDevice(request);
		if (device.isMobile()){
			registerFormMobileValidator.validate(registerForm, bindingResult);
		}else{
			registerFormNormalValidator.validate(registerForm, bindingResult);
		}
		if (bindingResult.hasErrors()){
			DefaultResultMessage defaultResultMessage = new DefaultResultMessage();
			defaultResultMessage.setMessage(getMessage(bindingResult.getAllErrors().get(0).getDefaultMessage()));

			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("reigster.validator.errors");
			defaultReturnResult.setResultMessage(defaultResultMessage);

			return defaultReturnResult;
		}

		// 密码解密，密码传输通过RSA做了加密，此处需要解密
		registerForm.setPassword(decryptSensitiveDataEncryptedByJs(registerForm.getPassword(), request));

		try{
			MemberFrontendCommand memberFrontendCommand = registerForm.toMemberFrontendCommand();
			/** 检查验证码 */
			defaultReturnResult = (DefaultReturnResult) checkCaptcha(request, memberFrontendCommand.getRandomCode());
			if (!defaultReturnResult.isResult()){
				return defaultReturnResult;
			}

			/** 检查email，mobile等是否合法 */
			defaultReturnResult = (DefaultReturnResult) checkRegisterData(memberFrontendCommand, request, response);
			if (!defaultReturnResult.isResult()){
				return defaultReturnResult;
			}

			/** 设置注册会员附加信息 */
			setupMemberReference(memberFrontendCommand, request);

			/** 用户注册 */
			Member member = memberManager.rewriteRegister(memberFrontendCommand);

			// member convert to memberCommand
			MemberCommand memberCommand = (MemberCommand) ConvertUtils.convertTwoObject(new MemberCommand(), member);

			// 给发送激活邮件使用
			request.getSession().setAttribute(SessionKeyConstants.MEMBER_REG_MEMBID, member.getId());
			request.getSession().setAttribute(SessionKeyConstants.MEMBER_REG_EMAIL_URL, member.getLoginEmail());

			return onRegisterSuccess(constructMemberDetails(memberCommand), request, response);

		}catch (BusinessException e){
			LOGGER.error("", e);
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("register.failed");
			return defaultReturnResult;
		}
	}

	/**
	 * 检查验证码是否正确
	 * 
	 * @param request
	 * @param randomCode
	 *            验证码
	 */
	protected NebulaReturnResult checkCaptcha(HttpServletRequest request,String randomCode){
		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;
		// 验证码

		return defaultReturnResult;
	}

	/**
	 * 设置注册会员附加信息<br/>
	 * 首次生命状态,注册来源，会员类型,MemberConductCommand等
	 * 
	 * @param memberFrontendCommand
	 */
	protected void setupMemberReference(MemberFrontendCommand memberFrontendCommand,HttpServletRequest request){
		// 生命周期：未激活状态
		memberFrontendCommand.setLifecycle(Member.LIFECYCLE_UNACTIVE);
		// 来源：自注册
		memberFrontendCommand.setSource(Member.MEMBER_SOURCE_SINCE_REG_MEMBERS);
		// 类型：自注册会员
		memberFrontendCommand.setType(Member.MEMBER_TYPE_SINCE_REG_MEMBERS);

		int loginCount = 0;
		Date registerTime = new Date();
		String clientIp = RequestUtil.getClientIp(request);

		MemberConductCommand conductCommand = new MemberConductCommand(loginCount, registerTime, clientIp);

		memberFrontendCommand.setMemberConductCommand(conductCommand);
	}

	/**
	 * 检查email，mobile等是否合法,重复问题
	 * 
	 * @param mfc
	 *            MemberFrontendCommand
	 * @param request
	 * @param response
	 * @return
	 */
	protected NebulaReturnResult checkRegisterData(MemberFrontendCommand mfc,HttpServletRequest request,HttpServletResponse response){
		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;
		Map<String, String> returnObject = new HashMap<String, String>();

		// 验证email
		String loginEmail = mfc.getLoginEmail();
		if (Validator.isNotNullOrEmpty(loginEmail)){
			MemberCommand findMemberByLoginEmail = sdkMemberManager.findMemberByLoginEmail(loginEmail);
			if (Validator.isNotNullOrEmpty(findMemberByLoginEmail)){
				defaultReturnResult.setResult(false);
				returnObject.put("loginEmail", "register.loginemail.unavailable");
			}
		}
		// 验证mobile
		String loginMobile = mfc.getLoginMobile();
		if (Validator.isNotNullOrEmpty(loginMobile)){
			MemberCommand findMemberByLoginMobile = sdkMemberManager.findMemberByLoginMobile(loginMobile);
			if (Validator.isNotNullOrEmpty(findMemberByLoginMobile)){
				defaultReturnResult.setResult(false);
				returnObject.put("loginMobile", "register.loginmobile.unavailable");
			}
		}

		// 验证 LoginName
		String loginName = mfc.getLoginName();
		if (Validator.isNotNullOrEmpty(loginName)){
			MemberCommand findMemberByLoginName = sdkMemberManager.findMemberByLoginName(loginName);
			if (Validator.isNotNullOrEmpty(findMemberByLoginName)){
				defaultReturnResult.setResult(false);
				returnObject.put("loginName", "register.loginname.unavailable");
			}
		}
		defaultReturnResult.setReturnObject(returnObject);

		return defaultReturnResult;
	}

	/**
	 * 用户注册成功切入点
	 * 
	 * @param memberDetails
	 * @param request
	 * @param response
	 * @return
	 */
	protected NebulaReturnResult onRegisterSuccess(MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response){
		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;
		// 注册成功后是否需要自动登录
		if (isAutoLoginAfterRegister()){
			super.onAuthenticationSuccess(memberDetails, request, response);
		}

		eventPublisher.publish(new RegisterSuccessEvent(memberDetails, getClientContext(request, response)));

		defaultReturnResult.setReturnObject(VIEW_MEMBER_REGISTER_ACTIVE_EMAIL);

		return defaultReturnResult;
	}

	/**
	 * 注册成功后是否需要自动登录
	 * 
	 * @return
	 */
	protected boolean isAutoLoginAfterRegister(){
		return false;
	}

	/**
	 * 判断是否可以给指定的mobile发生短信<br/>
	 * 间隔时间、次数等限制判断
	 * 
	 * @param request
	 * @param mobile
	 *            要发送短信的手机
	 * @return
	 */
	protected boolean isAbleSendMessageToMobile(HttpServletRequest request,String mobile){
		return true;
	}

	/**
	 * 发送短信
	 * 
	 * @param request
	 * @param mobile
	 *            要发送短信的手机
	 * @return
	 */
	protected boolean sendRegisterMessage(HttpServletRequest request,String mobile){

		SMSCommand messageCommand = new SMSCommand();
		messageCommand.setMobile(mobile);
		//messageCommand.setContent("11111111111111111111111");
		// 发送短信
		try{
		//	boolean sendMessage = smsManager.sendMessage(messageCommand);
			// tokenManager.saveToken(businessCode, human, liveTime, token);
			return false;
		}catch (Exception e){
			LOGGER.error("{}", e);
			return false;
		}

	}

}
