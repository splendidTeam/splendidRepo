package com.baozun.nebula.web.controller.member;

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
import com.feilong.core.RegexPattern;
import com.feilong.core.util.RegexUtil;

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

	public static final String			CONFIRMPASSWORD				= "confirmPassword";

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
	 * @RequestMapping(value = "/forgetpassword.htm", method = RequestMethod.GET)
	 */
	public String showForgetPassword(){
		return VIEW_FORGET_PASSWORD;
	}

	/**
	 * <pre>
	 * 判断验证方式是邮箱验证还是手机验证：邮箱验证：type=2； 手机验证：type=1
	 * </pre>
	 * 
	 * @param type
	 * @return String
	 * @throws Exception
	 * @RequestMapping(value="/member/checkValidateType.json",method=RequestMethod.GET)
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
			String data = null;
			String regexType = null;
			if (forgetPasswordForm.getType() == ForgetPasswordFormValidator.MOBILE){
				// 为手机验证方式
				data = forgetPasswordForm.getMobile();
				regexType = RegexPattern.MOBILEPHONE;
			}else if (forgetPasswordForm.getType() == ForgetPasswordFormValidator.EMAIL){
				data = forgetPasswordForm.getEmail();
				regexType = RegexPattern.EMAIL;
			}
			// 验证手机格式
			if (RegexUtil.matches(regexType, data)){
				flag = memberPasswordManager.sendValidateCode(forgetPasswordForm);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		returnResult.setResult(flag);
		returnResult.setResultMessage(defaultResultMessage);
		return returnResult;
	}

	/**
	 * 点击保存时，对比验证码是否正确
	 * 
	 * @RequestMapping(value ="/member/checkValidateCode.htm",method=RequestMethod .POST)
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
	 * 重置密码
	 * 
	 * @RequestMapping(value = "/member/resetpassword.htm", method =RequestMethod.POST)
	 */
	public String resetPassword(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
			BindingResult bindingResult){
		boolean flag = false;
		// 获取到页面表单数据，进行比较后，调用service的方法，将原来的密码覆盖掉
		// 前台传过来的密码解密后的密码
		String password = decryptSensitiveDataEncryptedByJs(request.getParameter(PASSWORD), request);
		String confirmPassword = decryptSensitiveDataEncryptedByJs(request.getParameter(CONFIRMPASSWORD), request);
		// 判断密码是否为空
		if (StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(confirmPassword) && password.equals(confirmPassword)){
			flag = memberPasswordManager.resetPassword(forgetPasswordForm, password);
			if (flag){
				return VIEW_RESET_PASSWORD_SUCCESS;
			}
			// 修改密码失败，（可以提示修改密码失败，几秒后跳转至重置密码页面）可重新跳转至重置密码页面
			return VIEW_RESET_PASSWORD;
		}else{
			// 重置密码时，输入的密码为空，则重新跳转至重置密码页面
			return VIEW_RESET_PASSWORD;
		}
	}
}
