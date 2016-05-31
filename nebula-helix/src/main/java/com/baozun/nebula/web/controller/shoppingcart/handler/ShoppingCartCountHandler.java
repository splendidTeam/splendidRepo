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

import java.util.List;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 专门来处理购物车数量的.
 * 
 * <p>
 * 购物车数量,目前业界有两种实现,天猫是显示的 list size;京东是 list quantity sum 总和
 * </p>
 *
 * @author feilong
 * @since 5.3.1
 */
public interface ShoppingCartCountHandler{

    /**
     * 构造 count.
     * 
     * @param shoppingCartLineCommandList
     * @return 如果 <code>shoppingCartLineCommandList</code> 是null或者empty,返回 0<br>
     */
    int buildCount(List<ShoppingCartLineCommand> shoppingCartLineCommandList);
}
