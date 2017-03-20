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
package com.baozun.nebula.web.controller.shoppingcart;

import com.baozun.nebula.web.controller.BaseController;

/**
 * 购物车Base控制器.
 * 
 * <h3>定义:</h3>
 * 
 * <blockquote>
 * <p>
 * 网上商店所说的购物车是对现实的购物车而喻，买家可以像在超市里购物一样，随意添加、删除商品，选购完毕后，统一下单<br>
 * 网上商店的购物车要能过跟踪顾客所选的的商品，记录下所选商品，还要能随时更新，可以支付购买，能给顾客提供很大的方便
 * </p>
 * </blockquote>
 * 
 * <h3>Nebula购物车体系:</h3>
 * <blockquote>
 * <p>
 * Nebula购物车体系分为两类:
 * 
 * <ol>
 * <li><b>普通购物车</b> 即 {@link NebulaAbstractCommonShoppingCartController},含通常意义上大家理解的购物车页面相关功能,类似于在超市买东西,一个一个的放小推车里面去结账</li>
 * <li><b>不走购物车页面的购物车</b> 即 {@link NebulaAbstractImmediatelyBuyShoppingCartController},可以最大化的利用原来的购物车体系的功能及代码,类似于在超市直接拿东西去结账</li>
 * </ol>
 * </p>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand
 * @see com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand
 * @since 5.3.2.13
 */
public abstract class NebulaAbstractShoppingCartController extends BaseController{

}
