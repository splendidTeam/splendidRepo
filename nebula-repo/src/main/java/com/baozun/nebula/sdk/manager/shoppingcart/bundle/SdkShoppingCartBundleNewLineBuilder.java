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
package com.baozun.nebula.sdk.manager.shoppingcart.bundle;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * bundle基于整个单行记录 生成 新的每个小行记录.
 * 
 * <p>
 * 参见 ISP 接口隔离原则(Interface Segregation Principle)
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public interface SdkShoppingCartBundleNewLineBuilder{

    /**
     * Builds the new shopping cart line command.
     *
     * @param relatedItemId
     *            the related item id
     * @param skuId
     *            the sku id
     * @param quantity
     *            the quantity
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @return the shopping cart line command
     */
    ShoppingCartLineCommand buildNewShoppingCartLineCommand(
                    Long relatedItemId,
                    Long skuId,
                    Integer quantity,
                    ShoppingCartLineCommand shoppingCartLineCommand);
}
