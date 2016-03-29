
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

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.exception.LoginException;
import com.baozun.nebula.exception.PasswordNotMatchException;
import com.baozun.nebula.exception.UserExpiredException;
import com.baozun.nebula.exception.UserNotExistsException;
import com.baozun.nebula.manager.member.MemberExtraManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.LoginForm;
import com.baozun.nebula.web.controller.member.validator.LoginFormValidator;
import com.feilong.core.Validator;
import com.feilong.servlet.http.CookieUtil;

/**
 * 登录相关方法controller
 * @author 冯明雷
 * @version 1.0
 * @time 2016年3月28日  上午10:26:57
 */
public class NebulaLoginController extends NebulaAbstractLoginController{

	/**
	 * log定义
	 */
	private static final Logger	LOG							= LoggerFactory.getLogger(NebulaLoginController.class);

	/* Remember me user cookie key */
	public static final String	COOKIE_KEY_REMEMBER_ME_USER	= "rmbu";

	/* Remember me pwd cookie key */
	public static final String	COOKIE_KEY_REMEMBER_ME_PWD	= "rmbp";

	/* Login Page 的默认定义 */
	public static final String	VIEW_MEMBER_LOGIN			= "member.login";
	
	/* 登录成功的默认定义 */
	public static final String	VIEW_MEMBER_LOGIN_SUCCESS	= "member.login.success";

	/* Login 登录ID */
	public static final String	MODEL_KEY_MEMBER_LOGIN_ID	= "loginId";

	/* Login 登录密码 */
	public static final String	MODEL_KEY_MEMBER_LOGIN_PWD	= "loginPwd";

	private static final String	REMEMBER_PWD				= "********";

	/**
	 * 会员登录Form的校验器
	 */
	@Autowired
	@Qualifier("loginFormValidator")
	private LoginFormValidator	loginFormValidator;

	/**
	 * 会员业务管理类
	 */
	@Autowired
	private MemberManager		memberManager;

	@Autowired
	private MemberExtraManager	memberExtraManager;

	/**
	 * 登录页面，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/login", method = RequestMethod.GET)
	 * @param memberDetails
	 *            用户未登录的情况下进入该页面该值将为空，主要用于已登录用户的页面跳转处理
	 * @param httpRequest
	 * @param httpResponse
	 * @param model
	 */
	public String showLogin(@LoginMember MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response,Model model){
		// 记住我的用户名和密码的处理流程
		rememberMeProcess(request, response, model);

		// 密码前端JS加密准备工作
		init4SensitiveDataEncryptedByJs(request, model);
		
		
		// 如果用户已经登录，默认返回
		if (Validator.isNotNullOrEmpty(memberDetails)) {
			LOG.info("[USER_ALREADY_LOGIN] {} [{}] \"User alerady login and will jump profile page\"", memberDetails.getLoginName(), new Date());			
			return getShowPage4LoginedUserViewLoginPage(memberDetails, request, model);			
		}
		
		
		return VIEW_MEMBER_LOGIN;
	}

