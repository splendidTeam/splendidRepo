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
package com.baozun.nebula.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.command.MemberFrontendCommand;


public class MemberCommandValidator implements Validator{

	public static final int	TYPE_REGISTER	= 1;

	public static final int	TYPE_LOGIN		= 2;

	private int				type			= TYPE_REGISTER;

	public MemberCommandValidator(){}

	public MemberCommandValidator(int type){
		this.type = type;
	}

	@Override
	public boolean supports(Class<?> clazz){
		return MemberFrontendCommand.class.equals(clazz);
	}

	@Override
	public void validate(Object target,Errors errors){
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "loginName", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
		if (type == TYPE_REGISTER){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "repassword", "field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "randomCode", "field.required");
		}
	}
}
