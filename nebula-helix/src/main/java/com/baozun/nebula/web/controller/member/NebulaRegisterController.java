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
import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.constant.SMSTemplateConstants;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.captcha.CaptchaUtil;
import com.baozun.nebula.manager.captcha.CaptchaValidate;
import com.baozun.nebula.manager.captcha.entity.CaptchaContainerAndValidateConfig;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.system.SMSCaptchaValidate;
import com.baozun.nebula.manager.system.SMSManager;
import com.baozun.nebula.manager.system.SMSManager.CaptchaType;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.RegisterForm;
import com.baozun.nebula.web.controller.member.validator.RegisterFormMobileValidator;
import com.baozun.nebula.web.controller.member.validator.RegisterFormNormalValidator;
import com.feilong.core.RegexPattern;
import com.feilong.core.Validator;
import com.feilong.core.util.RegexUtil;
import com.feilong.servlet.http.RequestUtil;

/**
 * 会员注册基类控制器
 * <ol>
 * <li>{@link #showRegister(MemberDetails, Model, HttpServletRequest)} 进入注册页面</li>
 * <li>{@link #checkLoginEmailAvailable(String)} 注册时验证邮箱是否可用</li>
 * <li>{@link #checkLoginMobileAvailable(String)} 注册时验证mobile是否可用</li>
 * <li>{@link #sendRegisterMobileMessage(HttpServletRequest, HttpServletResponse, Model, String)} 注册时发送短信验证码</li>
 * <li>{@link #register(RegisterForm, BindingResult, HttpServletRequest, HttpServletResponse, Model) } 注册</li>
 * </ol>
 * <h3>showRegister方法 Tips:</h3> <blockquote>
 * <ol>
 * <li>判断用户如果已经登录了，跳转到【membercenter】</li>
 * <li>初始化 RSA非对称加密的key，商城端也可以实现{@link #init4SensitiveDataEncryptedByJs(HttpServletRequest, Model)} 完成自己的逻辑</li>
 * </ol>
 * </blockquote> <h3>register方法 Tips:</h3> <blockquote>
 * <ol>
 * <li>根据Device分别验证登录提交的表单，商城端可以自己实现 {@link #registerFormValidate(Device, RegisterForm, BindingResult) }来实现自定义</li>
 * <li>注册，持久化数据</li>
 * <li>注册数据操作成功之后【注意此时注册流程不一样已经完成，还有可能需要激活邮件，完善profile等其他异步动作】；<br/>
 * 商城端也可以自己实现{@link #onRegisterSuccess(MemberDetails, HttpServletRequest, HttpServletResponse)}</li>
 * </ol>
 * </blockquote>
 * 
 * @author Viktor Huang
 * @author D.C
 * @time 2016年3月20日 下午6:25:09
 */
public class NebulaRegisterController extends NebulaLoginController{

	private static final Logger					LOGGER						= LoggerFactory.getLogger(NebulaRegisterController.class);

	/* Register Page 的默认定义 */
	public static final String					VIEW_MEMBER_REGISTER		= "member.register";

	public static final String					VIEW_MEMBER_CENTER			= "/member/center.htm";

	/** 发送手机验证码短信长度 */
	public static Integer						SEND_MOBILE_MSG_LENGTH		= 5;

	/** 发送手机验证码有效期 */
	public static Integer						SEND_MOBILE_MSG_LIVETIME	= 2 * 60;

	/**
	 * PC || Tablet <br/>
	 * 会员注册Form的校验器
	 */
	@Autowired
	@Qualifier("registerFormNormalValidator")
	private RegisterFormNormalValidator			registerFormNormalValidator;

	/**
	 * Mobile <br/>
	 * 会员注册Form的校验器
	 */
	@Autowired
	@Qualifier("registerFormMobileValidator")
	private RegisterFormMobileValidator			registerFormMobileValidator;

	/**
	 * botdetect captcha validate
	 */
	@Autowired(required = false)
	@Qualifier("registerCaptchaContainerAndValidateConfig")
	private CaptchaContainerAndValidateConfig	registerCaptchaContainerAndValidateConfig;

	/**
	 * botdetect captcha validate
	 * <p>
	 * 此处配置的{@link CaptchaValidate} 请参考 {@link SMSCaptchaValidate}
	 * </p>
	 */
	@Autowired(required = false)
	@Qualifier("registerSMSCaptchaContainerAndValidateConfig")
	private CaptchaContainerAndValidateConfig	registerSMSCaptchaContainerAndValidateConfig;

	/**
	 * 会员业务管理类
	 */
	@Autowired
	private MemberManager						memberManager;

	@Autowired
	private SdkMemberManager					sdkMemberManager;

