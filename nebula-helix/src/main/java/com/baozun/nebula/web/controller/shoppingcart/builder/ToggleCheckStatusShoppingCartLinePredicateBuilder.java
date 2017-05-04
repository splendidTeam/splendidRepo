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
package com.baozun.nebula.web.controller.shoppingcart.builder;

import java.util.List;

import org.apache.commons.collections4.Predicate;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 全选/全不选操作时候购物车行选择器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.14
 */
public interface ToggleCheckStatusShoppingCartLinePredicateBuilder{

    /**
     * 全选/全不选操作时候购物车行选择器.
     *
     * @param shoppingCartLineCommandList
     *            用户所有的购物车行
     * @param checkStatus
     *            选中还是不选中,true为选中, false 为不选中
     * @return
     */
    Predicate<ShoppingCartLineCommand> build(List<ShoppingCartLineCommand> shoppingCartLineCommandList,boolean checkStatus);
}
