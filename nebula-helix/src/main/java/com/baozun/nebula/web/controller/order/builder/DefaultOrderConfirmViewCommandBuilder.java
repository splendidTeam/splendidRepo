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
package com.baozun.nebula.web.controller.order.builder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.web.controller.order.viewcommand.OrderConfirmViewCommand;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Component("orderConfirmViewCommandBuilder")
public class DefaultOrderConfirmViewCommandBuilder implements OrderConfirmViewCommandBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderConfirmViewCommandBuilder.class);

    @Autowired
    private ShoppingCartCommandShowBuilder shoppingCartCommandShowBuilder;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.OrderConfirmViewCommandBuilder#buildOrderConfirmViewCommand(java.util.List, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand)
     */
    @Override
    public OrderConfirmViewCommand build(List<ContactCommand> contactCommandList,ShoppingCartCommand shoppingCartCommand){
        // 封装viewCommand
        OrderConfirmViewCommand orderConfirmViewCommand = new OrderConfirmViewCommand();
        orderConfirmViewCommand.setShoppingCartCommand(shoppingCartCommandShowBuilder.build(shoppingCartCommand));
        // 收获地址信息
        orderConfirmViewCommand.setAddressList(contactCommandList);
        return orderConfirmViewCommand;
    }

}
