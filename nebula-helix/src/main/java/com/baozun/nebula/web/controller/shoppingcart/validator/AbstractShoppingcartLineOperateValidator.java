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

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractShoppingcartLineOperateValidator{

    /** The shoppingcart one line max quantity validator. */
    @Autowired(required = false)
    private ShoppingcartOneLineMaxQuantityValidator shoppingcartOneLineMaxQuantityValidator;

    @Autowired
    protected ShoppingCartInventoryValidator shoppingCartInventoryValidator;

    /**
     * 如果有配置 {@link #shoppingcartOneLineMaxQuantityValidator},那么使用他;如果没有,那么使用默认的 {@link DefaultShoppingcartOneLineMaxQuantityValidator}.
     *
     * @return the use shoppingcart one line max quantity validator
     */
    protected ShoppingcartOneLineMaxQuantityValidator getUseShoppingcartOneLineMaxQuantityValidator(){
        return defaultIfNull(shoppingcartOneLineMaxQuantityValidator, new DefaultShoppingcartOneLineMaxQuantityValidator());
    }

}
