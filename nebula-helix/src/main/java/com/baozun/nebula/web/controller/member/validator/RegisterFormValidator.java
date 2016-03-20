package com.baozun.nebula.web.controller.member.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.RegisterForm;


public class RegisterFormValidator implements Validator{

	public RegisterFormValidator() {
		super();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return RegisterForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		//校验数据
	}
	
}
