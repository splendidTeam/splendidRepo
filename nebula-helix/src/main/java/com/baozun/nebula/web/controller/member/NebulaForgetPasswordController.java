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

import com.baozun.nebula.manager.member.MemberExtraManager;
import com.baozun.nebula.manager.member.MemberPasswordManager;
import com.baozun.nebula.manager.system.TokenManager;
import com.baozun.nebula.manager.system.TokenManager.VerifyResult;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;
import com.baozun.nebula.web.controller.member.validator.ForgetPasswordFormValidator;

/**
 * @author Wanrong.Wang
 * @Date 2016/03/22
 * @Controller
 */
public class NebulaForgetPasswordController extends BaseController{

	private static final Logger			LOG							= LoggerFactory.getLogger(NebulaForgetPasswordController.class);

	/* 忘记密码的页面定义 */
	public static final String			VIEW_FORGET_PASSWORD		= "store.login.forgetpassword";

	/* 重置密码的页面定义 */
	public static final String			VIEW_RESET_PASSWORD			= "store.login.resetpassword";

	/* 重置密码成功的页面定义 */
	public static final String			VIEW_RESET_PASSWORD_SUCCESS	= "resetpassword.success";

	public static final String			PASSWORD					= "password";

	public static final String			CONFIRM_PASSWORD			= "confirmPassword";

	/* 存储用户手机或邮箱信息的session的key */
	private static final String			TOKEN						= "VALIDATED_USER_MESSAGE_KEY";

	@Autowired
	private TokenManager				tokenManager;

	@Autowired
	private MemberExtraManager			memberExtraManager;

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
	 * @param type
	 * @return String
	 * @RequestMapping(value="/member/sendValidateCode.json",method=RequestMethod.POST)
	 */

	public NebulaReturnResult sendValidateCode(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
			BindingResult bindingResult){

		DefaultReturnResult returnResult = new DefaultReturnResult();
		DefaultResultMessage defaultResultMessage = new DefaultResultMessage();

		boolean flag = false;
		try{
			// 校验页面的数据
			forgetPasswordFormValidator.validate(forgetPasswordForm, bindingResult);
			if (bindingResult.hasErrors()){
				returnResult.setResult(false);
				returnResult.setResultMessage(defaultResultMessage);
				// 校验页面数据有错，则直接返回
				return returnResult;
			}

			// 数据校验都成功后，调用发送验证码的方法
			flag = memberPasswordManager.sendValidateCode(forgetPasswordForm);
			// 在此处将用户的提交的信息存放到session中去，供后续修改密码时验证是否是同一个用户使用
			request.getSession().setAttribute(TOKEN, forgetPasswordForm);
		}catch (Exception e){
			e.printStackTrace();
		}
		returnResult.setResult(flag);
		returnResult.setResultMessage(defaultResultMessage);
		return returnResult;
	}

	/**
	 * 点击下一步时，对比验证码是否正确
	 * 
	 * @RequestMapping(value ="/member/checkValidateCode",method=RequestMethod .POST)
	 */
	public String checkValidateCode(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
			BindingResult bindingResult){
		boolean flag = false;
		// 检测输入的验证码是否和发送的验证码相同
		if (StringUtils.isNotBlank(forgetPasswordForm.getSecurityCode())){
			// 需要区分是手机验证方式还是邮件验证方式（1为手机验证方式，2为邮件验证方式）
			if (forgetPasswordForm.getType() == ForgetPasswordFormValidator.MOBILE){
				flag = VerifyResult.SUCESS == tokenManager.verifyToken(
						null,
						forgetPasswordForm.getMobile(),
						forgetPasswordForm.getSecurityCode());
			}else{
				flag = VerifyResult.SUCESS == tokenManager.verifyToken(
						null,
						forgetPasswordForm.getEmail(),
						forgetPasswordForm.getSecurityCode());
			}
			if (flag){
				// 验证成功：则跳转到重置密码页面
				return VIEW_RESET_PASSWORD;
			}
		}
		// 验证失败：则重新跳转到忘记密码页面，要求重新进行验证
		return VIEW_FORGET_PASSWORD;
	}

	/**
	 * 重置密码 注意点：需要判断是否是同一用户的操作，防止越过验证层，直接到修改密码层操作，或者伪造数据
	 * 
	 * @RequestMapping(value = "/member/resetpassword", method =RequestMethod.POST)
	 */
	@SuppressWarnings("static-access")
	public String resetPassword(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
			BindingResult bindingResult){

		boolean flag = false;
		ForgetPasswordForm passwordForm = null;
		// 从session中获取到之前验证过的用户的信息
		passwordForm = (ForgetPasswordForm) request.getSession().getAttribute(TOKEN);

		// 验证是否是同一用户操作，需要区分是手机验证方式还是邮箱验证方式
		if (forgetPasswordForm.getType() == forgetPasswordFormValidator.MOBILE){
			// 从现有提交的数据中获取到的mobile值
			String mobile2 = forgetPasswordForm.getMobile();
			// 从session中取到的mobile值
			String mobile = passwordForm.getMobile();
			// 比较两个值是否相等
			if (mobile2 == null || mobile == null || !(mobile.equals(mobile2))){
				// 说明不是同一个用户
				// 修改密码失败，可以重新跳转至验证页面
				return VIEW_FORGET_PASSWORD;
			}
		}else{
			// 说明是邮箱验证方式
			// 从现有提交的数据中获取到的email值
			String email2 = forgetPasswordForm.getEmail();
			// 从session中取到的email值
			String email = passwordForm.getEmail();
			// 比较两个值是否相等
			if (email2 == null || email == null || !(email.equals(email2))){
				// 说明不是同一个用户
				// 修改密码失败，可以重新跳转至验证页面
				return VIEW_FORGET_PASSWORD;
			}
		}

		// 验证通过，确认是同一用户操作。接下来获取到页面数据，进行比较后，调用service的方法，将原来的密码覆盖掉
		// 前台传过来的密码解密后的密码
		String password = decryptSensitiveDataEncryptedByJs(request.getParameter(PASSWORD), request);
		String confirmPassword = decryptSensitiveDataEncryptedByJs(request.getParameter(CONFIRM_PASSWORD), request);
		// 判断密码是否为空
		if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(confirmPassword) && password.equals(confirmPassword)){
			// 调用manager层的方法，重置密码
			LOG.info("[The member try to reset password] {} [{}]", new Date());
			flag = memberPasswordManager.resetPassword(forgetPasswordForm, password);
			if (flag){
				LOG.info("[The member reset password success] {} [{}]", new Date());
				return VIEW_RESET_PASSWORD_SUCCESS;
			}
			// 修改密码失败，（可以提示修改密码失败，几秒后跳转至重置密码页面）可重新跳转至重置密码页面
			LOG.info("[The member reset password failed] {} [{}]", new Date());
			return VIEW_RESET_PASSWORD;
		}else{
			// 重置密码时，输入的密码为空，则重新跳转至重置密码页面
			LOG.info("[The member reset password failed] {} [{}]", new Date());
			return VIEW_RESET_PASSWORD;
		}
	}
}
