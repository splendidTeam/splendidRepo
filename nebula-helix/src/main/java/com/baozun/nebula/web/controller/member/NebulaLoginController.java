
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

import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.exception.LoginException;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.manager.captcha.CaptchaUtil;
import com.baozun.nebula.manager.captcha.entity.CaptchaContainerAndValidateConfig;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.LoginForm;
import com.baozun.nebula.web.controller.member.validator.LoginFormValidator;
import com.baozun.nebula.web.controller.member.viewcommand.MemberLoginViewCommand;
import com.feilong.core.Validator;
import com.feilong.servlet.http.CookieUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.servlet.http.entity.CookieEntity;


/**
 * 登录相关方法controller
 * 
 * <ol>
 * <li>{@link #showLogin(memberDetails,request,respons,model)} 进入登录页面</li>
 * <li>{@link #login(loginForm,bindingResult,request,response,model)}登录方法</li>
 * <li>{@link #loginOut(request,response,model)}退出登录方法</li>
 * </ol>
 * 
 * <h3>showLogin方法,主要有以下几点:</h3>
 * <blockquote>
 * <ol>
 * <li>设置回填的用户名和密码;</li>
 * <li>将js加密使用的公钥传入页面;</li>
 * <li>如果memberDetails不为null视为登录用户，默认还是可以进入登录页的，商城可以重写 {@link #getShowPage4LoginedUserViewLoginPage},来决定登录用户是否可以进入登录页</li>
 * </ol>
 * </blockquote>
 * 
 * <h3>login方法,主要有以下几点:</h3>
 * <blockquote>
 * <ol>
 * <li>校验页面传来的参数，主要是非空校验;</li>
 * <li>对页面传来的用户名、密码进行解密;</li>
 * <li>判断是否支持自动登录，如果支持校验是否可以自动登录;</li>
 * <li>如果不支持自动登录或自动登录校验失败，走正常的登录流程，校验用户名密码是否正确等;</li>
 * <li>登录成功后处理是否记住用户名和密码;</li>
 * <li>登录成功的后续操作，包括返回页面、重置session等</li>
 * </ol>
 * </blockquote>
 * 
 * <h3>loginOut方法,主要有以下几点:</h3>
 * <blockquote>
 * <ol>
 * <li>清空session;</li>
 * <li>登出成功的后续操作，商城可以重写 {@link #onLogoutSuccess}方法</li>
 * </ol>
 * </blockquote>
 * 
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年3月31日  下午2:37:59
 */
public class NebulaLoginController extends NebulaAbstractLoginController{

	/**
	 * log定义
	 */
	private static final Logger						LOG								= LoggerFactory.getLogger(NebulaLoginController.class);

	/* Remember me user cookie key */
	public static final String						COOKIE_KEY_REMEMBER_ME_USER		= "rmbu";

	/* Remember me pwd cookie key */
	public static final String						COOKIE_KEY_AUTO_LOGIN			= "rmbc";

	/* Login Page 的默认定义 */
	public static final String						VIEW_MEMBER_LOGIN				= "member.login";

	/* Login 登录ID */
	public static final String						MODEL_KEY_MEMBER_LOGIN_ID		= "login";

	/* Login 登录密码 */
	public static final String						MODEL_KEY_MEMBER_LOGIN_PWD		= "password";	
	
	/** 默认的记住用户名cookie有效期，商城可以重写set方法 */
	private int										remberMeValidityPeriod 			= -1;

	/**
	 * 会员登录Form的校验器
	 */
	@Autowired
	@Qualifier("loginFormValidator")
	private LoginFormValidator						loginFormValidator;
	
	/**
	 * 验证码校验器
	 */
	@Autowired(required = false)
	@Qualifier("loginCaptchaContainerAndValidateConfig")
	private CaptchaContainerAndValidateConfig 		loginCaptchaContainerAndValidateConfig;

	/**
	 * 会员业务管理类
	 */
	@Autowired
	private MemberManager							memberManager;
	

