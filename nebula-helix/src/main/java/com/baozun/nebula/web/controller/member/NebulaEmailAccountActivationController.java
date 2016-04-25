/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.constant.EmailType;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodesFoo;
import com.baozun.nebula.manager.member.CommonEmailManager.SendEmailResultCode;
import com.baozun.nebula.manager.member.MemberEmailManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utils.EmailUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.constants.CommonUrlConstants;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.CharsetType;
import com.feilong.core.Validator;
import com.feilong.core.net.ParamUtil;

/**
 * 邮箱账户激活相关的控制器，里面主要控制如下操作：
 * sendRegEmail ：发送激活邮件
 * 
 * 
 * @author yufei.kong 2016.3.22
 *
 */
public class NebulaEmailAccountActivationController extends NebulaAbstractLoginController {

	/**
	 * log 定义
	 */
	private static final Logger LOG = LoggerFactory.getLogger(NebulaEmailAccountActivationController.class);

	/* 发送邮件后激活页面 */
	public static final String VIEW_MEMBER_REGISTER_ACTIVE_EMAIL = "member.register-activate-email";
	
	/*激活后返回页面*/
	public static final String VIEW_MEMEBER_ACTIVE_BACK="member.register-email-back";
	
	/* Login Page 的默认定义 */
	public static final String VIEW_MEMBER_LOGIN = "member.login";

	/* 用户sdk相关业务类 注入 */
	@Autowired
	private SdkMemberManager sdkMemberManager;
	
	/* 用户相关业务类 注入 */
	@Autowired
	private MemberManager memberManager;
	
	/* 用户邮件相关业务类 注入 */
	@Autowired
	private MemberEmailManager memberEmailManager;
	
	
	/**
	 * 发送激活邮件,成功后跳转到提示用户激活页面,默认推荐配置如下
	 * <ol>
	 * <li>由于可能出现不登录 也可以发送激活邮件的情况,memberDetails可能为空,所以需要验证</li>
	 * <li>当memberDetails为空时,获取注册成功之后存储于session中的email,通过email获取memberDetails</li>
	 * <li>model.addAttribute("sendEmail", e.getWebsite());将该邮箱的源地址传递到前台.便于直接跳转到该邮箱</li>
	 * <li>注册成功后,绑定第三方和商城账户,并保存行为数据,重新构建MemberDetails以及Status</li>
	 * </ol>
	 * 
	 * @RequestMapping(value = "/member/sendRegEmail", method =RequestMethod.GET)
	 * 
	 * @param memberDetails 
	 * @param request
	 * @param response
	 * @param model
	 */
	public String sendRegEmail(@LoginMember MemberDetails memberDetails,
							   HttpServletRequest httpRequest, 
							   HttpServletResponse httpResponse,
							   Model model) {
		
		LOG.info("valid or get loginEmail  start");
		Long memberId=null;
		String email="";
		//判断memberId是否为空 如为空通过session中存的loginemail获取email地址
		if(memberDetails!=null){
			memberId = memberDetails.getMemberId();
			email = memberDetails.getLoginEmail();
		}else{
			String loginEmail = (String)httpRequest.getSession().getAttribute(SessionKeyConstants.MEMBER_REG_EMAIL_URL);
			MemberCommand member = sdkMemberManager.findMemberByLoginEmail(loginEmail);
			memberId = member.getId();
			email =  member.getLoginEmail();
		}
		LOG.info("valid or get loginEmail   end");
		
		//拼接发送地址
		String path=getRegEmailValidPath(httpRequest);
		
		//调用方法,发送邮件
		LOG.info("begin sendActiveEmail");
		//发送邮件获取响应码 
		SendEmailResultCode resultCode=memberEmailManager.sendActiveEmail(memberId, path,email);
		
		//********************************************************************************
		//获取跳转地址
		EmailType e = EmailUtil.getEmailType(email);
		if(Validator.isNotNullOrEmpty(e)) {
			model.addAttribute("sendEmail", e.getWebsite());
		} else {
			model.addAttribute("sendEmail", "");
		}
		
		model.addAttribute("resultCode", resultCode);
		
		return VIEW_MEMBER_REGISTER_ACTIVE_EMAIL;
	}
	
	
	/**
	 * 拼接邮件链接
	 * 
	 * @param request
	 * @return
	 */
	// http://wwww/www/
	private String getRegEmailValidPath(HttpServletRequest request) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path
				+ "/m/validEmailActiveUrl";
		return basePath;
	}
	
	
	/**
	 * 激活验证,默认推荐配置如下
	 * @RequestMapping("/m/validEmailActiveUrl")
	 * 
	 * 账户已激活
	 * model.addAttribute("result", "urlInvalid");
	 * 
	 * 链接已过期
	 * model.addAttribute("result", "urlUnInvalid");
	 * 
	 * @param registerComfirm
	 * @param httpRequest
	 * @param httpResponse
	 * @param model
	 * @return
	 */
	public String validEmailActiveUrl(@RequestParam(required = true, value = "registerComfirm") String registerComfirm,
			 						  HttpServletRequest httpRequest, 
			 						  HttpServletResponse httpResponse,
			 						  Model model) {
		try {
			LOG.info("valid register Email start");
			//获取链接中的参数
			List<String> paramList =memberEmailManager.analysisTheUrl(registerComfirm);
			
			//判断参数列表是否为空  如果为空则返回首页
			if (CollectionUtils.isEmpty(paramList)) {
				return "redirect:/index";
			}
			
			//获取参数列表中的第一个参数 为memberid
			Long memberId=Long.valueOf(paramList.get(0));
			//获取用户信息
			MemberCommand member = memberManager.findMemberById(memberId);
			
			if (Validator.isNullOrEmpty(member)) {
				throw new BusinessException(ErrorCodesFoo.member_not_exist);
			}
			
			//调用激活方法
			memberEmailManager.activeMemberAccount(memberId);
			
			MemberPersonalData personalData = sdkMemberManager.findMemberPersonData(memberId);
			String returnUrl = CommonUrlConstants.DEFAULT_REDIRECT_URL;
			if (httpRequest.getSession().getAttribute(SessionKeyConstants.MEMBER_IBACK_URL) != null) {
				returnUrl = (String) httpRequest.getSession().getAttribute(SessionKeyConstants.MEMBER_IBACK_URL);
			}
			
			model.addAttribute("returnUrl", returnUrl);
			model.addAttribute("email", member.getLoginEmail());
			model.addAttribute("result", "success");
			
			//发送激活成功邮件
			memberEmailManager.sendRegsiterSuccessEmail(member.getLoginEmail(),personalData.getNickname());
			MemberDetails memberDetails=super.constructMemberDetails(member,httpRequest);
			onAuthenticationSuccess(memberDetails, httpRequest, httpResponse);
			
			LOG.info("valid register Email end");
			
			return VIEW_MEMEBER_ACTIVE_BACK;
		} catch (Exception e) {
			LOG.error("valid error",e);
			return "redirect:/member/login";
		}
	}

	
}
