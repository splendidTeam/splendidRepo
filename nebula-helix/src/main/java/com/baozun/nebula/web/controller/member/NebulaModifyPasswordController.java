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

import com.baozun.nebula.manager.member.MemberPasswordManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.MemberPasswordForm;
import com.baozun.nebula.web.controller.member.validator.MemberPasswordFormValidator;

/**
 * <pre>
 * 
 * 修改密码的操作：
 * 
 * 跳转到修改密码页面：
 * 点击提交时：
 * 	1，验证输入的旧密码是否正确
 * 	2，正确：调用service层的方法，对对应用户下的密码进行修改。最终跳转至修改成功该页面。
 * 	3，若不正确，则继续回到修改密码页面。
 * 
 * </pre>
 * 
 * @author Wanrong.Wang 2016/03/31
 * @Controller
 */

public class NebulaModifyPasswordController extends BaseController{

	private static final Logger			LOG						= LoggerFactory.getLogger(NebulaModifyPasswordController.class);

	/* 修改密码的页面定义 */
	public static final String			VIEW_MODIFY_PASSWORD	= "member.modifyPassword";

	@Autowired
	private MemberPasswordManager		memberPasswordManager;

	@Autowired
	@Qualifier("memberPasswordFormValidator")
	private MemberPasswordFormValidator	memberPasswordFormValidator;

	/**
	 * 去修改密码页面(此步骤的操作需要在登录状态下)
	 * 
	 * @NeedLogin
	 * @RequestMapping(value = "/member/modifyPassword", method = RequestMethod.GET)
	 */
	public String showModifyPassword(){
		return VIEW_MODIFY_PASSWORD;
	}

	/**
	 * 修改密码
	 * 
	 * @NeedLogin
	 * @RequestMapping(value = "/member/modifyPassword.json", method = RequestMethod.POST)
	 * @param memberDetails
	 * @param memberPasswordForm
	 * @param result
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	public NebulaReturnResult modifyPassword(
			@LoginMember MemberDetails memberDetails,
			@ModelAttribute("memberPasswordForm") MemberPasswordForm memberPasswordForm,
			BindingResult result,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response){

		DefaultReturnResult returnResult = new DefaultReturnResult();
		DefaultResultMessage defaultResultMessage = new DefaultResultMessage();

		// 获取当前登录用户的id
		Long memberId = memberDetails.getMemberId();
		LOG.info("[The member try to modify password,memberId: ] {} [{}]", memberId, new Date());

		// 校验页面数据
		memberPasswordFormValidator.validate(memberPasswordForm, result);
		LOG.info("开始校验页面数据");
		if (result.hasErrors()){
			returnResult.setResult(false);
			returnResult.setResultMessage(defaultResultMessage);
			// 校验页面数据有错，则直接返回
			return returnResult;
		}

		LOG.info("校验页面数据成功");

		// 解密页面数据
		String oldPassword = decryptSensitiveDataEncryptedByJs(memberPasswordForm.getOldPassword(), request);
		String newPassword = decryptSensitiveDataEncryptedByJs(memberPasswordForm.getNewPassword(), request);
		String confirmPassword = decryptSensitiveDataEncryptedByJs(memberPasswordForm.getConfirmPassword(), request);
		LOG.info("即将调用service层的方法");
		// 若页面数据校验通过，则调用service层的方法，进行修改密码操作
		boolean flag = memberPasswordManager.modifyPassword(oldPassword, newPassword, confirmPassword, memberId);

		if (flag){
			// 修改密码成功
			LOG.info("[The member have modified password success,memberId: ] {} [{}]", memberId, new Date());
			returnResult.setResult(true);
			returnResult.setResultMessage(defaultResultMessage);
		}else{
			// 修改密码失败
			LOG.info("[The member try to modify password but failed ,memberId: ] {} [{}]", memberId, new Date());
			returnResult.setResult(false);
			returnResult.setResultMessage(defaultResultMessage);
		}
		return returnResult;
	}

}
