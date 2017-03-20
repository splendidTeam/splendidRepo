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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour.createline;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 专门来保存订单行包装信息的.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
public interface OrderLinePackInfoCreateManager extends BaseManager{

    /**
     * 专门保存订单行包装信息.<br>
     * 
     * 
     * 如果 <code>orderLine</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>shoppingCartLineCommand</code> 是null,抛出 {@link NullPointerException}<br>
     * 
     * @param orderLine
     * @param shoppingCartLineCommand
     * 
     */
    void saveOrderLinePackInfo(OrderLine orderLine,ShoppingCartLineCommand shoppingCartLineCommand);

}
