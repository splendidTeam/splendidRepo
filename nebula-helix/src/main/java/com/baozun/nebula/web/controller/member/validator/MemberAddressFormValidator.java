package com.baozun.nebula.web.controller.member.validator;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.MemberAddressForm;


/**
 * 个人地址信息校验器
 * 
 * @author hengheng.wang
 *
 */
public class MemberAddressFormValidator implements Validator {

	public MemberAddressFormValidator() {
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return MemberAddressForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		if (target instanceof MemberAddressForm) {
			MemberAddressForm memberAddressForm = (MemberAddressForm) target;

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "province", "field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "area", "field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "town", "field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "field.required");				
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "consignee", "field.required");			
			// 通用性检验
			if (StringUtils.isNotBlank(memberAddressForm.getPhone())) {
				Pattern p1 = Pattern.compile("^(1[3-9]{1}[0-9]{1})\\d{8}$");
				if (!p1.matcher(memberAddressForm.getPhone().trim()).matches()) {
					errors.rejectValue("phone", "memberaddress.phone.error");
				}				
			}
			if (StringUtils.isNotBlank(memberAddressForm.getTelphone())) {				
				Pattern p2 = Pattern.compile("^\\d+$");
				if (!p2.matcher(memberAddressForm.getTelphone().trim()).matches()) {
					errors.rejectValue("telephone", "memberaddress.telephone.error");
				}
			}
			if (StringUtils.isNotBlank(memberAddressForm.getPostcode())) {
				Pattern p1 = Pattern.compile("^[0-9][0-9]{5}$");
				if (!p1.matcher(memberAddressForm.getPostcode().trim()).matches()) {
					errors.rejectValue("postcode", "memberaddress.postcode.error");
				}
			}
		}
	}

}
