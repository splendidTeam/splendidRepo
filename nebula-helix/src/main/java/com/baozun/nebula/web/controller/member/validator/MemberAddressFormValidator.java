package com.baozun.nebula.web.controller.member.validator;


import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.MemberAddressForm;
import com.feilong.core.RegexPattern;
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
			
			validateAddress(memberAddressForm);	
			
			// 通用性检验
			if (StringUtils.isNotBlank(memberAddressForm.getPhone())) {
				if (!RegexUtil.matches(RegexPattern.MOBILEPHONE,memberAddressForm.getPhone().trim())) {
					errors.rejectValue("phone", "memberaddress.phone.error");
				}					
			}
			if (StringUtils.isNotBlank(memberAddressForm.getTelphone())) {	
				if (!RegexUtil.matches(RegexPattern.TELEPHONE,memberAddressForm.getTelphone().trim())) {
					errors.rejectValue("telephone", "memberaddress.telephone.error");
				}	
			}
			if (StringUtils.isNotBlank(memberAddressForm.getPostcode())) {
				if (!RegexUtil.matches(RegexPattern.ZIPCODE,memberAddressForm.getPostcode().trim())) {
					errors.rejectValue("postcode", "memberaddress.postcode.error");
				}	
			}
		}
	}
	
	/**
	 * 地址中手机和电话二选一
	 * @return
	 */
	protected boolean validateAddress(MemberAddressForm  memberAddressForm){
		return StringUtils.isNotBlank(memberAddressForm.getPhone()) || StringUtils.isNotBlank(memberAddressForm.getTelphone());
	}
}
