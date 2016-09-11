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

import java.io.File;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.converter.MemberViewCommandConverter;
import com.baozun.nebula.web.controller.member.form.MemberProfileForm;
import com.baozun.nebula.web.controller.member.validator.MemberProfileFormValidator;
import com.baozun.nebula.web.controller.member.viewcommand.MemberViewCommand;
import com.feilong.core.Validator;

/**
 * 会员信息相关控制器，含显示会员信息和修改会员信息。
 * 
 * @author Benjamin.Liu
 * 
 */
public class NebulaMemberProfileController extends BaseController {

	/**
	 * log 定义
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(NebulaMemberProfileController.class);

	/* Model 对应的键值定义 */
	public static final String MODEL_KEY_MEMBER_PROFILE = "memberDetail";

	/* View 的默认定义 */
	public static final String VIEW_MEMBER_PROFILE = "myAccount.profile";

	/* 配置用户头像上传配置文件路径 */
	protected String CONFIG = "config/metainfo.properties";

	/* 读取配置文件中配置的用户头像路径 */
	protected String MEMBER_HEAD_IMAGE = ProfileConfigUtil.findPro(CONFIG).getProperty("upload.img.base");

	/**
	 * 会员业务管理类
	 */
	@Autowired
	private MemberManager memberManager;

	@Autowired
	private SdkMemberManager sdkMemberManager;
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
	 * 
	 * @RequestMapping(value = "/member/profile", method = RequestMethod.GET)
	 * @NeedLogin 可以通过重载方法在获取用户信息后增加额外操作，或读取额外信息
	 * 
	 * @param memberDetails
	 * @param httpRequest
	 * @param httpResponse
	 * @param model
	 * @return
	 */
	public String showMemberProfile(@LoginMember MemberDetails memberDetails,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			Model model) {
		// 因为有NeedLogin控制，进来的一定是已经登录的有效用户
		assert memberDetails != null : "Please Check NeedLogin Annotation";
		LOG.info("[MEM_VIEW_PROFILE] {} [{}] \"show LoginName\"",
				memberDetails.getLoginName(), new Date());

		// 获取会员信息
		MemberCommand memberCommand = memberManager.findMemberById(memberDetails.getMemberId());

		// 将MemberCommand对象数据全部转入MemberViewCommand中
		MemberViewCommand memberViewCommand = memberViewCommandConverter.convert(memberCommand);

		LOG.info("[MEM_VIEW_PROFILE] {} [{}] \"copy MemberCommand Properties to MemberViewCommand\"",memberViewCommand, new Date());

		MemberPersonalData findMemberPersonData = sdkMemberManager.findMemberPersonData(memberDetails.getMemberId());
	    memberViewCommand.setRealName(findMemberPersonData.getNickname());
	    memberViewCommand.setSex(findMemberPersonData.getSex());
		model.addAttribute(MODEL_KEY_MEMBER_PROFILE, memberViewCommand);

		return VIEW_MEMBER_PROFILE;
	}

