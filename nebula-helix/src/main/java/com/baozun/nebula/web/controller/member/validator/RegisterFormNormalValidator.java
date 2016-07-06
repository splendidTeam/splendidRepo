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
 * PC || Tablet 注册的表单数据验证(例子)，如需重新定制，需自己实现RegisterFormValidator
 * 
 * @author Viktor Huang
 * @date 2016年3月23日 上午10:42:01
 */
public class RegisterFormNormalValidator extends RegisterFormValidator{

	
	/* 
	 * @see com.baozun.nebula.web.controller.member.validator.RegisterFormValidator#extraValidate()
	 */
	@Override
	public void processValidate(Object target, Errors errors) {
		RegisterForm form = (RegisterForm) target;

		// pc端注册：邮箱，密码，验证码必需
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "loginEmail", "loginEmail.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "repassword", "repassword.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "randomCode", "randomCode.field.required");

		if (!errors.hasFieldErrors("loginEmail")){
			if (!RegexUtil.matches(RegexPattern.EMAIL, form.getLoginEmail())){
				errors.rejectValue("loginEmail", "member.email.error");
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
