/**
 * 
 */
package com.baozun.nebula.web.controller.member.validator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.RegisterForm;
import com.feilong.core.RegexPattern;
import com.feilong.core.util.RegexUtil;

/**
 * Mobile 端注册表单数据验证
 * 
 * @author Viktor Huang
 * @date 2016年3月23日 上午10:45:43
 */
public class RegisterFormMobileValidator implements Validator{

	@SuppressWarnings("unused")
	private static final Logger	LOGGER	= LoggerFactory.getLogger(RegisterFormMobileValidator.class);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
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
			if (!form.getPassword().equals(form.getRepassword())){
				errors.rejectValue("repassword", "passwordAgain.error");
			}
		}

	}
}