	@Autowired
	private SMSManager							smsManager;

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
		// 判断用户是否登陆
		if (!Validator.isNullOrEmpty(memberDetails)){
			return "redirect:" + VIEW_MEMBER_CENTER;
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

		DefaultReturnResult defaultReturnResult = new DefaultReturnResult();

		if (!RegexUtil.matches(RegexPattern.EMAIL, email)){
			// 电子邮箱 格式不正确
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("member.email.error");
			return defaultReturnResult;
		}

		MemberCommand findMemberByLoginEmail = sdkMemberManager.findMemberByLoginEmail(email);
		if (Validator.isNotNullOrEmpty(findMemberByLoginEmail)){
			// eamil不可用
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("register.loginemail.unavailable");
			return defaultReturnResult;
		}

		defaultReturnResult.setResult(true);

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

		DefaultReturnResult defaultReturnResult = new DefaultReturnResult();

		if (!RegexUtil.matches(RegexPattern.MOBILEPHONE, mobile)){
			// 手机号码 格式不正确
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("member.mobile.error");
			return defaultReturnResult;
		}

		MemberCommand findMemberByLoginMobile = sdkMemberManager.findMemberByLoginMobile(mobile);
		if (Validator.isNotNullOrEmpty(findMemberByLoginMobile)){
			// mobile不可用
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode("register.loginmobile.unavailable");
			return defaultReturnResult;
		}

		defaultReturnResult.setResult(true);

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

		if (!RegexUtil.matches(RegexPattern.MOBILEPHONE, mobile)){
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
			Model model){

		DefaultReturnResult defaultReturnResult = new DefaultReturnResult();
		/** 数据校验 */
		Device device = getDevice(request);
		defaultReturnResult = (DefaultReturnResult) registerFormValidate(device, registerForm, bindingResult);

		if (!defaultReturnResult.isResult()){
			defaultReturnResult.setStatusCode("reigster.validator.errors");
			return defaultReturnResult;
		}

		// 密码解密，密码传输通过RSA做了加密，此处需要解密
		registerForm.setPassword(decryptSensitiveDataEncryptedByJs(registerForm.getPassword(), request));

		try{

			MemberFrontendCommand memberFrontendCommand = registerForm.toMemberFrontendCommand();
			/**
			 * 检查验证码
			 */
			boolean result = registerCaptchaValidate(request);

			if (!result){
				defaultReturnResult.setResult(false);
				defaultReturnResult.setStatusCode("reigster.captcha.validate.errors");
				return defaultReturnResult;
			}

			/** 检查email，mobile等是否合法 */
			defaultReturnResult = (DefaultReturnResult) memberManager.checkRegisterData(memberFrontendCommand);
			if (!defaultReturnResult.isResult()){
				return defaultReturnResult;
			}

			/** 设置注册会员附加信息 */
			String clientIp = RequestUtil.getClientIp(request);
			memberManager.setupMemberReference(memberFrontendCommand, clientIp);

			/** 用户注册 */
			Member member = memberManager.rewriteRegister(memberFrontendCommand);

			// member convert to memberCommand
			MemberCommand memberCommand = (MemberCommand) ConvertUtils.convertTwoObject(new MemberCommand(), member);

			/**
			 * 构造MemberDetails<br/>
			 * 此时如果注册需要‘邮件激活’等功能，需要商城端设置 MemberDetails.status
			 */
			MemberDetails memberDetails = constructMemberDetails(memberCommand, request);

			// 返回NebulaReturnResult中包含下一步动作的url
			return onRegisterSuccess(memberDetails, request, response);

		}catch (BusinessException e){
			LOGGER.error("", e);
			defaultReturnResult.setResult(false);
			defaultReturnResult.setStatusCode(getMessage("register.failed"));
			return defaultReturnResult;
		}
	}

	/**
	 * 注册时候验证‘图形验证码’or‘手机短信’验证码是否合法
	 * 
	 * @param request
	 * @return
	 */
	protected boolean registerCaptchaValidate(HttpServletRequest request){
		Device device = getDevice(request);
		boolean result = false;
		if (device.isMobile()){
			result = CaptchaUtil.validate(registerCaptchaContainerAndValidateConfig, request);
		}else{
			result = CaptchaUtil.validate(registerSMSCaptchaContainerAndValidateConfig, request);
		}

		return result;
	}

	/**
	 * 数据注册表单校验
	 * 
	 * @param device
	 *            终端
	 * @param registerForm
	 *            注册表单
	 * @param bindingResult
	 *            验证结果
	 * @return
	 */
	protected NebulaReturnResult registerFormValidate(Device device,RegisterForm registerForm,BindingResult bindingResult){
		if (device.isMobile()){
			registerFormMobileValidator.validate(registerForm, bindingResult);
		}else{
			registerFormNormalValidator.validate(registerForm, bindingResult);
		}

		NebulaReturnResult resultFromBindingResult = getResultFromBindingResult(bindingResult);

		return resultFromBindingResult;
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

		// 给发送激活邮件使用
		request.getSession().setAttribute(SessionKeyConstants.MEMBER_REG_MEMBID, memberDetails.getMemberId());
		request.getSession().setAttribute(SessionKeyConstants.MEMBER_REG_EMAIL_URL, memberDetails.getLoginEmail());

		// eventPublisher.publish(new RegisterSuccessEvent(memberDetails, getClientContext(request, response)));

		// 注册成功后是否需要自动登录
		// if (isAutoLoginAfterRegister()){
		// defaultReturnResult = ;
		// }
		/***
		 * 不管是否注册成功之后自动登录，都跑此方法<br/>
		 * 方法中的【Processor】会过滤出注册需要完善的下一步动作，返回NebulaReturnResult中包含下一步动作的url
		 */
		return onAuthenticationSuccess(memberDetails, request, response);
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

		SMSCommand smsCommand = new SMSCommand();
		smsCommand.setMobile(mobile);
		smsCommand.setTemplateCode(SMSTemplateConstants.SMS_REGISTER_CAPTCHA);

		// 发送验证码短信，captcha会根据validity保存在redis中
		boolean sendResult = smsManager.send(smsCommand, CaptchaType.MIXED, SEND_MOBILE_MSG_LENGTH, SEND_MOBILE_MSG_LIVETIME);

		return sendResult;
	}
}
