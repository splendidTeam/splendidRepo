package com.baozun.nebula.web.controller.member.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;
import com.feilong.core.RegexPattern;
import com.feilong.core.util.RegexUtil;

public class ForgetPasswordFormValidator implements Validator{

	public static final int	EMAIL	= 2;

	public static final int	MOBILE	= 1;

	public ForgetPasswordFormValidator(){}

	@Override
	public boolean supports(Class<?> clazz){
		return ForgetPasswordForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target,Errors errors){

		ForgetPasswordForm command = (ForgetPasswordForm) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "field.required");

		Integer type = command.getType();

		// 判断是手机验证方式还是邮箱验证方式
		// 邮箱验证方式，则邮箱不能为空
		if (type == EMAIL){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required");

			// 验证邮箱规格
			if (!errors.hasFieldErrors("email")){
				if (!RegexUtil.matches(RegexPattern.EMAIL, command.getEmail())){
					errors.rejectValue("email", "member.email.error");
				}
			}
		}else{
			// 手机验证方式，则手机不能为空
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobile", "field.required");
			// 验证手机号是否符合规则
			if (!errors.hasFieldErrors("mobile")){
				if (!RegexUtil.matches(RegexPattern.MOBILEPHONE, command.getMobile())){
					errors.rejectValue("mobile", "member.mobile.error");
				}
			}
		}
	}
}
