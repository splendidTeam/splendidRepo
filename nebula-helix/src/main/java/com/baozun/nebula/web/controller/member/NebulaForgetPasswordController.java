package com.baozun.nebula.web.controller.member;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.manager.member.MemberPasswordManager;
import com.baozun.nebula.manager.system.TokenManager;
import com.baozun.nebula.manager.system.TokenManager.VerifyResult;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;
import com.baozun.nebula.web.controller.member.validator.ForgetPasswordFormValidator;

/**
 * 忘记密码功能的实现： 通过手机或邮箱任意一种验证方式来重置密码，最终达到修改了数据库中对应用户下的密码的目的
 * 
 * @author Wanrong.Wang
 * @Date 2016/03/31
 * @Controller NebulaForgetPasswordController
 */
public class NebulaForgetPasswordController extends BaseController{

	private static final Logger			LOG							= LoggerFactory.getLogger(NebulaForgetPasswordController.class);

	/* 忘记密码的页面定义 */
	public static final String			VIEW_FORGET_PASSWORD		= "store.login.forgetpassword";

	/* 重置密码的页面定义 */
	public static final String			VIEW_RESET_PASSWORD			= "store.login.resetpassword";

	/* 重置密码成功的页面定义 */
	public static final String			VIEW_RESET_PASSWORD_SUCCESS	= "resetpassword.success";

	/* 页面传递过来的密码 */
	public static final String			PASSWORD					= "password";

	/* 页面传递过来的确认密码 */
	public static final String			CONFIRM_PASSWORD			= "confirmPassword";

	/* 存储用户手机或邮箱信息的session的key */
	private static final String			TOKEN						= "VALIDATED_USER_MESSAGE_KEY";

	/* redis中存储发送的验证码的业务码 */
	public static final String			BUSINESS_CODE				= "FORGET_PASSWORD_BUSINESS";

	@Autowired
	private TokenManager				tokenManager;

	@Autowired
	private MemberPasswordManager		memberPasswordManager;

	@Autowired
	@Qualifier("forgetPasswordFormValidator")
	private ForgetPasswordFormValidator	forgetPasswordFormValidator;

	/**
	 * 跳转到忘记密码的页面
	 * 
	 * @return String
	 * @RequestMapping(value = "/forgetpassword", method = RequestMethod.GET)
	 */
	public String showForgetPassword(){
		return VIEW_FORGET_PASSWORD;
	}

	/**
	 * 发送验证码
	 * 
	 * @RequestMapping(value="/member/sendValidateCode.json", method= RequestMethod.POST) @param request
	 * @param response
	 * @param model
	 * @param forgetPasswordForm
	 * @param bindingResult
	 * @return 返回结果集NebulaReturnResult
	 */
	public NebulaReturnResult sendValidateCode(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
			BindingResult bindingResult){

		DefaultReturnResult returnResult = new DefaultReturnResult();

		// 校验页面的数据
		forgetPasswordFormValidator.validate(forgetPasswordForm, bindingResult);
		if (bindingResult.hasErrors()){
			return super.getResultFromBindingResult(bindingResult);
		}

		// 数据校验都成功后，调用发送验证码的方法
		boolean result = memberPasswordManager.sendValidateCode(forgetPasswordForm);

		returnResult.setResult(result);

		return returnResult;
	}

