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
package com.baozun.nebula.web.controller.shoppingcart.handler;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineQuantityExtractor;
import com.baozun.nebula.utils.ShoppingCartUtil;

/**
 * 累计 shoppingCartLineCommandList 每行 Quantity 总和的 QuantityShoppingCartCountHandler.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public class SumQuantityShoppingCartCountHandler implements ShoppingCartCountHandler{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.handler.ShoppingCartCountHandler#buildCount(java.util.List)
     */
    @Override
    public <T extends ShoppingCartLineQuantityExtractor> int buildCount(Iterable<T> shoppingCartLineCommandList){
        return ShoppingCartUtil.getSumQuantity(shoppingCartLineCommandList);
    }
}
