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

import java.util.Calendar;
import java.util.Date;
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

import com.baozun.nebula.constant.EmailConstants;
import com.baozun.nebula.constant.EmailType;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodesFoo;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.member.MemberEmailManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.EmailUtil;
import com.baozun.nebula.utils.ShopDateUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.constants.CommonUrlConstants;
import com.baozun.nebula.web.constants.Constants;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.BaseController;

import loxia.utils.DateUtil;

/**
 * 邮箱账户激活相关的控制器，里面主要控制如下操作：
 * sendRegEmail ：发送激活邮件
 * validEmailActiveUrl :验证激活
 * 
 * @author yufei.kong 2016.3.22
 *
 */
public class NebulaEmailAccountActivationController extends BaseController {

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

	/* 用户相关业务类 注入 */
	@Autowired
	private SdkMemberManager sdkMemberManager;
	
	@Autowired
	private MemberManager memberManager;
	
	@Autowired
	private MemberEmailManager memberEmailManager;
	
	/* jedis缓存业务类 注入 */
	@Autowired
	CacheManager cacheManager;


	/**
	 * 发送激活邮件,成功后跳转到提示用户激活页面,默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/sendRegEmail", method =RequestMethod.GET)
	 * 
	 * 获取邮件源地址,便于直接从邮件中跳转到该邮箱,具体在项目实施中自行使用
	 * model.addAttribute("sendEmail", e.getWebsite()); 
	 * 
	 * 激活邮件发送次数判断,初始设置次数999999 自行配置
	 * model.addAttribute(Constants.RESULTCODE,"numberErr");
	 * 
	 * 邮件发送间隔时间判断,初始设置2分钟 自行配置 
	 * model.addAttribute(Constants.RESULTCODE,"unEnoughTime");
	 * 邮件发送间隔不足时,具体差异时间
	 * model.addAttribute(EmailConstants.EMAIL_SEND_TIME_KEY,expiredSeconds);
	 * 
	 * @param memberDetails
	 * @param httpRequest
	 * @param httpResponse
	 * @param model
	 * @return
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
			String loginEmailKey="";
			String loginEmail = (String)httpRequest.getSession().getAttribute(loginEmailKey);
			MemberCommand member = sdkMemberManager.findMemberByLoginEmail(loginEmail);
			memberId = member.getId();
			email =  member.getLoginEmail();
		}
		LOG.info("valid or get loginEmail   end");
		
		//获取跳转地址
		EmailType e = EmailUtil.getEmailType(email);
		if(Validator.isNotNullOrEmpty(e)) {
			model.addAttribute("sendEmail", e.getWebsite());
		} else {
			model.addAttribute("sendEmail", "");
		}
		
		
		LOG.info("valid sendNumber start");
		Integer sendNumber = 0;
		// 激活次數判斷
		if(Validator.isNotNullOrEmpty(cacheManager.getValue(email+EmailConstants.NEBULA_MEMBER_REGISTER))){
			sendNumber = Integer.parseInt(cacheManager.getValue(email+EmailConstants.NEBULA_MEMBER_REGISTER));
			if(sendNumber>EmailConstants.SEND_EMAIL_NUMBER){
				LOG.info("cant't send email,sendNumber error");
				model.addAttribute(Constants.RESULTCODE,"numberErr");
				return VIEW_MEMBER_REGISTER_ACTIVE_EMAIL;
			}
		}
		
		LOG.info("valid sendNumber end");
		
		LOG.info("valid expiredTime start");
		//获取发送邮件间隔时间
		Integer timeSpan = EmailConstants.NEBULA_SEND_EMAIL_AGAIN_TIME_SPAN;
		Integer expiredSeconds = timeSpan * 60;
		
		//获取上一次发送时间
		Date expiredTime = null;
		if (httpRequest.getSession().getAttribute(EmailConstants.SEND_ACTIVE_EMAIL_EXPIRED_TIME) != null) { 
			 expiredTime = (Date)httpRequest.getSession().getAttribute(EmailConstants.SEND_ACTIVE_EMAIL_EXPIRED_TIME); 
		} 
		
		// 还未到再次发送时间，不进行发送 
		Date now = Calendar.getInstance().getTime(); 
		if (expiredTime != null && now.compareTo(expiredTime) > 0) { 
			 expiredSeconds = Long.valueOf((now.getTime() - expiredTime.getTime()) /1000).intValue();
			 if(expiredSeconds < timeSpan * 60){
				 model.addAttribute(Constants.RESULTCODE,"unEnoughTime");
				 model.addAttribute(EmailConstants.EMAIL_SEND_TIME_KEY,expiredSeconds);
				 LOG.info("cant't send email,expiredSeconds------------"+expiredSeconds);
				 return VIEW_MEMBER_REGISTER_ACTIVE_EMAIL;
			 }
		}
		
		LOG.info("valid expiredTime end");
		
		//调用方法,发送邮件
		LOG.info("begin sendActiveEmail");
		memberEmailManager.sendActiveEmail(memberId, httpRequest);
		// 發送一次加一次
		sendNumber++;
		
		// 過期時間
		Integer expireSeconds = EmailUtil.getEmailExpireSeconds();
		cacheManager.setValue(email +EmailConstants.NEBULA_MEMBER_REGISTER, sendNumber.toString(), expireSeconds);
		
		//将发送时间存入session 
		expiredTime = DateUtil.addMinutes(now, timeSpan);
		httpRequest.getSession().setAttribute(EmailConstants.SEND_ACTIVE_EMAIL_EXPIRED_TIME, expiredTime);
		
		return VIEW_MEMBER_REGISTER_ACTIVE_EMAIL;
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
			
			if (StringUtils.isBlank(registerComfirm)) {
				return "redirect:/index";
			} else {
				registerComfirm = EncryptUtil.getInstance().base64Decode(registerComfirm);
			}
			
			//解密
			String decrypt = EncryptUtil.getInstance().decrypt(registerComfirm);
			
			//获取链接中的参数
			List<String> paramList = EmailUtil.getRequestParams(decrypt);
			
			if (CollectionUtils.isEmpty(paramList)) {
				return "redirect:/index";
			}
			//获取用户信息
			MemberCommand member = memberManager.findMemberById(Long.valueOf(paramList.get(0)));
			if (Validator.isNullOrEmpty(member)) {
				throw new BusinessException(ErrorCodesFoo.member_not_exist);
			}
			
			Long memberId = member.getId();
			// 判斷帳號是否激活
			if (memberEmailManager.isMemberEmailActive(memberId)) {
				// 链接已激活
				model.addAttribute("result", "urlInvalid");
				LOG.info("valid register Email invalid");
				return VIEW_MEMEBER_ACTIVE_BACK;
			}
			boolean flag = ShopDateUtil.countTime(Long.valueOf(paramList.get(2)), new Date().getTime());
			if (!flag) {
				// 链接已经过期
				model.addAttribute("result", "urlUnInvalid");
				return VIEW_MEMEBER_ACTIVE_BACK;
			}
			
			// 验证激活链接
			memberManager.validEmailActiveUrl(memberId, paramList.get(1));
			//修改数据库字段
			MemberPersonalData personalData = sdkMemberManager.findMemberPersonData(memberId);
			personalData.setShort2(EmailConstants.EMAIL_ACTIVE_YES);
			sdkMemberManager.savePersonData(personalData);
			
			String returnUrl = CommonUrlConstants.DEFAULT_REDIRECT_URL;
			if (httpRequest.getSession().getAttribute(SessionKeyConstants.MEMBER_IBACK_URL) != null) {
				returnUrl = (String) httpRequest.getSession().getAttribute(SessionKeyConstants.MEMBER_IBACK_URL);
			}
			
			model.addAttribute("returnUrl", returnUrl);
			model.addAttribute("email", member.getLoginEmail());
			model.addAttribute("result", "success");
			memberEmailManager.sendRegsiterSuccessEmail(member.getLoginEmail(),personalData);
			
			LOG.info("valid register Email end");
			
			return VIEW_MEMEBER_ACTIVE_BACK;
		} catch (Exception e) {
			LOG.error("valid error",e);
			return "redirect:/member/login";
		}
	}

	
}
