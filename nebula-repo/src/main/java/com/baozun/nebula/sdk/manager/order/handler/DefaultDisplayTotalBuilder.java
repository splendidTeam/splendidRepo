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
package com.baozun.nebula.sdk.manager.order.handler;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.feilong.core.lang.NumberUtil;

/**
 * 计算显示的金额.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.baozun.nebula.sdk.manager.payment.PayMoneyBuilder
 * @since 5.3.2.18
 * @since 5.3.2.22 change to repo from helix
 */
@Component("displayTotalBuilder")
public class DefaultDisplayTotalBuilder implements DisplayTotalBuilder{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDisplayTotalBuilder.class);

    @Autowired
    private OrderTotalPackInfoFeeBuilder orderTotalPackInfoFeeBuilder;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.subview.DisplayTotalBuilder#build(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public BigDecimal build(SalesOrderCommand salesOrderCommand){
        BigDecimal total = salesOrderCommand.getTotal();
        BigDecimal actualFreight = salesOrderCommand.getActualFreight();
        Number totalPackInfoFee = orderTotalPackInfoFeeBuilder.build(salesOrderCommand);

        //此处显示的金额 等于 用户需要支付的金额
        //等于 订单里面的total + 运费+ 包装费用
        BigDecimal displayTotal = NumberUtil.getAddValue(total, actualFreight, totalPackInfoFee);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("salesOrderCommand:[{}],total:[{}],actualFreight:[{}],totalPackInfoFee:[{}],displayTotal:[{}]", salesOrderCommand.getCode(), total, actualFreight, totalPackInfoFee, displayTotal);
        }
        return displayTotal;
    }

}
