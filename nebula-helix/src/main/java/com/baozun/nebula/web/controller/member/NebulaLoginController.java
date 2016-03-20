/**
 * Copyright (c) 2016 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.member;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.event.LoginSuccessEvent;
import com.baozun.nebula.event.RegisterSuccessEvent;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.PasswordNotMatchException;
import com.baozun.nebula.exception.UserExpiredException;
import com.baozun.nebula.exception.UserNotExistsException;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;
import com.baozun.nebula.web.controller.member.form.LoginForm;
import com.baozun.nebula.web.controller.member.form.RegisterForm;
import com.baozun.nebula.web.controller.member.validator.LoginFormValidator;
import com.baozun.nebula.web.controller.member.validator.RegisterFormValidator;
import com.feilong.core.util.Validator;
import com.feilong.servlet.http.CookieUtil;

/**
 * 用户登录
 * 
 * @author D.C
 *
 */
public class NebulaLoginController extends NebulaBaseLoginController {

	/* Remember me cookie key */
	public static final String COOKIE_KEY_REMEMBER_ME = "rmb";

	/* Login Page 的默认定义 */
	public static final String VIEW_MEMBER_LOGIN = "member.login";
	/* Register Page 的默认定义 */
	public static final String VIEW_MEMBER_REGISTER = "member.register";

	/* Login 登录ID */
	public static final String PROPERTY_KEY_MEMBER_LOGIN_ID = "member.login.id";

	/**
	 * 会员登录Form的校验器
	 */
	@Autowired
	@Qualifier("loginFormValidator")
	private LoginFormValidator loginFormValidator;

	/**
	 * 会员注册Form的校验器
	 */
	@Autowired
	@Qualifier("registerFormValidator")
	private RegisterFormValidator registerFormValidator;

	/**
	 * 会员业务管理类
	 */
	@Autowired
	private MemberManager memberManager;

	/**
	 * 登录页面，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/login", method = RequestMethod.GET)
	 * @param memberDetails
	 *            用户未登录的情况下进入该页面该值将为空，主要用于已登录用户的页面跳转处理
	 * @param httpRequest
	 * @param model
	 * @return
	 */
	public String showLogin(@LoginMember MemberDetails memberDetails, HttpServletRequest request, Model model) {
		// 登录用户
		if (!Validator.isNullOrEmpty(memberDetails)) {
			return getShowPage4LoginedUserViewLoginPage(memberDetails, request, model);
		}
		// 记住我，处理流程
		rememberMeProcess(request, model);
		// 密码前端JS加密准备工作
		prepare4SensitiveDataEncryptedByJs(request, model);

		return VIEW_MEMBER_LOGIN;
	}

