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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.exception.PasswordNotMatchException;
import com.baozun.nebula.exception.UserExpiredException;
import com.baozun.nebula.exception.UserNotExistsException;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.event.LoginSuccessEvent;
import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;
import com.baozun.nebula.web.controller.member.form.LoginForm;
import com.baozun.nebula.web.controller.member.validator.ForgetPasswordFormValidator;
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
public class NebulaLoginController extends NebulaAbstractLoginController {

	/* Remember me cookie key */
	public static final String COOKIE_KEY_REMEMBER_ME = "rmb";

	/* Login Page 的默认定义 */
	public static final String VIEW_MEMBER_LOGIN = "member.login";
	/* Register Page 的默认定义 */
	public static final String VIEW_MEMBER_REGISTER = "member.register";
	/* Fortget Password Page 的默认定义*/
	public static final String VIEW_MEMBER_FORTGET_PASSWORD	=	"member.forget.password";

	/* Login 登录ID */
	public static final String MODEL_KEY_MEMBER_LOGIN_ID = "loginId";

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
	 * 忘记密码Form的校验器
	 */
	@Autowired
	@Qualifier("forgetPasswordFormValidator")
	private ForgetPasswordFormValidator forgetPasswordFormValidator;

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
	public String showLogin(@LoginMember MemberDetails memberDetails, HttpServletRequest request,HttpServletResponse response, Model model) {
		// 登录用户
		if (!Validator.isNullOrEmpty(memberDetails)) {
			return getShowPage4LoginedUserViewLoginPage(memberDetails, request, model);
		}
		// 记住我，处理流程
		rememberMeProcess(request, model);
		// 密码前端JS加密准备工作
		init4SensitiveDataEncryptedByJs(request, model);

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
			return super.onAuthenticationSuccess(constructMemberDetails(member), request, response);

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
			model.addAttribute(MODEL_KEY_MEMBER_LOGIN_ID,
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
	 * 忘记密码页面, 默认推荐配置如下
	 * @RequestMapping(value = "/member/forgetPassword", method = RequestMethod.GET)
	 * @param model
	 * @return
	 */
	public String showForgetPassword(Model model) {
		return VIEW_MEMBER_FORTGET_PASSWORD;
	}

	/**
	 * 发送验证码, 默认推荐配置如下
	 * @RequestMapping(value = "/member/forgetPassword.json", method = RequestMethod.POST)
	 * @param forgetPasswordForm
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	public NebulaReturnResult forgetPassword(@ModelAttribute ForgetPasswordForm forgetPasswordForm,
			BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response, Model model) {
		// 1.数据校验 validator
		forgetPasswordFormValidator.validate(forgetPasswordForm, bindingResult);
		if(bindingResult.hasErrors()){
			//TODO 
			return null;
		}

		// 2.判断是手机还是邮箱，分别调用发送验证码逻辑
		

		return null;
	}
	
	

	/**
	 * 
	 * 登出, 默认推荐配置如下
	 * @RequestMapping(value = "/member/loginOut.json", method = RequestMethod.POST)
	 * @param request
	 * @return
	 */
	public NebulaReturnResult active(HttpServletRequest request,HttpServletResponse response, Model model) {
		// 1.清空session
		resetSession(request);

		return null;
	}
}
