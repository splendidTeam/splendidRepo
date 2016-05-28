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
package com.baozun.nebula.web.controller.shoppingcart.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.baozun.nebula.web.controller.shoppingcart.form.ImmediatelyBuyBundleForm;

/**
 * 立即购买 bundle参数的校验.
 *
 * @author feilong
 * @version 5.3.1 2016年5月28日 下午4:48:53
 * @since 5.3.1
 */
public class ImmediatelyBuyBundleFormValidator implements Validator{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    @Override
    public boolean supports(Class<?> clazz){
        return ImmediatelyBuyBundleForm.class.isAssignableFrom(clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    public void validate(Object target,Errors errors){
        ImmediatelyBuyBundleForm immediatelyBuyBundleForm = (ImmediatelyBuyBundleForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "relatedItemId", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "skuIds", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "count", "field.required");
    }
}