	/**
	 * 登录处理，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/login.json", method = RequestMethod.POST)
	 * @param memberDetails
	 *            用户未登录的情况下进入该页面该值将为空，主要用于已登录用户的页面跳转处理
	 * @param loginForm
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @param model
	 * @return NebulaReturnResult
	 */
	public NebulaReturnResult login(
			@ModelAttribute LoginForm loginForm,
			BindingResult bindingResult,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model){
		/*
		//校验输入数据
		loginFormValidator.validate(loginForm, bindingResult);
		//如果校验失败，返回错误
		if (bindingResult.hasErrors()) {
			//因为目前都还是密文，所以可以直接Debug输出
			LOG.debug("loginForm validation error. [{}/{}]", loginForm.getLoginName(), loginForm.getPassword());
			return getResultFromBindingResult(bindingResult);
		}
		//处理Form中数据的加解密
		loginForm.setLoginName(decryptSensitiveDataEncryptedByJs(loginForm.getLoginName(), request));
		loginForm.setPassword(decryptSensitiveDataEncryptedByJs(loginForm.getPassword(), request));
		//此后使用 loginForm.toMemberCommand 就可以获得业务层模型了
		
		MemberCommand memberCommand = null;
		//判断AutoLogin的设定，RemeberMe不用考虑，因为页面已经自动回写用户名到Form中了
		if(isAutoLogin()){
			//这里需要检查AutoLogin，检查的方法是检查解密后的Password和之前提供的随机Hash是否一致
			if(validateAutoLogin(loginForm, request)){
				//AutoLogin
				LOG.info("[MEM_AUTO_LOGIN ] {} [{}] \"\"", loginForm.getLoginName(), new Date());
				memberCommand = memberManager.findMemberByUsername(loginForm.getLoginName());
			}else{
				LOG.info("[MEM_AUTO_LOGIN_FAILED ] {} [{}] \"Will check password later\"", loginForm.getLoginName(), new Date());
			}
		}
		//开始进行普通用户登录校验
		if(memberCommand == null){
			LOG.debug("Check Login information for user {}", loginForm.getLoginName());
			try {
				memberCommand=memberManager.login(loginForm.toMemberFrontendCommand());
			} catch (LoginException e) {
				LOG.info("[MEM_LOGIN_FAILURE] {} [{}] \"{}\"", 
						loginForm.getLoginName(), new Date(), e.getClass().getSimpleName());
			}
		}
		if(memberCommand != null){
			//登录成功的处理
			LOG.debug("{} login success", memberCommand.getLoginName());
			//处理RemeberMe和AutoLogin(这两个东西需要分开来，因此是两个不同的Cookie Key)
			//这里处理的逻辑需要注意以下几点：
			//1. 如果RemeberMe被设置，那么需要重新设置RemeberMe的用户名，有效期需要配置
			//2. 如果AutoLogin被设置，那么需要重新设置AutoLogin的用户名，有效期需要配置
			doRememberMeProcess(loginForm, memberId, request, response, model);
			return super.onAuthenticationSuccess(constructMemberDetails(memberCommand), request, response);
		}else{
			//登录失败的处理
			LOG.debug("{} login failure", memberCommand.getLoginName());
			DefaultReturnResult returnResult = new DefaultReturnResult();
			returnResult.setResult(false);
			DefaultResultMessage message = new DefaultResultMessage();
			message.setMessage("login.failure");
			returnResult.setResultMessage(message);
			return returnResult;
		}
		
		
		*/
		////////////////////////////////////////////////////////////////////////////////
		
		
		DefaultReturnResult returnResult = new DefaultReturnResult();
		DefaultResultMessage defaultResultMessage = new DefaultResultMessage();

		String pwd = loginForm.getPassword();

		// ****************************************************************** 数据校验
		loginFormValidator.validate(loginForm, bindingResult);
		if (bindingResult.hasErrors()) {
			returnResult.setResult(false);
			defaultResultMessage.setMessage(bindingResult.getAllErrors().get(0).getCode());
			returnResult.setResultMessage(defaultResultMessage);
			return returnResult;
		}

		// ***************************************************************解密用户名和密码
		// 是否已经记住了密码,默认没有
		boolean isHaveReMemberPwd = false;

		// 记住用户名和密码
		if (REMEMBER_PWD.equals(pwd) && isSupportAutoLogin()) {
			isHaveReMemberPwd = isHaveReMemberPwd(loginForm, request, response, model);
		}else{
			// 不记住密码的，直接解密密码，进行验证
			// 密码解密，密码传输通过RSA做了加密，此处需要解密
			loginForm.setPassword(decryptSensitiveDataEncryptedByJs(loginForm.getPassword(), request));
		}

		// **********************************************************登录
		try{
			
			// 登录判断，登录失败会抛出异常，通过捕获处理
			MemberCommand memberCommand =null;
			
			if(isHaveReMemberPwd){
				//如果记住了密码，登录时不验证密码
				memberCommand=memberManager.loginWithOutPwd(loginForm.toMemberFrontendCommand());
			}else{
				memberCommand=memberManager.login(loginForm.toMemberFrontendCommand());
			}

			Long memberId = memberCommand.getId();

			// 设置记住用户名密码
			doRememberMeProcess(loginForm, memberId, request, response, model);

			// 认证成功处理
			return super.onAuthenticationSuccess(constructMemberDetails(memberCommand), request, response);
		}catch (UserNotExistsException e){
			// 登录用户不存在
			LOG.error(e.getMessage());
			returnResult.setResult(false);
			defaultResultMessage.setMessage("loginerror.userNotExists");
			returnResult.setResultMessage(defaultResultMessage);
		}catch (UserExpiredException e){
			// 登录用户生命周期不对
			LOG.error(e.getMessage());
			returnResult.setResult(false);
			defaultResultMessage.setMessage("loginerror.UserExpiredException");
			returnResult.setResultMessage(defaultResultMessage);
		}catch (PasswordNotMatchException e){
			// 登录密码错误
			LOG.error(e.getMessage());
			returnResult.setResult(false);
			defaultResultMessage.setMessage("loginerror.passwordError");
			returnResult.setResultMessage(defaultResultMessage);
		}catch (Exception e){
			// 登录失败处理
			LOG.error(e.getMessage());
			returnResult.setResult(false);
			defaultResultMessage.setMessage("loginerror.systemError");
			returnResult.setResultMessage(defaultResultMessage);
		}

		return returnResult;
	}

