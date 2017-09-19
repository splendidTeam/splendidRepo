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

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 所有购物车操作的base 校验.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public abstract class AbstractShoppingcartLineOperateValidator{

    /** The shoppingcart one line max quantity validator. */
    @Autowired
    protected ShoppingcartOneLineMaxQuantityValidator shoppingcartOneLineMaxQuantityValidator;

    /** 购物车 sku 库存校验. */
    @Autowired
    protected ShoppingCartInventoryValidator shoppingCartInventoryValidator;

    //---------------------------------------------------------------------


}
