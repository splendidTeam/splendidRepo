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
package com.baozun.nebula.web.controller.shoppingcart.builder;

import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.manager.shoppingcart.extractor.PackageInfoElement;
import com.baozun.nebula.sdk.manager.shoppingcart.extractor.ShoppingcartAddDetermineSameLineElements;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;

import static com.feilong.core.util.CollectionsUtil.collect;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Component("shoppingcartAddDetermineSameLineElementsBuilder")
public class DefaultShoppingcartAddDetermineSameLineElementsBuilder implements ShoppingcartAddDetermineSameLineElementsBuilder{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartAddDetermineSameLineElementsBuilder#build(com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm)
     */
    @Override
    public ShoppingcartAddDetermineSameLineElements build(ShoppingCartLineAddForm shoppingCartLineAddForm){
        ShoppingcartAddDetermineSameLineElements shoppingcartAddDetermineSameLineElements = new ShoppingcartAddDetermineSameLineElements();
        shoppingcartAddDetermineSameLineElements.setSkuId(shoppingCartLineAddForm.getSkuId());
        shoppingcartAddDetermineSameLineElements.setPackageInfoElementList(//
                        collect(shoppingCartLineAddForm.getPackageInfoFormList(), PackageInfoElement.class, "type", "featureInfo"));
        return shoppingcartAddDetermineSameLineElements;
    }

}
