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
package com.baozun.nebula.sdk.manager.shoppingcart;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * The Interface SdkShoppingCartLinePackManager.
 *
 * @author feilong
 * @since 5.3.1
 */
public interface SdkShoppingCartLinePackManager extends BaseManager{

    /**
     * 封装购物车行数据.
     * 
     * <h3>为什么要封装订单行数据?</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>游客购物车持久化信息比较少,需要再次封装</li>
     * <li>立即购买的数据,需要再次封装</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>封装之后的作用是什么?</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>用于前端转成 viewcommand数据的展示</li>
     * <li>用于下订单结算</li>
     * </ol>
     * </blockquote>
     * 
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     */
    void packShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand);
}
