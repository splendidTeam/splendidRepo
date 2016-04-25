package com.baozun.nebula.web.controller.member.validator;

import org.springframework.validation.Errors;
/**
 * @author Wanrong.Wang
 * @Date 2016/03/31
 * 
 * 类名：MemberPasswordFormValidator
 * 修改密码页面数据的校验
 * 
 */
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.MemberPasswordForm;
import com.feilong.core.RegexPattern;
import com.feilong.core.util.RegexUtil;

public class MemberPasswordFormValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz){
		return MemberPasswordForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target,Errors errors){

		MemberPasswordForm command = (MemberPasswordForm) target;

		String newPassword = command.getNewPassword();

		// 校验数据（非空校验）
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldPassword", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "field.required");

		// 新密码规则的校验（必须为数字和字母的组合：此规则可以自己重新定义）
		RegexUtil.matches(RegexPattern.AN, newPassword);

		// 还需要校验两次输入密码的一致性（service层最终进行修改密码的时候也进行了校验，不知道此处的校验还需要否？）
		// if (!errors.hasFieldErrors("newPassword") && !errors.hasFieldErrors("confirmPassword")){
		// if (!command.getNewPassword().equals(command.getConfirmPassword())){
		// errors.rejectValue("confirmPassword", "register.confirmPassword.error");// 提示两次输入的密码不一致
		// }
		// }

	}
}