	/**
	 * 登出，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/loginOut.json", method = RequestMethod.POST)
	 * @param request
	 * @param response
	 * @return
	 */
	public NebulaReturnResult logout(HttpServletRequest request,HttpServletResponse response,Model model){
		// 1.清空session
		resetSession(request);
		onLogoutSuccess(request, response);
		return DefaultReturnResult.SUCCESS;
	}

	/**
	 * 验证是否已经记住了密码 boolean
	 * 
	 * @author 冯明雷
	 * @time 2016-3-23下午3:51:07
	 */
	protected boolean isHaveReMemberPwd(LoginForm loginForm,HttpServletRequest request,HttpServletResponse response,Model model){
		// cookie中保存的密码的key
		Cookie pwdKeyCookie = CookieUtil.getCookie(request, COOKIE_KEY_REMEMBER_ME_PWD);
		// cookie保存的用户名
		Cookie loginNameCookie = CookieUtil.getCookie(request, COOKIE_KEY_REMEMBER_ME_USER);

		// cookie中保存的用户名和密码不能为空
		if (Validator.isNotNullOrEmpty(loginNameCookie) && Validator.isNotNullOrEmpty(pwdKeyCookie)) {
			String loginName = loginForm.getLoginName();
			try{
				if (loginNameCookie.getValue().equals(EncryptUtil.getInstance().encrypt(loginName))) {
					// 根据用户名查询该用户修改密码的最后时间，用来判断记住的密码是否过期，和解密cookie中保存的密码的key
					MemberPersonalData memberPersonalData = memberExtraManager.findMemPersonalDataByLoginName(loginName);
					if (memberPersonalData != null) {
						String short4 = memberPersonalData.getShort4();
						String checkNum = EncryptUtil.getInstance().encrypt(EncryptUtil.getInstance().hash(loginName + short4,remberMePwdKey()));
						// 如果验证通过，去根据用户名查询密码
						return pwdKeyCookie.getValue().equals(checkNum);
					}
				}
			}catch (Exception e){
				LOG.error(e.getMessage());
			}
		}
		return false;
	}
	

	/**
	 * 登出成功
	 * 
	 * @param request
	 * @param response
	 */
	protected void onLogoutSuccess(HttpServletRequest request,HttpServletResponse response){

	}

	/**
	 * 处理已登录用户访问登录页面
	 * 
	 * @param memberDetails
	 * @param request
	 * @param model
	 * @return 默认返回登录成功页
	 */
	protected String getShowPage4LoginedUserViewLoginPage(MemberDetails memberDetails,HttpServletRequest request,Model model){
		return VIEW_MEMBER_LOGIN_SUCCESS;
	}

