/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.member.validator;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.member.form.MemberProfileForm;
import com.feilong.core.RegexPattern;
import com.feilong.core.util.RegexUtil;

/**
 * MemberProfileForm的校验器
 * 
 * @author liuliu
 * 
 */
public class MemberProfileFormValidator implements Validator {

	// public static final int TYPE_REGISTER = 1;
	// public static final int TYPE_LOGIN = 2;

	// private int type = TYPE_REGISTER;

	public MemberProfileFormValidator() {
	}

	public MemberProfileFormValidator(int type) {
		// this.type = type;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return MemberProfileForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// 重点检查Email，手机号码，密码一致性等

		// TODO Auto-generated method stub
		if (target instanceof MemberProfileForm) {
			MemberProfileForm command = (MemberProfileForm) target;

			//验证邮箱，手机，密码，确认密码是否为空
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email","field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobile","field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordAgain","field.required");

			if (!errors.hasFieldErrors("password")
					&& !errors.hasFieldErrors("passwordAgain")) {
				if (!command.getPassword().equals(command.getRepassword())) {
					errors.rejectValue("passwordAgain", "passwordAgain.error");
				}
				if (command.getOldPassword().equals(command.getPassword())) {
					errors.rejectValue("passwordAgain", "oldPasswordSame.error");
				}
			}

			if (!errors.hasFieldErrors("email")) {
				if (!"".equals(command.getLoginEmail().trim())
						&& !RegexUtil.matches(RegexPattern.EMAIL,command.getLoginEmail().trim())) {
					errors.rejectValue("email", "member.email.error");
				}
			}

			if (!errors.hasFieldErrors("mobile")) {
				if (!"".equals(command.getLoginMobile().trim())
						&& !RegexUtil.matches(RegexPattern.MOBILEPHONE,command.getLoginMobile().trim())) {
					errors.rejectValue("mobile", "member.mobile.error");
				}
			}
		}
	}

}