	/**
	 * 进入登录页面 	
	 * <ol>
	 * <li>判断是否记住用户名、密码，如果记住传递到页面进行回填</li>
	 * <li>把js要用到的加密使用的公钥出入页面</li>
	 * <li>根据memberDetails判断用户是否登录，确定是否进入登录页面</li>
	 * </ol>
	 * @RequestMapping(value = "/member/login", method = RequestMethod.GET)
	 * @param memberDetails
	 *            用户未登录的情况下进入该页面该值将为空，主要用于已登录用户的页面跳转处理
	 * @param request
	 * @param response
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
	 * 登录处理
	 * <ol>
	 * <li>首先会进行数据的校验</li>
	 * <li>校验成功后，判断是否支持自动登录，如果支持 去校验自动登录是否成功</li>
	 * <li>如果不支持自动登录或自动登录校验不成功，走正常的登录流程，校验用户名和密码是否正确</li>
	 * <li>登录成功后判断是否记住用户名密码，然后继续重置session等动作</li>
	 * <li>登录失败或校验失败，返回错误信息的key，在js当中进行国际化</li>
	 * </ol>
	 * @RequestMapping(value = "/member/login.json", method = RequestMethod.POST)
	 * @param loginForm 页面传来的参数，主要有用户名、密码、是否记住用户名、是否记住密码等
	 * @param bindingResult validate的校验结果
	 * @param request
	 * @param response
	 * @param model
	 * @return NebulaReturnResult 返回结果
	 */
	public NebulaReturnResult login(
			@ModelAttribute LoginForm loginForm,
			BindingResult bindingResult,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model){
		
		//校验验证码
		boolean result = CaptchaUtil.validate(loginCaptchaContainerAndValidateConfig, request);
		if (!result){
			//因为目前都还是密文，所以可以直接Debug输出 
			LOG.debug("loginForm captcha validate fail. [{}/{}]", loginForm.getLoginName(), loginForm.getPassword());			
			DefaultReturnResult returnResult = new DefaultReturnResult();
			returnResult.setResult(false);
			DefaultResultMessage message = new DefaultResultMessage();
			message.setMessage("login.captcha.error");
			returnResult.setResultMessage(message);
			return returnResult;
        }
		
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
			//根据用户名查询memberCommand,用来判断自动登录保存的密码hash值是否正确
			memberCommand = memberManager.findMemberCommandByLoginName(loginForm.getLoginName());
			
			//这里需要检查AutoLogin，检查的方法是检查解密后的Password和之前提供的随机Hash是否一致
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
				MemberFrontendCommand memberFrontendCommand = loginForm.toMemberFrontendCommand();
				
				//用户行为信息
				MemberConductCommand memberConductCommand=new MemberConductCommand(new Date(), RequestUtil.getClientIp(request));
				memberFrontendCommand.setMemberConductCommand(memberConductCommand);
				
				memberCommand=memberManager.login(memberFrontendCommand);
			} catch (LoginException e) {
				LOG.info("[MEM_LOGIN_FAILURE] {} [{}] \"{}\"", loginForm.getLoginName(), new Date(), e.getClass().getSimpleName());
			}
		}
		
		if(loginCaptchaContainerAndValidateConfig != null && loginCaptchaContainerAndValidateConfig.getCaptchaContainer() != null){
			//登录成功后，清空登录失败次数,登录失败记录失败次数，用来判断验证码显示 memberCommand!= null 为true代表登录成功
			CaptchaUtil.clearOrIncrTryNumber(loginCaptchaContainerAndValidateConfig.getCaptchaContainer().getId(), loginForm.getLoginName(), memberCommand!= null, request);
		}		
		
		if(memberCommand!= null){
			//登录成功的处理
			LOG.debug("{} login success", memberCommand.getLoginName());
			
			//处理RemeberMe和AutoLogin
			doRememberMeProcess(loginForm, memberCommand, request, response, model);
			
			return onAuthenticationSuccess(constructMemberDetails(memberCommand,request), request, response); 
		}else{
			//登录失败的处理 
			LOG.debug("{} login failure", loginForm.getLoginName());
			DefaultReturnResult returnResult = new DefaultReturnResult();
			returnResult.setResult(false);
			DefaultResultMessage message = new DefaultResultMessage();
			message.setMessage("login.failure");
			returnResult.setResultMessage(message);
			return returnResult;
		}
	}

	/**
	 * 登出，主要是重置session
	 * 
	 * @RequestMapping(value = "/member/loginOut.json", method = RequestMethod.POST)
	 * @param request
	 * @param response
	 * @return
	 */
	public NebulaReturnResult loginOut(HttpServletRequest request,HttpServletResponse response,Model model){
		// 1.清空session中保存的用户信息
		request.getSession().removeAttribute(SessionKeyConstants.MEMBER_CONTEXT);
		
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
			if(loginForm.getPassword().equals(autoLoginPassword) &&loginName.equals(loginForm.getLoginName())){
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
	 * 进入登录页面的时候记住用户名和密码的操作<br/>
	 * 判断是否支持记住用户名，如果支持将cookie中存的用户名去除并解密传到页面<br/>
	 * 判断是否支持自动登录，如果支持将cookie中存储的关于密码的hash值解密传到页面
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
			
			//如果loginName不为空
			if (Validator.isNotNullOrEmpty(loginName)) {
				LOG.debug("RememberMe information is loaded: [User:{}]", loginName);
				memberLoginViewCommand.setIsRememberMe(true);
				memberLoginViewCommand.setLoginName(loginName);
			}
		}
		
		//如果支持自动登录或记住用户名
		if(isSupportAutoLogin()){
			//如果支持记自动登录的话，必须要支持记住用户名
			assert isSupportRemeberMe() : "SupportAutoLogin can only be actived when SupportRemeberMe";
			
			//cookie中用户名不能为空，然后才会去获取cookie中的密码的hash
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
		//这里的memberCommand不能为空，在调用方法之前应该有判断
		assert memberCommand!=null : "This memberCommand cannot be empty.";		
		
		String pwd = EncryptUtil.getInstance().hash(memberCommand.getLoginName(), memberCommand.getPassword());
		
		//如果用户名和密码hash之后的值为null，抛出一个RuntimeException
		if(pwd==null)
			throw new RuntimeException("generateAutoLoginPassword Encrypt hash fail.");
		
		return pwd.substring(0,6);
	}
	
	/**
	 * 根据name从cookie中取取值，并且解密
	 * @return String
	 * @param request
	 * @param name
	 * @throws EncryptionException 
	 * @time 2016年3月31日下午2:34:58
	 */
	private String getEncryptedInformationFromCookie(HttpServletRequest request, String name) throws EncryptionException{
		String encValue = CookieUtil.getCookieValue(request, name);
		if(encValue != null){
			return EncryptUtil.getInstance().decrypt(name);
		}
		return null;
	}
	
	
	/**
	 * 将value值加密存储在cookie中
	 * @return void
	 * @param response
	 * @param name	存储的key
	 * @param value  存储的值
	 * @param maxAge  有效期
	 * @throws EncryptionException 
	 * @time 2016年3月31日下午2:35:42
	 */
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
		
		//是否记住了密码，如果记住密码默认记住用户名
		if(loginForm.getIsRemberMePwd()){
			//是否支持记住密码
			if(isSupportAutoLogin()){
				LOG.debug("Need update Both RemeberMe & AutoLogin information");
				//是的话直接在cookie中保存，加密后的用户名和加密后的pwd的hash值
				setRememberedLogin(response, loginForm.getLoginName());
				setAutoLoginPassword(response, memberCommand);
				LOG.info("[RMBER_ME_AUTOLOGIN_SET] {} [{}] \"\"", loginForm.getLoginName(), new Date());
			}else{
				//不支持的话，直接删除cookie中保存的用户名和密码
				LOG.warn("System don't support AutoLogin!");
				CookieUtil.deleteCookie(COOKIE_KEY_REMEMBER_ME_USER, response);
				CookieUtil.deleteCookie(COOKIE_KEY_AUTO_LOGIN, response);
			}			
		} else if(loginForm.getIsRemberMeLoginName()){
			//是否记住了用户名
			
			//是否支持记住用户名			
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
		return remberMeValidityPeriod >0 ? remberMeValidityPeriod : TimeInterval.SECONDS_PER_MONTH;
	}

	
	/**
	 * set remberMeValidityPeriod 
	 * @param remberMeValidityPeriod
	 */
	public void setRemberMeValidityPeriod(int remberMeValidityPeriod){
		this.remberMeValidityPeriod = remberMeValidityPeriod;
	}

}