	/**
	 * remember me 处理
	 * 
	 * @param request
	 * @param model
	 */
	protected void rememberMeProcess(HttpServletRequest request,HttpServletResponse response,Model model){
		boolean isSupportRemeberMe = isSupportRemeberMe();

		// 是否记住密码，如果是记住密码，默认就记住用户名
		if (isSupportAutoLogin()) {
			isSupportRemeberMe = true;
			Cookie pwd = CookieUtil.getCookie(request, COOKIE_KEY_REMEMBER_ME_PWD);
			if (Validator.isNotNullOrEmpty(pwd)) {
				model.addAttribute(MODEL_KEY_MEMBER_LOGIN_PWD, REMEMBER_PWD);
			}
		}

		if (isSupportRemeberMe) {
			Cookie userName = CookieUtil.getCookie(request, COOKIE_KEY_REMEMBER_ME_USER);
			if (Validator.isNotNullOrEmpty(userName)) {
				model.addAttribute(MODEL_KEY_MEMBER_LOGIN_ID, remberMeValueDecrypt(userName.getValue(), request));
			}
		}
	}

	/**
	 * 记住用户名和密码(加密后保存到cookie中)
	 * 
	 * @return void
	 * @param loginForm
	 *            页面传过来的参数，这个方法用到的属性是：isRemberMeLoginName是否记住用户名；isRemberMePwd:是否记住密码(默认不记住)
	 * @param memberId
	 *            登录用户的id
	 * @param request
	 * @param response
	 * @param model
	 * @author 冯明雷
	 * @throws EncryptionException
	 * @time 2016年3月25日下午1:58:43
	 */
	protected void doRememberMeProcess(
			LoginForm loginForm,
			Long memberId,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model) throws EncryptionException{
		// 先清除记住的用户名和密码
		CookieUtil.deleteCookie(COOKIE_KEY_REMEMBER_ME_USER, response);
		CookieUtil.deleteCookie(COOKIE_KEY_REMEMBER_ME_PWD, response);

		if (loginForm.getIsRemberMeLoginName()) {
			// 设置cookie值
			CookieUtil.addCookie(COOKIE_KEY_REMEMBER_ME_USER,EncryptUtil.getInstance().encrypt(loginForm.getLoginName()),remberMeValidityPeriod(),response);

			if (loginForm.getIsRemberMePwd()) {
				String timestamp = String.valueOf(new Date().getTime());
				memberExtraManager.rememberPwd(memberId, timestamp);
				String checkNum = EncryptUtil.getInstance().hash(loginForm.getLoginName() + timestamp, remberMePwdKey());
				CookieUtil.addCookie(COOKIE_KEY_REMEMBER_ME_PWD, EncryptUtil.getInstance().encrypt(checkNum), remberMeValidityPeriod(), response);
			}
		}

	}

	/**
	 * remember me 值的解密处理
	 * 
	 * @param value
	 * @return 默认原值返回
	 */
	protected String remberMeValueDecrypt(String value,HttpServletRequest request){
		return decryptSensitiveDataEncryptedByJs(value, request);
	}

	/**
	 * remember me 值的加密处理
	 * 
	 * @param value
	 * @return 默认原值返回
	 */
	protected String remberMeValueEncrypt(String value){
		return value;
	}

	/**
	 * 是否支持remember me 用户名，各商城可重写
	 * 
	 * @return 默认关闭
	 */
	protected boolean isSupportRemeberMe(){
		return Boolean.FALSE.booleanValue();
	}

	/**
	 * 是否支持remember me 密码，(如果记住密码默认记住用户名)各商城可重写
	 * 
	 * @return 默认关闭
	 */
	protected boolean isSupportAutoLogin(){
		return Boolean.FALSE.booleanValue();
	}

	
	/**
	 * 获取记住密码加密使用的key
	 * @return String
	 * @author 冯明雷
	 * @time 2016年3月25日下午3:20:55
	 */
	protected String remberMePwdKey(){
		return ProfileConfigUtil.findPro("config/metainfo.properties").getProperty("rember.pwd.key");
	}
	
	/**
	 * 记住我的用户名、密码cookie有效期(默认30天)
	 * @return int
	 * @return 
	 * @author 冯明雷
	 * @time 2016年3月25日下午3:24:35
	 */
	protected int remberMeValidityPeriod(){
		return 30*24*60*60;
	}

}
