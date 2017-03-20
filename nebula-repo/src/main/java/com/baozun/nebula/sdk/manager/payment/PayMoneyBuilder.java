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
package com.baozun.nebula.sdk.manager.payment;

import java.math.BigDecimal;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;

/**
 * 在线支付金额构造器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
public interface PayMoneyBuilder{

    /**
     * 构造用户需要在线支付的金额.
     * 
     * <p>
     * 目前的逻辑是 shoppingCartCommand.getCurrentPayAmount() (实付金额) 减去salesOrderCommand.getSoPayMentDetails() 相关项支付的金额
     * </p>
     * 
     * @param shoppingCartCommand
     * @return
     * @since 5.3.2.13
     */
    BigDecimal build(SalesOrderCommand salesOrderCommand,ShoppingCartCommand shoppingCartCommand);
}
