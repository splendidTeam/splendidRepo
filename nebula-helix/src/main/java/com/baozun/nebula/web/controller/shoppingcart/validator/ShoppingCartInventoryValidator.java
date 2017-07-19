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

import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 购物车 sku 库存校验.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.3
 */
public interface ShoppingCartInventoryValidator{

    /**
     * 校验购物车里面的指定的skuId(累加)是否超过库存量.
     *
     * @param shoppingCartLineCommandList
     *            用户所有的购物车
     * @param skuId
     *            指定购买的sku id
     * @return 如果超过库存量,返回true;否则返回false
     * 
     * @since 5.3.2.13 change method ,remove extentionCode param
     */
    boolean isMoreThanInventory(List<ShoppingCartLineCommand> shoppingCartLineCommandList,Long skuId);

    /**
     * 买了 某个 <code>skuId</code>,和总购买量 <code>sumBuyCount</code>,判断是否超过库存量
     *
     * @param skuId
     *            指定的sku id
     * @param sumBuyCount
     *            总购买量
     * @return 如果超过库存量,返回true;否则返回false<br>
     *         如果 {@code sumBuyCount <= 0},抛出 {@link IllegalArgumentException}<br>
     *         如果 skuId 找不到 {@link SkuInventory},抛出 {@link NullPointerException}<br>
     * @since 5.3.2.18
     */
    boolean isMoreThanInventory(long skuId,int sumBuyCount);
}
