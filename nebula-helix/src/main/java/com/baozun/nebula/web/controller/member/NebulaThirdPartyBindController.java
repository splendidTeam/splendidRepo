
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
import com.baozun.nebula.constant.ThirdPartyLoginBindConstants;
import com.baozun.nebula.exception.LoginException;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.member.ThirdPartyMemberManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.LoginForm;
import com.baozun.nebula.web.controller.member.form.RegisterForm;
import com.baozun.nebula.web.controller.member.validator.LoginFormValidator;
import com.baozun.nebula.web.controller.member.validator.RegisterFormMobileValidator;
import com.baozun.nebula.web.controller.member.validator.RegisterFormNormalValidator;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestUtil;


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
 * @author yufei.kong
 * @version 1.0
 * @time 2016年4月1日 15:41:33
 */
public class NebulaThirdPartyBindController extends NebulaAbstractLoginController{

	/**
	 * log定义
	 */
	private static final Logger	LOG								= LoggerFactory.getLogger(NebulaThirdPartyBindController.class);

	/* bind Page 的默认定义 */
	public static final String	VIEW_THIRDPARTY_MEMBER_BIND				= "member.thirdParty.bind";
	
	/* bindSuccess Page 的默认定义 */
	public static final String	VIEW_THIRDPARTY_MEMBER_BIND_SUCCESS		= "member.thirdParty.bindSuccess";
	
	/* bindfailure Page 的默认定义 */
	public static final String	VIEW_THIRDPARTY_MEMBER_BIND_FAILURE		= "member.thirdParty.bindFailure";

	
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
	private SdkMemberManager	sdkMemberManager;
	
	/**
	 * 第三方绑定业务类
	 */
	@Autowired
	private ThirdPartyMemberManager thirdPartyMemberManager;
	
	/**
	 * Mobile <br/>
	 * 会员注册Form的校验器
	 */
	@Autowired
	@Qualifier("registerFormMobileValidator")
	private RegisterFormMobileValidator	registerFormMobileValidator;
	
	/**
	 * PC || Tablet <br/>
	 * 会员注册Form的校验器
	 */
	@Autowired
	@Qualifier("registerFormNormalValidator")
	private RegisterFormNormalValidator	registerFormNormalValidator;
	

	/**
	 * 进去绑定页面	
	 * @RequestMapping(value = "/member/showBind", method = RequestMethod.GET)
	 * @param request
	 * @param response
	 * @param model
	 */
	public String showBind(HttpServletRequest request,HttpServletResponse response,Model model){
		return VIEW_THIRDPARTY_MEMBER_BIND;
	}

	/**
	 * 绑定处理(已有商城用户)
	 * <ol>
	 * <li>首先需要第三方用户登录</li>
	 * <li>其次会进行商城登录数据的校验</li>
	 * <li>校验用户名和密码是否正确</li>
	 * <li>校验成功后,绑定第三方和商城账户,并保存行为数据,重新构建MemberDetails以及Status</li>
	 * <li>登录失败或校验失败，返回错误信息,错误码详见ThirdPartyLoginBindConstants</li>
	 * <li>{@link com.baozun.nebula.constant.ThirdPartyLoginBindConstants}</li>
	 * </ol>
	 * 
	 * @needLogin 需要第三方用户登录
	 * 
	 * @RequestMapping(value = "/member/bindThirdPartyMemberWithStoreMember", method = RequestMethod.POST)
	 * 
	 * @param type 页面传来的参数，表示是什么第三方绑定
	 * @param memberDetails 第三方登录用户
	 * @param loginForm 页面传来的参数，主要有用户名、密码、是否记住用户名、是否记住密码等
	 * @param bindingResult validate的校验结果
	 * @param request
	 * @param response
	 * @param model
	 * @return NebulaReturnResult 返回结果
	 */
	public String bindThirdPartyMemberWithStoreMember(
			@RequestParam String type,
			@LoginMember MemberDetails memberDetails,
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
			getResultFromBindingResult(bindingResult);
		}
		
		//处理Form中数据的加解密
		loginForm.setLoginName(decryptSensitiveDataEncryptedByJs(loginForm.getLoginName(), request));
		loginForm.setPassword(decryptSensitiveDataEncryptedByJs(loginForm.getPassword(), request));
		
