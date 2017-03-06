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

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Component("shoppingcartUpdateDetermineSameLineElementsBuilder")
public class DefaultShoppingcartUpdateDetermineSameLineElementsBuilder implements ShoppingcartUpdateDetermineSameLineElementsBuilder{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartUpdateDetermineSameLineElementsBuilder#buildShoppingcartUpdateDetermineSameLineElements(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm)
     */
    @Override
    public ShoppingcartUpdateDetermineSameLineElements build(ShoppingCartLineCommand currentShoppingCartLineCommand,ShoppingCartLineUpdateSkuForm shoppingCartLineUpdateSkuForm){
        Validate.notNull(currentShoppingCartLineCommand, "currentShoppingCartLineCommand can't be null!");
        Validate.notNull(shoppingCartLineUpdateSkuForm, "shoppingCartLineUpdateSkuForm can't be null!");

        ShoppingcartUpdateDetermineSameLineElements shoppingcartUpdateDetermineSameLineElements = new ShoppingcartUpdateDetermineSameLineElements();
        shoppingcartUpdateDetermineSameLineElements.setCurrentLineId(currentShoppingCartLineCommand.getId());
        shoppingcartUpdateDetermineSameLineElements.setLineGroup(currentShoppingCartLineCommand.getLineGroup());
        shoppingcartUpdateDetermineSameLineElements.setPackageInfoFormList(shoppingCartLineUpdateSkuForm.getPackageInfoFormList());
        shoppingcartUpdateDetermineSameLineElements.setRelatedItemId(currentShoppingCartLineCommand.getRelatedItemId());
        shoppingcartUpdateDetermineSameLineElements.setSkuId(defaultIfNull(currentShoppingCartLineCommand.getSkuId(), currentShoppingCartLineCommand.getSkuId()));
        return shoppingcartUpdateDetermineSameLineElements;
    }
}