	/**
	 * 更新用户头像，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/upload/memberPortrait", method = RequestMethod.POST)
	 * @NeedLogin
	 * 
	 * @param memberDetails
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 */
	public NebulaReturnResult updatePortrait(
			@LoginMember MemberDetails memberDetails,
			@RequestParam("fileData") MultipartFile multipartFile,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

		// 因为有NeedLogin控制，进来的一定是已经登录的有效用户
		assert memberDetails != null : "Please Check NeedLogin Annotation";

		// 更新用户头像，需要一个通用的异步图片处理标准
		httpResponse.setContentType("text/html;charset=UTF-8");
		String imageZipPath = MEMBER_HEAD_IMAGE;

		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;
		DefaultResultMessage defaultResultMessage = new DefaultResultMessage();

		// 文件如果是空或者大小为0反馈失败
		if (multipartFile.isEmpty()) {
			defaultReturnResult.setResult(false);
			defaultResultMessage.setMessage("memberPortrait.isEmpty");
			defaultReturnResult.setResultMessage(defaultResultMessage);
			return defaultReturnResult;
		}

		// 上传图片
		String fileName = multipartFile.getOriginalFilename();
		String ranStr = memberDetails.getLoginName()
				+ System.currentTimeMillis();
		if (!multipartFile.isEmpty()) {
			File imageDir = new File(imageZipPath);
			if (!imageDir.exists()) {
				imageDir.mkdirs();
			}
			File targetFile = new File(imageZipPath + ranStr);
			try {
				multipartFile.transferTo(targetFile);
			} catch (Exception e) {
				LOG.error(e.getMessage());
				defaultReturnResult.setResult(false);
				defaultResultMessage.setMessage(getMessage("updatePortrait.error"));
				defaultReturnResult.setResultMessage(defaultResultMessage);
				return defaultReturnResult;
			}
		}
		return defaultReturnResult;
	}

