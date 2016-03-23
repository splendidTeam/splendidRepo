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

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.baozun.nebula.web.controller.member.converter.MemberAddressViewCommandConverter;

/**
 * 邮箱账户激活相关的控制器，里面主要控制如下操作：
 * sendRegEmail ：发送激活邮件
 * 
 * 
 * @author yufei.kong 2016.3.22
 *
 */
public class NebulaEmailAccountActivationController extends BaseController {

	/**
	 * log 定义
	 */
	private static final Logger LOG = LoggerFactory.getLogger(NebulaEmailAccountActivationController.class);

	/* Model 对应的键值定义 */
	//public static final String MODEL_KEY_MEMBER_REGISTER_ACTIVE_EMAIL = "memberAddressList";

	/* 发送邮件后激活页面 */
	public static final String VIEW_MEMBER_REGISTER_ACTIVE_EMAIL = "member.register-activate-email";
	
	/*激活后返回页面*/
	public static final String VIEW_MEMEBER_ACTIVE_BACK="member.register-email-back";

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

	@Autowired
	@Qualifier("memberAddressViewCommandConverter")
	private MemberAddressViewCommandConverter memberAddressViewCommandConverter;

	/**
	 * 发送激活邮件,成功后跳转到提示用户激活页面,默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/sendRegEmail", method =RequestMethod.GET)
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
		
		LOG.info("[SEND_REGISTER_EMAIL] {} [{}] \"\"", memberDetails.getLoginEmail(), new Date());
	
		Long memberId = memberDetails.getMemberId();
		String email = memberDetails.getLoginEmail();
		//判断memberId是否为空 如为空通过session中存的loginemail获取email地址
		if(Validator.isNullOrEmpty(memberId)){
			//session中的key需和做注册的沟通,暂时自定义loginEmailKey↓
			String loginEmailKey="";
			String loginEmail = (String)httpRequest.getSession().getAttribute(loginEmailKey);
			MemberCommand member = sdkMemberManager.findMemberByLoginEmail(loginEmail);
			memberId = member.getId();
			email =  member.getLoginEmail();
		} 
		
		EmailType e = EmailUtil.getEmailType(email);
		if(Validator.isNotNullOrEmpty(e)) {
			model.addAttribute("sendEmail", e.getWebsite());
		} else {
			model.addAttribute("sendEmail", "");
		}
		
		Integer sendNumber = 0;
		// 激活次數判斷
		if(Validator.isNotNullOrEmpty(cacheManager.getValue(email+EmailConstants.NEBULA_MEMBER_REGISTER))){
			sendNumber = Integer.parseInt(cacheManager.getValue(email+EmailConstants.NEBULA_MEMBER_REGISTER));
			if(sendNumber>EmailConstants.SEND_EMAIL_NUMBER){
				model.addAttribute(Constants.RESULTCODE,"numberErr");
				return VIEW_MEMBER_REGISTER_ACTIVE_EMAIL;
			}
		}
		memberEmailManager.sendActiveEmail(memberId, httpRequest);
		// 發送一次加一次
		sendNumber++;
		
		// 過期時間
		Integer expireSeconds = EmailUtil.getEmailExpireSeconds();
		cacheManager.setValue(email +EmailConstants.NEBULA_MEMBER_REGISTER, sendNumber.toString(), expireSeconds);
		model.addAttribute("memberId", memberId);
		model.addAttribute("loginEmail", email);
		
		return VIEW_MEMBER_REGISTER_ACTIVE_EMAIL;
	}
	
	
	
	/**
	 * 激活验证,默认推荐配置如下
	 * @RequestMapping("/m/validEmailActiveUrl")
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
			
			String decrypt = EncryptUtil.getInstance().decrypt(registerComfirm);
			
			List<String> paramList = EmailUtil.getRequestParams(decrypt);
			if (CollectionUtils.isEmpty(paramList)) {
				return "redirect:/index";
			}
			
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
			MemberPersonalData personalData = sdkMemberManager.findMemberPersonData(memberId);
			personalData.setShort2(EmailConstants.EMAIL_ACTIVE_YES);
			sdkMemberManager.savePersonData(personalData);
			
			// 同步购物车、保存用户信息信息 TODO
			// 设置cookie中的购物车 TODO
			// 同步購物車數據後，刪除cookie TODO
			// 记录登录状态 TODO
			
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
