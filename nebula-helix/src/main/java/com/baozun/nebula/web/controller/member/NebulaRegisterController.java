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
import java.util.List;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.controller.AbstractReturnResult;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.event.RegisterSuccessEvent;
import com.baozun.nebula.web.controller.member.form.RegisterForm;
import com.baozun.nebula.web.controller.member.validator.RegisterFormMobileValidator;
import com.baozun.nebula.web.controller.member.validator.RegisterFormNormalValidator;
import com.feilong.core.util.Validator;
import com.feilong.servlet.http.RequestUtil;

/**
 * 会员注册基类控制器
 * 
 * @author Viktor Huang
 * @author D.C
 * @time 2016年3月20日 下午6:25:09
 */
public class NebulaRegisterController extends NebulaLoginController{

	private static final Logger			LOGGER					= LoggerFactory.getLogger(NebulaRegisterController.class);

	/* Register Page 的默认定义 */
	public static final String			VIEW_MEMBER_REGISTER	= "member.register";

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
		// TODO 此处加入这个是否有必要？
		init4SensitiveDataEncryptedByJs(request, model);
		return VIEW_MEMBER_REGISTER;
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
	// TODO 验证码
	public NebulaReturnResult register(
			@ModelAttribute RegisterForm registerForm,
			BindingResult bindingResult,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model){

		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;
		/** 数据校验 */
		Device device = getDevice(request);
		if (device.isMobile()){
			registerFormMobileValidator.validate(registerForm, bindingResult);
		}else{
			registerFormNormalValidator.validate(registerForm, bindingResult);
		}
		if (bindingResult.hasErrors()){
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			for (ObjectError objectError : allErrors){
				LOGGER.info("{}", objectError);
			}
			return null;
		}

		// 密码解密，密码传输通过RSA做了加密，此处需要解密
		registerForm.setPassword(decryptSensitiveDataEncryptedByJs(registerForm.getPassword(), request));

		try{
			MemberFrontendCommand memberFrontendCommand = registerForm.toMemberFrontendCommand(registerForm);
			/** 检查验证码 ，email，mobile等是否合法 */
			defaultReturnResult = (DefaultReturnResult) checkCoreData(memberFrontendCommand, request, response);
			if (!defaultReturnResult.isResult()){
				return defaultReturnResult;
			}
			// 设置注册会员附加信息
			setupMemberReference(memberFrontendCommand, request);

			/** 用户注册 */
			Member member = memberManager.rewriteRegister(memberFrontendCommand);

			// member convert to memberCommand
			MemberCommand memberCommand = (MemberCommand) ConvertUtils.convertTwoObject(new MemberCommand(), member);

			return onRegisterSuccess(constructMemberDetails(memberCommand), request, response);
		}catch (BusinessException e){
			// TODO 异常处理
			return null;
		}
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
	 * 检查验证码 ，email，mobile等是否合法,重复问题
	 * 
	 * @param mfc
	 *            MemberFrontendCommand
	 * @param request
	 * @param response
	 * @return
	 */
	protected NebulaReturnResult checkCoreData(MemberFrontendCommand mfc,HttpServletRequest request,HttpServletResponse response){
		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;
		Map<String, String> returnObject = new HashMap<String, String>();
		// TODO 验证码
		String randomCode = mfc.getRandomCode();

		// 验证email
		String loginEmail = mfc.getLoginEmail();
		if (Validator.isNotNullOrEmpty(loginEmail)){
			MemberCommand findMemberByLoginEmail = sdkMemberManager.findMemberByLoginEmail(loginEmail);
			if (Validator.isNotNullOrEmpty(findMemberByLoginEmail)){
				defaultReturnResult.setResult(false);
				returnObject.put("loginEmail", "error.loginemail.notavailable");
			}
		}
		// 验证mobile
		String loginMobile = mfc.getLoginMobile();
		if (Validator.isNotNullOrEmpty(loginMobile)){
			MemberCommand findMemberByLoginMobile = sdkMemberManager.findMemberByLoginMobile(loginMobile);
			if (Validator.isNotNullOrEmpty(findMemberByLoginMobile)){
				defaultReturnResult.setResult(false);
				returnObject.put("loginMobile", "error.loginmobile.notavailable");
			}
		}

		// 验证 LoginName
		String loginName = mfc.getLoginName();
		if (Validator.isNotNullOrEmpty(loginName)){
			MemberCommand findMemberByLoginName = sdkMemberManager.findMemberByLoginName(loginName);
			if (Validator.isNotNullOrEmpty(findMemberByLoginName)){
				defaultReturnResult.setResult(false);
				returnObject.put("loginName", "error.loginname.notavailable");
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
		if (isAutoLoginAfterRegister()){
			super.onAuthenticationSuccess(memberDetails, request, response);
		}
		
		eventPublisher.publish(new RegisterSuccessEvent(memberDetails, getClientContext(request, response)));
		return DefaultReturnResult.SUCCESS;
	}

	/**
	 * 注册成功后是否需要自动登录
	 * 
	 * @return
	 */
	protected boolean isAutoLoginAfterRegister(){
		return false;
	}

}
