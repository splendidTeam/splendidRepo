package com.baozun.nebula.web.controller.member.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class LoginFormValidator implements Validator{

	public LoginFormValidator() {
		super();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		//校验数据
	}
	
}
