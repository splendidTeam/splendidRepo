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
import com.feilong.core.RegexPattern;
import com.feilong.core.util.RegexUtil;

/**
 * PC || Tablet 注册的表单数据验证
 * 
 * @author Viktor Huang
 * @date 2016年3月23日 上午10:42:01
 */
public class BindRegisterFormNormalValidator implements Validator{

	@SuppressWarnings("unused")
	private static final Logger	LOGGER	= LoggerFactory.getLogger(BindRegisterFormNormalValidator.class);

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
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "loginEmail", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");

		if (!errors.hasFieldErrors("loginEmail")){
			if (!RegexUtil.matches(RegexPattern.EMAIL, form.getLoginEmail())){
				errors.rejectValue("loginEmail", "member.email.error");
			}
		}
	}
}
