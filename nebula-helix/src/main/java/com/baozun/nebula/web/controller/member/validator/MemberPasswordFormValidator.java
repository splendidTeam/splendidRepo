package com.baozun.nebula.web.controller.member.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.MemberPasswordForm;

public class MemberPasswordFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return MemberPasswordForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

	MemberPasswordForm command = (MemberPasswordForm) target;

	// 校验数据（各个密码中均不能为空）
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldPassword",
		"field.required");
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword",
		"field.required");
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword",
		"field.required");

	// 还需要校验两次输入密码的一致性
	if (!errors.hasFieldErrors("newPassword")
		&& !errors.hasFieldErrors("confirmPassword")) {
	    if (!command.getNewPassword().equals(command.getConfirmPassword())) {
		errors.rejectValue("confirmPassword",
			"register.confirmPassword.error");// 提示两次输入的密码不一致
	    }
	}

    }

}