	/**
	 * 登录处理，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/login.json", method =
	 *                       RequestMethod.POST)
	 * @param memberDetails
	 *            用户未登录的情况下进入该页面该值将为空，主要用于已登录用户的页面跳转处理
	 * @param loginForm
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	// TODO 验证码
	public NebulaReturnResult login(@ModelAttribute LoginForm loginForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		// 数据校验
		loginFormValidator.validate(loginForm, bindingResult);
		if (bindingResult.hasErrors()) {
			// TODO出错处理
			return null;
		}

		// 密码解密，密码传输通过RSA做了加密，此处需要解密
		loginForm.setPassword(decryptSensitiveDataEncryptedByJs(loginForm.getPassword(), request));

		try {
			// 登录判断，登录失败会抛出异常，通过捕获处理
			MemberCommand member = memberManager.login(loginForm.toMemberFrontendCommand());

			// 认证成功处理
			return onAuthenticationSuccess(constructMemberDetails(member), request, response);

		} catch (UserNotExistsException | UserExpiredException | PasswordNotMatchException e) { // 登录失败
			// TODO 登录失败处理
			return null;
		}
	}

	/**
	 * 登出，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/loginOut.json", method =
	 *                       RequestMethod.GET)
	 * @param request
	 * @param response
	 * @return
	 */
	public NebulaReturnResult logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		onLogoutSuccess(request, response);
		return DefaultReturnResult.SUCCESS;
	}

	/**
	 * 登出成功
	 * 
	 * @param request
	 * @param response
	 */
	protected void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response) {

	}

	/**
	 * 处理已登录用户访问登录页面
	 * 
	 * @param memberDetails
	 * @param request
	 * @param model
	 * @return 默认返回登录页
	 */
	protected String getShowPage4LoginedUserViewLoginPage(MemberDetails memberDetails, HttpServletRequest request,
			Model model) {
		return VIEW_MEMBER_LOGIN;
	}

	/**
	 * remember me 处理
	 * 
	 * @param request
	 * @param model
	 */
	protected void rememberMeProcess(HttpServletRequest request, Model model) {
		if (isSupportRemberMe() && !Validator.isNullOrEmpty(CookieUtil.getCookie(request, COOKIE_KEY_REMEMBER_ME))) {
			model.addAttribute(PROPERTY_KEY_MEMBER_LOGIN_ID,
					remberMeValueDecrypt(CookieUtil.getCookie(request, COOKIE_KEY_REMEMBER_ME).getValue()));
		}
	}

	/**
	 * remember me 值的解密处理
	 * 
	 * @param value
	 * @return 默认原值返回
	 */
	protected String remberMeValueDecrypt(String value) {
		return value;
	}

	/**
	 * remember me 值的加密处理
	 * 
	 * @param value
	 * @return 默认原值返回
	 */
	protected String remberMeValueEncrypt(String value) {
		return value;
	}

	/**
	 * 是否支持remember me，各商城可重写
	 * 
	 * @return 默认关闭
	 */
	protected boolean isSupportRemberMe() {
		return Boolean.FALSE.booleanValue();
	}

	/**
	 * 构造MemberDetails
	 * 
	 * @param member
	 * @return
	 */
	protected MemberDetails constructMemberDetails(MemberCommand member) {
		MemberDetails memberDetails = new MemberDetails();
		// TODO property填充
		memberDetails.setActived(this.isActivedMember(member));
		return memberDetails;
	}

	/**
	 * 认证成功，未激活用户也认为是认证成功
	 * 
	 * @param memberDetails
	 * @param request
	 * @param response
	 */
	protected NebulaReturnResult onAuthenticationSuccess(MemberDetails memberDetails, HttpServletRequest request,
			HttpServletResponse response) {
		// 未激活
		if (!memberDetails.isActived()) {
			return onLoginSuccess4InactivatedMember(memberDetails, request, response);
		} else {
			return onLoginSuccess4ActivedMember(memberDetails, request, response);
		}
	}

	/**
	 * 激活的用户登录成功
	 * 
	 * @param memberDetails
	 * @param request
	 * @param response
	 * @return
	 */
	protected NebulaReturnResult onLoginSuccess4ActivedMember(MemberDetails memberDetails, HttpServletRequest request,
			HttpServletResponse response) {
		super.resetSession(request);
		request.getSession().setAttribute(SessionKeyConstants.MEMBER_CONTEXT, memberDetails);
		// 触发登录成功事件，用于异步处理其他的业务
		eventPublisher.publish(new LoginSuccessEvent(memberDetails, getClientContext(request, response)));
		return DefaultReturnResult.SUCCESS;
	}

	/**
	 * 获取用户环境上下文，如ip agent等信息，默认是空实现
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected Map<String, String> getClientContext(HttpServletRequest request, HttpServletResponse response) {
		return new HashMap<String, String>();
	}

	/**
	 * 未激活的用户登录成功, 默认不区分激活不激活的用户，区分时请重构
	 * 
	 * @param memberDetails
	 * @param request
	 * @param response
	 * @return
	 */
	protected NebulaReturnResult onLoginSuccess4InactivatedMember(MemberDetails memberDetails,
			HttpServletRequest request, HttpServletResponse response) {
		return this.onLoginSuccess4ActivedMember(memberDetails, request, response);
	}

	/**
	 * 用户是否激活
	 * 
	 * @param member
	 * @return
	 */
	protected boolean isActivedMember(MemberCommand member) {
		// TODO 判断逻辑
		return false;
	}

	/**
	 * 注册页面，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/register", method = RequestMethod.GET)
	 * @param model
	 * @param request
	 * @return
	 */
	public String showRegister(Model model, HttpServletRequest request) {
		prepare4SensitiveDataEncryptedByJs(request, model);
		return VIEW_MEMBER_REGISTER;
	}

	/**
	 * 注册处理，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/register.json", method =
	 *                       RequestMethod.POST)
	 * @param registerForm
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	// TODO 验证码
	public NebulaReturnResult register(@ModelAttribute RegisterForm registerForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response, Model model) {

		// 数据校验
		registerFormValidator.validate(registerForm, bindingResult);
		if (bindingResult.hasErrors()) {
			// TODO出错处理
			return null;
		}

		// 密码解密，密码传输通过RSA做了加密，此处需要解密
		registerForm.setPassword(decryptSensitiveDataEncryptedByJs(registerForm.getPassword(), request));
		// 用户注册
		try {
			Member member = memberManager.register(registerForm.toMemberFrontendCommand());
			//TODO member convert to memberCommand, 激活状态处理
			MemberCommand memberCommand = null;
			
			return onRegisterSuccess(constructMemberDetails(memberCommand), request, response);
		} catch (BusinessException e) {
			// TODO 异常处理
			return null;
		}
	}
	/**
	 * 用户注册成功切入点
	 * @param memberDetails
	 * @param request
	 * @param response
	 * @return
	 */
	protected NebulaReturnResult onRegisterSuccess(MemberDetails memberDetails, HttpServletRequest request,
			HttpServletResponse response) {
		if(isAutoLoginAfterRegister()) {
			this.onAuthenticationSuccess(memberDetails, request, response);
		}
		
		eventPublisher.publish(new RegisterSuccessEvent(memberDetails, getClientContext(request, response)));
		return DefaultReturnResult.SUCCESS;
	}

	/**
	 * 注册成功后是否需要自动登录
	 * 
	 * @return
	 */
	protected boolean isAutoLoginAfterRegister() {
		return false;
	}

	// 去忘记密码页面，"/member/forgetPassword"
	public String showForgetPassword(Model model) {
		return "store.member.fortgetPassword";
	}

	// 发送验证码,"/member/forgetPassword.json"
	public BackWarnEntity forgetPassword(@ModelAttribute ForgetPasswordForm forgetPasswordForm,
			BindingResult bindingResult, Model model) {
		// 1.数据校验 validator

		// 2.判断是手机还是邮箱，分别调用发送验证码逻辑

		// 3.builder backWarnEntity返回

		return new BackWarnEntity();
	}

	// 登出 ,"/member/loginOut.json"
	public BackWarnEntity active(HttpServletRequest request) {
		// 1.清空session

		// 2.builder backWarnEntity返回

		return new BackWarnEntity();
	}
}
