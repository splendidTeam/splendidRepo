package com.baozun.nebula.web.controller.member.validator;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.MemberAddressForm;

/**
 * 个人地址信息校验器
 * @author hengheng.wang
 *
 */
public class MemberAddressFormValidator implements Validator{

	public MemberAddressFormValidator() {
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return MemberAddressForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(null != errors){
			return;
		}
		MemberAddressForm memberAddressForm = (MemberAddressForm) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "province", "field.required");
		
		if((memberAddressForm.getProvince()!=null && "".equals(memberAddressForm.getProvince()))){
			errors.rejectValue("province", "member.address.province.error");
		}		
		if((memberAddressForm.getCity()!=null && "".equals(memberAddressForm.getCity()))){
			errors.rejectValue("city", "member.address.city.error");
		}
		if((memberAddressForm.getArea()!=null && "".equals(memberAddressForm.getArea()))){
			errors.rejectValue("area", "member.address.area.error");
		}
		if((memberAddressForm.getTown()!=null && "".equals(memberAddressForm.getTown()))){
			errors.rejectValue("town", "member.address.town.error");
		}
		if((memberAddressForm.getAddress()!=null && "".equals(memberAddressForm.getAddress()))){
			errors.rejectValue("address", "member.address.address.error");
		}
		if((memberAddressForm.getPhone()!=null && "".equals(memberAddressForm.getPhone())
				&&	memberAddressForm.getTelphone()!=null && "".equals(memberAddressForm.getTelphone()))){
			Pattern p1 = Pattern.compile("^(1[3-9]{1}[0-9]{1})\\d{8}$");
			if(!"".equals(memberAddressForm.getPhone().trim()) &&  !p1.matcher(memberAddressForm.getPhone().trim()).matches()) {
				errors.rejectValue("phone", "member.address.phone.error");
			}
			Pattern p2 = Pattern.compile("^\\d+$");
			if(!"".equals(memberAddressForm.getTelphone().trim()) &&  !p2.matcher(memberAddressForm.getTelphone().trim()).matches()) {
				errors.rejectValue("telephone", "member.address.telephone.error");
			}
		}
		if((memberAddressForm.getConsignee()!=null && "".equals(memberAddressForm.getConsignee()))){
			errors.rejectValue("consignee", "member.address.consignee.error");
		}
		if((memberAddressForm.getPostcode()!=null && "".equals(memberAddressForm.getPostcode()))){
			Pattern p1 = Pattern.compile("^[0-9][0-9]{5}$");
			if (!"".equals(memberAddressForm.getPostcode().trim()) && !p1.matcher(memberAddressForm.getPostcode().trim()).matches()) {
				errors.rejectValue("consignee", "member.address.consignee.error");
			}			
		}
	}

}