	/**
	 * 用户输入验证码后，就对比验证码是否正确（ajax请求，未点击下一步提交表单）
	 * 
	 * @RequestMapping(value ="/member/checkValidateCode.json",method=RequestMethod.POST)
	 * @param request
	 * @param response
	 * @param model
	 * @param forgetPasswordForm
	 * @return String 返回需要跳转到的页面
	 */
	public boolean checkValidateCode(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm){
		boolean flag = false;
		// 检测输入的验证码是否和发送的验证码相同
		if (StringUtils.isNotBlank(forgetPasswordForm.getSecurityCode())){
			// 需要区分是手机验证方式还是邮件验证方式（1为手机验证方式，2为邮件验证方式）
			if (forgetPasswordForm.getType() == ForgetPasswordForm.MOBILE){
				flag = VerifyResult.SUCESS == tokenManager.verifyToken(
						BUSINESS_CODE,
						forgetPasswordForm.getMobile(),
						forgetPasswordForm.getSecurityCode());
			}else{
				flag = VerifyResult.SUCESS == tokenManager.verifyToken(
						BUSINESS_CODE,
						forgetPasswordForm.getEmail(),
						forgetPasswordForm.getSecurityCode());
			}
			if (flag){
				// 验证成功
				// 在此处将用户的提交的信息存放到session中去，供后续修改密码时验证是否是同一个用户使用
				request.getSession().setAttribute(TOKEN, forgetPasswordForm);
				return true;
			}
		}
		// 验证失败
		return false;
	}

	/**
	 * 点击下一步，进行页面跳转的判断（前端ajax请求可以使用此方法）
	 * 
	 * @RequestMapping(value = "/member/showResetPassword", method =RequestMethod.GET)
	 */
	public String showResetPassword(HttpServletRequest request){
		// 从session中获取到之前验证过的用户的信息
		ForgetPasswordForm forgetPasswordForm = (ForgetPasswordForm) request.getSession().getAttribute(TOKEN);
		if (forgetPasswordForm != null){
			// session中有值，则跳转到重置密码页面
			return VIEW_RESET_PASSWORD;
		}
		// 若session中没有值，则重新跳转到验证页面
		return VIEW_FORGET_PASSWORD;
	}

	/**
	 * 点击重置密码按钮， 重置密码 注意点：需要判断是否是同一用户的操作，防止越过验证层，直接到修改密码层操作，或者伪造数据
	 * 
	 * @RequestMapping(value = "/member/resetpassword", method =RequestMethod.POST)
	 * @param request
	 * @param response
	 * @param model
	 * @return String 返回需要跳转到的页面
	 */
	public String resetPassword(HttpServletRequest request,HttpServletResponse response,Model model){

		// 从session中获取到之前验证过的用户的信息
		ForgetPasswordForm forgetPasswordForm = (ForgetPasswordForm) request.getSession().getAttribute(TOKEN);
		// 判断session是否有值
		if (forgetPasswordForm == null){
			// 说明session中没有值，则不是同一个用户操作，需要他重新跳转到验证页面，重新验证信息
			// 修改密码失败，可以重新跳转至验证页面
			return VIEW_FORGET_PASSWORD;
		}

		// session中有值，确认是同一用户操作，则进行修改密码的操作
		// 前台传过来的密码解密后的密码
		String password = decryptSensitiveDataEncryptedByJs(request.getParameter(PASSWORD), request);
		String confirmPassword = decryptSensitiveDataEncryptedByJs(request.getParameter(CONFIRM_PASSWORD), request);
		// 判断密码是否为空
		if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(confirmPassword) && password.equals(confirmPassword)){
			// 调用manager层的方法，重置密码
			LOG.info("[The member try to reset password] {} [{}]", new Date());
			boolean flag = memberPasswordManager.resetPassword(forgetPasswordForm, password);
			if (flag){
				LOG.info("[The member reset password success] {} [{}]", new Date());
				request.getSession().removeAttribute(TOKEN);
				return VIEW_RESET_PASSWORD_SUCCESS;
			}
			// 修改密码失败，（可以提示修改密码失败，几秒后跳转至重置密码页面）可重新跳转至验证页面
			LOG.info("[The member reset password failed] {} [{}]", new Date());
			return VIEW_RESET_PASSWORD;
		}else{
			// 重置密码时，输入的密码为空，则重新跳转至重置密码页面
			LOG.info("[The member reset password failed] {} [{}]", new Date());
			return VIEW_RESET_PASSWORD;
		}
	}
}
