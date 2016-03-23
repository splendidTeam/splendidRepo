package com.baozun.nebula.web.controller.member.validator;

import org.springframework.mobile.device.Device;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.RegisterForm;

public class RegisterFormValidator implements Validator{

	/** A model for the user agent or device that submitted the current request. */
	private Device	device;

	public RegisterFormValidator(){
		super();
	}

	@Override
	public boolean supports(Class<?> clazz){
		return RegisterForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target,Errors errors){
		// 校验数据
		RegisterForm form = (RegisterForm) target;

		assert null != device : "please set request DEVICE first!";

		if (device.isNormal()){
			// pc端注册：邮箱，密码，验证码必需
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "repassword", "repassword.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "randomCode", "randomCode.required");

		}else if (device.isMobile()){

		}else if (device.isTablet()){

		}

	}

	/**
	 * @return the device
	 */
	public Device getDevice(){
		return device;
	}

	/**
	 * @param device
	 *            the device to set
	 */
	public void setDevice(Device device){
		this.device = device;
	}

}
