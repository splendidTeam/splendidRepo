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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand;

/**
 * 更新保存购物车商品数量的Cookie
 * <p>
 * 该接口可以通过传入ShoppingCartViewCommand获取ShoppingCartLineSubViewCommand然后通过ShoppingcartCountPersister计算购物车中购买的商品总数量并更新对应的Cookie
 * </p>
 * <h4>设计目的和使用场景</h4>
 * <p>
 * 该接口目前使用在{@link com.baozun.nebula.web.controller.shoppingcart.NebulaShoppingCartController}的showShoppingCart()中
 * 主要是为了简化控制层代码并且增加灵活性所以将方法提取成了接口
 * </p>
 * @see com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand
 * @see com.baozun.nebula.web.controller.shoppingcart.persister.ShoppingcartCountPersister
 * @author bowen.dai
 * @since 5.3.2.23
 */
public interface UpdateShoppingCartCountCookieHandler{

    void update(ShoppingCartViewCommand shoppingCartViewCommand,HttpServletRequest request,HttpServletResponse response);
}
