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

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import com.baozun.nebula.web.controller.shoppingcart.form.PackageInfoForm;

import static com.feilong.core.Validator.isNotNullOrEmpty;

/**
 * 默认的 校验购物车行表单包装信息 校验器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Component("shoppingcartLinePackageInfoFormListValidator")
public class DefaultShoppingcartLinePackageInfoFormListValidator implements ShoppingcartLinePackageInfoFormListValidator{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartLinePackageInfoFormListValidator#validator(java.util.List)
     */
    @Override
    public void validator(List<PackageInfoForm> packageInfoFormList){

        if (isNotNullOrEmpty(packageInfoFormList)){
            for (PackageInfoForm packageInfoForm : packageInfoFormList){
                Validate.notNull(packageInfoForm, "packageInfoForm can't be null!");

                //包装类型不能是 null
                Validate.notNull(packageInfoForm.getType(), "packageInfoForm type can't be null!");

                //包装金额不能是 null
                Validate.notNull(packageInfoForm.getTotal(), "packageInfoForm total can't be null!");
            }
        }
    }
}
