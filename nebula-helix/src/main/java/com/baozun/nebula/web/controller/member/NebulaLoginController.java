
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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.exception.LoginException;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.LoginForm;
import com.baozun.nebula.web.controller.member.validator.LoginFormValidator;
import com.baozun.nebula.web.controller.member.viewcommand.MemberLoginViewCommand;
import com.feilong.core.Validator;
import com.feilong.servlet.http.CookieUtil;
import com.feilong.servlet.http.entity.CookieEntity;

/**
 * 登录相关方法controller
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年3月28日 上午10:26:57
 */
public class NebulaLoginController extends NebulaAbstractLoginController{

	/**
	 * log定义
	 */
	private static final Logger	LOG								= LoggerFactory.getLogger(NebulaLoginController.class);

	/* Remember me user cookie key */
	public static final String	COOKIE_KEY_REMEMBER_ME_USER		= "rmbu";

	/* Remember me pwd cookie key */
	public static final String	COOKIE_KEY_AUTO_LOGIN			= "rmbc";

	/* Login Page 的默认定义 */
	public static final String	VIEW_MEMBER_LOGIN				= "member.login";

	/* Login 登录ID */
	public static final String	MODEL_KEY_MEMBER_LOGIN_ID		= "login";

	/* Login 登录密码 */
	public static final String	MODEL_KEY_MEMBER_LOGIN_PWD		= "password";
	
	/**
	 * 默认RemeberMe过期时间
	 */
	public static final int DEFAULT_REMEBERME_MAX_AGE = 30*24*60*60;
	
	/** 默认的记住用户名cookie有效期，商城可以重写set方法 */
	private int					remberMeValidityPeriod = -1;

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
		MemberLoginViewCommand memberLoginViewCommand = new MemberLoginViewCommand();

		// 记住我的用户名和密码的处理流程
		rememberMeProcess(memberLoginViewCommand, request, response, model);

		// 密码前端JS加密准备工作
		init4SensitiveDataEncryptedByJs(request, model);
		
		//将信息传到页面
		model.addAttribute("memberLoginViewCommand", memberLoginViewCommand);
		