	/**
	 * 编辑用户信息，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/profile/edit", method =
	 *                       RequestMethod.POST)
	 * @NeedLogin
	 * 
	 * @param memberDetails
	 * @param memberProfileForm
	 * @param httpRequest
	 * @param httpResponse
	 * @param bindingResult
	 * @return
	 */
	public NebulaReturnResult editMemberProfile(
			@LoginMember MemberDetails memberDetails,
			@ModelAttribute("memberProfile") MemberProfileForm memberProfileForm,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			BindingResult bindingResult) {
		// 因为有NeedLogin控制，进来的一定是已经登录的有效用户
		assert memberDetails != null : "Please Check NeedLogin Annotation";

		LOG.info("[MEM_EDIT_PROFILE] {} [{}] \"Start edit MemberProfile\"",
				memberDetails.getLoginName(), new Date());

		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;
		DefaultResultMessage defaultResultMessage = new DefaultResultMessage();

		//MemberProfileForm Password可能为两种情况：1，为修改即为MD5+RSA加密值；2，修改密码之后，为明文+RSA加密值
		memberProfileForm.setPassword(decryptSensitiveDataEncryptedByJs(memberProfileForm.getPassword(), httpRequest));
		if(Validator.isNotNullOrEmpty(memberProfileForm.getRepassword())){
			memberProfileForm.setRepassword(decryptSensitiveDataEncryptedByJs(memberProfileForm.getRepassword(), httpRequest));
		}
		//oldPassword不为空时，即标示在修改密码
		if(Validator.isNotNullOrEmpty(memberProfileForm.getOldPassword())){
			memberProfileForm.setOldPassword(decryptSensitiveDataEncryptedByJs(memberProfileForm.getOldPassword(), httpRequest));
		}
		
		LOG.debug("Start Validation input profile");
		// 这里需要一个标准化的校验流程，和校验失败后的消息处理过程（是否和DefaultReturnResult整合？）
		memberProfileFormValidator.validate(memberProfileForm, bindingResult);
		if (bindingResult.hasErrors()) {
			LOG.info("[MEM_EDIT_PROFILE] {} [{}] \"Validator memberProfileForm has Error\"",
					memberDetails.getLoginName(), new Date());
			defaultReturnResult.setResult(false);
			defaultResultMessage.setMessage(getMessage(bindingResult.getAllErrors().get(0).getDefaultMessage()));
			defaultReturnResult.setResultMessage(defaultResultMessage);
			return defaultReturnResult;
		}
		LOG.debug("input profile is validated");

		// 获取会员信息
		MemberCommand memberCommand = memberManager
				.findMemberById(memberDetails.getMemberId());
		MemberPersonalData memberProfile = sdkMemberManager
				.findMemberPersonData(memberDetails.getMemberId());
		
		String encodeNewPassword = memberProfileFormValidator.validatePassword(memberProfileForm, memberCommand, bindingResult);
		
		memberProfile = memberProfileForm.toMemberPersonalData(memberProfile);

		// 这里需要通过Form和会员信息来判断这些关键信息是否变化
		boolean isPasswordChange = false;
		boolean isEmailChange = false;
		boolean isMobileChange = false;

		if (Validator.isNotNullOrEmpty(encodeNewPassword) && !memberCommand.getPassword().equals(encodeNewPassword)) {
			LOG.debug("memberProfile update Passwd.");
			isPasswordChange = true;
			memberManager.updatePasswd(memberDetails.getMemberId(),
					memberCommand.getPassword(),
					memberProfileForm.getPassword(),
					memberProfileForm.getRepassword());
		}

		if (!memberCommand.getLoginEmail().equals(
				memberProfileForm.getLoginEmail())) {
			LOG.debug("memberProfile update Email.");
			isEmailChange = true;
			if (memberCommand.getLoginEmail().equals(
					memberCommand.getLoginName())) {
				memberProfileForm.setMemberLoginName(memberCommand,
						memberProfileForm.getLoginEmail());
			}
			memberCommand = memberProfileForm
					.setMemberLoginEmail(memberCommand);
		}

		if (!memberCommand.getLoginMobile().equals(
				memberProfileForm.getLoginMobile())) {
			LOG.debug("memberProfile update Mobile.");
			isMobileChange = true;
			if (memberCommand.getLoginMobile().equals(
					memberCommand.getLoginName())) {
				memberProfileForm.setMemberLoginName(memberCommand,
						memberProfileForm.getLoginMobile());
			}
			memberCommand = memberProfileForm
					.setMemberLoginMobile(memberCommand);
		}

		if (isPasswordChange) {
			// 保存重设密码
			isPasswordChange = postProcessForPasswordChange();
			LOG.info("[MEM_EDIT_PROFILE] {} [{}] \"[PASSWORD_CHANGED]\"",
					memberDetails.getLoginName(), new Date());
		}
		if (isEmailChange) {
			isEmailChange = postProcessForEmailChange();
			LOG.info("[MEM_EDIT_PROFILE] {} [{}] \"[EMAIL_CHANGED]\"",
					memberDetails.getLoginName(), new Date());
		}
		if (isMobileChange) {
			isEmailChange = postProcessForMobileChange();
			LOG.info("[MEM_EDIT_PROFILE] {} [{}] \"[MOBILE_CHANGED]\"",
					memberDetails.getLoginName(), new Date());
		}

		// 这里会把需要修改的值都设置到 memberProfile 中，请确保所有值都成功存储
		memberProfile = memberManager.savePersonData(memberProfile);
		if (isPasswordChange && isEmailChange && isMobileChange) {
			memberCommand = memberManager.saveMember(memberCommand);
		}
		LOG.info("[MEM_EDIT_PROFILE] {} [{}] \"Edit MemberProfile Finished\"",
				memberDetails.getLoginName(), new Date());
		return defaultReturnResult;
	}

	/**
	 * 如果isPasswordChange,预留各个商城对于修改密码不同的验证业务的预留扩展点<br>
	 * 默认返回true,如果返回false之后就不会保存memberCommand<br>
	 * 
	 * @return
	 */
	protected boolean postProcessForPasswordChange() {
		return true;
	}

	/**
	 * 如果isEmailChange,预留各个商城对于修改邮箱不同的验证业务的预留扩展点，如发送验证邮箱之类<br>
	 * 默认返回true,如果返回false之后就不会保存memberCommand<br>
	 * 
	 * @return
	 */
	protected boolean postProcessForEmailChange() {
		return true;
	}

	/**
	 * 如果isMobileChange,预留各个商城对于修改Mobile不同的验证业务的预留扩展点，如发送验证码之类<br>
	 * 默认返回true,如果返回false之后就不会保存memberCommand<br>
	 * 
	 * @return
	 */
	protected boolean postProcessForMobileChange() {
		return true;
	}

}
