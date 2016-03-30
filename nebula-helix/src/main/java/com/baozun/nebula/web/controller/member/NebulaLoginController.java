
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

import javax.servlet.http.Cookie;
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
import com.baozun.nebula.manager.member.MemberExtraManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.web.HelixConfig;
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
	public static final String	COOKIE_KEY_AUTO_LOGIN			= "ckal";

	/* Login Page 的默认定义 */
	public static final String	VIEW_MEMBER_LOGIN				= "member.login";

	/* Login 登录ID */
	public static final String	MODEL_KEY_MEMBER_LOGIN_ID		= "loginId";

	/* Login 登录密码 */
	public static final String	MODEL_KEY_MEMBER_LOGIN_PWD		= "loginPwd";
	
	/** 默认的记住用户名cookie有效期，商城可以重写set方法 */
	private int					remberMeValidityPeriod;

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
			if(validateAutoLogin(loginForm, request)){
				 LOG.info("[MEM_AUTO_LOGIN ] {} [{}] \"\"", loginForm.getLoginName(), new Date());
				 memberCommand = memberManager.findMemberCommandByLoginName(loginForm.getLoginName());
			}else{
				LOG.info("[MEM_AUTO_LOGIN_FAILED ] {} [{}] \"Will check password later\"", loginForm.getLoginName(), new Date()); 
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
			
			//处理RemeberMe和AutoLogin(这两个东西需要分开来，因此是两个不同的Cookie Key) 
			//这里处理的逻辑需要注意以下几点：			
			//1. 如果RemeberMe被设置，那么需要重新设置RemeberMe的用户名，有效期需要配置 
			//2.如果AutoLogin被设置，那么需要重新设置AutoLogin的用户名，有效期需要配置 
			doRememberMeProcess(loginForm, memberCommand.getId(), request, response, model);
			
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
	 * @param loginForm
	 * @param request
	 * @param response
	 * @param model
	 * @author 冯明雷
	 * @time 2016年3月30日上午11:09:29
	 */
	protected boolean validateAutoLogin(LoginForm loginForm,HttpServletRequest request){
		// cookie中保存的自动登录的key
		String pwdKey =getRememberInfo(request, COOKIE_KEY_AUTO_LOGIN);
		
		//cookie保存的用户名
		String loginName =getRememberInfo(request, COOKIE_KEY_REMEMBER_ME_USER);

		// cookie中保存的用户名和密码不能为空
		if (Validator.isNotNullOrEmpty(pwdKey) && Validator.isNotNullOrEmpty(loginName)) {
			//页面传来的密码必须和默认密码相同
			String defaultPwd=getDefaultPwd(pwdKey);
			if(loginForm.getPassword().equals(defaultPwd)){
				try{
					if (loginName.equals(loginForm.getLoginName())) {
						// 根据用户名查询该用户修改密码的最后时间，用来判断记住的密码是否过期，和解密cookie中保存的密码的key
						MemberPersonalData memberPersonalData = memberExtraManager.findMemPersonalDataByLoginName(loginName);
						if (memberPersonalData != null) {
							String short4 = memberPersonalData.getShort4();
							
							String checkNum = EncryptUtil.getInstance().encrypt(EncryptUtil.getInstance().hash(loginName + short4, HelixConfig.getInstance().get("remberme.pwd.salt")));
							// 如果验证通过，去根据用户名查询密码
							return pwdKey.equals(checkNum);
						}
					}
				}catch (Exception e){
					LOG.info("[VALIDATE_AUTOLOGIN_FAIL] {} [{}] \"{}\"", loginForm.getLoginName(), new Date(), e.getClass().getSimpleName());
				}
			}else{
				LOG.info("[VALIDATE_AUTOLOGIN_FAIL] {} [{}] \"The password is different from the default password and page\"", loginForm.getLoginName(), new Date());
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
			String loginName = getRememberInfo(request, COOKIE_KEY_REMEMBER_ME_USER);
			if (Validator.isNotNullOrEmpty(loginName)) {
				memberLoginViewCommand.setIsRememberMe(true);
				memberLoginViewCommand.setLoginName(loginName);
			}

			if(isSupportAutoLogin()){
				//从cooike中获取是否自动登录的key
				String pwdKey = getRememberInfo(request, COOKIE_KEY_AUTO_LOGIN);
				if (Validator.isNotNullOrEmpty(pwdKey)) {
					memberLoginViewCommand.setIsAutoLogin(true);
					String defaultPwd=getDefaultPwd(CookieUtil.getCookie(request,COOKIE_KEY_AUTO_LOGIN).getValue());
					memberLoginViewCommand.setPassword(defaultPwd);
				}
			}
		}

	}

	/**
	 * 从cookie中获取记住用户名的信息
	 * 
	 * @return String
	 * @param request
	 * @param key
	 * @author 冯明雷
	 * @time 2016年3月29日下午6:17:31
	 */
	protected String getRememberInfo(HttpServletRequest request,String key){
		Cookie cookie = CookieUtil.getCookie(request, key);
		// 如果cookie不为空的话
		if (Validator.isNotNullOrEmpty(cookie)) {
			String value = cookie.getValue();
			try{
				value = EncryptUtil.getInstance().decrypt(value);
				return value;
			}catch (Exception e){
				LOG.info("[COOKIE_VALUE_DECRYPT_FAIL] {} [{}] \"The cookie value decrypt fail.\"", value, new Date());
			}
		}
		return null;
	}
	
	/**
	 * 获取默认用户名,根据从cookie中获取的pwk的值，取前八位当做默认密码
	 * @return String
	 * @param pwdKey
	 * @author 冯明雷
	 * @time 2016年3月30日上午9:45:21
	 */
	protected String getDefaultPwd(String pwdKey){
		return pwdKey.substring(0, 7);
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
			Model model){
		// 先清除记住的用户名和密码
		CookieUtil.deleteCookie(COOKIE_KEY_REMEMBER_ME_USER, response);
		CookieUtil.deleteCookie(COOKIE_KEY_AUTO_LOGIN, response);

		if (loginForm.getIsRemberMeLoginName()) {
			try{
				// 设置登录名的cookie值
				CookieUtil.addCookie(COOKIE_KEY_REMEMBER_ME_USER,remberMeValueEncrypt(loginForm.getLoginName()),getRemberMeValidityPeriod(),response);

				//设置密码对应的key的cookie的值,获取当前时间的毫秒数加上登录名和一个配置的key进行加密存储
				if (loginForm.getIsRemberMePwd()) {
					String timestamp = String.valueOf(new Date().getTime());
					memberExtraManager.rememberPwd(memberId, timestamp);
					String checkNum = EncryptUtil.getInstance().hash(loginForm.getLoginName() + timestamp, HelixConfig.getInstance().get("remberme.pwd.salt"));
					CookieUtil.addCookie(COOKIE_KEY_AUTO_LOGIN,remberMeValueEncrypt(checkNum),getRemberMeValidityPeriod(),response);
					
					LOG.info("[REMEMBER_ME_PROCESS_SUCCESS] {} [{}]", loginForm.getLoginName(), new Date());
				}
			}catch (Exception e){
				LOG.info("[REMEMBER_ME_PROCESS_FAILURE] {} [{}] \"{}\"", loginForm.getLoginName(), new Date(), e.getClass().getSimpleName());
			}
			
		}

	}
	

	/**
	 * remember me 值的加密处理
	 * 
	 * @param value
	 * @return 默认原值返回
	 * @throws EncryptionException 
	 */
	protected String remberMeValueEncrypt(String value) throws EncryptionException{
		return EncryptUtil.getInstance().encrypt(value);
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
		if(Validator.isNullOrEmpty(remberMeValidityPeriod)){
			return 30 * 24 * 60 * 60;
		}		
		return remberMeValidityPeriod;
	}

	
	/**
	 * set remberMeValidityPeriod 
	 * @param remberMeValidityPeriod
	 */
	public void setRemberMeValidityPeriod(int remberMeValidityPeriod){
		this.remberMeValidityPeriod = remberMeValidityPeriod;
	}

}
