/**
 * 
 */
package com.baozun.nebula.web.controller.member.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.RegisterForm;

/**
 * PC || Tablet 注册的表单数据验证
 * 
 * @author Viktor Huang
 * @date 2016年3月23日 上午10:42:01
 */
public class RegisterFormNormalValidator implements Validator{

	@SuppressWarnings("unused")
	private static final Logger	LOGGER	= LoggerFactory.getLogger(RegisterFormNormalValidator.class);

	@Override
	public boolean supports(Class<?> clazz){
		return RegisterForm.class.isAssignableFrom(clazz);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target,Errors errors){

		RegisterForm form = (RegisterForm) target;

		// pc端注册：邮箱，密码，验证码必需
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required.registerform.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required.registerform.password");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "repassword", "required.registerform.repassword");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "randomCode", "required.registerform.randomCode");

		
		if (!errors.hasFieldErrors("password") && !errors.hasFieldErrors("repassword")){
			// if (form.getPassword().indexOf(" ") > -1){
			// errors.rejectValue("password", "register.password.spaces");
			// }
			// if (form.getRepassword().indexOf(" ") > -1){
			// errors.rejectValue("repassword", "register.repassword.spaces");
			// }
			if (!form.getPassword().equals(form.getRepassword())){
				errors.rejectValue("repassword", "register.repassword.error");
			}
		}
	}
}
