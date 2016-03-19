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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.converter.MemberViewCommandConverter;
import com.baozun.nebula.web.controller.member.form.MemberProfileForm;
import com.baozun.nebula.web.controller.member.validator.MemberProfileFormValidator;

/**
 * 会员信息相关控制器，含显示会员信息和修改会员信息。
 * @author liuliu
 *
 */
public class NebulaMemberProfileController extends BaseController {
	
	/**
	 * log 定义
	 */
	private static final Logger log = LoggerFactory.getLogger(NebulaMemberProfileController.class);

	/* Model 对应的键值定义 */
	public static final String MODEL_KEY_MEMBER_PROFILE = "memberDetail";
	
	/* View 的默认定义*/
	public static final String VIEW_MEMBER_PROFILE = "member.profile";
	
	/**
	 * 会员业务管理类
	 */
	@Autowired
	private MemberManager memberManager;
	
	/**
	 * 会员信息Form的校验器
	 */
	@Autowired
	@Qualifier("memberProfileFormValidator")
	private MemberProfileFormValidator memberProfileFormValidator;
	
	/**
	 * 会员信息转换器
	 */
	@Autowired
	@Qualifier("memberViewCommandConverter")
	private MemberViewCommandConverter memberViewCommandConverter;
	
	/**
	 * 显示用户信息，默认推荐配置如下
	 * @RequestMapping(value = "/member/profile", method = RequestMethod.GET)
	 * @NeedLogin
	 * 可以通过重载方法在获取用户信息后增加额外操作，或读取额外信息
	 * 
	 * @param memberDetails
	 * @param httpRequest
	 * @param httpResponse
	 * @param model
	 * @return
	 */
	public String showMemberProfile(@LoginMember MemberDetails memberDetails, 
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model){		
		//因为有NeedLogin控制，进来的一定是已经登录的有效用户
		assert memberDetails != null : "Please Check NeedLogin Annotation";
		
		log.info("[MEM_VIEW_PROFILE] {} [{}] \"\"", memberDetails.getLoginName(), new Date());
		//获取会员信息
		MemberCommand memberCommand = memberManager.findMemberById(memberDetails.getMemberId());
		
		model.addAttribute(MODEL_KEY_MEMBER_PROFILE,
				memberViewCommandConverter.convertFromMemberCommand(memberCommand));
		
		return VIEW_MEMBER_PROFILE;
	}
	
	/**
	 * 更新用户头像，默认推荐配置如下
	 * @RequestMapping(value = "", method = RequestMethod.POST)
	 * @NeedLogin
	 * 
	 * @param memberDetails
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 */
	public NebulaReturnResult updatePortrait(@LoginMember MemberDetails memberDetails, 
			HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		//TODO 更新用户头像，需要一个通用的异步图片处理标准
		return DefaultReturnResult.SUCCESS;
	}
	
	/**
	 * 编辑用户信息，默认推荐配置如下
	 * @RequestMapping(value = "/member/profile/edit", method = RequestMethod.POST)
	 * @NeedLogin
	 * 
	 * @param memberDetails
	 * @param memberProfileForm
	 * @param httpRequest
	 * @param httpResponse
	 * @param bindingResult
	 * @return
	 */
	public NebulaReturnResult editMemberProfile(@LoginMember MemberDetails memberDetails, 
			@ModelAttribute("memberProfile") MemberProfileForm memberProfileForm,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			BindingResult bindingResult){
		//因为有NeedLogin控制，进来的一定是已经登录的有效用户
		assert memberDetails != null : "Please Check NeedLogin Annotation";
				
		log.info("[MEM_EDIT_PROFILE] {} [{}] \"Start\"", memberDetails.getLoginName(), new Date());
		
		log.debug("Start to check input profile");
		
		//TODO 这里需要一个标准化的校验流程，和校验失败后的消息处理过程（是否和DefaultReturnResult整合？）
		//memberProfileFormValidator.validate(memberProfileForm, bindingResult);
		log.debug("input profile is validated");
		
		//获取会员信息
		MemberCommand memberCommand = memberManager.findMemberById(memberDetails.getMemberId());
		MemberPersonalData memberProfile = memberManager.findMemberPersonData(memberDetails.getMemberId());
		
		//TODO 这里需要通过Form和会员信息来判断这些关键信息是否变化
		boolean isPasswordChange = false;
		boolean isEmailChange = false;		
		//TODO 这里会把需要修改的值都设置到 memberProfile 中，请确保所有值都成功存储
		
		memberManager.savePersonData(memberProfile);
		
		if(isPasswordChange){
			//TODO 保存重设密码
			postProcessForPasswordChange();
			log.info("[MEM_EDIT_PROFILE] {} [{}] \"[PASSWORD_CHANGED]\"", memberDetails.getLoginName(), new Date());
		}
		if(isEmailChange){
			postProcessForEmailChange();
			log.info("[MEM_EDIT_PROFILE] {} [{}] \"[EMAIL_CHANGED]\"", memberDetails.getLoginName(), new Date());
		}
		
		log.info("[MEM_EDIT_PROFILE] {} [{}] \"Finished\"", memberDetails.getLoginName(), new Date());
		
		return DefaultReturnResult.SUCCESS;
	}
	
	protected void postProcessForPasswordChange(){
		
	}
	
	protected void postProcessForEmailChange(){
		//如果Email同时是登录名，可能需要考虑重新检查Email可用性，Email不可用之前账号会被禁用
	}
}
