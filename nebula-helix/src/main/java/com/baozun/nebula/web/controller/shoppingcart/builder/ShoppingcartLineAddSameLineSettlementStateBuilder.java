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

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 需要操作购物车行的选中状态构造器
 * 
 * @author bowen.dai
 * @since 5.3.2.22
 */

public interface ShoppingcartLineAddSameLineSettlementStateBuilder{

    /**
     * 构建需要操作购物车行的选中状态
     * 
     * @param ShoppingCartLineCommand
     *            需要操作的购物车行
     * @return
     */
    boolean build(ShoppingCartLineCommand shoppingCartLineCommand);

}
