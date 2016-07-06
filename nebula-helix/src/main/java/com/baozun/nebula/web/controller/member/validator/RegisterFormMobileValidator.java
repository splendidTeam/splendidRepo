/**
 * 
 */
package com.baozun.nebula.web.controller.member.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.baozun.nebula.web.controller.member.form.RegisterForm;
import com.feilong.core.RegexPattern;
import com.feilong.core.util.RegexUtil;

/**
 * Mobile 端注册表单数据验证(例子)，如需重新定制，需自己实现RegisterFormValidator
 * 
 * @author Viktor Huang
 * @date 2016年3月23日 上午10:45:43
 */
public class RegisterFormMobileValidator extends RegisterFormValidator{
	
	/* 
	 * @see com.baozun.nebula.web.controller.member.validator.RegisterFormValidator#extraValidate()
	 */
	@Override
	public void processValidate(Object target, Errors errors) {
		
		RegisterForm form = (RegisterForm) target;

		// Mobile端注册：手机，密码，验证码必需
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "loginMobile", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "repassword", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "randomCode", "field.required");

		if (!errors.hasFieldErrors("loginMobile")){
			if (!RegexUtil.matches(RegexPattern.MOBILEPHONE, form.getLoginMobile())){
				errors.rejectValue("loginMobile", "member.mobile.error");
			}
		}
		if (!errors.hasFieldErrors("password") && !errors.hasFieldErrors("repassword")){
			//密码与确认密码是否一致
			if (!form.getPassword().equals(form.getRepassword())){
				errors.rejectValue("repassword", "passwordAgain.error");
			}
		}
	}
}
