package com.baozun.nebula.web.controller.order.validator;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.order.form.ReturnOrderForm;


public abstract class ReturnApplicationValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return ReturnOrderForm.class.isAssignableFrom(clazz);
	}

	
	@Override
	public void validate(Object target, Errors errors) {
		processValidate(target, errors);
	}
	
	abstract public void processValidate(Object target, Errors errors);



	

}