		// 如果用户已经登录，默认返回
		if (Validator.isNotNullOrEmpty(memberDetails)) {
			LOG.info("[USER_ALREADY_LOGIN] {} [{}] \"User alerady login and will jump profile page\"",memberDetails.getLoginName(),new Date());
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
		if(isSupportAutoLogin()){
			//这里需要检查AutoLogin，检查的方法是检查解密后的Password和之前提供的随机Hash是否一致
			memberCommand = memberManager.findMemberCommandByLoginName(loginForm.getLoginName());
			if(memberCommand != null && validateAutoLogin(loginForm, memberCommand, request)){
				 LOG.info("[MEM_AUTO_LOGIN ] {} [{}] \"\"", loginForm.getLoginName(), new Date());
			}else{
				LOG.info("[MEM_AUTO_LOGIN_FAILED ] {} [{}] \"Will check password later\"", loginForm.getLoginName(), new Date()); 
				memberCommand = null;
			}
		}
		
		
		//如果memberCommand为空，代表不能自动登录或者自动登录失败开始进行普通用户登录校验
		if(memberCommand == null){
			LOG.debug("Check Login information for user {}", loginForm.getLoginName());
			try{
				memberCommand=memberManager.login(loginForm.toMemberFrontendCommand());
			} catch (LoginException e) {
				LOG.info("[MEM_LOGIN_FAILURE] {} [{}] \"{}\"", loginForm.getLoginName(), new Date(), e.getClass().getSimpleName());
			} 
		}
		
		
		if(memberCommand!= null){
			//登录成功的处理
			LOG.debug("{} login success", memberCommand.getLoginName());
			
			//处理RemeberMe和AutoLogin
			doRememberMeProcess(loginForm, memberCommand, request, response, model);
			
			return super.onAuthenticationSuccess(constructMemberDetails(memberCommand), request, response); 
		}else{
			//登录失败的处理 
			LOG.debug("{} login failure", loginForm.getLoginName());
			DefaultReturnResult returnResult = new DefaultReturnResult();
			returnResult.setResult(false);
			DefaultResultMessage message = new DefaultResultMessage(); message.setMessage("login.failure");
			returnResult.setResultMessage(message);
			return returnResult;
		}
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
	 * 验证是否自动登录
	 * @return boolean
	 * @param memberCommand
	 * @param request
	 * @author 冯明雷
	 * @time 2016年3月30日上午11:09:29
	 */
	protected boolean validateAutoLogin(LoginForm loginForm, MemberCommand memberCommand, HttpServletRequest request){
		//cookie保存的用户名
		String loginName =getRememberedLogin(request);
				
		// cookie中保存的自动登录的key
		String autoLoginPassword =getAutoLoginPassword(request);
		
		// cookie中保存的用户名和密码不能为空
		if (Validator.isNotNullOrEmpty(loginName) && Validator.isNotNullOrEmpty(autoLoginPassword)) {
			//页面传来的密码必须和默认密码相同
			if(loginForm.getPassword().equals(autoLoginPassword) &&
					loginName.equals(loginForm.getLoginName())){
				LOG.debug("Auto login password is same with saved one, will check whether it is expired.");
				return loginForm.getPassword().equals(generateAutoLoginPassword(memberCommand));
			}else{
				LOG.debug("Auto Login Password is not valid from Input for {}", loginForm.getLoginName());
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
	 * 处理已登录用户访问登录页面(商城可以重写)
	 * 
	 * @param memberDetails
	 * @param request
	 * @param model
	 * @return 默认返回登录页
	 */
	protected String getShowPage4LoginedUserViewLoginPage(MemberDetails memberDetails,HttpServletRequest request,Model model){		
		return VIEW_MEMBER_LOGIN;
	}

	/**
	 * 进入登录页面的时候记住用户名和密码的操作
	 * 
	 * @return void
	 * @param memberLoginViewCommand
	 * @param request
	 * @param response
	 * @param model
	 * @author 冯明雷
	 * @time 2016年3月29日下午6:15:30
	 */
	protected void rememberMeProcess(
			MemberLoginViewCommand memberLoginViewCommand,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model){

		// 如果支持记住用户名
		if (isSupportRemeberMe()) {
			// 从cookie中获取解密后的loginName
			String loginName = getRememberedLogin(request);
			if (Validator.isNotNullOrEmpty(loginName)) {
				LOG.debug("RememberMe information is loaded: [User:{}]", loginName);
				memberLoginViewCommand.setIsRememberMe(true);
				memberLoginViewCommand.setLoginName(loginName);
			}
		}
		
		if(isSupportAutoLogin()){
			assert isSupportRemeberMe() : "SupportAutoLogin can only be actived when SupportRemeberMe";
			if(memberLoginViewCommand.getLoginName() != null){
				LOG.debug("User Login Name exists. Try to get Auto Login password.");
				String password = getAutoLoginPassword(request);
				if (Validator.isNotNullOrEmpty(password)) {
					memberLoginViewCommand.setIsAutoLogin(true);
					memberLoginViewCommand.setPassword(password);
				}
			}else{
				LOG.debug("No Auto Login informaton exists.");
			}
		}

	}
	
	/**
	 * 读取暂存用户名，默认方法是加密存放在Cookie中
	 * @param request
	 * @return
	 */
	protected String getRememberedLogin(HttpServletRequest request){
		try {
			return getEncryptedInformationFromCookie(request, COOKIE_KEY_REMEMBER_ME_USER);
		} catch (EncryptionException e) {
			LOG.warn("Remeber Me information is not correct: {}", e.getOriginText());				
		}
		return null;
	}
	
	/**
	 * 设置暂存用户名，默认方法是加密存放在Cookie中
	 * @param response
	 * @param loginName
	 */
	protected void setRememberedLogin(HttpServletResponse response, String loginName){
		try {
			setEncryptedInformationFromCookie(response, COOKIE_KEY_REMEMBER_ME_USER, loginName, getRemberMeValidityPeriod());
		} catch (EncryptionException e) {
			LOG.warn("Error when Settting Remeber Me information: {}", e);
		}
	}
	
	/**
	 * 读取暂存自动登录密码，默认方法是加密存放在Cookie中
	 * @param request
	 * @return
	 */
	protected String getAutoLoginPassword(HttpServletRequest request){
		try {
			return getEncryptedInformationFromCookie(request, COOKIE_KEY_AUTO_LOGIN);
		} catch (EncryptionException e) {
			LOG.warn("AutoLogin Password information is not correct: {}", e.getOriginText());				
		}
		return null;
	}
	
	/**
	 * 设置暂存自动登录密码，默认方法是加密存放在Cookie中
	 * @param response
	 * @param memberCommand
	 */
	protected void setAutoLoginPassword(HttpServletResponse response, MemberCommand memberCommand){
		try {
			setEncryptedInformationFromCookie(response, COOKIE_KEY_AUTO_LOGIN, 
					generateAutoLoginPassword(memberCommand), getRemberMeValidityPeriod());
		} catch (EncryptionException e) {
			LOG.warn("Error when Settting Remeber Me information: {}", e);
		}
	}
	
	/**
	 * 生成自动登录密码，方法可以被重载
	 * @param memberCommand
	 * @return
	 */
	protected String generateAutoLoginPassword(MemberCommand memberCommand){
		String pwd = EncryptUtil.getInstance().hash(memberCommand.getLoginName(), memberCommand.getPassword());
		return pwd.substring(0,6);
	}
	
	private String getEncryptedInformationFromCookie(HttpServletRequest request, String name) throws EncryptionException{
		String encValue = CookieUtil.getCookieValue(request, name);
		if(encValue != null){
			return EncryptUtil.getInstance().decrypt(name);
		}
		return null;
	}
	
	private void setEncryptedInformationFromCookie(HttpServletResponse response, 
			String name, String value, int maxAge) throws EncryptionException{
		CookieEntity cookie = new CookieEntity(name, EncryptUtil.getInstance().encrypt(value));
		cookie.setMaxAge(maxAge);
		CookieUtil.addCookie(cookie, response);
	}

	/**
	 * 记住用户名和密码(加密后保存到cookie中)
	 * 
	 * @return void
	 * @param loginForm
	 *            页面传过来的参数，这个方法用到的属性是：isRemberMeLoginName是否记住用户名；isRemberMePwd:是否记住密码(默认不记住)
	 * @param memberCommand
	 *            登录用户的信息
	 * @param request
	 * @param response
	 * @param model
	 * @author 冯明雷
	 * @throws EncryptionException
	 * @time 2016年3月25日下午1:58:43
	 */
	protected void doRememberMeProcess(
			LoginForm loginForm,
			MemberCommand memberCommand,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model){
		
		if(loginForm.getIsRemberMePwd()){
			if(isSupportAutoLogin()){
				LOG.debug("Need update Both RemeberMe & AutoLogin information");
				setRememberedLogin(response, loginForm.getLoginName());
				setAutoLoginPassword(response, memberCommand);
				LOG.info("[RMBER_ME_AUTOLOGIN_SET] {} [{}] \"\"", loginForm.getLoginName(), new Date());
			}else{
				LOG.warn("System don't support AutoLogin!");
				CookieUtil.deleteCookie(COOKIE_KEY_REMEMBER_ME_USER, response);
				CookieUtil.deleteCookie(COOKIE_KEY_AUTO_LOGIN, response);
			}			
		} else if(loginForm.getIsRemberMeLoginName()){
			if(isSupportRemeberMe()){
				LOG.debug("Need update RemeberMe information");
				setRememberedLogin(response, loginForm.getLoginName());
				LOG.info("[RMBER_ME_SET] {} [{}] \"\"", loginForm.getLoginName(), new Date());
			}else{
				LOG.warn("System don't support RememberMe!");
				CookieUtil.deleteCookie(COOKIE_KEY_REMEMBER_ME_USER, response);
			}
		} else{
			LOG.debug("No Remember Me. Clear any possible remember me settings");
			if(CookieUtil.getCookieValue(request, COOKIE_KEY_REMEMBER_ME_USER) != null){
				//清除记住的用户名和密码
				CookieUtil.deleteCookie(COOKIE_KEY_REMEMBER_ME_USER, response);
				CookieUtil.deleteCookie(COOKIE_KEY_AUTO_LOGIN, response);
			}		
		}

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
	 * get remberMeValidityPeriod  
	 * @return remberMeValidityPeriod  
	 */
	public int getRemberMeValidityPeriod(){
		return remberMeValidityPeriod >0 ? remberMeValidityPeriod : DEFAULT_REMEBERME_MAX_AGE;
	}

	
	/**
	 * set remberMeValidityPeriod 
	 * @param remberMeValidityPeriod
	 */
	public void setRemberMeValidityPeriod(int remberMeValidityPeriod){
		this.remberMeValidityPeriod = remberMeValidityPeriod;
	}

}
