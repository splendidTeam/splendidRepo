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
package com.baozun.nebula.sdk.manager.shoppingcart.handler;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 选中的购物车行封装器.
 * 
 * <p>
 * 我们得到了被选中的订单行,商城端需要重新加工,通常会设置 {@link ShoppingCartLineCommand#setCustomParamMap(Map)} ,以用于后续的自定义促销
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand
 * @since 5.3.2.7
 */
public interface ChoosedShoppingCartLineCommandListPacker{

    /**
     * 选中的购物车行封装器.
     *
     * @param chooseLinesShoppingCartLineCommandList
     *            被选中的购物车行
     * @param calcFreightCommand
     *            物流方式及收货地址信息
     * @param coupons
     *            优惠券信息
     */
    void packChoosedShoppingCartLineCommandList(List<ShoppingCartLineCommand> chooseLinesShoppingCartLineCommandList,CalcFreightCommand calcFreightCommand,List<String> coupons);

}