		//此后使用 loginForm.toMemberCommand 就可以获得业务层模型了
		MemberCommand memberCommand = null;		
		
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
			String resultCode=thirdPartyMemberManager.bindThirdPartyLoginAccount(memberDetails.getMemberId(),memberCommand.getId(),type);
			model.addAttribute("resultCode", resultCode);
			super.onAuthenticationSuccess(constructMemberDetails(memberCommand), request, response); 
		}else{
			//登录失败的处理 
			LOG.debug("{} login failure", loginForm.getLoginName());
			model.addAttribute("resultCode", ThirdPartyLoginBindConstants.LOGINNAME_PWD_ERROR);
			return VIEW_THIRDPARTY_MEMBER_BIND_FAILURE;
		}
		
		return VIEW_THIRDPARTY_MEMBER_BIND_SUCCESS;
	}
	
	
	
	
	/**
	 * 绑定处理(没有商城账户,需重新注册)
	 * <ol>
	 * <li>首先需要第三方用户登录</li>
	 * <li>其次会进行商城账户注册</li>
	 * <li>注册成功后,绑定第三方和商城账户,并保存行为数据,重新构建MemberDetails以及Status</li>
	 * </ol>
	 * 
	 * @needLogin 需要第三方用户登录
	 * 
	 * @RequestMapping(value = "/member/bindThirdPartyMemberWithOutStoreMember", method = RequestMethod.POST)
	 * 
	 * @param type 页面传来的参数，表示是什么第三方绑定
	 * @param memberDetails 第三方登录用户
	 * @param registerForm 页面传来的参数，主要有用户名、密码等
	 * @param bindingResult validate的校验结果
	 * @param request
	 * @param response
	 * @param model
	 * @return NebulaReturnResult 返回结果
	 */
	public String bindThirdPartyMemberWithOutStoreMember(
			@RequestParam String type,
			@LoginMember MemberDetails memberDetails,
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
			return VIEW_THIRDPARTY_MEMBER_BIND_FAILURE;
		}

		// 密码解密，密码传输通过RSA做了加密，此处需要解密
		registerForm.setPassword(decryptSensitiveDataEncryptedByJs(registerForm.getPassword(), request));

		MemberFrontendCommand memberFrontendCommand = registerForm.toMemberFrontendCommand();

		// 这个地方的方法应该抽取公用方法
		NebulaRegisterController nc = new NebulaRegisterController();
		/** 检查email，mobile等是否合法 */
		defaultReturnResult = (DefaultReturnResult) nc.checkRegisterData(memberFrontendCommand, request, response);

		if (!defaultReturnResult.isResult()) {
			return VIEW_THIRDPARTY_MEMBER_BIND_FAILURE;
		}

		/** 设置注册会员附加信息 */
		setupMemberReference(memberFrontendCommand, request);

		/** 用户注册 */
		Member member = memberManager.rewriteRegister(memberFrontendCommand);

		// member convert to memberCommand
		MemberCommand memberCommand = (MemberCommand) ConvertUtils.convertTwoObject(new MemberCommand(), member);

		/**
		 * 构造MemberDetails<br/>
		 * 此时如果注册需要‘邮件激活’等功能，需要商城端设置 MemberCommand.status
		 */
		MemberDetails storeMemberDetails = constructMemberDetails(memberCommand);

		// 给发送激活邮件使用
		request.getSession().setAttribute(SessionKeyConstants.MEMBER_REG_MEMBID, storeMemberDetails.getMemberId());
		request.getSession().setAttribute(SessionKeyConstants.MEMBER_REG_EMAIL_URL, storeMemberDetails.getLoginEmail());

		LOG.debug("{} login success", memberCommand.getLoginName());
		String resultCode = thirdPartyMemberManager.bindThirdPartyLoginAccount(memberDetails.getMemberId(),memberCommand.getId(), type);
		model.addAttribute("resultCode", resultCode);
		super.onAuthenticationSuccess(memberDetails, request, response);

		return VIEW_THIRDPARTY_MEMBER_BIND_SUCCESS;
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
		// 来源：自第三方注册会员
		memberFrontendCommand.setSource(Member.MEMBER_TYPE_THIRD_PARTY_MEMBER);
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
}
