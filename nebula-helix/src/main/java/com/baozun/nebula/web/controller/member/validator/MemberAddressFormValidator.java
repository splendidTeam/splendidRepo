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

			//验证必填字段
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "provinceId", "field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cityId", "field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "areaId", "field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "townId", "field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "field.required");				
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required");	
			
			//验证手机号码和电话号码
			validateAddress(memberAddressForm);	
			
			// 通用性检验
			if (com.feilong.core.Validator.isNotNullOrEmpty(memberAddressForm.getMobile())) {
				if (!RegexUtil.matches(RegexPattern.MOBILEPHONE,memberAddressForm.getMobile().trim())) {
					errors.rejectValue("mobile", "memberaddress.phone.error");
				}					
			}
			if (com.feilong.core.Validator.isNotNullOrEmpty(memberAddressForm.getTelphone())) {	
				if (!RegexUtil.matches(RegexPattern.TELEPHONE,memberAddressForm.getTelphone().trim())) {
					errors.rejectValue("telphone", "memberaddress.telephone.error");
				}	
			}
			if (com.feilong.core.Validator.isNotNullOrEmpty(memberAddressForm.getPostcode())) {
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
		return StringUtils.isNotBlank(memberAddressForm.getMobile()) || StringUtils.isNotBlank(memberAddressForm.getTelphone());
	}
}
