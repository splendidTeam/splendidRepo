package com.baozun.nebula.web.controller.member.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.LoginForm;

public class LoginFormValidator implements Validator{

	public LoginFormValidator(){
		super();
	}

	@Override
	public boolean supports(Class<?> clazz){
		return LoginForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target,Errors errors){
		LoginForm loginForm = (LoginForm) target;

		// 校验数据
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "loginName", "loginerror.loginname.notavailable");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "loginerror.password.notavailable");
		

		// errors.rejectValue("shippingInfoCommand.name", "member.loginname.error");
	}

}
