package com.baozun.nebula.web.controller.member.validator;


import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.MemberAddressForm;
import com.feilong.core.util.RegexUtil;


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
				if (!RegexUtil.matches("^(1[3-9]{1}[0-9]{1})\\d{8}$",memberAddressForm.getPhone().trim())) {
					errors.rejectValue("phone", "memberaddress.phone.error");
				}				
			}
			if (StringUtils.isNotBlank(memberAddressForm.getTelphone())) {	
				if (!RegexUtil.matches("^\\d+$",memberAddressForm.getTelphone().trim())) {
					errors.rejectValue("phone", "memberaddress.telephone.error");
				}	
			}
			if (StringUtils.isNotBlank(memberAddressForm.getPostcode())) {
				if (!RegexUtil.matches("^[0-9][0-9]{5}$",memberAddressForm.getPostcode().trim())) {
					errors.rejectValue("postcode", "memberaddress.postcode.error");
				}				
			}
		}
	}
}
